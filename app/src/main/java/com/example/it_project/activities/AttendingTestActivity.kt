package com.example.it_project.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.example.it_project.R
import com.example.it_project.StartFragment
import com.example.it_project.models.RepresentModel
import com.example.it_project.values.DATABASE_ROOT_NEW_PRIVATE_TEST
import com.example.it_project.values.DATABASE_ROOT_NEW_PUBLIC_TEST
import com.example.it_project.values.NODE_QUESTIONS
import com.example.it_project.values.NODE_QUESTION_NAME
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AttendingTestActivity : BaseActivity() {

    private lateinit var privacy: String

    private lateinit var testName: String

    private var position: Int = 0

    private lateinit var listData: ArrayList<RepresentModel>

    private lateinit var answerList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attending_test)
        initToolbar(true)
        init()
        val extras: Bundle? = intent.extras
        if(extras != null) {
            privacy = extras.getString("privacy").toString()
            testName = extras.getString("testName").toString()
            setToolbarTitle(extras.getString("testName").toString())
        }
        getDataFromDb()
        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, StartFragment())
                .commit()
        }
        Log.d("listData.size", listData.size.toString())
    }

    private fun getDataFromDb() {
        val questionsListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(listData.size > 0) {listData.clear()}
                for(questionInfo: DataSnapshot in snapshot.children) {
                    var name = questionInfo.child("name").getValue(String::class.java)
                    var type = questionInfo.child("type").getValue(String::class.java)
                    var answerNumber = questionInfo.child("answerNumber").getValue(String::class.java)
                    var correctAnswer = questionInfo.child("correctAnswer").getValue(String::class.java)
                    if(answerList.size > 0) {answerList.clear()}
                    for(answerInfo: DataSnapshot in questionInfo.child("answers").children) {
                        var answer: String? = answerInfo.getValue(String::class.java)
                        if(answer != null) {
                            answerList.add(answer)
                            Log.d("Answer", answer)
                        }
                    }
                    var representModel: RepresentModel = RepresentModel(name!!, type!!, answerNumber!!, correctAnswer!!, answerList)
                    listData.add(representModel)
                }
                Log.d("list", listData.size.toString())
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        if(privacy == "Публичный") {
            DATABASE_ROOT_NEW_PUBLIC_TEST.child(testName).child(NODE_QUESTIONS).addListenerForSingleValueEvent(questionsListener)
            Log.d("listData.size", listData.size.toString())
        }
        else {
            DATABASE_ROOT_NEW_PRIVATE_TEST.child(testName).child(NODE_QUESTIONS).addListenerForSingleValueEvent(questionsListener)
        }
    }

    private fun init() {
        listData = ArrayList()
        answerList = ArrayList()
        privacy = ""
        testName = ""
    }
}