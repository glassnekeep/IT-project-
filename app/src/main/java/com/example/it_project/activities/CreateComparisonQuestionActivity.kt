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
import com.example.it_project.utilities.createComparisonQuestionInTest
import com.example.it_project.values.CURRENT_TEST_NAME
import com.example.it_project.values.CURRENT_TEST_PRIVACY
import com.example.it_project.values.NEW_QUESTION
import kotlinx.android.synthetic.main.activity_create_comparison_question.*

class CreateComparisonQuestionActivity : BaseActivity() {

    private lateinit var layout1: LinearLayout
    private lateinit var layout2: LinearLayout
    private lateinit var layout3: LinearLayout
    private lateinit var layout4: LinearLayout
    private lateinit var layout5: LinearLayout
    private lateinit var layout6: LinearLayout

    private lateinit var firstPart1: EditText
    private lateinit var firstPart2: EditText
    private lateinit var firstPart3: EditText
    private lateinit var firstPart4: EditText
    private lateinit var firstPart5: EditText
    private lateinit var firstPart6: EditText

    private lateinit var secondPart1: EditText
    private lateinit var secondPart2: EditText
    private lateinit var secondPart3: EditText
    private lateinit var secondPart4: EditText
    private lateinit var secondPart5: EditText
    private lateinit var secondPart6: EditText

    private lateinit var exitButton: Button
    private lateinit var createQuestionButton: Button

    private lateinit var arrayOfFirstParts: ArrayList<String>
    private lateinit var arrayOfSecondParts: ArrayList<String>

    private lateinit var correctAnswer: EditText


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
            arrayOfFirstParts = ArrayList()
            arrayOfSecondParts = ArrayList()
            arrayOfFirstParts.add(firstPart1.text.toString())
            arrayOfFirstParts.add(firstPart2.text.toString())
            arrayOfSecondParts.add(secondPart1.text.toString())
            arrayOfSecondParts.add(secondPart2.text.toString())
            if(secondPart3.visibility != View.GONE) {
                arrayOfFirstParts.add(firstPart3.text.toString())
                arrayOfSecondParts.add(secondPart3.text.toString())
            }
            if(secondPart4.visibility != View.GONE) {
                arrayOfFirstParts.add(firstPart4.text.toString())
                arrayOfSecondParts.add(secondPart4.text.toString())
            }
            if(secondPart5.visibility != View.GONE) {
                arrayOfFirstParts.add(firstPart5.text.toString())
                arrayOfSecondParts.add(secondPart5.text.toString())
            }
            if(secondPart6.visibility != View.GONE) {
                arrayOfFirstParts.add(firstPart6.text.toString())
                arrayOfSecondParts.add(secondPart6.text.toString())
            }
            val questionName = extras?.getString("QuestionName")
            val answerNumber = extras?.getString("AnswerNumber")
            //createQuestionInTest(extras?.getString("TestThisName")!!, QuestionModel(questionName!!, "Comparison", answerNumber!!), arrayOfAnswers)
            var correctAnswerList = ArrayList<String>()
            correctAnswerList.add(correctAnswer.text.toString())
            createComparisonQuestionInTest(CURRENT_TEST_NAME!!, QuestionModel(questionName!!, "Comparison", answerNumber!!, correctAnswerList), arrayOfFirstParts, arrayOfSecondParts, CURRENT_TEST_PRIVACY!!)
            //createQuestionInTest(CURRENT_TEST_NAME!!, QuestionModel(questionName!!, "Comparison", answerNumber!!, correctAnswerList), arrayOfAnswers, CURRENT_TEST_PRIVACY!!)
            val intent = Intent(this@CreateComparisonQuestionActivity, CreateTestActivity::class.java)
            intent.putExtra("NewQuestionName", questionName)
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

        firstPart1 = findViewById(R.id.textFirstPartOne)
        firstPart2 = findViewById(R.id.textFirstPartTwo)
        firstPart3 = findViewById(R.id.textFirstPartThree)
        firstPart4 = findViewById(R.id.textFirstPartFour)
        firstPart5 = findViewById(R.id.textFirstPartFive)
        firstPart6 = findViewById(R.id.textFirstPartSix)


        secondPart1 = findViewById(R.id.secondPartOne)
        secondPart2 = findViewById(R.id.secondPartTwo)
        secondPart3 = findViewById(R.id.secondPartThree)
        secondPart4 = findViewById(R.id.secondPartFour)
        secondPart5 = findViewById(R.id.secondPartFive)
        secondPart6 = findViewById(R.id.secondPartSix)

        exitButton = findViewById(R.id.button_exit)
        createQuestionButton = findViewById(R.id.button_create_question)

        correctAnswer = findViewById(R.id.question_answer)
    }

    private fun hide3() {
        layout3.visibility = View.GONE
        secondPart3.visibility = View.GONE
    }
    private fun hide4() {
        layout4.visibility = View.GONE
        secondPart4.visibility = View.GONE
    }
    private fun hide5() {
        layout5.visibility = View.GONE
        secondPart5.visibility = View.GONE
    }
    private fun hide6() {
        layout6.visibility = View.GONE
        secondPart6.visibility = View.GONE
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