package com.example.it_project.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.it_project.R

import kotlinx.android.synthetic.main.activity_many_answers.*

class CreateManyAnswersActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_many_answers)
        initToolbar(true)
    }

    override fun onStart() {
        super.onStart()
        val extras: Bundle? = intent.extras
        if(extras != null) {textView3.text = extras.getString("AnswerNumber")}
        if(extras != null) {setToolbarTitle(extras.getString("QuestionName").toString())}
    }
}