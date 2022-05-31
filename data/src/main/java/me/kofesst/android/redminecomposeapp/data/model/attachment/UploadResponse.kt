package me.kofesst.android.redminecomposeapp.data.model.attachment

data class UploadResponse(
    val upload: Upload
)

data class Upload(
    val token: String
)

data class UploadDetails(
    val token: String,
    val filename: String,
    val content_type: String
)