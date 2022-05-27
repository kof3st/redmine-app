package me.kofesst.android.redminecomposeapp.domain.model

data class IdName(
    val id: Int,
    val name: String,
) {
    companion object {
        /**
         * Так метод /enumerations/issue_priorities.json в RedmineAPI
         * не всегда корректно возвращает ответ, используется данный
         * словарь приоритетов задачи. В случае изменения системы
         * приоритетов в API, следует обновить данный словарь
         * до актуального.
         */
        val priorities = listOf(
            IdName(id = 3, name = "Низкий"),
            IdName(id = 4, name = "Нормальный"),
            IdName(id = 5, name = "Высокий"),
            IdName(id = 6, name = "Срочный"),
            IdName(id = 7, name = "Немедленный"),
        )
    }

    override fun toString(): String = name
}