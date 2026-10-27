package com.route.api.helpers

import com.route.api.data.BaseResponse
import com.route.domain.data.Resource
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.HttpException

fun parseApiError(t: Throwable): Throwable {
    if (t is HttpException) {
        try {
            val errorBody = t.response()?.errorBody()?.string()
            if (!errorBody.isNullOrBlank()) {
                val json = JSONObject(errorBody)
                val message = when {
                    json.has("message") && json.optString("message").isNotEmpty() && json.optString("message") != "fail" ->
                        json.optString("message")
                    json.has("errors") && json.optJSONObject("errors") != null -> {
                        val errorsObj = json.getJSONObject("errors")
                        if (errorsObj.has("msg")) errorsObj.optString("msg") else errorBody
                    }
                    else -> t.message
                }
                if (!message.isNullOrBlank() && message != "fail") {
                    return Exception(message)
                }
            }
        } catch (_: Exception) {
        }
    }
    return t
}

fun<T> safeApiCall(api : suspend ()-> BaseResponse<T>) = flow<Resource<T>> {
    try {
        emit(Resource.Loading())
        val response = api.invoke()
        emit(Resource.Success(data = response) as Resource<T>)
    }catch (t: Throwable){
        emit(Resource.Error(parseApiError(t)))
    }
}

fun<T,R> safeApiCallV2(api : suspend ()-> BaseResponse<T>,
                       converter: (T?) -> R?) = flow<Resource<R?>> {
    try {
        emit(Resource.Loading())
        val response = api.invoke()
        val convertedData = converter.invoke(response.data)
        emit(Resource.Success(data = convertedData))
    }catch (t: Throwable){
        emit(Resource.Error(parseApiError(t)))
    }
}

fun <T, R> safeAuthCall(
    api: suspend () -> T,
    converter: (T) -> R?
) = flow<Resource<R?>> {
    try {
        emit(Resource.Loading())
        val response = api.invoke()
        val convertedData = converter.invoke(response)
        emit(Resource.Success(data = convertedData))
    } catch (t: Throwable) {
        emit(Resource.Error(parseApiError(t)))
    }
}
