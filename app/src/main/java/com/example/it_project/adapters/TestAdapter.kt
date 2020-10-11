package com.example.it_project.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.R
import com.example.it_project.holders.GroupHolder
import com.example.it_project.holders.TestHolder
import com.example.it_project.models.GroupModel
import com.example.it_project.models.TestModel
import java.util.ArrayList

class TestAdapter(var mContext: Context,
                  var mActivity: Activity,
                  var testsList: ArrayList<TestModel>) : RecyclerView.Adapter<TestHolder>(), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_tests, parent, false)
        return TestHolder(itemView)
    }

    override fun onBindViewHolder(holder: TestHolder, position: Int) {
        val model: TestModel = testsList[position]
        holder.testName.text = "${model.testName}"
        holder.creatorName.text = "${model.creatorName}"
        holder.container.background = ContextCompat.getDrawable(mContext, R.drawable.category_item_background)
    }

    override fun onClick(p0: View?) {

    }

    override fun getItemCount(): Int {
        return if (testsList != null) testsList.size else 0
    }
}