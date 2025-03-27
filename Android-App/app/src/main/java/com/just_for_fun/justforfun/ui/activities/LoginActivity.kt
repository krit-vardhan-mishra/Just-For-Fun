package com.just_for_fun.justforfun.ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.just_for_fun.justforfun.data.User
import com.just_for_fun.justforfun.databinding.ActivityLoginBinding
import com.just_for_fun.justforfun.ui.activities.viewmodel.LoginViewModel
import com.just_for_fun.justforfun.ui.fragments.account.AccountViewModel
import com.just_for_fun.justforfun.util.delegates.viewBinding
import okio.IOException
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class LoginActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityLoginBinding::inflate)
    private val viewModel: LoginViewModel by viewModel()
    private val accountViewModel: AccountViewModel by viewModel()

    private val FILE_NAME = "user.json"
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val signUpClickable = SpannableString("If you don't have an ID, Sign Up then")

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLUE
                ds.isUnderlineText = true
            }
        }

        signUpClickable.setSpan(clickableSpan, 24, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvSignUp.text = signUpClickable
        binding.tvSignUp.movementMethod = LinkMovementMethod.getInstance()

        binding.btnSubmit.setOnClickListener {
            val email = binding.emailInputLogin.text.toString()
            val password = binding.passwordInputLogin.text.toString()
            viewModel.login(email, password)
        }

        viewModel.loginStatus.observe(this) { result ->
            result.fold(
                onSuccess = { message ->
                    val users = readUserData()
                    val loggedInUser = users.find {
                        it.email == binding.emailInputLogin.text.toString()
                    }

                    loggedInUser?.let { user ->
                        accountViewModel.setCurrentUser(user)
                        accountViewModel.currentUser.observe(this) {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    }

                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                },
                onFailure = {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun readUserData(): MutableList<User> {
        val file = getJsonFile()

        if (!file.exists()) {
            try {
                file.createNewFile()
                file.writeText("[]") // Ensure the file contains a valid empty JSON array
                return mutableListOf()
            } catch (e: IOException) {
                Log.e("AuthRepository", "Failed to create file: ${e.message}")
                return mutableListOf()
            }
        }

        return try {
            val jsonString = file.readText()
            if (jsonString.isBlank()) return mutableListOf()

            val type = object : TypeToken<MutableList<User>>() {}.type
            gson.fromJson(jsonString, type) ?: mutableListOf()
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error reading JSON file", e)
            mutableListOf()
        }
    }

    private fun getJsonFile(): File {
        return File(filesDir, FILE_NAME) // Use filesDir instead of context.filesDir
    }
}
