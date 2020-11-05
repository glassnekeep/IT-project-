package com.example.it_project.adapters

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.R
import com.example.it_project.holders.HistoryHolder
import com.example.it_project.holders.StatisticsHolder
import com.example.it_project.models.HistoryModel
import com.example.it_project.models.StatisticsModel
import java.util.*
import kotlin.collections.ArrayList

class StatisticsAdapter (var mContext: Context,
                         var mActivity: Activity,
                         var statisticsList: ArrayList<StatisticsModel>): RecyclerView.Adapter<StatisticsHolder>(), Filterable {

    var statisticsFilterList: ArrayList<StatisticsModel> = ArrayList()

    init {
        statisticsFilterList = statisticsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_statistics_table_row, parent, false)
        return StatisticsHolder(itemView)
    }

    override fun onBindViewHolder(holder: StatisticsHolder, position: Int) {
        val model: StatisticsModel = statisticsFilterList[position]
        holder.subject.text = model.subject
        holder.numberTests.text = model.numberTests
        holder.avgPerCent.text = "${model.avgPerCent}%"
        holder.avgGrade.text = model.avgGrade
    }

    override fun getItemCount(): Int {
        return statisticsFilterList.size
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var charSearch = constraint.toString()
                if(charSearch.isEmpty()) {
                    statisticsFilterList = statisticsList
                }
                else {
                    val resultList = ArrayList<StatisticsModel>()
                    for(row in statisticsList) {
                        if(row.subject.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(
                                Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    statisticsFilterList = resultList
                    Log.d("RESULT", statisticsFilterList.size.toString())
                }
                val filterResults = FilterResults()
                filterResults.values = statisticsFilterList
                Log.d("FINAL_RESULT", filterResults.values.toString())
                return filterResults
            }
            //@Suppress("UNCHECKED CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                statisticsFilterList = results?.values as ArrayList<StatisticsModel>
                //testsList = results?.values as ArrayList<TestModel>
                notifyDataSetChanged()
            }
        }
    }
}