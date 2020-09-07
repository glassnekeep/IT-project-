package com.example.it_project

import android.app.Activity
import android.content.Context
import android.widget.Toast

private var backPressed: Long = 0

fun showToast(context: Context?, message: String?) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun tapPromtToExit(activity: Activity) {
    if (backPressed + 2500 > System.currentTimeMillis()) {
        activity.finish()
        //super.onBackPressed()
    } else {
        showToast(activity.applicationContext, activity.resources.getString(R.string.tap_again))
    }
    backPressed = System.currentTimeMillis()
}
