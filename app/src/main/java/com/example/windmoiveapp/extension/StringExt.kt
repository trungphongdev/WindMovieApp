package com.example.windmoiveapp.extension

import android.util.Patterns
import android.widget.EditText
import java.math.BigDecimal




fun String?.convertStringToNumber(): Float {
    return try {
        this?.replace("[^\\d.-]".toRegex(), "")?.toFloat() ?: 0f
    } catch (e: NumberFormatException) {
        0f
    }
}

fun String?.convertStringToBigDecimalNumber(scale: Int? = null): BigDecimal {
    val result = try {
        this?.replace("[^\\d.-]".toRegex(), "")?.toBigDecimal() ?: "0".toBigDecimal()
    } catch (e: NumberFormatException) {
        "0".toBigDecimal()
    }
    if (scale != null && scale >= result.scale()) {
        return result.setScale(scale)
    }
    return result
}

fun String?.convertStringToInt(): Int {
    return try {
        this?.replace("[^\\d.-]".toRegex(), "")?.toInt() ?: 0
    } catch (e: NumberFormatException) {
        0
    }
}

fun String?.convertStringToNumberToLong(): Long {
    return try {
        this?.replace("[^\\d.-]".toRegex(), "")?.toLong() ?: 0
    } catch (e: NumberFormatException) {
        0
    }
}

fun String?.convertStringToDouble(): Double {
    return try {
        this?.replace("[^\\d.-]".toRegex(), "")?.toDouble() ?: "0".toDouble()
    } catch (e: NumberFormatException) {
        "0".toDouble()
    }
}
