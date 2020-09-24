package com.example.it_project.activities

import android.os.Bundle
import android.view.MenuItem
import com.example.it_project.utilities.ActivityUtilities
import com.example.it_project.R

class AboutDevActivity : BaseActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_dev)
        initToolbar(true)
        setToolbarTitle("О приложении")
        enableUpButton()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                ActivityUtilities.getInstance()
                    .invokeNewActivity(this@AboutDevActivity, MainActivity::class.java, true)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}