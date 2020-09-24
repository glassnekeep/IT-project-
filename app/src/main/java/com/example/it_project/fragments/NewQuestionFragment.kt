package com.example.it_project.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.it_project.R
import com.example.it_project.activities.*
import com.example.it_project.values.TEST_NAME
import kotlinx.android.synthetic.main.fragment_new_question.*

class NewQuestionFragment : AppCompatDialogFragment() {

    private lateinit var intent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_question, container, false)
    }

    override fun onResume() {
        super.onResume()
        spinner_answer_type.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                //if((spinner_answer_type.selectedItem.toString() == "Сопоставление") && (spinner_answer_number.selectedItem.toString() == "1")) { }
                if(spinner_answer_type.selectedItem.toString() != "Свободный ответ") {spinner_answer_number.visibility = View.VISIBLE}
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        button_create_question.setOnClickListener {
            if((spinner_answer_number.selectedItem.toString() == "1") && (spinner_answer_type.selectedItem.toString() == "Сопоставление")) {
                Toast.makeText(activity?.applicationContext, "Сопоставляться должны 2 и более элемента!", Toast.LENGTH_SHORT).show()
            }
            else {
                when (spinner_answer_type.selectedItem.toString()) {
                    "Сопоставление" -> intent =
                        Intent(activity, CreateComparisonQuestionActivity::class.java)
                    "Термин" -> intent = Intent(activity, CreateTerminAnswerActivity::class.java)
                    "Один вариант ответа" -> intent =
                        Intent(activity, CreateOneAnswerActivity::class.java)
                    "Несколько вариантов ответа" -> intent =
                        Intent(activity, CreateManyAnswersActivity::class.java)
                }
                intent.putExtra("AnswerNumber", spinner_answer_number.selectedItem.toString())
                intent.putExtra("QuestionName", edit_text_question_name.text.toString())
                intent.putExtra("TestThisName", TEST_NAME)
                //val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                fragmentManager?.beginTransaction()?.remove(this@NewQuestionFragment)?.commit()
            }
        }
        button_exit.setOnClickListener {
            fragmentManager?.beginTransaction()?.remove(this@NewQuestionFragment)?.commit()
        }
    }
}