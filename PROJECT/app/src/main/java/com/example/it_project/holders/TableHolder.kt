package com.example.it_project.holders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.R
import kotlinx.android.synthetic.main.item_recycler_table_row.view.*

open class TableHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var container: ConstraintLayout
    var questionNumber: TextView
    var chosenAnswer: TextView
    var correctAnswer: TextView
    var score: TextView

    init {
        container = itemView.findViewById(R.id.containerTable)
        questionNumber = itemView.findViewById(R.id.questionNumber)
        chosenAnswer = itemView.findViewById(R.id.chosenAnswer)
        correctAnswer = itemView.findViewById(R.id.correctAnswer)
        score = itemView.findViewById(R.id.score)
    }
}