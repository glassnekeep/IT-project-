package com.example.it_project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.it_project.R
import com.example.it_project.models.QuestionModel
import com.example.it_project.utilities.createQuestionInTest
import com.example.it_project.utilities.initFirebase
import com.example.it_project.values.CURRENT_TEST_NAME
import com.example.it_project.values.CURRENT_TEST_PRIVACY

class CreateOneAnswerQuestionActivity : BaseActivity() {

    private lateinit var answer1: EditText
    private lateinit var answer2: EditText
    private lateinit var answer3: EditText
    private lateinit var answer4: EditText
    private lateinit var answer5: EditText
    private lateinit var answer6: EditText

    private lateinit var correctAnswer: EditText

    private lateinit var exitButton: Button
    private lateinit var createQuestionButton: Button

    private lateinit var arrayOfAnswers: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_one_answer_question)
        initToolbar(true)
        initFirebase()
        init()
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
            arrayOfAnswers.add(answer1.text.toString().trim())
            arrayOfAnswers.add(answer2.text.toString().trim())
            if(answer3.visibility != View.GONE) {arrayOfAnswers.add(answer3.text.toString().trim())}
            if(answer4.visibility != View.GONE) {arrayOfAnswers.add(answer4.text.toString().trim())}
            if(answer5.visibility != View.GONE) {arrayOfAnswers.add(answer5.text.toString().trim())}
            if(answer6.visibility != View.GONE) {arrayOfAnswers.add(answer6.text.toString().trim())}
            val questionName = extras?.getString("QuestionName")
            val answerNumber = extras?.getString("AnswerNumber")
            //createQuestionInTest(extras?.getString("TestThisName")!!, QuestionModel(questionName!!, "Comparison", answerNumber!!), arrayOfAnswers)
            var correctAnswerList = ArrayList<String>()
            correctAnswerList.add(correctAnswer.text.toString().trim())
            createQuestionInTest(CURRENT_TEST_NAME!!, QuestionModel(questionName!!.trim(), "One Answer", answerNumber!!, correctAnswerList), arrayOfAnswers, CURRENT_TEST_PRIVACY!!)
            val intent = Intent(this@CreateOneAnswerQuestionActivity, CreateTestActivity::class.java)
            intent.putExtra("NewQuestionName", questionName)
            startActivity(intent)
        }
        enableUpButton()
    }

    private fun init() {
        answer1 = findViewById(R.id.answer1)
        answer2 = findViewById(R.id.answer2)
        answer3 = findViewById(R.id.answer3)
        answer4 = findViewById(R.id.answer4)
        answer5 = findViewById(R.id.answer5)
        answer6 = findViewById(R.id.answer6)

        exitButton = findViewById(R.id.button_exit)
        createQuestionButton = findViewById(R.id.button_create_question)

        correctAnswer = findViewById(R.id.question_answer)
    }

    private fun hide3() {
        answer3.visibility = View.GONE
    }
    private fun hide4() {
        answer4.visibility = View.GONE
    }
    private fun hide5() {
        answer5.visibility = View.GONE
    }
    private fun hide6() {
        answer6.visibility = View.GONE
    }

    private fun openQuitDialog() {
        var quitDialog = AlertDialog.Builder(
            this@CreateOneAnswerQuestionActivity
        )
        quitDialog.setTitle("Вы уверены, что хотите отменить создание вопроса?")

        quitDialog.setPositiveButton("Да!"
        ) { dialog, which ->
            startActivity(Intent(this@CreateOneAnswerQuestionActivity, CreateTestActivity::class.java))
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