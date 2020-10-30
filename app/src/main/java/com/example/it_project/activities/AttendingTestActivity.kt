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
import com.example.it_project.models.TableModel
import com.example.it_project.values.*
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AttendingTestActivity : BaseActivity(), Communicator {

    private lateinit var privacy: String

    private lateinit var testName: String

    private var position: Int = 0

    private lateinit var listData: ArrayList<RepresentModel>

    private lateinit var tableData: ArrayList<TableModel>

    //private lateinit var answerList: ArrayList<String>

    //private lateinit var correctAnswerList: ArrayList<String>

    private lateinit var nextQuestionButton: Button

    private lateinit var previousQuestionButton: Button

    private lateinit var finishTestButton: Button

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
        if((position < listData.size)/*&&(position != 0)*/) {
            position++
            currentQuestionType = listData[position-1].type
            Log.d("currentQuestionType", currentQuestionType)
            Log.d("position", position.toString())
            when(currentQuestionType) {
                "Comparison" -> /*if (savedInstanceState == null)*/ {
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
                "Termin" -> /*if (savedInstanceState == null)*/ {
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
                "One Answer" -> /*if (savedInstanceState == null)*/ {
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
                    bundle.putStringArrayList("answerList", listData[position-1].answerList)
                    bundle.putString("answerNumber", listData[position-1].answerNumber)
                    bundle.putString("questionName", "${position}. ${listData[position-1].name}")
                    Log.d("listData[position-2]", listData[position-1].answerList[0])
                    val transaction = this.supportFragmentManager.beginTransaction()
                    fragmentManyAnswers.arguments = bundle
                    transaction.replace(R.id.fragmentContainer, fragmentManyAnswers)
                    transaction.addToBackStack(null)
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    transaction.commit()
                }
            }
        }
        /*if(position == 0) {

        }*/
        if(!arrayList.isEmpty()){Log.d("ArrayList[0]", arrayList[0])}
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
                        if(type == "Termin") {
                            tableModel = TableModel(name!!, type!!, correctAnswerList.joinToString(separator = ", "), "-", "0")
                        } else {
                            tableModel = TableModel(name!!, type!!, correctAnswerList.joinToString(), "-", "0")
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

    private fun openFinishTestDialog() {
        var finishTestDialog = AlertDialog.Builder(
            this@AttendingTestActivity
        )
        finishTestDialog.setTitle("Вы уверены, что хотите завершить выполнение теста?")
        finishTestDialog.setPositiveButton("Да!") {
            dialog, which ->
            val intent = Intent(this, TestResultsActivity::class.java)
            intent.putExtra("list", tableData)
            startActivity(intent)
            this.finish()
        }

        finishTestDialog.setNegativeButton("Нет") {
            dialog, which ->
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
}