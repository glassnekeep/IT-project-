package com.example.it_project.utilities

import android.app.Activity
import android.content.Intent

fun invokeNewActivity(activity: Activity, tClass: Class<*>?, shouldFinish: Boolean) {
    val intent = Intent(activity, tClass)
    activity.startActivity(intent)
    if (shouldFinish) {
        activity.finish()
    }
}
