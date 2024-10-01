package com.sit.common.model

data class ErrorResponse<T>(
    val message: String?,
    val errors: T?
)
