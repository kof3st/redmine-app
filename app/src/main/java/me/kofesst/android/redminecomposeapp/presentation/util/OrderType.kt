package me.kofesst.android.redminecomposeapp.presentation.util

import androidx.annotation.StringRes
import me.kofesst.android.redminecomposeapp.R

sealed class OrderType(@StringRes val nameRes: Int) {
    object Ascending : OrderType(R.string.ascending)
    object Descending : OrderType(R.string.descending)
}