package com.just_for_fun.justforfun.util

sealed class UiState<out T : Any> {
    object Error : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    class Result<out T : Any>(val data: T) : UiState<T>()
}