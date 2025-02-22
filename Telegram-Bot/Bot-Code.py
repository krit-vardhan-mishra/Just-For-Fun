from telegram import Update, Chat
from telegram.ext import Application, CommandHandler, ContextTypes, MessageHandler, filters
from telegram.error import Forbidden, ChatMigrated, BadRequest
from datetime import datetime
import json
import asyncio
import logging

# Configure logging
logging.basicConfig(
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    level=logging.INFO
)

class TelegramFileBot:
    def __init__(self, token: str, channel_id: str, group_id: str):
        """
        Initialize the bot with necessary credentials and storage
        
        Args:
            token (str): Telegram Bot API token
            channel_id (str): ID of the private channel to monitor
            group_id (str): ID of the group where bot will respond
        """
        self.token = token
        self.channel_id = channel_id
        self.group_id = group_id
        self.file_database = {}
        self.message_database = {}
        self.authorized_users = set()  # Store authorized user IDs
        self.load_databases()
        self.load_authorized_users()
    
    def load_authorized_users(self):
        """Load authorized users from file"""
        try:
            with open('authorized_users.json', 'r') as f:
                self.authorized_users = set(json.load(f))
        except FileNotFoundError:
            self.authorized_users = set()
    
    def save_authorized_users(self):
        """Save authorized users to file"""
        with open('authorized_users.json', 'w') as f:
            json.dump(list(self.authorized_users), f)

    def load_databases(self):
        """Load existing databases from files"""
        try:
            with open('file_database.json', 'r') as f:
                self.file_database = json.load(f)
            with open('message_database.json', 'r') as f:
                self.message_database = json.load(f)
        except FileNotFoundError:
            pass

    def save_databases(self):
        """Save databases to files"""
        with open('file_database.json', 'w') as f:
            json.dump(self.file_database, f)
        with open('message_database.json', 'w') as f:
            json.dump(self.message_database, f)

    async def verify_channel_access(self, context: ContextTypes.DEFAULT_TYPE):
        """Verify bot has access to the private channel"""
        try:
            chat = await context.bot.get_chat(self.channel_id)
            if chat.type not in [Chat.CHANNEL, Chat.PRIVATE]:
                logging.error("Provided ID is not a channel")
                return False
            return True
        except (Forbidden, BadRequest) as e:
            logging.error(f"Bot doesn't have access to the channel: {str(e)}")
            return False

    async def authorize(self, update: Update, context: ContextTypes.DEFAULT_TYPE):
        """Handle user authorization"""
        if not context.args:
            await update.message.reply_text(
                "Please provide the authorization code after /authorize command"
            )
            return

        auth_code = context.args[0]
        if auth_code == "your_secret_code_here":  # Replace with your actual authorization system
            user_id = update.effective_user.id
            self.authorized_users.add(user_id)
            self.save_authorized_users()
            await update.message.reply_text("You have been authorized to use the bot!")
        else:
            await update.message.reply_text("Invalid authorization code.")

    async def check_user_auth(self, user_id: int) -> bool:
        """Check if a user is authorized"""
        return user_id in self.authorized_users

    async def start(self, update: Update, context: ContextTypes.DEFAULT_TYPE):
        """Handle the /start command"""
        user_id = update.effective_user.id
        if await self.check_user_auth(user_id):
            await update.message.reply_text(
                "Hello! I'm a file search bot. Use /request followed by search terms to find files."
            )
        else:
            await update.message.reply_text(
                "Welcome! Please use /authorize <code> to access the bot. "
                "Contact the channel admin for the authorization code."
            )

    async def handle_new_content(self, update: Update, context: ContextTypes.DEFAULT_TYPE):
        """Handle new messages in the channel, including forwarded content"""
        message = update.channel_post or update.message
        
        if not message or str(message.chat.id) != self.channel_id:
            return
            
        try:
            if not await self.verify_channel_access(context):
                logging.error("Bot doesn't have access to the channel")
                return

            # Handle files
            if message.document:
                source_info = "Forwarded from: " + message.forward_from_chat.title if message.forward_from_chat else "Direct upload"
                
                self.file_database[str(message.message_id)] = {
                    'file_id': message.document.file_id,
                    'file_name': message.document.file_name,
                    'mime_type': message.document.mime_type,
                    'caption': message.caption,
                    'date': message.date.isoformat(),
                    'source': source_info,
                    'forward_date': message.forward_date.isoformat() if message.forward_date else None
                }
            
            # Handle text messages
            if message.text:
                source_info = "Forwarded from: " + message.forward_from_chat.title if message.forward_from_chat else "Direct message"
                
                self.message_database[str(message.message_id)] = {
                    'text': message.text,
                    'date': message.date.isoformat(),
                    'source': source_info,
                    'forward_date': message.forward_date.isoformat() if message.forward_date else None
                }
            
            self.save_databases()
            
        except Exception as e:
            logging.error(f"Error handling new content: {str(e)}")

    async def index_channel_content(self, context: ContextTypes.DEFAULT_TYPE):
        """Index all content from the channel"""
        try:
            if not await self.verify_channel_access(context):
                logging.error("Cannot index channel - access denied")
                return

            async with context.bot:
                messages = await context.bot.get_chat_history(self.channel_id)
                async for message in messages:
                    await self.handle_new_content(Update(update_id=0, channel_post=message), context)
                
        except Exception as e:
            logging.error(f"Error indexing channel content: {str(e)}")

    async def search_and_send(self, update: Update, context: ContextTypes.DEFAULT_TYPE):
        """Handle file search and sending"""
        user_id = update.effective_user.id
        if not await self.check_user_auth(user_id):
            await update.message.reply_text(
                "You are not authorized to use this bot. "
                "Please use /authorize <code> to get access."
            )
            return

        if update.message.chat.id != int(self.group_id):
            await update.message.reply_text("This bot can only be used in the designated group.")
            return
        
        query = ' '.join(context.args).lower()
        if not query:
            await update.message.reply_text("Please provide search terms after /request")
            return

        found_items = []
        
        # Search in file database
        for file_id, file_info in self.file_database.items():
            if (query in file_info['file_name'].lower() or 
                (file_info['caption'] and query in file_info['caption'].lower())):
                found_items.append(('file', file_info))

        # Search in message database
        for msg_id, msg_info in self.message_database.items():
            if query in msg_info['text'].lower():
                found_items.append(('message', msg_info))

        if found_items:
            found_items.sort(key=lambda x: x[1]['date'], reverse=True)
            
            for item_type, item in found_items[:5]:
                try:
                    if item_type == 'file':
                        caption = (
                            f"File: {item['file_name']}\n"
                            f"Uploaded: {item['date']}\n"
                            f"Source: {item['source']}"
                        )
                        if item['forward_date']:
                            caption += f"\nOriginally shared: {item['forward_date']}"
                            
                        await context.bot.send_document(
                            chat_id=update.message.chat_id,
                            document=item['file_id'],
                            caption=caption
                        )
                    else:
                        text = (
                            f"Message from {item['date']}\n"
                            f"Source: {item['source']}\n"
                            f"{'Originally shared: ' + item['forward_date'] + '\n' if item['forward_date'] else ''}"
                            f"Content:\n{item['text']}"
                        )
                        await context.bot.send_message(
                            chat_id=update.message.chat_id,
                            text=text
                        )
                except Exception as e:
                    logging.error(f"Error sending item: {str(e)}")
        else:
            await update.message.reply_text("No matching files or messages found.")

    async def run(self):
        """Run the bot"""
        app = Application.builder().token(self.token).build()
        
        # Register handlers
        app.add_handler(CommandHandler("start", self.start))
        app.add_handler(CommandHandler("authorize", self.authorize))
        app.add_handler(CommandHandler("request", self.search_and_send))
        app.add_handler(MessageHandler(filters.ChatType.CHANNEL, self.handle_new_content))
        
        # Schedule periodic content indexing (every 6 hours)
        job_queue = app.job_queue
        job_queue.run_repeating(self.index_channel_content, interval=21600)
        
        # Start the bot
        await app.run_polling()

if __name__ == "__main__":
    # Replace these with your actual values
    BOT_TOKEN = "6655049152:AAGwgNifA4AXGajDkuIOmMipOfb-pNedhqg"
    CHANNEL_ID = "-1001850781166"
    GROUP_ID = "-1001825811680"
    
    bot = TelegramFileBot(BOT_TOKEN, CHANNEL_ID, GROUP_ID)
    asyncio.run(bot.run())