package me.kofesst.android.redminecomposeapp.presentation.util

import androidx.annotation.StringRes
import me.kofesst.android.redminecomposeapp.R

sealed class OrderType(@StringRes val nameRes: Int, val apiName: String) {
    object Ascending : OrderType(R.string.ascending, "asc")
    object Descending : OrderType(R.string.descending, "desc")
}