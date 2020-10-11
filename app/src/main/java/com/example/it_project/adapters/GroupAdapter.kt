package com.example.it_project.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.holders.GroupHolder
import com.example.it_project.R
import com.example.it_project.activities.CurrentGroupActivity
import com.example.it_project.models.GroupModel
import com.example.it_project.utilities.initParticipantList
import com.example.it_project.values.ADAPTER_GROUP_NAME
import com.example.it_project.values.CURRENT_GROUP_NAME
import kotlinx.android.synthetic.main.item_recycler_group.view.*
import java.util.ArrayList

class GroupAdapter(var mContext: Context,
                   var mActivity: Activity,
                   var groupList: ArrayList<GroupModel>): RecyclerView.Adapter<GroupHolder>(), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_group, parent, false)
        return GroupHolder(itemView)
    }

    override fun onBindViewHolder(holder: GroupHolder, position: Int) {
        val model: GroupModel = groupList[position]
        holder.itemView.setOnClickListener {
            ADAPTER_GROUP_NAME = holder.itemView.groupName.text.toString()
            CURRENT_GROUP_NAME = ADAPTER_GROUP_NAME
            if(CURRENT_GROUP_NAME != null) {
                initParticipantList(CURRENT_GROUP_NAME!!)
            }
            var intent: Intent = Intent(mActivity, CurrentGroupActivity::class.java)
            mActivity.startActivity(intent)
        }
        holder.groupName.text = "${model.groupName}"
        holder.userNumber.text = "${model.numberUsers}"
        holder.creatorName.text = "${model.creatorName}"
        holder.container.background = ContextCompat.getDrawable(mContext, R.drawable.category_item_background)
    }

    override fun onClick(p0: View?) {

    }

    override fun getItemCount(): Int {
        return if (groupList != null) groupList.size else 0
    }
}