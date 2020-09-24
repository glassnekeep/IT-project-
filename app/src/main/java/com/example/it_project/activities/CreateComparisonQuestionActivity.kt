package com.example.it_project.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.example.it_project.R
import com.example.it_project.utilities.createQuestionInTest
import com.example.it_project.utilities.initFirebase
import com.example.it_project.models.QuestionModel

class CreateComparisonQuestionActivity : BaseActivity() {

    private lateinit var layout1: LinearLayout
    private lateinit var layout2: LinearLayout
    private lateinit var layout3: LinearLayout
    private lateinit var layout4: LinearLayout
    private lateinit var layout5: LinearLayout
    private lateinit var layout6: LinearLayout

    private lateinit var editText1: EditText
    private lateinit var editText2: EditText
    private lateinit var editText3: EditText
    private lateinit var editText4: EditText
    private lateinit var editText5: EditText
    private lateinit var editText6: EditText

    private lateinit var exitButton: Button
    private lateinit var createQuestionButton: Button

    private lateinit var arrayOfAnswers: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_comparison_question)
        initToolbar(true)
        init()
        initFirebase()
    }

    override fun onStart() {
        super.onStart()
        val extras: Bundle? = intent.extras
        if(extras != null) {setToolbarTitle(extras.getString("QuestionName").toString())}
        if(extras != null) {
            when(extras.getString("AnswerNumber")) {
                "2" -> {
                    hide3()
                    hide4()
                    hide5()
                    hide6()
                }
                "3" -> {
                    hide4()
                    hide5()
                    hide6()
                }
                "4" -> {
                    hide5()
                    hide6()
                }
                "5" -> {
                    hide6()
                }
                "6" -> { }
            }
        }
        exitButton.setOnClickListener {
            openQuitDialog()
        }
        createQuestionButton.setOnClickListener {
            arrayOfAnswers = ArrayList()
            arrayOfAnswers.add(editText1.text.toString())
            arrayOfAnswers.add(editText2.text.toString())
            if(editText3.visibility != View.GONE) {arrayOfAnswers.add(editText3.text.toString())}
            if(editText4.visibility != View.GONE) {arrayOfAnswers.add(editText4.text.toString())}
            if(editText5.visibility != View.GONE) {arrayOfAnswers.add(editText5.text.toString())}
            if(editText6.visibility != View.GONE) {arrayOfAnswers.add(editText6.text.toString())}
            val questionName = extras?.getString("QuestionName")
            val answerNumber = extras?.getString("AnswerNumber")
            createQuestionInTest(extras?.getString("TestThisName")!!, QuestionModel(questionName!!, "Comparison", answerNumber!!), arrayOfAnswers)
            val intent = Intent(this@CreateComparisonQuestionActivity, CreateTestActivity::class.java)
            startActivity(intent)
        }
        enableUpButton()
    }

    private fun init() {
        layout1 = findViewById(R.id.firstPartOne)
        layout2 = findViewById(R.id.firstPartTwo)
        layout3 = findViewById(R.id.firstPartThree)
        layout4 = findViewById(R.id.firstPartFour)
        layout5 = findViewById(R.id.firstPartFive)
        layout6 = findViewById(R.id.firstPartSix)

        editText1 = findViewById(R.id.secondPartOne)
        editText2 = findViewById(R.id.secondPartTwo)
        editText3 = findViewById(R.id.secondPartThree)
        editText4 = findViewById(R.id.secondPartFour)
        editText5 = findViewById(R.id.secondPartFive)
        editText6 = findViewById(R.id.secondPartSix)

        exitButton = findViewById(R.id.button_exit)
        createQuestionButton = findViewById(R.id.button_create_question)
    }

    private fun hide3() {
        layout3.visibility = View.GONE
        editText3.visibility = View.GONE
    }
    private fun hide4() {
        layout4.visibility = View.GONE
        editText4.visibility = View.GONE
    }
    private fun hide5() {
        layout5.visibility = View.GONE
        editText5.visibility = View.GONE
    }
    private fun hide6() {
        layout6.visibility = View.GONE
        editText6.visibility = View.GONE
    }
    private fun openQuitDialog() {
        var quitDialog = AlertDialog.Builder(
            this@CreateComparisonQuestionActivity
        )
        quitDialog.setTitle("Вы уверены, что хотите отменить создание вопроса?")

        quitDialog.setPositiveButton("Да!"
        ) { dialog, which ->
            startActivity(Intent(this@CreateComparisonQuestionActivity, CreateTestActivity::class.java))
            finish()
        }

        quitDialog.setNegativeButton("Нет"
        ) { dialog, which ->
            // TODO Auto-generated method stub
        }
        quitDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //ActivityUtilities.getInstance()
                //.invokeNewActivity(this@CreateTestActivity, MainActivity::class.java, true)
                openQuitDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        openQuitDialog()
    }
}