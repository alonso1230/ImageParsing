package com.example.imageparsing.model

data class Resource<T>(
    val status: Status = Status.LOADING,
    val data: T? = null,
    val error: Throwable? = null
) {
    enum class Status {
        LOADING,
        SUCCESS,
        ERROR
    }
}