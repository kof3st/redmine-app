package me.kofesst.android.redminecomposeapp.feature.domain.usecase

import me.kofesst.android.redminecomposeapp.feature.domain.util.ValidationResult

class ValidateForEmptyField {

    operator fun invoke(value: String): ValidationResult {
        if (value.isBlank()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Это обязательное поле"
            )
        }

        return ValidationResult(true)
    }
}