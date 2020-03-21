package com.erank.nsappsmoviesapi.utils

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar


fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    return connectivityManager?.activeNetworkInfo?.isConnected ?: false
}

fun Context.toast(
    msg: String,
    duration: Int = Toast.LENGTH_SHORT
) =
    Toast.makeText(this, msg, duration).show()

fun Context.toast(
    @StringRes resId: Int,
    duration: Int = Toast.LENGTH_SHORT
) =
    Toast.makeText(this, resId, duration).show()

fun Context.snackbar(
    view: View,
    msg: String,
    @Snackbar.Duration
    length: Int = Snackbar.LENGTH_SHORT
) =
    Snackbar.make(view, msg, length).show()

fun Context.snackbar(
    view: View,
    @StringRes msg: Int,
    @Snackbar.Duration
    duration: Int = Snackbar.LENGTH_SHORT
) =
    Snackbar.make(view, msg, duration).show()