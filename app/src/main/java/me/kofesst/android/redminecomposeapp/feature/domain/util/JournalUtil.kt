package me.kofesst.android.redminecomposeapp.feature.domain.util

import me.kofesst.android.redminecomposeapp.feature.data.model.journal.Detail
import me.kofesst.android.redminecomposeapp.feature.data.model.status.Status

fun Detail.getInfoText(
    statuses: List<Status>
): String {
    return when (this.property) {
        DetailType.Attachment.propertyName -> {
            "Файл %s добавлен".format(this.new_value)
        }
        DetailType.Attribute.propertyName -> {
            val values: Pair<String, String?>
            val attrTitle: String

            when (this.name) {
                Attribute.StatusAttr.name -> {
                    values = Attribute.StatusAttr.getValuesText(
                        statuses,
                        this.new_value,
                        this.old_value
                    )
                    attrTitle = Attribute.StatusAttr.title
                }
                Attribute.DoneRatioAttr.name -> {
                    values = Attribute.DoneRatioAttr.getValuesText(
                        this.new_value,
                        this.old_value
                    )
                    attrTitle = Attribute.DoneRatioAttr.title
                }
                else -> return "Invalid name"
            }

            val pattern = "Параметр %s изменился %s%s"
            val hasOldValuePattern = "с \"%s\" "
            val newValuePattern = "на \"%s\""

            return pattern.format(
                attrTitle,
                if (values.second != null) {
                    hasOldValuePattern.format(values.second)
                } else {
                    ""
                },
                newValuePattern.format(values.first)
            )
        }
        else -> "Invalid"
    }
}

sealed class Attribute(val name: String, val title: String) {
    object StatusAttr : Attribute("status_id", "Статус") {
        private fun getValueText(statuses: List<Status>, value: String): String {
            val statusId = value.toIntOrNull() ?: return "Invalid value"
            val status = statuses.firstOrNull { it.id == statusId } ?: return "Invalid status id"
            return status.name
        }

        fun getValuesText(
            statuses: List<Status>,
            newValue: String,
            oldValue: String?
        ): Pair<String, String?> {
            return getValueText(statuses, newValue) to oldValue?.let {
                getValueText(statuses, it)
            }
        }
    }

    object DoneRatioAttr : Attribute("done_ratio", "Готовность") {
        fun getValuesText(newValue: String, oldValue: String?): Pair<String, String?> {
            return "$newValue%" to oldValue?.let {
                "$oldValue%"
            }
        }
    }
}

sealed class DetailType(val propertyName: String) {
    object Attachment : DetailType("attachment")
    object Attribute : DetailType("attr")
}