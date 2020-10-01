package com.example.it_project

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.activities.CurrentGroupActivity
import com.example.it_project.activities.GroupsActivity
import com.example.it_project.utilities.invokeNewActivity

open class GroupHolder(itemView: View) : RecyclerView.ViewHolder(itemView)/*, View.OnClickListener*/ {

    /*override fun onClick(v: View?) {
        invokeNewActivity(GroupsActivity(), CurrentGroupActivity::class.java, true)
    }*/

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
