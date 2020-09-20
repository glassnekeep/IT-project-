package com.example.it_project.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.it_project.R
import kotlinx.android.synthetic.main.activity_one_answer.*

class CreateOneAnswerActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_answer)
        initToolbar(true)
    }

    override fun onStart() {
        super.onStart()
        val extras: Bundle? = intent.extras
        if(extras != null) {textView2.text = extras.getString("AnswerNumber")}
        if(extras != null) {setToolbarTitle(extras.getString("QuestionName").toString())}
    }
}