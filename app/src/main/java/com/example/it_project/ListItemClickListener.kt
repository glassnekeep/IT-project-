package com.example.it_project

import android.view.View

interface ListItemClickListener {
    fun onItemClick(position: Int, view: View?)
}