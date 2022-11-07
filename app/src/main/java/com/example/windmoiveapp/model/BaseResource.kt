package com.example.windmoiveapp.model


data class ErrorMessage(val code: Int?, val message: String? = null)


data class BaseResource<out T>(
    var status: Status,
    val data: T?,
    val message: String?,
    val errorMessage: ErrorMessage?
) {
    companion object {
        fun <T> success(data: T?, message: String?): BaseResource<T> =
            BaseResource(
                status = Status.SUCCESS,
                data = data,
                message = message,
                errorMessage = null
            )

        fun <T> error(data: T?, errorMessage: ErrorMessage): BaseResource<T> =
            BaseResource(
                status = Status.ERROR,
                data = data,
                message = null,
                errorMessage = errorMessage
            )

        fun <T> loading(): BaseResource<T> =
            BaseResource(
                status = Status.LOADING,
                data = null,
                message = null,
                errorMessage = null
            )
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}
