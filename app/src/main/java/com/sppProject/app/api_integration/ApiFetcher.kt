package com.sppProject.app.api_integration

/**
 * A generic class to manage API calls for different types of data.
 *
 * @param service The API service instance to use for calls.
 */
class ApiFetcher<T>(
    private val service: Any // we can change to something more specific
) {
    suspend fun handleApiCallList(call: suspend () -> List<T>): List<T> {
        return call() // Directly call the suspend function
    }

    suspend fun handleApiCallSingle(call: suspend () -> T): T {
        return call() // Directly call the suspend function
    }
}

