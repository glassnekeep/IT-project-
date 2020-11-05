package com.example.it_project.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.R
import com.example.it_project.activities.AttendingTestActivity
import com.example.it_project.activities.CurrentGroupActivity
import com.example.it_project.activities.NewTestInGroupActivity
import com.example.it_project.holders.TestHolder
import com.example.it_project.models.TestModel
import com.example.it_project.utilities.addNewTestToGroup
import java.util.*

class CreateNewTestInGroupAdapter (var mContext: Context,
                                   var mActivity: Activity,
                                   var groupName: String,
                                   var testsList: ArrayList<TestModel>) : RecyclerView.Adapter<TestHolder>(), View.OnClickListener, Filterable {
    var testsFilterList: ArrayList<TestModel> = ArrayList()

    init {
        testsFilterList = testsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_tests, parent, false)
        return TestHolder(itemView)
    }

    override fun onBindViewHolder(holder: TestHolder, position: Int) {
        val model: TestModel = testsFilterList[position]
        holder.itemView.setOnClickListener {
            var intent = Intent(mActivity, CurrentGroupActivity::class.java)
            //intent.putExtra("privacy", testsFilterList[position].privacy)
            //intent.putExtra("testName", testsFilterList[position].testName)
            //intent.putExtra("subject", testsFilterList[position].subject)
            //intent.putExtra("testCreator", testsFilterList[position].creatorName)
            //intent.putExtra("time", testsFilterList[position].time)
            intent.putExtra("groupName", groupName)
            mActivity.startActivity(intent)
            mActivity.finish()
            addNewTestToGroup(groupName, model.testName, model.subject, model.privacy, model.testId, model.time)
        }

        holder.testName.text = "${model.testName}"
        holder.creatorName.text = "${model.creatorName}"
        holder.privacy.text = "${model.privacy}"
        holder.subject.text = "${model.subject}"
        holder.testId.text = "${model.testId}"
        holder.testTime.text = "${model.time}"
        holder.container.background = ContextCompat.getDrawable(mContext, R.drawable.category_item_background)
    }

    override fun onClick(p0: View?) {

    }

    override fun getItemCount(): Int {
        return testsFilterList.size
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var charSearch = constraint.toString()
                if(charSearch.isEmpty()) {
                    testsFilterList = testsList
                }
                else {
                    val resultList = ArrayList<TestModel>()
                    for(row in testsList) {
                        if(row.testName.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(
                                Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    testsFilterList = resultList
                    Log.d("RESULT", testsFilterList.size.toString())
                }
                val filterResults = FilterResults()
                filterResults.values = testsFilterList
                Log.d("FINAL_RESULT", filterResults.values.toString())
                return filterResults
            }
            //@Suppress("UNCHECKED CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                testsFilterList = results?.values as ArrayList<TestModel>
                //testsList = results?.values as ArrayList<TestModel>
                notifyDataSetChanged()
            }
        }
    }
}