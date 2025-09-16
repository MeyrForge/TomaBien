package com.meyrforge.tomabien.common

sealed class Result<out T, out E> {
    data class Success<out T>(val data: T) : Result<T, Nothing>()
    data class Error<out E>(val error: E) : Result<Nothing, E>()
}

sealed class RepositoryError {
    object MedicationNotFound : RepositoryError()
    object DatabaseError : RepositoryError()
    data class UnknownError(val message: String?) : RepositoryError()
}
