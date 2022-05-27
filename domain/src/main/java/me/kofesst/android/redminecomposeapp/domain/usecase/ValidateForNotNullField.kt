package me.kofesst.android.redminecomposeapp.domain.usecase

import me.kofesst.android.redminecomposeapp.domain.util.ValidationResult

class ValidateForNotNullField {

    operator fun invoke(value: Any?): ValidationResult {
        if (value == null) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Это обязательное поле"
            )
        }

        return ValidationResult(true)
    }
}