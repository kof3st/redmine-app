package me.kofesst.android.redminecomposeapp.data.model.attachment

import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "upload")
data class UploadDataDto(
    @PropertyElement
    val token: String,

    @PropertyElement(name = "filename")
    val fileName: String,

    @PropertyElement(name = "content_type")
    val type: String,
)