package com.example.it_project.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import com.example.it_project.R
import kotlinx.android.synthetic.main.activity_create_termin_answers.*

class CreateTerminAnswerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_termin_answers)
        initToolbar(true)
    }

    override fun onStart() {
        super.onStart()
        val extras: Bundle? = intent.extras
        if(extras != null) {textView1.text = extras.getString("AnswerNumber")}
        if(extras != null) {setToolbarTitle(extras.getString("QuestionName").toString())}
    }
}