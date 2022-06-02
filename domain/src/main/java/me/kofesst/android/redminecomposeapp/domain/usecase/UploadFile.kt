package me.kofesst.android.redminecomposeapp.domain.usecase

import me.kofesst.android.redminecomposeapp.domain.repository.RedmineRepository
import java.io.File

class UploadFile(
    private val repository: RedmineRepository
) {

    suspend operator fun invoke(fileContent: ByteArray, fileName: String, type: String): String {
        return repository.uploadFile(fileContent, fileName, type)
    }
}