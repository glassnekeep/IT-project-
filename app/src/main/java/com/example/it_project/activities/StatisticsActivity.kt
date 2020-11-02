package com.example.it_project.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.R
import com.example.it_project.adapters.HistoryAdapter
import com.example.it_project.adapters.StatisticsAdapter
import com.example.it_project.models.HistoryModel
import com.example.it_project.models.RawStatisticsModel
import com.example.it_project.models.StatisticsModel
import com.example.it_project.values.DATABASE_ROOT_USER
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mikepenz.fastadapter.dsl.genericFastAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StatisticsActivity : BaseActivity() {

    private lateinit var context: Context

    private lateinit var statisticRecyclerView: RecyclerView

    private lateinit var activity: Activity

    private lateinit var adapter: StatisticsAdapter

    private lateinit var listData: ArrayList<StatisticsModel>

    private lateinit var searchView: SearchView

    private lateinit var rawDataList: ArrayList<RawStatisticsModel>

    private lateinit var subjectList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        initToolbar(true)
        init()
        enableUpButton()
        setToolbarTitle("Статистика")
        fillSubjectList()
        //CoroutineScope(Dispatchers.IO).launch {
          //  getDataFromDb()
        //}
        getDataFromDb()
        //fillListData()
        Log.d("subjectList.size", subjectList.size.toString())
        Log.d("rawDataList.size", rawDataList.size.toString())
        Log.d("listData.size", listData.size.toString())
        adapter.notifyDataSetChanged()
    }

    private fun init() {
        context = applicationContext
        searchView = findViewById(R.id.searchViewStatistics)
        statisticRecyclerView = findViewById(R.id.statisticsRecyclerView)
        activity = this@StatisticsActivity
        listData = ArrayList<StatisticsModel>()
        subjectList = ArrayList()
        rawDataList = ArrayList()
        adapter = StatisticsAdapter(context, activity, listData)
        statisticRecyclerView.layoutManager = LinearLayoutManager(this)
        statisticRecyclerView.adapter = adapter
    }

    private fun getDataFromDb() {
        val rawListListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(rawDataList.size > 0) {rawDataList.clear()}
                for(currentAttendance: DataSnapshot in snapshot.children) {
                    for(test: DataSnapshot in currentAttendance.children) {
                        var subject = test.child("test info").child("subject").getValue(String::class.java)
                        Log.d("subject", subject!!)
                        var perCent = test.child("total").child("totalScore").getValue(String::class.java)
                        Log.d("total", perCent!!)
                        var grade = test.child("total").child("grade").getValue(String::class.java)
                        Log.d("grade", grade!!)
                        perCent = perCent?.substringBefore('%')
                        Log.d("total", perCent!!)
                        val model = RawStatisticsModel(subject!!, perCent!!, grade!!)
                        rawDataList.add(model)
                        //fillListData()
                    }
                    //fillListData()
                    //adapter.notifyDataSetChanged()
                }
                fillListData()
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        DATABASE_ROOT_USER.child("test attendance").addListenerForSingleValueEvent(rawListListener)
        //fillListData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this@StatisticsActivity, MainActivity::class.java))
                this.finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@StatisticsActivity, MainActivity::class.java))
        this.finish()
    }

    private fun fillSubjectList() {
        subjectList.add("История")
        subjectList.add("Биология")
        subjectList.add("География")
        subjectList.add("Англ. Яз.")
        subjectList.add("Рус. Яз")
        subjectList.add("Алгебра")
        subjectList.add("Геометрия")
        subjectList.add("Физика")
        subjectList.add("Литература")
        subjectList.add("Химия")
        subjectList.add("Информатика")
    }

    private fun fillListData() {
        //Log.d("subjectList!!!.size", subjectList.size.toString())
        //Log.d("rawDataList!!!.size", rawDataList.size.toString())
        for(subject in subjectList) {
            var testNumber = 0
            var perCent = 0
            var grade = 0
            Log.d("subjectList!!!.size", subjectList.size.toString())
            Log.d("rawDataList!!!.size", rawDataList.size.toString())
            for(test in rawDataList) {
                Log.d("i'", test.toString())
                if(test.subject == subject) {
                    testNumber ++
                    Log.d("testNumber", testNumber.toString())
                    perCent = perCent + test.perCent.toInt()
                    grade = grade + test.grade.toInt()
                    Log.d("testNumber", testNumber.toString())
                } else {
                    Log.d("subject", "${subject} ${test.subject}")
                }
                Log.d("subject", "${subject} ${test.subject}")
            }
            var finalTestNumber = 0
            var finalPerCent = 0
            var finalGrade = 0
            if(testNumber != 0) {
                finalTestNumber = testNumber
                finalPerCent = perCent/testNumber
                finalGrade = grade/testNumber
            }
            listData.add(StatisticsModel(subject, finalTestNumber.toString(), (finalPerCent).toString(), (finalGrade).toString()))
            adapter.notifyDataSetChanged()
        }
    }
}