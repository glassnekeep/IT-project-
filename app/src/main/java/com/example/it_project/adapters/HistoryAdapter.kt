package com.example.it_project.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.R
import com.example.it_project.activities.TestResultsActivity
import com.example.it_project.holders.HistoryHolder
import com.example.it_project.holders.TestHolder
import com.example.it_project.models.HistoryModel
import com.example.it_project.models.TestModel
import java.util.ArrayList

class HistoryAdapter (var mContext: Context,
                      var mActivity: Activity,
                      var historyList: ArrayList<HistoryModel>) : RecyclerView.Adapter<HistoryHolder>(), View.OnClickListener {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_history_recycler, parent, false)
        return HistoryHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        val model: HistoryModel = historyList[position]
        holder.itemView.setOnClickListener {
            var intent = Intent(mActivity, TestResultsActivity::class.java)
            intent.putExtra("list", model.tableList)
            intent.putExtra("testName", model.testName)
            intent.putExtra("privacy", model.privacy)
            intent.putExtra("testCreator", model.testCreator)
            intent.putExtra("fromHistory", true)
            mActivity.startActivity(intent)
        }
        //val model: HistoryModel = historyList[position]
        holder.testName.text = model.testName
        holder.testCreator.text = model.testCreator
        holder.testPrivacy.text = model.privacy
        holder.perCent.text = model.total.totalScore
        holder.grade.text = model.total.grade
        //holder.container.background = ContextCompat.getDrawable(mContext, R.drawable.category_item_background)
    }
    override fun onClick(p0: View?) {

    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}