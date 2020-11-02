package com.example.it_project.holders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.R

class StatisticsHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
    var container: ConstraintLayout
    var subject: TextView
    var numberTests: TextView
    var avgPerCent: TextView
    var avgGrade: TextView

    init {
        container = itemView.findViewById(R.id.statistics_table_row)
        subject = itemView.findViewById(R.id.subjectStatistics)
        numberTests = itemView.findViewById(R.id.numberTests)
        avgPerCent = itemView.findViewById(R.id.avgPerCent)
        avgGrade = itemView.findViewById(R.id.avgGrade)
    }
}