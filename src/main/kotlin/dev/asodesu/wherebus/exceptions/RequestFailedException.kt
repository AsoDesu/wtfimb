package dev.asodesu.wherebus.exceptions

import okhttp3.Response

class RequestFailedException(response: Response)
    : Exception("Request failed with status code ${response.code}. ${response.body?.string()}") {
        val code = response.code
    }