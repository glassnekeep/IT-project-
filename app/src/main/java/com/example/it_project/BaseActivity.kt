package com.example.it_project

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

private lateinit var activity: Activity
private var context: Context? = null
private var toolbar: Toolbar? = null


class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this@BaseActivity
        context = activity.applicationContext
    }

    fun initToolbar(isTitleEnabled: Boolean) {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(isTitleEnabled)
    }

    fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    fun enableUpButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


}