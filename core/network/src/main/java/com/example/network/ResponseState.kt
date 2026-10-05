package com.example.network

import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


sealed interface ResponseState<out T> {
    data class Success<T>(val data: T?) : ResponseState<T>

    data class Error<T>(val error: ErrorState, val message: String? = null, val data: T? = null) :
        ResponseState<T>

}


//sealed interface is a marker interface, no exhaustive when, does not deal with class or constructor
// more flexible as you can implement Parcelable,Loggable inside
sealed interface ErrorState {
    data object ServerDownError : ErrorState
    data object NotFoundError : ErrorState
    data object NoInternetException : ErrorState
    data object UndefinedError : ErrorState
}

suspend fun <T> safeApiCall(safeCall: suspend () -> T): ResponseState<T> {
    return try {
        ResponseState.Success(safeCall())

    } catch (httpException: HttpException) {
        when (httpException.code()) {
            ErrorCode.CLIENT_ERROR -> ResponseState.Error(ErrorState.NotFoundError)
            in ErrorCode.ERROR_SERIES -> ResponseState.Error(ErrorState.ServerDownError)
            else -> ResponseState.Error(ErrorState.UndefinedError)
        }
    }catch (e: UnknownHostException){
        ResponseState.Error(ErrorState.NoInternetException,e.message)
    }
    catch (e: SocketTimeoutException){
        ResponseState.Error(ErrorState.ServerDownError,e.message)
    }
}

object ErrorCode {
    const val CLIENT_ERROR = 404
    val ERROR_SERIES = (404..599)
}

// Ek marker interface — batata hai ki ye error retry-capable hai
/*
interface Retryable {
    val retryCount: Int
}

sealed interface ErrorStat {
    data object ServerDownError : ErrorStat, Retryable {
        override val retryCount = 3
    }

    data object NoInternetException : ErrorStat, Retryable {
        override val retryCount = 5
    }

    data object UndefinedError : ErrorState  // Retryable nahi

    data object NotFoundError : ErrorState   // Retryable nahi
}*/
