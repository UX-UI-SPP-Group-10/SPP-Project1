package com.sppProject.app.api_integration

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A helper function to handle API calls.
 * This function makes the API call and processes the response.
 *
 * @param call The Retrofit call to execute.
 * @param onSuccess Callback for successful responses.
 * @param onError Callback for error responses.
 */
fun <T> handleApiCall(
    call: Call<T>,
    onSuccess: (T) -> Unit,
    onError: (String) -> Unit
) {
    call.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                response.body()?.let {
                    onSuccess(it) // Call success callback with data
                } ?: run {
                    onError("Error: Empty response body") // Handle empty body case
                }
            } else {
                val errorMsg = "Error: ${response.code()} ${response.message()}"
                println(errorMsg)
                onError(errorMsg) // Handle error from server
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            val errorMsg = "Failed to execute call: ${t.message}"
            println(errorMsg)
            onError(errorMsg) // Handle connection failure or exceptions
        }
    })
}
