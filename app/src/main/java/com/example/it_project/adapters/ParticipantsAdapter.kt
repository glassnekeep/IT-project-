package com.example.it_project.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.GroupHolder
import com.example.it_project.ParticipantsHolder
import com.example.it_project.R
import com.example.it_project.activities.CurrentGroupActivity
import com.example.it_project.models.GroupModel
import com.example.it_project.models.ParticipantModel
import com.example.it_project.values.ADAPTER_GROUP_NAME
import kotlinx.android.synthetic.main.item_recycler_group.view.*
import java.util.ArrayList

class ParticipantsAdapter(var mContext: Context, var mActivity: Activity, var participantList: ArrayList<ParticipantModel>): RecyclerView.Adapter<ParticipantsHolder>(), View.OnClickListener{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantsHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_participant, parent, false)
        return ParticipantsHolder(itemView)
    }

    override fun onBindViewHolder(holder: ParticipantsHolder, position: Int) {
        val model: ParticipantModel = participantList[position]
        holder.itemView.setOnClickListener {
            //ADAPTER_GROUP_NAME = holder.itemView.groupName.text.toString()
            //var adapterGroupName = ADAPTER_GROUP_NAME
            //getAdapterGroupID(adapterGroupName!!)
            //var intent: Intent = Intent(mActivity, CurrentGroupActivity::class.java)
            //mActivity.startActivity(intent)
        }
        holder.userName.text = "${model.userName}"
        holder.userID.text = "${model.userID}"
        holder.container.background = ContextCompat.getDrawable(mContext, R.drawable.category_item_background)
    }

    override fun getItemCount(): Int {
        return if (participantList != null) participantList.size else 0
    }

    override fun onClick(p0: View?) {

    }
}