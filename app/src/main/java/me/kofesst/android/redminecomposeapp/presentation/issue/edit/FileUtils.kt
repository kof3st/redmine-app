package me.kofesst.android.redminecomposeapp.presentation.issue.edit

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

var hasStoragePermission: Boolean = false

val filePickerIntent = Intent().apply {
    type = "*/*"
    action = ACTION_GET_CONTENT
}

@Composable
fun rememberPermissionLauncher(
    onAccess: () -> Unit = {},
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
) {
    hasStoragePermission = it

    if (hasStoragePermission) {
        onAccess()
    }
}

@Composable
fun rememberFilePicker(
    onFileSelected: (Intent) -> Unit = {},
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult()
) {
    if (it.resultCode == RESULT_OK && it.data != null) {
        onFileSelected(it.data!!)
    }
}

fun getFileMimeType(extension: String) =
    MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "*/*"

fun getFileName(
    uri: Uri,
    resolver: ContentResolver,
): String {
    val cursor = resolver.query(uri, null, null, null, null)
        ?: return "Invalid"
    cursor.moveToFirst()

    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    val fileName = cursor.getString(nameIndex)

    cursor.close()
    return fileName
}

val String.fileExtension: String
    get() = substring(lastIndexOf('.'))