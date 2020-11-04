package com.example.it_project.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
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
            if(((firstPart6.visibility == View.GONE)||(!firstPart6.text.toString().trim().isEmpty()))
                &&((firstPart5.visibility == View.GONE)||(!firstPart5.text.toString().trim().isEmpty()))
                &&((firstPart4.visibility == View.GONE)||(!firstPart4.text.toString().trim().isEmpty()))
                &&((firstPart3.visibility == View.GONE)||(!firstPart3.text.toString().trim().isEmpty()))
                &&((secondPart6.visibility == View.GONE)||(!secondPart6.text.toString().trim().isEmpty()))
                &&((secondPart5.visibility == View.GONE)||(!secondPart5.text.toString().trim().isEmpty()))
                &&((secondPart4.visibility == View.GONE)||(!secondPart4.text.toString().trim().isEmpty()))
                &&((secondPart3.visibility == View.GONE)||(!secondPart3.text.toString().trim().isEmpty()))
                &&(!firstPart2.text.toString().trim().isEmpty())
                &&(!firstPart1.text.toString().trim().isEmpty())
                &&(!secondPart2.text.toString().trim().isEmpty())
                &&(!secondPart1.text.toString().trim().isEmpty())
                &&(!correctAnswer.text.toString().trim().isEmpty())) {
                arrayOfFirstParts = ArrayList()
                arrayOfSecondParts = ArrayList()
                arrayOfFirstParts.add(firstPart1.text.toString().trim())
                arrayOfFirstParts.add(firstPart2.text.toString().trim())
                arrayOfSecondParts.add(secondPart1.text.toString().trim())
                arrayOfSecondParts.add(secondPart2.text.toString().trim())
                if(secondPart3.visibility != View.GONE) {
                    arrayOfFirstParts.add(firstPart3.text.toString().trim())
                    arrayOfSecondParts.add(secondPart3.text.toString().trim())
                }
                if(secondPart4.visibility != View.GONE) {
                    arrayOfFirstParts.add(firstPart4.text.toString().trim())
                    arrayOfSecondParts.add(secondPart4.text.toString().trim())
                }
                if(secondPart5.visibility != View.GONE) {
                    arrayOfFirstParts.add(firstPart5.text.toString().trim())
                    arrayOfSecondParts.add(secondPart5.text.toString().trim())
                }
                if(secondPart6.visibility != View.GONE) {
                    arrayOfFirstParts.add(firstPart6.text.toString().trim())
                    arrayOfSecondParts.add(secondPart6.text.toString().trim())
                }
                val questionName = extras?.getString("QuestionName")
                val answerNumber = extras?.getString("AnswerNumber")
                //createQuestionInTest(extras?.getString("TestThisName")!!, QuestionModel(questionName!!, "Comparison", answerNumber!!), arrayOfAnswers)
                var correctAnswerList = ArrayList<String>()
                var correct = correctAnswer.text.toString().trim()
                if(correct.length == answerNumber?.toInt()) {
                    var variant = "ABCDEF"
                    var contains = true
                    for(i in 0..correct.length-1) {
                        if(variant.contains(correct[i])) {

                        } else {
                            contains = false
                        }
                    }
                    if(contains) {
                        correctAnswerList.add(correctAnswer.text.toString().trim())
                        createComparisonQuestionInTest(CURRENT_TEST_NAME!!, QuestionModel(questionName!!.trim(), "Comparison", answerNumber!!, correctAnswerList), arrayOfFirstParts, arrayOfSecondParts, CURRENT_TEST_PRIVACY!!)
                        //createQuestionInTest(CURRENT_TEST_NAME!!, QuestionModel(questionName!!, "Comparison", answerNumber!!, correctAnswerList), arrayOfAnswers, CURRENT_TEST_PRIVACY!!)
                        val intent = Intent(this@CreateComparisonQuestionActivity, CreateTestActivity::class.java)
                        intent.putExtra("NewQuestionName", questionName)
                        startActivity(intent)
                        this.finish()
                    } else {
                        Toast.makeText(this, "Вы использовали не подходящую букву!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Длина ответа не соотвествует количеству вопросов!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Не все поля заполнены!", Toast.LENGTH_SHORT).show()
            }
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