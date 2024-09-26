package com.sit.common.ext

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val YYYY_MM_DD = "yyyy-MM-dd"
const val YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
const val DD_MM_YYYY_HH_MM_SS = "dd-MM-yyyy HH:mm:ss"
const val DD_MM_YY = "dd-MM-yyyy"
const val HH_MM_SS = "HH:mm:ss"


fun getCurrentDate(format: String = DD_MM_YY): String {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(Date())
}