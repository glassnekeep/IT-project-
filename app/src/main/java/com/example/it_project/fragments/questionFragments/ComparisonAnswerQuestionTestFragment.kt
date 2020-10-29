package com.example.it_project.fragments.questionFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.it_project.Communicator
import com.example.it_project.R
import kotlinx.android.synthetic.main.fragment_comparison_answer_question_test.view.*

class ComparisonAnswerQuestionTestFragment : Fragment() {

    private lateinit var layout1: LinearLayout
    private lateinit var layout2: LinearLayout
    private lateinit var layout3: LinearLayout
    private lateinit var layout4: LinearLayout
    private lateinit var layout5: LinearLayout
    private lateinit var layout6: LinearLayout

    private lateinit var firstPart1: TextView
    private lateinit var firstPart2: TextView
    private lateinit var firstPart3: TextView
    private lateinit var firstPart4: TextView
    private lateinit var firstPart5: TextView
    private lateinit var firstPart6: TextView

    private lateinit var secondPart1: TextView
    private lateinit var secondPart2: TextView
    private lateinit var secondPart3: TextView
    private lateinit var secondPart4: TextView
    private lateinit var secondPart5: TextView
    private lateinit var secondPart6: TextView

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
        val rootView = inflater.inflate(R.layout.fragment_comparison_answer_question_test, container, false)

        communicator = activity as Communicator

        question = rootView.findViewById(R.id.questionName)

        firstPart1 = rootView.findViewById(R.id.textFirstPartOne)
        firstPart2 = rootView.findViewById(R.id.textFirstPartTwo)
        firstPart3 = rootView.findViewById(R.id.textFirstPartThree)
        firstPart4 = rootView.findViewById(R.id.textFirstPartFour)
        firstPart5 = rootView.findViewById(R.id.textFirstPartFive)
        firstPart6 = rootView.findViewById(R.id.textFirstPartSix)

        secondPart1 = rootView.findViewById(R.id.secondPartOne)
        secondPart2 = rootView.findViewById(R.id.secondPartTwo)
        secondPart3 = rootView.findViewById(R.id.secondPartThree)
        secondPart4 = rootView.findViewById(R.id.secondPartFour)
        secondPart5 = rootView.findViewById(R.id.secondPartFive)
        secondPart6 = rootView.findViewById(R.id.secondPartSix)

        layout1 = rootView.findViewById(R.id.firstPartOne)
        layout2 = rootView.findViewById(R.id.firstPartTwo)
        layout3 = rootView.findViewById(R.id.firstPartThree)
        layout4 = rootView.findViewById(R.id.firstPartFour)
        layout5 = rootView.findViewById(R.id.firstPartFive)
        layout6 = rootView.findViewById(R.id.firstPartSix)

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
                layout3.visibility = View.GONE
                layout4.visibility = View.GONE
                layout5.visibility = View.GONE
                layout6.visibility = View.GONE

                secondPart3.visibility = View.GONE
                secondPart4.visibility = View.GONE
                secondPart5.visibility = View.GONE
                secondPart6.visibility = View.GONE

                firstPart1.text = answerList!![0]
                firstPart2.text = answerList!![1]

                secondPart1.text = answerList!![2]
                secondPart2.text = answerList!![3]
            }
            "3" -> {
                layout4.visibility = View.GONE
                layout5.visibility = View.GONE
                layout6.visibility = View.GONE

                secondPart4.visibility = View.GONE
                secondPart5.visibility = View.GONE
                secondPart6.visibility = View.GONE

                firstPart1.text = answerList!![0]
                firstPart2.text = answerList!![1]
                firstPart3.text = answerList!![2]

                secondPart1.text = answerList!![3]
                secondPart2.text = answerList!![4]
                secondPart3.text = answerList!![5]
            }
            "4" -> {
                layout5.visibility = View.GONE
                layout6.visibility = View.GONE

                secondPart5.visibility = View.GONE
                secondPart6.visibility = View.GONE

                firstPart1.text = answerList!![0]
                firstPart2.text = answerList!![1]
                firstPart3.text = answerList!![2]
                firstPart4.text = answerList!![3]

                secondPart1.text = answerList!![4]
                secondPart2.text = answerList!![5]
                secondPart3.text = answerList!![6]
                secondPart4.text = answerList!![7]
            }
            "5" -> {
                layout6.visibility = View.GONE

                secondPart6.visibility = View.GONE

                firstPart1.text = answerList!![0]
                firstPart2.text = answerList!![1]
                firstPart3.text = answerList!![2]
                firstPart4.text = answerList!![3]
                firstPart5.text = answerList!![4]

                secondPart1.text = answerList!![5]
                secondPart2.text = answerList!![6]
                secondPart3.text = answerList!![7]
                secondPart4.text = answerList!![8]
                secondPart5.text = answerList!![9]
            }
            "6" -> {
                firstPart1.text = answerList!![0]
                firstPart2.text = answerList!![1]
                firstPart3.text = answerList!![2]
                firstPart4.text = answerList!![3]
                firstPart5.text = answerList!![4]
                firstPart6.text = answerList!![5]

                secondPart1.text = answerList!![6]
                secondPart2.text = answerList!![7]
                secondPart3.text = answerList!![8]
                secondPart4.text = answerList!![9]
                secondPart5.text = answerList!![10]
                secondPart6.text = answerList!![11]
            }
        }

            rootView.buttonSaveOneAnswer.setOnClickListener {
                if(!rootView.question_answer.text.isEmpty()) {
                    chosenAnswer = rootView.question_answer.text.toString()
                    if(chosenAnswer!!.length == (answerNumber!!.toInt()/2)) {
                        chosenAnswerList.add(chosenAnswer!!)
                        communicator.passData(chosenAnswerList)
                    } else {
                        Toast.makeText(context, "Введено ${chosenAnswer!!.length} ответов, а нужно ${answerNumber!!.toInt()/2}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Не введён ответ", Toast.LENGTH_SHORT).show()
                }
            }

        return rootView
    }

}