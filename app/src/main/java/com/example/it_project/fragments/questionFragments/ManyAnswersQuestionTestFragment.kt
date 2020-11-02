package com.example.it_project.fragments.questionFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.it_project.Communicator
import com.example.it_project.R
import kotlinx.android.synthetic.main.fragment_one_answer_question_test.view.*

class ManyAnswersQuestionTestFragment : Fragment() {

    private lateinit var answer1: CheckBox
    private lateinit var answer2: CheckBox
    private lateinit var answer3: CheckBox
    private lateinit var answer4: CheckBox
    private lateinit var answer5: CheckBox
    private lateinit var answer6: CheckBox

    private lateinit var question: TextView

    private lateinit var communicator: Communicator

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
        val rootView = inflater.inflate(R.layout.fragment_many_answers_question_test, container, false)

        communicator = activity as Communicator

        question = rootView.findViewById(R.id.questionName)

        answer1 = rootView.findViewById(R.id.checkBox1)
        answer2 = rootView.findViewById(R.id.checkBox2)
        answer3 = rootView.findViewById(R.id.checkBox3)
        answer4 = rootView.findViewById(R.id.checkBox4)
        answer5 = rootView.findViewById(R.id.checkBox5)
        answer6 = rootView.findViewById(R.id.checkBox6)

        answerNumber = arguments?.getString("answerNumber")
        //Log.d("answerNumber", answerNumber!!)
        answerList = arguments?.getStringArrayList("answerList")
        //Log.d("answerList", answerList!!.size.toString())
        questionName = arguments?.getString("questionName")
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
        
        answer1.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked) {
                isChosen = true
                chosenAnswer = answer1.text.toString()
                if(chosenAnswer != null) {
                    chosenAnswerList.add(chosenAnswer!!)
                }
                Log.d("chosen.sie", chosenAnswerList.size.toString())
            }
            else {
                chosenAnswer = answer1.text.toString()
                if(chosenAnswer != null) {
                    chosenAnswerList.remove(chosenAnswer)
                }
                Log.d("chosen.sie", chosenAnswerList.size.toString())
            }
        }
        answer2.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked) {
                isChosen = true
                chosenAnswer = answer2.text.toString()
                if(chosenAnswer != null) {
                    chosenAnswerList.add(chosenAnswer!!)
                }
                Log.d("chosen.sie", chosenAnswerList.size.toString())
            }
            else {
                chosenAnswer = answer2.text.toString()
                if(chosenAnswer != null) {
                    chosenAnswerList.remove(chosenAnswer)
                }
                Log.d("chosen.sie", chosenAnswerList.size.toString())
            }
        }
        answer3.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked) {
                isChosen = true
                chosenAnswer = answer3.text.toString()
                if(chosenAnswer != null) {
                    chosenAnswerList.add(chosenAnswer!!)
                }
                Log.d("chosen.sie", chosenAnswerList.size.toString())
            }
            else {
                chosenAnswer = answer3.text.toString()
                if(chosenAnswer != null) {
                    chosenAnswerList.remove(chosenAnswer)
                }
                Log.d("chosen.sie", chosenAnswerList.size.toString())
            }
        }
        answer4.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked) {
                isChosen = true
                chosenAnswer = answer4.text.toString()
                if(chosenAnswer != null) {
                    chosenAnswerList.add(chosenAnswer!!)
                }
                Log.d("chosen.sie", chosenAnswerList.size.toString())
            }
            else {
                chosenAnswer = answer4.text.toString()
                if(chosenAnswer != null) {
                    chosenAnswerList.remove(chosenAnswer)
                }
                Log.d("chosen.sie", chosenAnswerList.size.toString())
            }
        }
        answer5.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked) {
                isChosen = true
                chosenAnswer = answer5.text.toString()
                if(chosenAnswer != null) {
                    chosenAnswerList.add(chosenAnswer!!)
                }
                Log.d("chosen.sie", chosenAnswerList.size.toString())
            }
            else {
                chosenAnswer = answer5.text.toString()
                if(chosenAnswer != null) {
                    chosenAnswerList.remove(chosenAnswer)
                }
                Log.d("chosen.sie", chosenAnswerList.size.toString())
            }
        }
        answer6.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked) {
                isChosen = true
                chosenAnswer = answer6.text.toString()
                if(chosenAnswer != null) {
                    chosenAnswerList.add(chosenAnswer!!)
                }
                Log.d("chosen.sie", chosenAnswerList.size.toString())
            }
            else {
                chosenAnswer = answer6.text.toString()
                if(chosenAnswer != null) {
                    chosenAnswerList.remove(chosenAnswer)
                }
                Log.d("chosen.sie", chosenAnswerList.size.toString())
            }
        }
        rootView.buttonSaveOneAnswer.setOnClickListener {
            //if(chosenAnswerList.size > 0) {chosenAnswerList.clear()}
            if(isChosen) {
                //if(chosenAnswer != null) {
                    //chosenAnswerList.add(chosenAnswer!!)
                    communicator.passData(chosenAnswerList)
                //}
            }
            else {
                Toast.makeText(context, "Ответ не выбран", Toast.LENGTH_SHORT).show()
            }
        }
        
        return rootView
    }

}