package com.example.movieinfomation.other

import android.content.Context
import androidx.appcompat.app.AlertDialog

object AppUtils {
    fun showDialogError(context: Context) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder
            .setMessage("Sorry! Unable to download data. Please check your network connection or try again later.")
            .setTitle("Error!!!")
            .setPositiveButton("Wait") { dialog, _ ->
                dialog.cancel()
            }
            .setNegativeButton("Close app") { _, _ ->
                android.os.Process.killProcess(android.os.Process.myPid())

            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}