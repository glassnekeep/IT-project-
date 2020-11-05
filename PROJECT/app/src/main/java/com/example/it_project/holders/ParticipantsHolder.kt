package com.example.it_project.holders

import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.R

class ParticipantsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var container: CardView
    var userName: TextView
    var userID: TextView
    var delete: ImageButton

    init {
        container = itemView.findViewById(R.id.containerLayout)
        userName = itemView.findViewById(R.id.participantName)
        userID = itemView.findViewById(R.id.participantID)
        delete = itemView.findViewById(R.id.delete)
    }
}