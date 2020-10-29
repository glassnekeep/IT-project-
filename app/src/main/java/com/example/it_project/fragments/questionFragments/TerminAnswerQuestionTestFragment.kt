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
import kotlinx.android.synthetic.main.fragment_termin_answer_question_test.view.*


class TerminAnswerQuestionTestFragment : Fragment() {

    private lateinit var answer1: EditText
    private lateinit var answer2: EditText
    private lateinit var answer3: EditText
    private lateinit var answer4: EditText
    private lateinit var answer5: EditText
    private lateinit var answer6: EditText

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
        val rootView =  inflater.inflate(R.layout.fragment_termin_answer_question_test, container, false)

        communicator = activity as Communicator

        question = rootView.findViewById(R.id.questionName)

        answer1 = rootView.findViewById(R.id.answer1)
        answer2 = rootView.findViewById(R.id.answer2)
        answer3 = rootView.findViewById(R.id.answer3)
        answer4 = rootView.findViewById(R.id.answer4)
        answer5 = rootView.findViewById(R.id.answer5)
        answer6 = rootView.findViewById(R.id.answer6)

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
            "1" -> {
                answer2.visibility = View.GONE
                answer3.visibility = View.GONE
                answer4.visibility = View.GONE
                answer5.visibility = View.GONE
                answer6.visibility = View.GONE
            }
            "2" -> {
                answer3.visibility = View.GONE
                answer4.visibility = View.GONE
                answer5.visibility = View.GONE
                answer6.visibility = View.GONE
            }
            "3" -> {
                answer4.visibility = View.GONE
                answer5.visibility = View.GONE
                answer6.visibility = View.GONE
            }
            "4" -> {
                answer5.visibility = View.GONE
                answer6.visibility = View.GONE
            }
            "5" -> {
                answer6.visibility = View.GONE
            }
            "6" -> {
            }
        }
        rootView.buttonSaveOneAnswer.setOnClickListener {
            when(answerNumber) {
                "1" -> {
                    if(!answer1.text.isEmpty()) {
                        chosenAnswerList.add(answer1.text.toString())
                        communicator.passData(chosenAnswerList)
                    } else {
                        Toast.makeText(context, "Не все поля ответов заполнены", Toast.LENGTH_SHORT).show()
                    }
                }
                "2" -> {
                    if((!answer1.text.isEmpty())
                        &&(!answer2.text.isEmpty())) {
                        chosenAnswerList.add(answer1.text.toString())
                        chosenAnswerList.add(answer2.text.toString())
                        communicator.passData(chosenAnswerList)
                    } else {
                        Toast.makeText(context, "Не все поля ответов заполнены", Toast.LENGTH_SHORT).show()
                    }
                }
                "3" -> {
                    if((!answer1.text.isEmpty())
                        &&(!answer2.text.isEmpty())
                        &&(!answer3.text.isEmpty())) {
                        chosenAnswerList.add(answer1.text.toString())
                        chosenAnswerList.add(answer2.text.toString())
                        chosenAnswerList.add(answer3.text.toString())
                        communicator.passData(chosenAnswerList)
                    } else {
                        Toast.makeText(context, "Не все поля ответов заполнены", Toast.LENGTH_SHORT).show()
                    }
                }
                "4" -> {
                    if((!answer1.text.isEmpty())
                        &&(!answer2.text.isEmpty())
                        &&(!answer3.text.isEmpty())
                        &&(!answer4.text.isEmpty())) {
                        chosenAnswerList.add(answer1.text.toString())
                        chosenAnswerList.add(answer2.text.toString())
                        chosenAnswerList.add(answer3.text.toString())
                        chosenAnswerList.add(answer4.text.toString())
                        communicator.passData(chosenAnswerList)
                    } else {
                        Toast.makeText(context, "Не все поля ответов заполнены", Toast.LENGTH_SHORT).show()
                    }
                }
                "5" -> {
                    if((!answer1.text.isEmpty())
                        &&(!answer2.text.isEmpty())
                        &&(!answer3.text.isEmpty())
                        &&(!answer4.text.isEmpty())
                        &&(!answer5.text.isEmpty())) {
                        chosenAnswerList.add(answer1.text.toString())
                        chosenAnswerList.add(answer2.text.toString())
                        chosenAnswerList.add(answer3.text.toString())
                        chosenAnswerList.add(answer4.text.toString())
                        chosenAnswerList.add(answer5.text.toString())
                        communicator.passData(chosenAnswerList)
                    } else {
                        Toast.makeText(context, "Не все поля ответов заполнены", Toast.LENGTH_SHORT).show()
                    }
                }
                "6" -> {
                    if((!answer1.text.isEmpty())
                        &&(!answer2.text.isEmpty())
                        &&(!answer3.text.isEmpty())
                        &&(!answer4.text.isEmpty())
                        &&(!answer5.text.isEmpty())
                        &&(!answer6.text.isEmpty())) {
                        chosenAnswerList.add(answer1.text.toString())
                        chosenAnswerList.add(answer2.text.toString())
                        chosenAnswerList.add(answer3.text.toString())
                        chosenAnswerList.add(answer4.text.toString())
                        chosenAnswerList.add(answer5.text.toString())
                        chosenAnswerList.add(answer6.text.toString())
                        communicator.passData(chosenAnswerList)
                    } else {
                        Toast.makeText(context, "Не все поля ответов заполнены", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return rootView
    }
}