package com.example.it_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var registrationButton: Button
    private lateinit var registrationEmail: EditText
    private lateinit var registrationPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initToolbar(true)
        setToolbarTitle("Регистрация нового пользователя")
        enableUpButton()
        registrationButton = findViewById(R.id.registration_button)
        registrationEmail = findViewById(R.id.registration_email_form)
        registrationPassword = findViewById(R.id.registration_password_form)

        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        var user: FirebaseUser? = auth.currentUser
        registrationButton.setOnClickListener {
            if(!TextUtils.isEmpty(registrationEmail.text.toString()) && !TextUtils.isEmpty(registrationPassword.text.toString()))
            {
                //auth.createUserWithEmailAndPassword(registrationEmail.text.toString(),registrationPassword.text.toString()).addOnCompleteListener
                auth.createUserWithEmailAndPassword(registrationEmail.text.toString(), registrationPassword.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            user?.sendEmailVerification()
                                ?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        startActivity(Intent(this, SignInActivity::class.java))
                                        finish()
                                    }
                                }
                        } else {
                            Toast.makeText(
                                baseContext, "Sign Up failed. Try again after some time.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Please enter Email and Password", Toast.LENGTH_SHORT).show();
            }
        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                ActivityUtilities.getInstance().invokeNewActivity(this@RegisterActivity, MainActivity::class.java, true)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}