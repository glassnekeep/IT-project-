package com.example.it_project.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.GroupHolder
import com.example.it_project.QuestionHolder
import com.example.it_project.R
import com.example.it_project.models.CategoryModel
import com.example.it_project.models.GroupModel
import java.util.ArrayList

class GroupAdapter(var mContext: Context, var mActivity: Activity, var groupList: ArrayList<GroupModel>): RecyclerView.Adapter<GroupHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_group, parent, false)
        return GroupHolder(itemView)
    }

    override fun onBindViewHolder(holder: GroupHolder, position: Int) {
        val model: GroupModel = groupList[position]
        holder.groupName.text = "${model.groupName}"
        holder.userNumber.text = "${model.numberUsers}"
        holder.creatorName.text = "${model.creatorName}"
        holder.container.background = ContextCompat.getDrawable(mContext, R.drawable.category_item_background)
    }

    override fun getItemCount(): Int {
        return if (groupList != null) groupList.size else 0
    }
}