package com.example.it_project

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.it_project.models.IdModel
import com.example.it_project.models.QuestionModel
import com.example.it_project.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private var backPressed: Long = 0

fun showToast(context: Context?, message: String?) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    CURRENT_UID = AUTH.uid.toString()
    DATABASE_ROOT_USER = REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
    DATABASE_ROOT_NEW_TEST = REF_DATABASE_ROOT.child(NODE_TEST)
    DATABASE_ROOT_TEST_IDS = REF_DATABASE_ROOT.child(NODE_TEST_IDS)
    USER = User()
}


fun createTestIDWithName(testName: String) {
    var id = DATABASE_ROOT_TEST_IDS.push().key
    var idName: IdModel = IdModel(id!!, testName)
    DATABASE_ROOT_TEST_IDS.child(id!!).child(NODE_ID).setValue(idName)
    DATABASE_ROOT_NEW_TEST.child(testName).child(NODE_ID).setValue(id)
}

fun createQuestionInTest(testName : String, questionInfo: QuestionModel, answers: ArrayList<String>) {
    DATABASE_ROOT_NEW_TEST.child(testName).child(NODE_QUESTIONS).setValue(questionInfo)
    var questionOrder = 1
    for (answer in answers) {
        DATABASE_ROOT_NEW_TEST.child(testName).child(NODE_QUESTIONS).child(NODE_ANSWERS).child("Answer №${questionOrder}").setValue(answer)
        questionOrder++
    }
}

fun createUserInDatabase(name: String, secName: String, email: String) {
    NEW_USER = User(name, secName, email)
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).setValue(NEW_USER)
}

fun tapPromtToExit(activity: Activity) {
    if (backPressed + 2500 > System.currentTimeMillis()) {
        activity.finish()
        //super.onBackPressed()
    } else {
        showToast(activity.applicationContext, activity.resources.getString(R.string.tap_again))
    }
    backPressed = System.currentTimeMillis()
}

fun getUserName(){
    var userNameEmailListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val user = dataSnapshot.getValue(User::class.java)
            val name = user?.name
        }
        override fun onCancelled(error: DatabaseError) {

        }
    }
}



