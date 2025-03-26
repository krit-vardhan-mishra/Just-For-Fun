package com.just_for_fun.justforfun.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.just_for_fun.justforfun.data.User
import java.io.File

class AuthRepository(private val context: Context) {

    private val FILE_NAME = "user.json"

    private fun getJsonFile(): File {
        return File(context.filesDir, FILE_NAME)
    }

    private fun readUserData(): MutableList<User> {
        val file = getJsonFile()
        if (!file.exists()) return mutableListOf()

        return try {
            val jsonString = file.readText()
            val type = object : TypeToken<MutableList<User>>() {}.type
            Gson().fromJson(jsonString, type) ?: mutableListOf()
        } catch (e: Exception) {
            Log.d("AuthRepository", "Error reading JSON file: ${e.message}")
            mutableListOf()
        }
    }

    private fun writeUserData(users: MutableList<User>) {
        val file = getJsonFile()
        try {
            val jsonString = Gson().toJson(users)
            file.writeText(jsonString)
        } catch (e: Exception) {
            Log.d("AuthRepository", "Error writing JSON file: ${e.message}")
        }
    }

    fun login(email: String, password: String): Result<String> {
        val users = readUserData()
        val user = users.find { it.email == email && it.password == password }

        return if (user != null) {
            Result.success("Login successful (Local)!")
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }

    fun signUp(name: String, email: String, username: String, password: String, profilePhoto: Uri?): Result<String> {
        val users = readUserData()

        if (users.any { it.username == username || it.email == email }) {
            return Result.failure(Exception("User already exists (Local)"))
        }

        users.add(User(name, email, username, password, profilePhoto))
        writeUserData(users)

        return Result.success("Sign-up successful (Local)!")
    }
}
