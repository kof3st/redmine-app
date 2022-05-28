package me.kofesst.android.redminecomposeapp.domain.util

import me.kofesst.android.redminecomposeapp.domain.model.Attachment
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

private val sizeUnits = listOf("Б", "КБ", "МБ", "ГБ", "ТБ")

val Attachment.fileSizeWithUnit: String
    get() {
        if (fileSize <= 0) return "0 Б"
        val digitGroups = (log10(fileSize.toDouble()) / log10(1024.0)).toInt()
        val size = fileSize / 1024.0.pow(digitGroups.toDouble())

        val formatter = DecimalFormat()
        formatter.maximumFractionDigits = 2
        formatter.minimumFractionDigits = 0

        return "${formatter.format(size)} ${sizeUnits[digitGroups]}"
    }

fun Attachment.getDownloadLink(apiKey: String): String {
    return "$url?key=$apiKey"
}