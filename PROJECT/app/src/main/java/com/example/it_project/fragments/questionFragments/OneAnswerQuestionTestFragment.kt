package com.example.it_project.fragments.questionFragments

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.example.it_project.Communicator
import com.example.it_project.R
import kotlinx.android.synthetic.main.fragment_one_answer_question_test.view.*


class OneAnswerQuestionTestFragment : Fragment() {

    private lateinit var answer1: RadioButton
    private lateinit var answer2: RadioButton
    private lateinit var answer3: RadioButton
    private lateinit var answer4: RadioButton
    private lateinit var answer5: RadioButton
    private lateinit var answer6: RadioButton

    private lateinit var question: TextView

    private lateinit var radioGroup: RadioGroup

    private lateinit var communicator: Communicator

    private var saved: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private var isChosen = false
    private var chosenAnswer: String? = ""
    private var answerNumber: String? = ""
    private var questionName: String? = ""
    private var answerList: ArrayList<String>? = ArrayList()
    private var chosenAnswerList: ArrayList<String> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_one_answer_question_test, container, false)

        communicator = activity as Communicator

        question = rootView.findViewById(R.id.questionName)

        answer1 = rootView.findViewById(R.id.answerRadioButton1)
        answer2 = rootView.findViewById(R.id.answerRadioButton2)
        answer3 = rootView.findViewById(R.id.answerRadioButton3)
        answer4 = rootView.findViewById(R.id.answerRadioButton4)
        answer5 = rootView.findViewById(R.id.answerRadioButton5)
        answer6 = rootView.findViewById(R.id.answerRadioButton6)

        radioGroup = rootView.findViewById(R.id.radioGroupOneAnswer)

        answerNumber = arguments?.getString("answerNumber")
        //Log.d("answerNumber", answerNumber!!)
        answerList = arguments?.getStringArrayList("answerList")
        //Log.d("answerList", answerList!!.size.toString())
        questionName = arguments?.getString("questionName")

        saved = arguments?.getBoolean("saved")
        if(saved!!) {
            rootView.saved.text = "Сохранено"
        } else {
            rootView.saved.text = "Не сохранено"
            rootView.saved.setTextColor(resources.getColor(R.color.superRed))
        }
        if(questionName != null) {question.text = questionName}
        for(answers in answerList!!) {
            Log.d("answerPosition", answers)
        }

        when(answerNumber) {
            "2" -> {
                answer3.visibility = View.GONE
                answer4.visibility = View.GONE
                answer5.visibility = View.GONE
                answer6.visibility = View.GONE

                answer1.text = answerList!![0]
                answer2.text = answerList!![1]
            }
            "3" -> {
                //radioGroup.removeViewInLayout(answer4)

                answer4.visibility = View.GONE
                answer5.visibility = View.GONE
                answer6.visibility = View.GONE

                answer1.text = answerList!![0]
                answer2.text = answerList!![1]
                answer3.text = answerList!![2]
            }
            "4" -> {
                answer5.visibility = View.GONE
                answer6.visibility = View.GONE

                answer1.text = answerList!![0]
                answer2.text = answerList!![1]
                answer3.text = answerList!![2]
                answer4.text = answerList!![3]
            }
            "5" -> {
                answer6.visibility = View.GONE

                answer1.text = answerList!![0]
                answer2.text = answerList!![1]
                answer3.text = answerList!![2]
                answer4.text = answerList!![3]
                answer5.text = answerList!![4]
            }
            "6" -> {
                answer1.text = answerList!![0]
                answer2.text = answerList!![1]
                answer3.text = answerList!![2]
                answer4.text = answerList!![3]
                answer5.text = answerList!![4]
                answer6.text = answerList!![5]
            }
        }

        radioGroup.setOnCheckedChangeListener { group, chechedId ->
            val rb = group.findViewById<RadioButton>(chechedId)
            isChosen = true
            chosenAnswer = rb.text.toString()
        }

        rootView.buttonSaveOneAnswer.setOnClickListener {
            //if(chosenAnswerList.size > 0) {chosenAnswerList.clear()}
            if(isChosen) {
                if(chosenAnswer != null) {
                    chosenAnswerList.add(chosenAnswer!!)
                    communicator.passData(chosenAnswerList)
                }
            }
            else {
                Toast.makeText(context, "Ответ не выбран", Toast.LENGTH_SHORT).show()
            }
        }
        return rootView
    }


}