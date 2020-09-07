package com.example.it_project

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog


class SettingsActivity : BaseActivity() {

    private lateinit var exitButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        init()
        exitButton.setOnClickListener {
            openQuitDialog()
        }
    }

    private fun init() {
        exitButton = findViewById(R.id.exit_button)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        var intentToMain = Intent(this@SettingsActivity, MainActivity::class.java)
        startActivity(intentToMain)
        this.finish()
    }

    private fun openQuitDialog() {
        var quitDialog = AlertDialog.Builder(
            this@SettingsActivity
        )
        quitDialog.setTitle("Вы уверены, что хотите выйти из данного аккаунта?")

        quitDialog.setPositiveButton("Да!"
        ) { dialog, which ->
            startActivity(Intent(this@SettingsActivity, SignInActivity::class.java))
            finish()
        }

        quitDialog.setNegativeButton("Нет"
        ) { dialog, which ->
            // TODO Auto-generated method stub
        }
        quitDialog.show()
    }
}