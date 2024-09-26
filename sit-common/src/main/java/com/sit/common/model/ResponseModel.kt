package com.sit.common.model

import com.google.gson.annotations.SerializedName

data class ResponseModel<T>(
    @SerializedName("data")
    val `data`: T,
    val message: String,
    @SerializedName("success")
    val success: Boolean,
)