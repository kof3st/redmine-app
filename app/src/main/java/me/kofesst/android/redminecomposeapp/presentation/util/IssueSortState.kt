package me.kofesst.android.redminecomposeapp.presentation.util

import androidx.annotation.StringRes
import me.kofesst.android.redminecomposeapp.R

sealed class IssueSortState(
    @StringRes val nameRes: Int,
    val orderType: OrderType,
    val apiName: String,
) {
    companion object {
        val default = ById()
    }

    class ById(orderType: OrderType = OrderType.Descending) :
        IssueSortState(R.string.sort_by_id, orderType, "id")

    class ByPriority(orderType: OrderType = OrderType.Descending) :
        IssueSortState(R.string.sort_by_priority, orderType, "priority")

    fun copy(orderType: OrderType): IssueSortState {
        return when (this) {
            is ById -> ById(orderType)
            is ByPriority -> ByPriority(orderType)
        }
    }

    fun toApiState(): String = "$apiName:${orderType.apiName}"
}
