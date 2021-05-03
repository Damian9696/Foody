package com.example.foody.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

class SnackBarUtil {
    companion object {
        fun showSnackBarWithMessage(view: View, message: String) {
            Snackbar.make(
                view,
                message,
                Snackbar.LENGTH_SHORT
            ).setAction("Okey") {}.show()
        }
    }
}