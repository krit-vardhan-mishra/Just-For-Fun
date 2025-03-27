package com.just_for_fun.justforfun.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.just_for_fun.justforfun.data.User
import okio.IOException
import java.io.File

class AuthRepository(private val context: Context) {

    private val FILE_NAME = "user.json"
    private val gson = Gson()

    private fun getJsonFile(): File {
        return File(context.filesDir, FILE_NAME)
    }

    private fun readUserData(): MutableList<User> {
        val file = getJsonFile()

        if (!file.exists()) {
            try {
                file.createNewFile()
                file.writeText("[]")
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

    private fun writeUserData(users: MutableList<User>) {
        val file = getJsonFile()
        try {
            val jsonString = Gson().toJson(users)
            file.writeText(jsonString)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error writing JSON file: ${e.message}")
        }
    }

    fun login(email: String, password: String): Result<String> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(Exception("Email and password cannot be empty"))
        }

        val users = readUserData()
        val user = users.find {
            it.email.equals(email, ignoreCase = true) && it.password == password
        }

        return if (user != null) {
            Result.success("Login successful!")
        } else {
            Result.failure(Exception("Invalid email or password"))
        }
    }

    fun signUp(
        name: String,
        email: String,
        username: String,
        password: String,
        profilePhoto: Uri?
    ): Result<String> {
        when {
            name.isBlank() -> return Result.failure(Exception("Name cannot be empty"))
            email.isBlank() -> return Result.failure(Exception("Email cannot be empty"))
            username.isBlank() -> return Result.failure(Exception("Username cannot be empty"))
            password.isBlank() -> return Result.failure(Exception("Password cannot be empty"))
            password.length < 6 -> return Result.failure(Exception("Password must be at least 6 characters"))
        }

        val users = readUserData()

        if (users.any { it.username.equals(username, ignoreCase = true) }) {
            return Result.failure(Exception("Username is already taken"))
        }

        users.add(
            User(
                name = name.trim(),
                email = email.trim(),
                username = username.trim(),
                password = password,
                profilePhoto = profilePhoto
            )
        )
        writeUserData(users)

        return Result.success("Sign-up successful (Local)!")
    }
}
