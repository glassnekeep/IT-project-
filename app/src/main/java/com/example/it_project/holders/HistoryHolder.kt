package com.example.it_project.holders

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.R

class HistoryHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
    var container: CardView
    var testName: TextView
    var testCreator: TextView
    var testPrivacy: TextView
    var perCent: TextView
    var grade: TextView

    init {
        container = itemView.findViewById(R.id.historyContainer)
        testName = itemView.findViewById(R.id.testName)
        testCreator = itemView.findViewById(R.id.testCreator)
        testPrivacy = itemView.findViewById(R.id.testPrivacy)
        perCent = itemView.findViewById(R.id.perCent)
        grade = itemView.findViewById(R.id.grade)
    }
}