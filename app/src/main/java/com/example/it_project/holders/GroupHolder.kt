package com.example.it_project.holders

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.R

open class GroupHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var container: CardView
    var groupName : TextView
    var creatorName: TextView
    var userNumber: TextView

    init {
        container = itemView.findViewById(R.id.groupContainer)
        groupName = itemView.findViewById(R.id.groupName)
        creatorName = itemView.findViewById(R.id.groupCreator)
        userNumber = itemView.findViewById(R.id.userNumber)
    }
}
