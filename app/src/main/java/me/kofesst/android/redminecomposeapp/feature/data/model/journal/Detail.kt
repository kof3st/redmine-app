package me.kofesst.android.redminecomposeapp.feature.data.model.journal

data class Detail(
    val name: String,
    val new_value: String,
    val old_value: String?,
    val `property`: String
) {
//    companion object {
//        private const val attachmentProperty = "attachment"
//        private const val attrProperty = "attr"
//
//        private const val attachmentDetailFormat = "Файл %s добавлен"
//
//        private const val attrDetailFormat = "Параметр %s изменился %s%s"
//        private const val fromOldValueFormat = "с %s "
//        private const val toNewValueFormat = "на %s"
//    }
//
//    fun getDetailText(
//        trackers: List<Tracker>,
//        statuses: List<Status>
//    ): String {
//        return when (property) {
//            attachmentProperty -> attachmentDetailFormat.format(new_value)
//            attrProperty -> {
//                when (name) {
//                    Attribute.StatusAttr.attrId -> Attribute.StatusAttr.getDetailText(
//                        statuses,
//                        old_value,
//                        new_value
//                    )
//                    else -> "Invalid property"
//                }
//            }
//            else -> "Invalid property"
//        }
//    }
//
//    sealed class Attribute<T : Any>(val attrId: String, val name: String) {
//        protected abstract fun getItemId(item: T): Int
//        protected abstract fun getItemName(item: T): String
//
//        protected fun getItemName(
//            items: List<T>,
//            value: String
//        ): String {
//            return getItemName(
//                items.first {
//                    getItemId(it) == value.toInt()
//                }
//            )
//        }
//
//        fun getDetailText(
//            items: List<T>,
//            old_value: String?,
//            new_value: String
//        ): String {
//            return attrDetailFormat.format(
//                name,
//                if (old_value != null) {
//                    fromOldValueFormat.format(getItemName(items, old_value))
//                } else {
//                    ""
//                },
//                toNewValueFormat.format(getItemName(items, new_value))
//            )
//        }
//
//        object StatusAttr : Attribute<Status>("status_id", "Статус") {
//            override fun getItemId(item: Status): Int = item.id
//            override fun getItemName(item: Status): String = item.name
//        }
//    }
}