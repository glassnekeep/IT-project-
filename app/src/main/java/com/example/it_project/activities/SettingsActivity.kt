package com.example.it_project.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.it_project.*
import com.example.it_project.models.User
import com.example.it_project.utilities.initFirebase
import com.example.it_project.values.DATABASE_ROOT_USER
import com.example.it_project.values.USER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class SettingsActivity : BaseActivity() {

    private lateinit var exitButton: ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var textViewName: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var settingsTextViewEmail: TextView
    private lateinit var settingsTextViewUserName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        init()
        initFirebase()
        textViewName = findViewById(R.id.settings_full_name)
        textViewEmail = findViewById(R.id.status_email)
        settingsTextViewEmail = findViewById(R.id.settings_email)
        settingsTextViewUserName = findViewById(R.id.settings_username)
        val dataListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                USER = snapshot.getValue(User::class.java)
                textViewName.setText("${USER?.name} ${USER?.secName}")
                textViewEmail.setText("${USER?.email}")
                settingsTextViewEmail.setText("${USER?.email}")
                settingsTextViewUserName.setText("${USER?.name} ${USER?.secName}")
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        DATABASE_ROOT_USER.addValueEventListener(dataListener)
        exitButton.setOnClickListener {
            openQuitDialog()
        }
    }

    private fun init() {
        exitButton = findViewById(R.id.exit_button)
        auth = FirebaseAuth.getInstance()
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
            auth.signOut()
            finish()
        }

        quitDialog.setNegativeButton("Нет"
        ) { dialog, which ->
            // TODO Auto-generated method stub
        }
        quitDialog.show()
    }
}