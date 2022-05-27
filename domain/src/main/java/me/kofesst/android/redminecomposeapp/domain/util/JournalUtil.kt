package me.kofesst.android.redminecomposeapp.domain.util

import me.kofesst.android.redminecomposeapp.domain.model.IdName
import me.kofesst.android.redminecomposeapp.domain.model.journal.JournalDetails

private val properties = listOf(
    DetailProperty.Attachment,
    DetailProperty.CustomField.Deadline,
    DetailProperty.Attribute.DoneRatio,
    DetailProperty.Attribute.Status,
    DetailProperty.Attribute.Priority,
    DetailProperty.Attribute.Tracker,
    DetailProperty.Attribute.Description,
    DetailProperty.Attribute.AssignedTo
)

private fun findProperty(details: JournalDetails): DetailProperty? {
    val property = properties.filter { it.propertyName == details.property }
    if (property.isEmpty()) {
        return null
    }

    if (property.first() is DetailProperty.CustomField) {
        return property.firstOrNull {
            val cf = it as DetailProperty.CustomField
            cf.name == details.name
        }
    }

    if (property.first() is DetailProperty.Attribute) {
        return property.firstOrNull {
            val attr = it as DetailProperty.Attribute
            attr.name == details.name
        }
    }

    return property.firstOrNull()
}

fun JournalDetails.parse(
    statuses: List<IdName>,
    priorities: List<IdName>,
    trackers: List<IdName>,
): String {
    val property = findProperty(this) ?: return "NotFound"
    return property.parseDetailsText(this, statuses, priorities, trackers)
}

sealed class DetailProperty(val propertyName: String, private val title: String) {
    companion object {
        private const val PropertyDeletedFormat = "Параметр \"%s\" удалён"
        private const val NewPropertyFormat = "Параметр \"%s\" изменился на \"%s\""
        private const val PropertyReplacedFormat = "Параметр \"%s\" изменился с \"%s\" на \"%s\""
        protected const val AttachmentFormat = "Файл \"%s\" добавлен"
        protected const val DescriptionFormat = "Описание задачи изменилось"
        protected const val AssignedToFormat = "Исполнитель задачи изменился"
    }

    protected open val customParse: ((JournalDetails) -> String)?
        get() = null

    fun parseDetailsText(
        details: JournalDetails,
        statuses: List<IdName>,
        priorities: List<IdName>,
        trackers: List<IdName>,
    ): String {
        if (details.newValue == null) {
            return PropertyDeletedFormat.format(title)
        }

        if (customParse != null) {
            return customParse!!.invoke(details)
        }

        val oldValueName = parseValue(details.oldValue, statuses, priorities, trackers)
        val newValueName = parseValue(details.newValue, statuses, priorities, trackers)

        if (details.oldValue == null) {
            if (newValueName == null) {
                return "Error #1"
            }

            return NewPropertyFormat.format(title, newValueName)
        }

        if (oldValueName == null) {
            return "Error #2"
        }

        if (newValueName == null) {
            return "Error #3"
        }

        return PropertyReplacedFormat.format(title, oldValueName, newValueName)
    }

    protected abstract fun parseValue(
        value: String?,
        statuses: List<IdName>,
        priorities: List<IdName>,
        trackers: List<IdName>,
    ): String?

    object Attachment : DetailProperty("attachment", "Вложение") {
        override val customParse: (JournalDetails) -> String
            get() = {
                if (it.newValue == null) {
                    "Error #4"
                } else {
                    AttachmentFormat.format(it.newValue)
                }
            }

        override fun parseValue(
            value: String?,
            statuses: List<IdName>,
            priorities: List<IdName>,
            trackers: List<IdName>,
        ): String? {
            return null
        }
    }

    sealed class CustomField(val name: String, title: String) : DetailProperty("cf", title) {
        object Deadline : CustomField("10", "Дедлайн") {
            override fun parseValue(
                value: String?,
                statuses: List<IdName>,
                priorities: List<IdName>,
                trackers: List<IdName>,
            ): String? {
                return value?.parseDeadlineString()?.formatDate()
            }
        }
    }

    sealed class Attribute(val name: String, title: String) : DetailProperty("attr", title) {
        object DoneRatio : Attribute("done_ratio", "Готовность") {
            override fun parseValue(
                value: String?,
                statuses: List<IdName>,
                priorities: List<IdName>,
                trackers: List<IdName>,
            ): String? {
                return value?.plus("%")
            }
        }

        object Status : Attribute("status_id", "Статус") {
            override fun parseValue(
                value: String?,
                statuses: List<IdName>,
                priorities: List<IdName>,
                trackers: List<IdName>,
            ): String? {
                return value?.let {
                    val statusId = it.toIntOrNull()
                    statuses.firstOrNull { status ->
                        status.id == statusId
                    }?.name
                }
            }
        }

        object Priority : Attribute("priority_id", "Приоритет") {
            override fun parseValue(
                value: String?,
                statuses: List<IdName>,
                priorities: List<IdName>,
                trackers: List<IdName>,
            ): String? {
                return value?.let {
                    val priorityId = it.toIntOrNull()
                    priorities.firstOrNull { priority ->
                        priority.id == priorityId
                    }?.name
                }
            }
        }

        object Tracker : Attribute("tracker_id", "Трекер") {
            override fun parseValue(
                value: String?,
                statuses: List<IdName>,
                priorities: List<IdName>,
                trackers: List<IdName>,
            ): String? {
                return value?.let {
                    val trackerId = it.toIntOrNull()
                    trackers.firstOrNull { tracker ->
                        tracker.id == trackerId
                    }?.name
                }
            }
        }

        object Description : Attribute("description", "Описание") {
            override val customParse: (JournalDetails) -> String
                get() = { DescriptionFormat }

            override fun parseValue(
                value: String?,
                statuses: List<IdName>,
                priorities: List<IdName>,
                trackers: List<IdName>,
            ): String? {
                return null
            }
        }

        object AssignedTo : Attribute("assigned_to_id", "Исполнитель") {
            override val customParse: (JournalDetails) -> String
                get() = { AssignedToFormat }

            override fun parseValue(
                value: String?,
                statuses: List<IdName>,
                priorities: List<IdName>,
                trackers: List<IdName>,
            ): String? {
                return null
            }
        }
    }
}