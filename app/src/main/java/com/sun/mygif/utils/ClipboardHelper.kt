package com.sun.mygif.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

class ClipboardHelper {
    companion object {
        @JvmStatic
        fun copyText(context: Context, label: String, content: String) {
            getClipboardManager(context).primaryClip = getClipData(label, content)
        }

        @JvmStatic
        fun getClipboardManager(context: Context) =
            (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)

        @JvmStatic
        fun getClipData(label: String, content: String) = ClipData.newPlainText(label, content)
    }
}
