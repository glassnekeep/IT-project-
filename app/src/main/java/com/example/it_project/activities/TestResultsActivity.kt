package com.example.it_project.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.R
import com.example.it_project.adapters.GroupAdapter
import com.example.it_project.adapters.TableAdapter
import com.example.it_project.models.TableModel
import com.example.it_project.utilities.initFirebase
import java.util.ArrayList

class TestResultsActivity : BaseActivity() {

    private var tableList: ArrayList<TableModel>? = null

    private lateinit var context: Context

    private lateinit var tableRecyclerView: RecyclerView

    private lateinit var activity: Activity

    private lateinit var adapter: TableAdapter

    private var testName: String? = null

    private var privacy: String? = null

    private var testCreator: String? = null

    private lateinit var total: TextView
    private lateinit var userScore: TextView
    private lateinit var answerNumber: TextView
    private lateinit var totalScore: TextView
    private lateinit var grade: TextView

    private lateinit var finish: Button

    private var fromHistory: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_results)
        init()
        initToolbar(true)
        setToolbarTitle("Результаты")
        initFirebase()

        val extras: Bundle? = intent.extras
        if(extras != null) {
            tableList = extras.getParcelableArrayList<TableModel>("list")
            testName = extras.getString("testName")
            privacy = extras.getString("privacy")
            fromHistory = extras.getBoolean("fromHistory")
            //testCreator = extras.getString("testCreator")
            Log.d("table", tableList?.size.toString())
        }
        if(fromHistory == true) {
            enableUpButton()
            finish.text = "Ок"
        }
        adapter = TableAdapter(context, activity, tableList!!)
        tableRecyclerView.layoutManager = LinearLayoutManager(this)
        tableRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        var score = 0
        if(tableList != null) {
            for(data in tableList!!) {
                var currentScore = data.isCorrect
                if(currentScore != "0") {score++}
            }
        }

        total.text = ""
        userScore.text = score.toString()
        answerNumber.text = tableList!!.size.toString()
        var totalRerc = (score*100/tableList!!.size)
        totalScore.text = (score*100/tableList!!.size).toString() + "%"
        when(totalRerc) {
            in 0..19 -> {grade.text = "1"}
            in 20..39 -> {grade.text = "2"}
            in 40..59 -> {grade.text = "3"}
            in 60..79 -> {grade.text = "4"}
            in 80..100 -> {grade.text = "5"}
            else -> {grade.text = "Error"}
        }

        finish.setOnClickListener {
            if(fromHistory == true) {
                val intent = Intent(context, HistoryActivity::class.java)
                startActivity(intent)
                activity.finish()
            } else {
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                activity.finish()
            }
        }
    }

    private fun init() {
        tableRecyclerView = findViewById(R.id.tableRecyclerView)
        context = applicationContext
        activity = this@TestResultsActivity
        //adapter = TableAdapter(context, activity, tableList!!)
        total = findViewById(R.id.total)
        userScore = findViewById(R.id.userScore)
        answerNumber = findViewById(R.id.answerNumber)
        totalScore = findViewById(R.id.totalScore)
        grade = findViewById(R.id.grade)
        finish = findViewById(R.id.button_finish_test)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(activity, HistoryActivity::class.java))
                this.finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}