package me.kofesst.android.redminecomposeapp.domain.usecase

import me.kofesst.android.redminecomposeapp.domain.repository.RedmineRepository
import java.io.File

class UploadFile(
    private val repository: RedmineRepository
) {

    suspend operator fun invoke(file: File, type: String): String {
        return repository.uploadFile(file, type)
    }
}