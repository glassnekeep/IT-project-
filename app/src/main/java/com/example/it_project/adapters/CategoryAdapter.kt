package com.example.it_project.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.ListItemClickListener
import com.example.it_project.QuestionHolder
import com.example.it_project.R
import com.example.it_project.models.CategoryModel
import java.util.*


class  CategoryAdapter(var mContext: Context, var mActivity: Activity,var categoryList: ArrayList<CategoryModel>): RecyclerView.Adapter<QuestionHolder>() {

    //private lateinit var mContext: Context
    //private lateinit var mActivity: Activity
    //private lateinit var categoryList: ArrayList<CategoryModel>
    private lateinit var itemClickListener: ListItemClickListener

    fun setItemClickListener(itemClickListener: ListItemClickListener) {
        this.itemClickListener = itemClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_category_recycler, parent, false)
        return QuestionHolder(itemView, viewType, itemClickListener)
    }

    override fun onBindViewHolder(holder: QuestionHolder, position: Int) {
        val model: CategoryModel = categoryList!![position]
        val categoryName: String = model.categoryId
        holder.tvCategoryTitle.text = "тут будет текст из базы данных"
        holder.tvCategoryId.text = (position + 1).toString()
        holder.lytContainer.background = ContextCompat.getDrawable(mContext, R.drawable.ic_dev)
    }

    override fun getItemCount(): Int {
        return if (categoryList != null) categoryList.size else 0
    }
}
