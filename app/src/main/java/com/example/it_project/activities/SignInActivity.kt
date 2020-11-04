package com.example.it_project.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.it_project.R
import com.example.it_project.utilities.getCurrentUser
import com.example.it_project.utilities.initFirebase
import com.example.it_project.utilities.showToast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class SignInActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var signInEmail: EditText
    private lateinit var signInPassword: EditText
    private lateinit var signInButton: Button
    private lateinit var createNewAccount: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        createNewAccount = findViewById(R.id.create_new_account_button)
        signInEmail = findViewById(R.id.sign_in_email_form)
        signInPassword = findViewById(R.id.sign_in_password_form)
        signInButton = findViewById(R.id.sign_in_button)

        initToolbar(true)
        setToolbarTitle("Вход в аккаунт")
        enableUpButton()

        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = auth.currentUser
        if (user != null && user.isEmailVerified) {
            Toast.makeText(this, "User not null", Toast.LENGTH_SHORT).show()
            var intentToMain = Intent(this@SignInActivity, MainActivity::class.java)
            startActivity(intentToMain)
            this@SignInActivity.finish()
        } else {
            user?.sendEmailVerification()
            Toast.makeText(this, "Подтвердите Email", Toast.LENGTH_SHORT).show()
        }

        signInButton.setOnClickListener {
            if (!TextUtils.isEmpty(
                    signInEmail.text.toString()
                ) && !TextUtils.isEmpty(signInPassword.text.toString())
            ) {
                auth.signInWithEmailAndPassword(
                    signInEmail.text.toString(),
                    signInPassword.text.toString()
                ).addOnCompleteListener(this,
                    OnCompleteListener<AuthResult?> { task ->
                        if (task.isSuccessful) {
                            var currenUser = auth.currentUser
                            if(currenUser!!.isEmailVerified) {
                                Toast.makeText(
                                    applicationContext,
                                    "User SignIn Successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                initFirebase()
                                val intentMain = Intent(this, MainActivity::class.java)
                                startActivity(intentMain)
                                this@SignInActivity.finish()
                            }
                            else {
                                showToast(this, "Подтвердите ваш Email")
                            }
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "User SignIn failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        }

        createNewAccount.setOnClickListener {
            val intentRegister = Intent(this, RegisterActivity::class.java)
            startActivity(intentRegister)
        }
    }
}