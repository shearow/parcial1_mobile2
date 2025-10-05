package com.example.practica1

import java.text.SimpleDateFormat
import java.util.Locale

fun getDayOfWeek(dateStr: String): String {
    val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = parser.parse(dateStr)
    val formatter = SimpleDateFormat("EEE", Locale.getDefault())
    return formatter.format(date!!)
}