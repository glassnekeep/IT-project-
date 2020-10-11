package com.example.it_project.holders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.R

class TestHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var container: ConstraintLayout
    var testName : TextView
    var creatorName: TextView

    init {
        container = itemView.findViewById(R.id.testContainer)
        testName = itemView.findViewById(R.id.testName)
        creatorName = itemView.findViewById(R.id.testCreator)
    }
}