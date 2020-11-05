package com.example.it_project.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.it_project.*
import com.example.it_project.utilities.*
import com.example.it_project.values.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener


class RegisterActivity : BaseActivity() {

    var adminStatus: String = "user"
    private lateinit var adminCode: EditText
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var USER_KEY: String = "User"
    private lateinit var registrationButton: Button
    private lateinit var registrationEmail: EditText
    private lateinit var registrationPassword: EditText
    private lateinit var registrationFirstName: EditText
    private lateinit var registrationSecondName: EditText
    private var emailList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initToolbar(true)
        setToolbarTitle("Регистрация нового пользователя")
        enableUpButton()
        init()
        initFirebase()
        fillList()



    //var user: FirebaseUser? = auth.currentUser
    registrationButton.setOnClickListener {
        if ((!TextUtils.isEmpty(registrationEmail.text.toString())) &&
            (!TextUtils.isEmpty(registrationPassword.text.toString())) &&
            (!TextUtils.isEmpty(registrationFirstName.text.toString())) &&
            (!TextUtils.isEmpty(registrationSecondName.text.toString()))
        ) {
            if(!emailList.contains(registrationEmail.text.toString())) {
                if(registrationPassword.text.toString().length > 5) {
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
                                            var name: String = registrationFirstName.text.toString()
                                            var secName: String = registrationSecondName.text.toString()
                                            var email: String = registrationEmail.text.toString()
                                            var code = adminCode.text.toString()
                                            CURRENT_UID = FirebaseAuth.getInstance().uid.toString()
                                            if(!TextUtils.isEmpty(code)) {var trueCode: String? = null
                                                val codeListener = object: ValueEventListener {
                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                        trueCode = snapshot.getValue(Long::class.java).toString()
                                                        administrator = snapshot.getValue(Long::class.java)
                                                        if(code == trueCode) {
                                                            adminStatus = "admin"
                                                            createUserInDatabase(name, secName, email, adminStatus)
                                                            invokeNewActivity(this@RegisterActivity, SignInActivity::class.java, true)
                                                            showToast(this@RegisterActivity, "Аккаунт создан")
                                                        }
                                                        else {
                                                            Toast.makeText(this@RegisterActivity, "Код преподавателя неверен", Toast.LENGTH_SHORT).show()
                                                            val currentUser = FirebaseAuth.getInstance().currentUser
                                                            user.delete().addOnCompleteListener { task ->
                                                                if(task.isSuccessful) {
                                                                    Log.d("TAG", "Account deleted")
                                                                }
                                                            }
                                                        }
                                                    }
                                                    override fun onCancelled(error: DatabaseError) {

                                                    }
                                                }
                                                REF_DATABASE_ROOT.child("CURRENT_ADMIN_KEY").addValueEventListener(codeListener)
                                            }
                                            else {
                                                createUserInDatabase(name, secName, email, adminStatus)
                                                invokeNewActivity(this@RegisterActivity, SignInActivity::class.java, true)
                                                Toast.makeText(this, "Аккаунт создан", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                            } else {
                                Toast.makeText(baseContext, "Sign Up failed. Try again after some time.", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Пароль должен быть длиной не менее 6 символов", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Пользователь с таким email уже зарегистрирован", Toast.LENGTH_SHORT).show()
            }
            //TODO поместить в блок процесс аутентификации
        } else {
            Toast.makeText(applicationContext, "Не все поля заполнены", Toast.LENGTH_SHORT).show()
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
        adminCode = findViewById(R.id.teacherCode)
        //database = FirebaseDatabase.getInstance().getReference(USER_KEY)
    }

    private fun fillList() {
        val listener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(id in snapshot.children) {
                    var name = id.child("email").getValue(String::class.java)
                    if(name != null) {
                        emailList.add(name)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        REF_DATABASE_ROOT.child(NODE_USERS).addListenerForSingleValueEvent(listener)
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

    private fun signUp(email: String, password: String) {

    }
}