package com.sppProject.app.api_integration

import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ApiFetcher<T>(
    private val service: Any // Use a more specific type if possible
) {
    suspend fun handleApiCallList(call: suspend () -> List<T>): List<T> {
        return call() // Directly call the suspend function
    }

    suspend fun handleApiCallSingle(call: suspend () -> T): T {
        return call() // Directly call the suspend function
    }
}

