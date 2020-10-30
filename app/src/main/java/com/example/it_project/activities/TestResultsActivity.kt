package com.example.it_project.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.it_project.R
import com.example.it_project.models.TableModel
import java.util.ArrayList

class TestResultsActivity : AppCompatActivity() {

    private var tableList: ArrayList<TableModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_results)

        val extras: Bundle? = intent.extras
        if(extras != null) {
            tableList = extras.getParcelableArrayList<TableModel>("list")
            Log.d("table", tableList?.size.toString())
        }
    }
}