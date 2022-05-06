package me.kofesst.android.redminecomposeapp.feature.domain.util

import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Priority
import me.kofesst.android.redminecomposeapp.feature.data.model.journal.Detail
import me.kofesst.android.redminecomposeapp.feature.data.model.status.Status
import java.text.SimpleDateFormat
import java.util.*

fun Pair<String, String?>.getValuesText(title: String): String {
    val pattern = "Параметр %s изменился %s%s"
    val hasOldValuePattern = "с \"%s\" "
    val newValuePattern = "на \"%s\""

    return pattern.format(
        title,
        if (this.second != null) {
            hasOldValuePattern.format(this.second)
        } else {
            ""
        },
        newValuePattern.format(this.first)
    )
}

fun Detail.getInfoText(
    statuses: List<Status>,
    priorities: List<Priority>
): String {
    return when (this.property) {
        DetailType.Attachment.propertyName -> {
            "Файл %s добавлен".format(this.new_value)
        }
        DetailType.CustomField.propertyName -> {
            val values: Pair<String, String?>
            val cfTitle: String

            when (this.name) {
                CustomField.Deadline.name -> {
                    values = CustomField.Deadline.getValuesText(
                        this.new_value,
                        this.old_value
                    )
                    cfTitle = CustomField.Deadline.title
                }
                else -> return "Незнакомый айди кастомного поля"
            }

            return values.getValuesText(cfTitle)
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
                Attribute.PriorityAttr.name -> {
                    values = Attribute.PriorityAttr.getValuesText(
                        priorities,
                        this.new_value,
                        this.old_value
                    )
                    attrTitle = Attribute.PriorityAttr.title
                }
                Attribute.DescriptionAttr.name -> {
                    return Attribute.DescriptionAttr.getText()
                }
                Attribute.AssignedToAttr.name -> {
                    return Attribute.AssignedToAttr.getText()
                }
                else -> return "Незнакомое имя атрибута"
            }

            return values.getValuesText(attrTitle)
        }
        else -> "Незнакомое значение"
    }
}

sealed class Attribute(val name: String, val title: String) {
    object StatusAttr : Attribute("status_id", "Статус") {
        private fun getValueText(statuses: List<Status>, value: String): String {
            val statusId = value.toIntOrNull() ?: return "Некорректный id статуса"
            val status = statuses.firstOrNull {
                it.id == statusId
            } ?: return "Некорректный id статуса"
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

    object PriorityAttr : Attribute("priority_id", "Приоритет") {
        private fun getValueText(priorities: List<Priority>, value: String): String {
            val priorityId = value.toIntOrNull() ?: return "Некорректный id приоритета"
            val priority = priorities.firstOrNull {
                it.id == priorityId
            } ?: return "Некорректный id приоритета"
            return priority.name
        }

        fun getValuesText(
            priorities: List<Priority>,
            newValue: String,
            oldValue: String?
        ): Pair<String, String?> {
            return getValueText(priorities, newValue) to oldValue?.let {
                getValueText(priorities, it)
            }
        }
    }

    object DescriptionAttr : Attribute("description", "Описание") {
        fun getText(): String {
            return "Описание задачи изменено"
        }
    }

    object AssignedToAttr : Attribute("assigned_to_id", "Исполнитель") {
        fun getText(): String {
            return "Смена исполнителя задачи"
        }
    }
}

sealed class CustomField(val name: String, val title: String) {
    object Deadline : CustomField("10", "Дедлайн") {
        fun getValuesText(
            newValue: String,
            oldValue: String?
        ): Pair<String, String?> {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
            return (format.parse(newValue)?.formatDate() ?: "Некорректная дата") to oldValue?.let {
                if (it.isBlank()) return@let null
                (format.parse(it)?.formatDate() ?: "Некорректная дата")
            }
        }
    }
}

sealed class DetailType(val propertyName: String) {
    object Attachment : DetailType("attachment")
    object Attribute : DetailType("attr")
    object CustomField : DetailType("cf")
}