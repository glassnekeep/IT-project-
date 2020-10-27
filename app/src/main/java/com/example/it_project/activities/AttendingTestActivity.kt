package com.example.it_project.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import com.example.it_project.Communicator
import com.example.it_project.R
import com.example.it_project.fragments.StartFragment
import com.example.it_project.fragments.questionFragments.ComparisonAnswerQuestionTestFragment
import com.example.it_project.fragments.questionFragments.ManyAnswersQuestionTestFragment
import com.example.it_project.fragments.questionFragments.OneAnswerQuestionTestFragment
import com.example.it_project.fragments.questionFragments.TerminAnswerQuestionTestFragment
import com.example.it_project.models.RepresentModel
import com.example.it_project.values.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AttendingTestActivity : BaseActivity(), Communicator {

    private lateinit var privacy: String

    private lateinit var testName: String

    private var position: Int = 0

    private lateinit var listData: ArrayList<RepresentModel>

    private lateinit var answerList: ArrayList<String>

    private lateinit var correctAnswerList: ArrayList<String>

    private lateinit var nextQuestionButton: Button

    private lateinit var previousQuestionButton: Button

    private lateinit var currentQuestionType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attending_test)
        initToolbar(true)
        init()
        enableUpButton()
        observePosition()
        val extras: Bundle? = intent.extras
        if(extras != null) {
            privacy = extras.getString("privacy").toString()
            testName = extras.getString("testName").toString()
            setToolbarTitle(extras.getString("testName").toString())
        }
        getDataFromDb()
            /*var position: Int by Delegates.observable(0) {property, oldValue, newValue ->
            if(newValue == 0) {
                nextQuestionButton.visibility = View.GONE
                previousQuestionButton.visibility = View.GONE
            }
            if((newValue > 1) && (newValue <= listData.size)) {
                previousQuestionButton.visibility = View.VISIBLE
            }
            if((newValue < listData.size) && (newValue > 0)) {
                nextQuestionButton.visibility = View.VISIBLE
            }
        }*/
        //getDataFromDb()
        if(position == 0) {
            /*if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, StartFragment())
                    .commit()
            }*/
            val fragmentStart = StartFragment()
            val bundle = Bundle()
            bundle.putString("input_txt", position.toString())
            val transaction = this.supportFragmentManager.beginTransaction()
            fragmentStart.arguments = bundle
            transaction.replace(R.id.fragmentContainer, fragmentStart)
            transaction.addToBackStack(null)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.commit()
        }
        Log.d("listData.size", listData.size.toString())

        nextQuestionButton.setOnClickListener{
            if(position < listData.size) {
                position++
                currentQuestionType = listData[position-1].type
                when(currentQuestionType) {
                    "Comparison" -> if (savedInstanceState == null) {
                        supportFragmentManager.beginTransaction()
                            .add(R.id.fragmentContainer, ComparisonAnswerQuestionTestFragment())
                            .commit()
                    }
                    "Termin" -> if (savedInstanceState == null) {
                        supportFragmentManager.beginTransaction()
                            .add(R.id.fragmentContainer, TerminAnswerQuestionTestFragment())
                            .commit()
                    }
                    "One Answer" -> if (savedInstanceState == null) {
                        supportFragmentManager.beginTransaction()
                            .add(R.id.fragmentContainer, OneAnswerQuestionTestFragment())
                            .commit()
                    }
                    "Many Answers" -> if (savedInstanceState == null) {
                        supportFragmentManager.beginTransaction()
                            .add(R.id.fragmentContainer, ManyAnswersQuestionTestFragment())
                            .commit()
                    }
                }
            }
            observePosition()
        }

        previousQuestionButton.setOnClickListener {
            if(position > 1) {
                position--
                currentQuestionType = listData[position-1].type
                when(currentQuestionType) {
                    "Comparison" -> if (savedInstanceState == null) {
                        supportFragmentManager.beginTransaction()
                            .add(R.id.fragmentContainer, ComparisonAnswerQuestionTestFragment())
                            .commit()
                    }
                    "Termin" -> if (savedInstanceState == null) {
                        supportFragmentManager.beginTransaction()
                            .add(R.id.fragmentContainer, TerminAnswerQuestionTestFragment())
                            .commit()
                    }
                    "One Answer" -> if (savedInstanceState == null) {
                        supportFragmentManager.beginTransaction()
                            .add(R.id.fragmentContainer, OneAnswerQuestionTestFragment())
                            .commit()
                    }
                    "Many Answers" -> if (savedInstanceState == null) {
                        supportFragmentManager.beginTransaction()
                            .add(R.id.fragmentContainer, ManyAnswersQuestionTestFragment())
                            .commit()
                    }
                }
            }
            observePosition()
        }
    }

    override fun passData(textInput: String) {
        Log.d("textInput", textInput)
    }

    private fun getDataFromDb() {
        val questionsListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(listData.size > 0) {listData.clear()}
                for(questionInfo: DataSnapshot in snapshot.children) {
                    var name = questionInfo.child("name").getValue(String::class.java)
                    var type = questionInfo.child("type").getValue(String::class.java)
                    var answerNumber =
                        questionInfo.child("answerNumber").getValue(String::class.java)
                    //var correctAnswer = questionInfo.child("correctAnswer").getValue(ArrayList::class.java)
                    if (correctAnswerList.size > 0) {
                        correctAnswerList.clear()
                    }
                    for (correctAnswer: DataSnapshot in questionInfo.child("correctAnswer").children) {
                        var correctAnswer: String? = correctAnswer.getValue(String::class.java)
                        if (correctAnswer != null) {
                            correctAnswerList.add(correctAnswer)
                            Log.d("CorrectAnswer", correctAnswer)
                        }
                    }
                    if (answerList.size > 0) {
                        answerList.clear()
                    }
                    if (type == "Comparison") {
                        for (answerInfo: DataSnapshot in questionInfo.child("firstParts").children) {
                            var answer: String? = answerInfo.getValue(String::class.java)
                            if (answer != null) {
                                answerList.add(answer)
                                Log.d("AnswerComparison", answer)
                            }
                        }
                        for (answerInfo: DataSnapshot in questionInfo.child("secondParts").children) {
                            var answer: String? = answerInfo.getValue(String::class.java)
                            if (answer != null) {
                                answerList.add(answer)
                                Log.d("AnswerComparison", answer)
                                Log.d("answerList", answerList.size.toString())
                            }
                        }
                        var representModel: RepresentModel = RepresentModel(name!!, type!!, answerNumber!!, correctAnswerList, answerList)
                        listData.add(representModel)
                    }
                    else {
                        for (answerInfo: DataSnapshot in questionInfo.child("answers").children) {
                            var answer: String? = answerInfo.getValue(String::class.java)
                            if (answer != null) {
                                answerList.add(answer)
                                Log.d("Answer", answer)
                            }
                        }
                        var representModel: RepresentModel = RepresentModel(name!!, type!!, answerNumber!!, correctAnswerList, answerList)
                        listData.add(representModel)
                    }
                    Log.d("list", listData.size.toString())
                }
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
        correctAnswerList = ArrayList()
        nextQuestionButton = findViewById(R.id.button_next_question)
        previousQuestionButton = findViewById(R.id.button_previous_question)
        privacy = ""
        testName = ""
        currentQuestionType = ""
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //startActivity(Intent(this@AttendingTestActivity, MainActivity::class.java))
                //finish()
                //GROUP_LIST = ArrayList()
                openQuitDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openQuitDialog() {
        var quitDialog = AlertDialog.Builder(
            this@AttendingTestActivity
        )
        quitDialog.setTitle("Вы уверены, что хотите прекратить прохождение теста?")

        quitDialog.setPositiveButton("Да!"
        ) { dialog, which ->
            startActivity(Intent(this@AttendingTestActivity, MainActivity::class.java))
            finish()
        }

        quitDialog.setNegativeButton("Нет"
        ) { dialog, which ->
        }
        quitDialog.show()
    }

    override fun onBackPressed() {
        //startActivity(Intent(this@AttendingTestActivity, MainActivity::class.java))
        //GROUP_LIST = ArrayList()
        //finish()
        openQuitDialog()
    }

    private fun observePosition() {
        if(position == 0) {
            nextQuestionButton.visibility = View.GONE
            previousQuestionButton.visibility = View.GONE
        }
        if((position > 1) && (position <= listData.size)) {
            previousQuestionButton.visibility = View.VISIBLE
        }
        if((position < listData.size) && (position > 0)) {
            nextQuestionButton.visibility = View.VISIBLE
        }
    }
}