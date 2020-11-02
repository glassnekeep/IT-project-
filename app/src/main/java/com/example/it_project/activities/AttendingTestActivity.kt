package com.example.it_project.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
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
import com.example.it_project.models.TableModel
import com.example.it_project.models.TotalModel
import com.example.it_project.utilities.createTestAttendanceByUserInTestNode
import com.example.it_project.utilities.createTestAttendanceByUserInUserNode
import com.example.it_project.values.*
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class AttendingTestActivity : BaseActivity(), Communicator {

    private lateinit var privacy: String

    private lateinit var subject: String

    private lateinit var testName: String

    private lateinit var testCreator: String

    private lateinit var time: String

    private var position: Int = 0

    private lateinit var listData: ArrayList<RepresentModel>

    private lateinit var tableData: ArrayList<TableModel>

    private var usedTime: Int = 0

    private lateinit var currentTime: TextView

    //private lateinit var answerList: ArrayList<String>

    //private lateinit var correctAnswerList: ArrayList<String>

    private lateinit var nextQuestionButton: Button

    private lateinit var previousQuestionButton: Button

    private lateinit var finishTestButton: Button

    private lateinit var currentQuestionType: String

    private var seconds: Int = 0

    private var minutes: Int = 0

    private var hours: Int = 0

    private val handler = Handler()

    private var timeFinish: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attending_test)
        initToolbar(true)
        init()
        enableUpButton()
        observePosition()
        val extras: Bundle? = intent.extras
        if(extras != null) {
            subject = extras.getString("subject").toString()
            privacy = extras.getString("privacy").toString()
            testName = extras.getString("testName").toString()
            testCreator = extras.getString("testCreator").toString()
            time = extras.getString("time").toString()
            usedTime = time.toInt()*60
            setToolbarTitle(extras.getString("testName").toString())
        }
        //var calendar = Calendar.getInstance()
        //val year = calendar.get(Calendar.YEAR)
        //val month = calendar.get(Calendar.MONTH)
        //val day = calendar.get(Calendar.DAY_OF_MONTH)
        //val hour = calendar.get(Calendar.HOUR_OF_DAY)
        //val minute = calendar.get(Calendar.MINUTE)
        //currentTime =
        getDataFromDb()
        //hours = (usedTime / (60*60))
        //minutes = ((usedTime / 60) % 60)
        //seconds = (usedTime % 60)

        //currentTime.text = ("${hours}:${minutes}:${seconds}")

        //updateTime()

        /*handler.post(object: Runnable {
            override fun run() {
                handler.postDelayed(this, 1000)
                updateTime()
            }
        })*/

        //getDataFromDb()
        if(position == 0) {
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
                        val fragmentComparison = ComparisonAnswerQuestionTestFragment()
                        val bundle = Bundle()
                        //listData.reverse()
                        bundle.putStringArrayList("answerList", listData[position-1].answerList)
                        bundle.putString("answerNumber", listData[position-1].answerNumber)
                        bundle.putString("questionName", "${position}. ${listData[position-1].name}")
                        //Log.d("listData[position-2]", listData[position-1].answerList[0])
                        val transaction = this.supportFragmentManager.beginTransaction()
                        fragmentComparison.arguments = bundle
                        transaction.replace(R.id.fragmentContainer, fragmentComparison)
                        transaction.addToBackStack(null)
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        transaction.commit()
                    }
                    "Termin" -> if (savedInstanceState == null) {
                        val fragmentTerminAnswer = TerminAnswerQuestionTestFragment()
                        val bundle = Bundle()
                        //listData.reverse()
                        bundle.putStringArrayList("answerList", listData[position-1].answerList)
                        bundle.putString("answerNumber", listData[position-1].answerNumber)
                        bundle.putString("questionName", "${position}. ${listData[position-1].name}")
                        Log.d("listData[position-2]", listData[position-1].answerList[0])
                        val transaction = this.supportFragmentManager.beginTransaction()
                        fragmentTerminAnswer.arguments = bundle
                        transaction.replace(R.id.fragmentContainer, fragmentTerminAnswer)
                        transaction.addToBackStack(null)
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        transaction.commit()
                    }
                    "One Answer" -> /*if (savedInstanceState == null) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, OneAnswerQuestionTestFragment())
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit()
                    }*/
                    {
                        val fragmentOneAnswer = OneAnswerQuestionTestFragment()
                        val bundle = Bundle()
                        //listData.reverse()
                        bundle.putStringArrayList("answerList", listData[position-1].answerList)
                        bundle.putString("answerNumber", listData[position-1].answerNumber)
                        bundle.putString("questionName", "${position}. ${listData[position-1].name}")
                        Log.d("listData[position-2]", listData[position-1].answerList[0])
                        val transaction = this.supportFragmentManager.beginTransaction()
                        fragmentOneAnswer.arguments = bundle
                        transaction.replace(R.id.fragmentContainer, fragmentOneAnswer)
                        transaction.addToBackStack(null)
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        transaction.commit()
                    }
                    "Many Answers" -> if (savedInstanceState == null) {
                        val fragmentOneAnswer = ManyAnswersQuestionTestFragment()
                        val bundle = Bundle()
                        //listData.reverse()
                        bundle.putStringArrayList("answerList", listData[position-1].answerList)
                        bundle.putString("answerNumber", listData[position-1].answerNumber)
                        bundle.putString("questionName", "${position}. ${listData[position-1].name}")
                        Log.d("listData[position-2]", listData[position-1].answerList[0])
                        val transaction = this.supportFragmentManager.beginTransaction()
                        fragmentOneAnswer.arguments = bundle
                        transaction.replace(R.id.fragmentContainer, fragmentOneAnswer)
                        transaction.addToBackStack(null)
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        transaction.commit()
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
                        val fragmentComparison = ComparisonAnswerQuestionTestFragment()
                        val bundle = Bundle()
                        //listData.reverse()
                        bundle.putStringArrayList("answerList", listData[position-1].answerList)
                        bundle.putString("answerNumber", listData[position-1].answerNumber)
                        bundle.putString("questionName", "${position}. ${listData[position-1].name}")
                        //Log.d("listData[position-2]", listData[position-1].answerList[0])
                        val transaction = this.supportFragmentManager.beginTransaction()
                        fragmentComparison.arguments = bundle
                        transaction.replace(R.id.fragmentContainer, fragmentComparison)
                        transaction.addToBackStack(null)
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        transaction.commit()
                    }
                    "Termin" -> if (savedInstanceState == null) {
                        val fragmentTerminAnswer = TerminAnswerQuestionTestFragment()
                        val bundle = Bundle()
                        //listData.reverse()
                        bundle.putStringArrayList("answerList", listData[position-1].answerList)
                        bundle.putString("answerNumber", listData[position-1].answerNumber)
                        bundle.putString("questionName", "${position}. ${listData[position-1].name}")
                        Log.d("listData[position-2]", listData[position-1].answerList[0])
                        val transaction = this.supportFragmentManager.beginTransaction()
                        fragmentTerminAnswer.arguments = bundle
                        transaction.replace(R.id.fragmentContainer, fragmentTerminAnswer)
                        transaction.addToBackStack(null)
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        transaction.commit()
                    }
                    "One Answer" -> /*if (savedInstanceState == null) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, OneAnswerQuestionTestFragment())
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit()
                    }*/
                    {
                        val fragmentOneAnswer = OneAnswerQuestionTestFragment()
                        val bundle = Bundle()
                        //listData.reverse()
                        bundle.putStringArrayList("answerList", listData[position-1].answerList)
                        bundle.putString("answerNumber", listData[position-1].answerNumber)
                        bundle.putString("questionName", "${position}. ${listData[position-1].name}")
                        Log.d("listData[position-2]", listData[position-1].answerList[0])
                        val transaction = this.supportFragmentManager.beginTransaction()
                        fragmentOneAnswer.arguments = bundle
                        transaction.replace(R.id.fragmentContainer, fragmentOneAnswer)
                        transaction.addToBackStack(null)
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        transaction.commit()
                    }
                    "Many Answers" -> if (savedInstanceState == null) {
                        val fragmentOneAnswer = ManyAnswersQuestionTestFragment()
                        val bundle = Bundle()
                        //listData.reverse()
                        bundle.putStringArrayList("answerList", listData[position-1].answerList)
                        bundle.putString("answerNumber", listData[position-1].answerNumber)
                        bundle.putString("questionName", "${position}. ${listData[position-1].name}")
                        Log.d("listData[position-2]", listData[position-1].answerList[0])
                        val transaction = this.supportFragmentManager.beginTransaction()
                        fragmentOneAnswer.arguments = bundle
                        transaction.replace(R.id.fragmentContainer, fragmentOneAnswer)
                        transaction.addToBackStack(null)
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        transaction.commit()
                    }
                }
            }
            observePosition()
        }

        finishTestButton.setOnClickListener {
            openFinishTestDialog()
        }
    }

    override fun passData(arrayList: ArrayList<String>) {
        if(position == 0) {
            handler.post(object: Runnable {
                override fun run() {
                    handler.postDelayed(this, 1000)
                    updateTime()
                }
            })
        }
        if((position <= listData.size)/*&&(position != 0)*/) {
            if (position in 1..listData.size) {
                if ((listData[position - 1].type == "Termin") && (listData[position - 1].answerNumber.toInt() > 1)) {
                    tableData[position - 1].chosenAnswer = arrayList.joinToString(separator = ", ")
                } else {
                    tableData[position - 1].chosenAnswer = arrayList.joinToString()
                }
                if (tableData[position - 1].chosenAnswer == tableData[position - 1].correctAnswer) {
                    tableData[position - 1].isCorrect = "1"
                } else {
                    tableData[position-1].isCorrect = "0"
                }
                //tableData[position-1].chosenAnswer = arrayList
            }
            position++
            if (position == listData.size + 1) {
                if (position in 1..listData.size) {
                    if ((listData[position - 1].type == "Termin") && (listData[position - 1].answerNumber.toInt() > 1)) {
                        tableData[position - 1].chosenAnswer = arrayList.joinToString(separator = ", ")
                    } else {
                        tableData[position - 1].chosenAnswer = arrayList.joinToString()
                    }
                    if (tableData[position - 1].chosenAnswer == tableData[position - 1].correctAnswer) {
                        tableData[position - 1].isCorrect = "1"
                    } else {
                        tableData[position - 1].isCorrect = "0"
                    }
                    //tableData[position-1].chosenAnswer = arrayList
                }
                openFinishTestDialog1()
            } else {
                currentQuestionType = listData[position - 1].type
                Log.d("currentQuestionType", currentQuestionType)
                Log.d("position", position.toString())
                when (currentQuestionType) {
                    "Comparison" -> /*if (savedInstanceState == null)*/ {
                        val fragmentComparison = ComparisonAnswerQuestionTestFragment()
                        val bundle = Bundle()
                        //listData.reverse()
                        bundle.putStringArrayList("answerList", listData[position - 1].answerList)
                        bundle.putString("answerNumber", listData[position - 1].answerNumber)
                        bundle.putString(
                            "questionName",
                            "${position}. ${listData[position - 1].name}"
                        )
                        //Log.d("listData[position-2]", listData[position-1].answerList[0])
                        val transaction = this.supportFragmentManager.beginTransaction()
                        fragmentComparison.arguments = bundle
                        transaction.replace(R.id.fragmentContainer, fragmentComparison)
                        transaction.addToBackStack(null)
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        transaction.commit()
                    }
                    "Termin" -> /*if (savedInstanceState == null)*/ {
                        val fragmentTerminAnswer = TerminAnswerQuestionTestFragment()
                        val bundle = Bundle()
                        //listData.reverse()
                        bundle.putStringArrayList("answerList", listData[position - 1].answerList)
                        bundle.putString("answerNumber", listData[position - 1].answerNumber)
                        bundle.putString(
                            "questionName",
                            "${position}. ${listData[position - 1].name}"
                        )
                        Log.d("listData[position-2]", listData[position - 1].answerList[0])
                        val transaction = this.supportFragmentManager.beginTransaction()
                        fragmentTerminAnswer.arguments = bundle
                        transaction.replace(R.id.fragmentContainer, fragmentTerminAnswer)
                        transaction.addToBackStack(null)
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        transaction.commit()
                    }
                    "One Answer" -> /*if (savedInstanceState == null)*/ {
                        val fragmentOneAnswer = OneAnswerQuestionTestFragment()
                        val bundle = Bundle()
                        //listData.reverse()
                        bundle.putStringArrayList("answerList", listData[position - 1].answerList)
                        bundle.putString("answerNumber", listData[position - 1].answerNumber)
                        bundle.putString(
                            "questionName",
                            "${position}. ${listData[position - 1].name}"
                        )
                        Log.d("listData[position-2]", listData[position - 1].answerList[0])
                        val transaction = this.supportFragmentManager.beginTransaction()
                        fragmentOneAnswer.arguments = bundle
                        transaction.replace(R.id.fragmentContainer, fragmentOneAnswer)
                        transaction.addToBackStack(null)
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        transaction.commit()
                        /*supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, OneAnswerQuestionTestFragment())
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit()*/
                    }
                    "Many Answers" -> /*if (savedInstanceState == null)*/ {
                        val fragmentManyAnswers = ManyAnswersQuestionTestFragment()
                        val bundle = Bundle()
                        //listData.reverse()
                        bundle.putStringArrayList("answerList", listData[position - 1].answerList)
                        bundle.putString("answerNumber", listData[position - 1].answerNumber)
                        bundle.putString(
                            "questionName",
                            "${position}. ${listData[position - 1].name}"
                        )
                        Log.d("listData[position-2]", listData[position - 1].answerList[0])
                        val transaction = this.supportFragmentManager.beginTransaction()
                        fragmentManyAnswers.arguments = bundle
                        transaction.replace(R.id.fragmentContainer, fragmentManyAnswers)
                        transaction.addToBackStack(null)
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        transaction.commit()
                    }
                }
            }
        }
        /*if(position == listData.size) {
            if(position in 1..listData.size) {
                if((listData[position-1].type == "Termin")&&(listData[position-1].answerNumber.toInt() > 1)) {
                    tableData[position-1].chosenAnswer = arrayList.joinToString(separator = ", ")
                } else {
                    tableData[position-1].chosenAnswer = arrayList.joinToString()
                }
                if(tableData[position-1].chosenAnswer == tableData[position-1].correctAnswer) {tableData[position-1].isCorrect = "1"}
                //tableData[position-1].chosenAnswer = arrayList
            }
            openFinishTestDialog()
        }*/
        /*if(position == 0) {


        }*/
        if(!arrayList.isEmpty()){
            for(i in arrayList) {
                Log.d("ArrayList", i)
            }
        }
        observePosition()
    }

    private fun getDataFromDb() {
        val questionsListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(listData.size > 0) {listData.clear()}
                if(tableData.size > 0) {tableData.clear()}
                for(questionInfo: DataSnapshot in snapshot.children) {
                    var correctAnswerList = ArrayList<String>()
                    var answerList = ArrayList<String>()
                    var name = questionInfo.child("name").getValue(String::class.java)
                    var type = questionInfo.child("type").getValue(String::class.java)
                    var answerNumber = questionInfo.child("answerNumber").getValue(String::class.java)
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
                        var tableModel: TableModel = TableModel(name!!, type!!, correctAnswerList.joinToString(), "-", "0")
                        tableData.add(tableModel)
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
                        var tableModel: TableModel
                        if((type == "Termin")&&(answerNumber.toInt() > 1)) {
                            tableModel = TableModel(name!!, type!!, correctAnswerList.joinToString(separator = ", "), "-", "0")
                        } else {
                            tableModel = TableModel(name!!, type!!, correctAnswerList.joinToString(), "-", "0")
                            Log.d("TYPE", "${answerNumber}  ${type}")
                        }
                        tableData.add(tableModel)
                        listData.add(representModel)
                        Log.d("answerList.size", representModel.answerList.size.toString())
                    }
                    Log.d("list", listData.size.toString())
//                    Log.d("listData[0].answerList0", listData[0].answerList[0])
                    if(listData.size>1){Log.d("listData[1].answerList1", listData[1].answerList[0])}
                    if(listData.size>1){
                        for(i in listData) {
                            println(i.name)
                            println(i.type)
                            println(i.answerNumber)
                            for(j in i.correctAnswer) {
                                println(j)
                            }
                            for(k in i.answerList) {
                                println(k)
                            }
                        }
                    }
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
        tableData = ArrayList()
        //answerList = ArrayList()
        //correctAnswerList = ArrayList()
        nextQuestionButton = findViewById(R.id.button_next_question)
        previousQuestionButton = findViewById(R.id.button_previous_question)
        finishTestButton = findViewById(R.id.button_finish_test)
        currentTime = findViewById(R.id.currentTestTime)
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

    private fun openTimeOutDialog() {
        var quitDialog = AlertDialog.Builder(
            this@AttendingTestActivity
        )
        quitDialog.setTitle("Время вышло!")

        quitDialog.setPositiveButton("Ок"
        ) { dialog, which ->
        }
    }

    private fun openFinishTestDialog1() {
        var finishTestDialog = AlertDialog.Builder(
            this@AttendingTestActivity
        )
        finishTestDialog.setTitle("Вы уверены, что хотите завершить выполнение теста?")
        finishTestDialog.setPositiveButton("Да!") {
            dialog, which ->
            val intent = Intent(this, TestResultsActivity::class.java)
            intent.putExtra("list", tableData)
            intent.putExtra("testName", testName)
            intent.putExtra("privacy", privacy)
            intent.putExtra("testCreator", testCreator)
            intent.putExtra("fromHistory", false)
            startActivity(intent)
            var score: Int = 0
            var userScore: String = ""
            var answerNumber: String = ""
            var totalScore: String = ""
            var grade: String = ""
            if(tableData != null) {
                for(data in tableData!!) {
                    var currentScore = data.isCorrect
                    if(currentScore != "0") {score++}
                }
            }
            getCurrentTime()
            userScore = score.toString()
            answerNumber = tableData!!.size.toString()
            var totalRerc = (score*100/tableData!!.size)
            totalScore = (score*100/tableData!!.size).toString() + "%"
            when(totalRerc) {
                in 0..19 -> {grade = "1"}
                in 20..39 -> {grade = "2"}
                in 40..59 -> {grade = "3"}
                in 60..79 -> {grade = "4"}
                in 80..100 -> {grade = "5"}
                else -> {grade = "Error"}
            }
            var totalModel = TotalModel(userScore, answerNumber, totalScore, grade)
            createTestAttendanceByUserInTestNode(testName, privacy, timeFinish, totalModel, tableData)
            createTestAttendanceByUserInUserNode(testName, privacy, subject, testCreator, timeFinish, totalModel, tableData)
            this.finish()
        }

        finishTestDialog.setNegativeButton("Нет") {
            dialog, which ->
            position--
            observePosition()
        }
        finishTestDialog.show()
    }

    private fun openFinishTestDialog() {
        var finishTestDialog = AlertDialog.Builder(
            this@AttendingTestActivity
        )
        finishTestDialog.setTitle("Вы уверены, что хотите завершить выполнение теста?")
        finishTestDialog.setPositiveButton("Да!") {
                dialog, which ->
            val intent = Intent(this, TestResultsActivity::class.java)
            intent.putExtra("list", tableData)
            intent.putExtra("testName", testName)
            intent.putExtra("privacy", privacy)
            intent.putExtra("testCreator", testCreator)
            intent.putExtra("fromHistory", false)
            startActivity(intent)
            var score: Int = 0
            var userScore: String = ""
            var answerNumber: String = ""
            var totalScore: String = ""
            var grade: String = ""
            if(tableData != null) {
                for(data in tableData!!) {
                    var currentScore = data.isCorrect
                    if(currentScore != "0") {score++}
                }
            }
            userScore = score.toString()
            answerNumber = tableData!!.size.toString()
            var totalRerc = (score*100/tableData!!.size)
            totalScore = (score*100/tableData!!.size).toString() + "%"
            when(totalRerc) {
                in 0..19 -> {grade = "1"}
                in 20..39 -> {grade = "2"}
                in 40..59 -> {grade = "3"}
                in 60..79 -> {grade = "4"}
                in 80..100 -> {grade = "5"}
                else -> {grade = "Error"}
            }
            getCurrentTime()
            var totalModel = TotalModel(userScore, answerNumber, totalScore, grade)
            createTestAttendanceByUserInTestNode(testName, privacy, timeFinish, totalModel, tableData)
            createTestAttendanceByUserInUserNode(testName, privacy, subject, testCreator, timeFinish, totalModel, tableData)
            this.finish()
        }

        finishTestDialog.setNegativeButton("Нет") {
                dialog, which ->
            observePosition()
        }
        finishTestDialog.show()
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
            currentTime.visibility = View.GONE
        } else {
            currentTime.visibility = View.VISIBLE
        }
        if((position > 1) && (position <= listData.size)) {
            previousQuestionButton.visibility = View.VISIBLE
        } else {
            previousQuestionButton.visibility = View.GONE
        }
        if((position < listData.size) && (position > 0)) {
            nextQuestionButton.visibility = View.VISIBLE
        } else {
            nextQuestionButton.visibility = View.GONE
        }
        if((position == listData.size)&&(listData.size > 0)) {
            nextQuestionButton.visibility = View.GONE
            finishTestButton.visibility = View.VISIBLE
        } else {
            finishTestButton.visibility = View.GONE
        }
    }

    private fun updateTime() {
        usedTime--
        hours = (usedTime / (60*60))
        minutes = ((usedTime / 60) % 60)
        seconds = (usedTime % 60)
        currentTime.text = ("Осталось ${hours} часов ${minutes} минут(ы) ${seconds} секунд(ы)")
        if((hours == 0) && (minutes < 10)) {
            currentTime.setTextColor(resources.getColor(R.color.orange))
        } else if ((hours == 0)&&(minutes < 5)) {
            currentTime.setTextColor(resources.getColor(R.color.superRed))
        }
        if(usedTime == 0) {
            openTimeOutDialog()
            /*var calendar = Calendar.getInstance()
            var year = calendar.get(Calendar.YEAR)
            var month = calendar.get(Calendar.MONTH)
            var day = calendar.get(Calendar.DAY_OF_MONTH)
            var hour = calendar.get(Calendar.HOUR_OF_DAY)
            var minute = calendar.get(Calendar.MINUTE)
            var second = calendar.get(Calendar.SECOND)
            hour = hour + 3
            day = day + (hour / 24)
            hour = hour % 24
            timeFinish = "${hour/10}${hour%10}:${minute/10}${minute%10}:${second/10}${second%10} ${day/10}${day%10}/${month/10}${month%10}/${year/1000}${year/100}${year/10}${year%10}"*/
            val intent = Intent(this, TestResultsActivity::class.java)
            intent.putExtra("list", tableData)
            intent.putExtra("testName", testName)
            intent.putExtra("privacy", privacy)
            intent.putExtra("testCreator", testCreator)
            intent.putExtra("fromHistory", false)
            startActivity(intent)
            getCurrentTime()
            var score: Int = 0
            var userScore: String = ""
            var answerNumber: String = ""
            var totalScore: String = ""
            var grade: String = ""
            if(tableData != null) {
                for(data in tableData!!) {
                    var currentScore = data.isCorrect
                    if(currentScore != "0") {score++}
                }
            }
            userScore = score.toString()
            answerNumber = tableData!!.size.toString()
            var totalRerc = (score*100/tableData!!.size)
            totalScore = (score*100/tableData!!.size).toString() + "%"
            when(totalRerc) {
                in 0..19 -> {grade = "1"}
                in 20..39 -> {grade = "2"}
                in 40..59 -> {grade = "3"}
                in 60..79 -> {grade = "4"}
                in 80..100 -> {grade = "5"}
                else -> {grade = "Error"}
            }
            var totalModel = TotalModel(userScore, answerNumber, totalScore, grade)
            createTestAttendanceByUserInTestNode(testName, privacy, timeFinish, totalModel, tableData)
            createTestAttendanceByUserInUserNode(testName, privacy, subject, testCreator, timeFinish, totalModel, tableData)
        }
    }

    private fun getCurrentTime() {
        var calendar = Calendar.getInstance()
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        var minute = calendar.get(Calendar.MINUTE)
        var second = calendar.get(Calendar.SECOND)
        hour = hour + 3
        day = day + (hour / 24)
        hour = hour % 24
        timeFinish = "${hour/10}${hour%10}:${minute/10}${minute%10}:${second/10}${second%10} ${day/10}${day%10}|${month/10}${month%10}|${year}"
    }
}