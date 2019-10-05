package com.sun.mygif.data.source.remote

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection

@Throws(IOException::class)
fun HttpURLConnection.build(method: String) = apply { requestMethod = method }.connect()

@Throws(IOException::class)
fun InputStreamReader.getJsonString(): String {
    val reader = BufferedReader(this)
    return StringBuilder().apply {
        var inputLine = reader.readLine()
        while (inputLine != null) {
            append(inputLine)
            inputLine = reader.readLine()
        }
        reader.close()
    }.toString()
}
