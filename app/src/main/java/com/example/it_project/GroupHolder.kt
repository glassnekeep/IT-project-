package com.example.it_project

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

open class GroupHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var container: ConstraintLayout
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
