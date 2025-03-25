package com.just_for_fun.justforfun.repository

import com.just_for_fun.justforfun.network.AuthApiService

class AuthRepository(private val apiService: AuthApiService) {

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response = apiService.login(email, password)
            if (response.isSuccessful) {
                Result.success("Login successful!")
            } else {
                Result.failure(Exception("Invalid credentials"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(name: String, email: String, username: String, password: String): Result<String> {
        return try {
            val response = apiService.signUp(name, email, username, password)
            if (response.isSuccessful) {
                Result.success("Sign-up successful! Please log in.")
            } else {
                Result.failure(Exception("Sign-up failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
