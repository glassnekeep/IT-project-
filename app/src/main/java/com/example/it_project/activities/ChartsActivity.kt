package com.example.it_project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.example.it_project.R
import com.example.it_project.models.AverageModel
import com.example.it_project.utilities.initFirebase
import com.example.it_project.values.DATABASE_ROOT_NEW_PRIVATE_TEST
import com.example.it_project.values.DATABASE_ROOT_USER
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_charts.*

class ChartsActivity : BaseActivity() {

    private var subject: String? = ""

    private lateinit var testList: ArrayList<String>
    private lateinit var averageList: ArrayList<AverageModel>
    private lateinit var userList: ArrayList<AverageModel>

    private lateinit var chart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charts)
        init()
        initToolbar(true)
        enableUpButton()
        initFirebase()
        val extras: Bundle? = intent.extras
        if(extras != null) {subject = extras.getString("subject")}
        getTestList()
        Log.d("testList", testList.size.toString())
        getAverageList()
        Log.d("averageList", averageList.size.toString())
        getUserList()
        Log.d("userList", userList.size.toString())

        Log.d("testList", testList.size.toString())
        Log.d("averageList", averageList.size.toString())
        Log.d("userList", userList.size.toString())

        /*var entries: ArrayList<BarEntry> = ArrayList()
        var labels: ArrayList<String> = ArrayList()
        for(i in 0..userList.size) {
            var score = userList[i].averageScore.toInt()
            var name = userList[i].testName
            entries.add(BarEntry(score.toFloat(), i.toFloat()))
            labels.add(name)
        }
        var dataset: BarDataSet = BarDataSet(entries, "Tests")
        var barData: BarData = BarData(dataset)
        chart.data = barData
        chart.invalidate()*/
    }

    private fun init() {
        testList = ArrayList()
        averageList = ArrayList()
        userList = ArrayList()

        chart = findViewById(R.id.barChart)
    }

    private fun getTestList() {
        val testListListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(testList.size > 0) {testList.clear()}
                for(attendance in snapshot.children) {
                    for(testName in attendance.children) {
                        var name = testName.child("test info").child("testName").getValue(String::class.java)
                        var currentSubject = testName.child("test info").child("subject").getValue(String::class.java)
                        if(currentSubject == subject) {
                            if(name != null) {
                                if(!testList.contains(name)) {
                                    testList.add(name)
                                    Log.d("TESTLIST", testList.size.toString())
                                }
                            }
                        } else {
                            Log.d("difference", "${currentSubject} ${subject}")
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        DATABASE_ROOT_USER.child("test attendance").addListenerForSingleValueEvent(testListListener)
    }
    private fun getAverageList() {
        val averageListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(averageList.size > 0) {averageList.clear()}
                for(testName in snapshot.children) {
                    var currentTestName = testName.child("test name").getValue(String::class.java)
                    for(neededTest in testList) {
                        if(neededTest == currentTestName) {
                            var score = testName.child("average").child("averageScore").getValue(String::class.java)
                            var grade = testName.child("average").child("averageGrade").getValue(String::class.java)
                            var model = AverageModel(currentTestName, score!!, grade!!)
                            averageList.add(model)
                            Log.d("AVERAGELIST", averageList.size.toString())
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        DATABASE_ROOT_NEW_PRIVATE_TEST.addListenerForSingleValueEvent(averageListener)
    }

    private fun getUserList() {
        val userListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(userList.size > 0) {userList.clear()}
                for(neededTest in testList) {
                    var number = 0
                    var grade = 0
                    var score = 0
                    for(time in snapshot.children) {
                        for(name in time.children) {
                            var testName = name.child("test info").child("testName").getValue(String::class.java)
                            if(testName == neededTest) {
                                number++
                                grade += name.child("total").child("grade").getValue(String::class.java)!!.toInt()
                                score += name.child("total").child("totalScore").getValue(String::class.java)!!.substringBefore("%").toInt()
                            }
                        }
                    }
                    var strGrade = (grade.toFloat()/number).toString()
                    if(strGrade.length > 4) {
                        strGrade = strGrade.substring(0,4)
                    }
                    var model = AverageModel(neededTest, (score/number).toString(), strGrade)
                    userList.add(model)
                    Log.d("USERLIST", userList.size.toString())
                }
                invalidate()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        DATABASE_ROOT_USER.child("test attendance").addListenerForSingleValueEvent(userListener)
    }

    private fun invalidate() {
        var entries: ArrayList<BarEntry> = ArrayList()
        var labels: ArrayList<String> = ArrayList()
        for(i in 0..userList.size - 1) {
            var score = userList[i].averageScore.toInt()
            var name = userList[i].testName
            entries.add(BarEntry(score.toFloat(), i))
            labels.add(name)
        }
        var dataset: BarDataSet = BarDataSet(entries, "Tests")
        //dataset.setColors(ColorTemplate.LIBERTY_COLORS)
        dataset.color = resources.getColor(R.color.red)
        var barData: BarData = BarData(labels, dataset)
        chart.data = barData
        chart.invalidate()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this@ChartsActivity, StatisticsActivity::class.java))
                this.finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ChartsActivity, StatisticsActivity::class.java))
        this.finish()
    }
}