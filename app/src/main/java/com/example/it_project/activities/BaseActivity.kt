package com.example.it_project.activities

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.it_project.R

open class BaseActivity : AppCompatActivity() {


    private var context: Context? = null
    private lateinit var activity: Activity
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        activity = this@BaseActivity
        context = activity.applicationContext
    }

    open fun initToolbar(isTitleEnabled: Boolean) {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(isTitleEnabled)
    }

    open fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    open fun enableUpButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override open fun onBackPressed() {
        super.onBackPressed()
    }
}