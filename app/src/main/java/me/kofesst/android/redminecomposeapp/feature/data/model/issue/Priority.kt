package me.kofesst.android.redminecomposeapp.feature.data.model.issue

data class Priority(
    val id: Int,
    val name: String
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
            Priority(id = 3, name = "Низкий"),
            Priority(id = 4, name = "Нормальный"),
            Priority(id = 5, name = "Высокий"),
            Priority(id = 6, name = "Срочный"),
            Priority(id = 7, name = "Немедленный"),
        )
    }
}