package com.example.it_project

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity : BaseActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var USER_KEY: String = "User"
    private lateinit var registrationButton: Button
    private lateinit var registrationEmail: EditText
    private lateinit var registrationPassword: EditText
    private lateinit var registrationFirstName: EditText
    private lateinit var registrationSecondName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initToolbar(true)
        setToolbarTitle("Регистрация нового пользователя")
        enableUpButton()
        init()



    //var user: FirebaseUser? = auth.currentUser
    registrationButton.setOnClickListener {
        if (!TextUtils.isEmpty(registrationEmail.text.toString()) && !TextUtils.isEmpty(
                registrationPassword.text.toString()
            )
        ) {
            auth.createUserWithEmailAndPassword(
                registrationEmail.text.toString(),
                registrationPassword.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.sendEmailVerification()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    //var id: String? = database.key
                                    var name: String = registrationFirstName.text.toString()
                                    var secName: String = registrationSecondName.text.toString()
                                    var email: String = registrationEmail.text.toString()
                                    //var userInfo: User = User(name, secName, email)
                                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(secName) && !TextUtils.isEmpty(email)
                                    ) {
                                        //database.push().setValue(userInfo)
                                        //database.child("users").child(id!!).setValue(userInfo)
                                        CURRENT_UID = FirebaseAuth.getInstance().uid.toString()
                                        createUserInDatabase(name, secName, email)
                                        Toast.makeText(this, "Аккаунт создан", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_SHORT).show()
                                    }
                                    startActivity(Intent(this, SignInActivity::class.java))
                                    finish()
                                }
                            }
                    } else {
                        Toast.makeText(baseContext, "Sign Up failed. Try again after some time.", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(applicationContext, "Please enter Email and Password", Toast.LENGTH_SHORT).show()
        }
    }
}

    open fun init() {
        registrationButton = findViewById(R.id.registration_button)
        registrationEmail = findViewById(R.id.registration_email_form)
        registrationPassword = findViewById(R.id.registration_password_form)
        registrationFirstName = findViewById(R.id.registration_name_form)
        registrationSecondName = findViewById(R.id.registration_surname_form)
        auth = FirebaseAuth.getInstance()
        //database = FirebaseDatabase.getInstance().getReference(USER_KEY)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                ActivityUtilities.getInstance().invokeNewActivity(
                    this@RegisterActivity,
                    SignInActivity::class.java,
                    true
                )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}