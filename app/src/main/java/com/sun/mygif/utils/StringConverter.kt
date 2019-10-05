package com.sun.mygif.utils

import android.util.Log
import com.sun.mygif.data.model.Gif
import com.sun.mygif.data.model.Topic
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.ParseException

private const val TAG = "StringConverter"
private const val HASH_MD5 = "MD5"
private const val CHARSET_UTF_8 = "UTF-8"
private const val FORMAT_HEX = "%02X"

/** Convert String to MD5 hash. */
@Throws(NoSuchAlgorithmException::class, UnsupportedEncodingException::class)
fun String.computeMD5Hash(): ByteArray = MessageDigest.getInstance(HASH_MD5).apply {
    update(toByteArray(charset(CHARSET_UTF_8)), 0, length)
}.digest()

/** Convert array of arbitrary bytes into a String of hexadecimal number-pairs. */
fun ByteArray.toHexString(): String = StringBuilder().also {
    it.append(this.map { byte -> String.format(FORMAT_HEX, byte) })
}.toString()

fun String.decodeToTopic(): Topic? {
    val properties = split(SEPARATOR_PROPERTY)
    return if (properties.size != 5) null else try {
        Topic(
            title = properties[0],
            gifBackground = Gif(
                id = properties[1],
                width = properties[2].toInt(),
                height = properties[3].toInt(),
                url = properties[4]
            )
        )
    } catch (exception: ParseException) {
        Log.e(TAG, exception.message.toString())
        null
    }
}
