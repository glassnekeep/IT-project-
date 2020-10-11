package com.example.it_project.holders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.R

class ParticipantsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var container: ConstraintLayout
    var userName: TextView
    var userID: TextView

    init {
        container = itemView.findViewById(R.id.containerLayout)
        userName = itemView.findViewById(R.id.participantName)
        userID = itemView.findViewById(R.id.participantID)
    }
}