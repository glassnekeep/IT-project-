package com.example.it_project

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

open class QuestionHolder(itemView: View, viewType: Int, private val itemClickListener: ListItemClickListener?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    lateinit var lytContainer: ConstraintLayout
    lateinit var tvCategoryTitle: TextView
    lateinit var tvCategoryId: TextView

    override fun onClick(view: View) {
        itemClickListener?.onItemClick(layoutPosition, view)
    }

    init {
        lytContainer = itemView.findViewById<View>(R.id.question_info_container) as ConstraintLayout
        tvCategoryId = itemView.findViewById<View>(R.id.question_number) as TextView
        tvCategoryTitle = itemView.findViewById<View>(R.id.question_name) as TextView
        lytContainer.setOnClickListener(this)
    }
}