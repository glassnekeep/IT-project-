package com.example.it_project

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

open class QuestionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var lytContainer: ConstraintLayout
    var tvCategoryTitle: TextView
    var tvCategoryId: TextView

    init {
        lytContainer = itemView.findViewById<View>(R.id.question_info_container) as ConstraintLayout
        tvCategoryId = itemView.findViewById<View>(R.id.question_number) as TextView
        tvCategoryTitle = itemView.findViewById<View>(R.id.question_name) as TextView
    }
}