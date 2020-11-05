package com.example.it_project.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.R
import com.example.it_project.holders.GroupHolder
import com.example.it_project.holders.TableHolder
import com.example.it_project.models.GroupModel
import com.example.it_project.models.TableModel
import java.util.ArrayList

class TableAdapter (var mContext: Context,
                    var mActivity: Activity,
                    var tableList: ArrayList<TableModel>): RecyclerView.Adapter<TableHolder>()/*, View.OnClickListener*/ {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_table_row, parent, false)
        return TableHolder(itemView)
    }

    override fun onBindViewHolder(holder: TableHolder, position: Int) {
        val model: TableModel = tableList[position]
        //holder.itemView.setOnClickListener {
           //
        //}
        holder.questionNumber.text = "${position+1}"
        holder.chosenAnswer.text = "${model.chosenAnswer}"
        holder.correctAnswer.text = "${model.correctAnswer}"
        holder.score.text = "${model.isCorrect}"
    }

    //override fun onClick(p0: View?) {
     //
    //}

    override fun getItemCount(): Int {
        return if (tableList != null) tableList.size else 0
    }
}