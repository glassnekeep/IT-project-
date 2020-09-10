package com.example.it_project

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Text


class SettingsActivity : BaseActivity() {

    private lateinit var exitButton: ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var textViewName: TextView
    private lateinit var textViewEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        init()
        initFirebase()
        textViewName = findViewById(R.id.settings_full_name)
        textViewEmail = findViewById(R.id.status_email)
        val dataListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                USER = snapshot.getValue(User::class.java)
                Toast.makeText(this@SettingsActivity, "${USER?.email}", Toast.LENGTH_SHORT).show()
                textViewName.setText("${USER?.name} ${USER?.secName}")
                textViewEmail.setText("${USER?.email}")
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).addValueEventListener(dataListener)
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