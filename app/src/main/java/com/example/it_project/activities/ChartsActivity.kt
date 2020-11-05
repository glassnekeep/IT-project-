package com.example.it_project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import com.example.it_project.R
import com.example.it_project.models.AverageModel
import com.example.it_project.utilities.initFirebase
import com.example.it_project.values.DATABASE_ROOT_NEW_PRIVATE_TEST
import com.example.it_project.values.DATABASE_ROOT_USER
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_charts.*

class ChartsActivity : BaseActivity() {

    private var subject: String? = ""
    var barEntries1: ArrayList<BarEntry> = ArrayList()
    var lineEntries1: ArrayList<Entry> = ArrayList()
    var labels: ArrayList<String> = ArrayList()

    private lateinit var testList: ArrayList<String>
    private lateinit var averageList: ArrayList<AverageModel>
    private lateinit var userList: ArrayList<AverageModel>

    private lateinit var chart: BarChart

    private lateinit var line: LineChart

    private lateinit var compare: Button

    private lateinit var barDataset1: BarDataSet
    private lateinit var barDataset2: BarDataSet
    private lateinit var lineDataSet1: LineDataSet
    private lateinit var lineDataSet2: LineDataSet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charts)
        init()
        initToolbar(true)
        enableUpButton()
        initFirebase()
        setToolbarTitle("Графики")
        val extras: Bundle? = intent.extras
        if(extras != null) {subject = extras.getString("subject")}
        getTestList()
        Log.d("testList", testList.size.toString())
        getAverageList()
        Log.d("averageList", averageList.size.toString())
        //getUserList()
        Log.d("userList", userList.size.toString())

        compare.setOnClickListener {
            /*getAverageList()
            var barData1 = BarData(barDataset1, barDataset2)
            var dataSets: ArrayList<ILineDataSet> = ArrayList()
            dataSets.add(lineDataSet1)
            dataSets.add(lineDataSet2)
            chart.data = barData1
            line.data = LineData(dataSets)
            barData1.barWidth = 0.2f
            var barSpace = 0.04f
            var groupSpace = 0.5f
            chart.groupBars(-0.5f, groupSpace, barSpace)
            chart.invalidate()
            line.invalidate()*/
            invalidate2()
        }

    }

    private fun init() {
        testList = ArrayList()
        averageList = ArrayList()
        userList = ArrayList()
        compare = findViewById(R.id.button_compare)
        chart = findViewById(R.id.barChart)
        line = findViewById(R.id.lineChart)
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
        getUserList()
        val averageListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(averageList.size > 0) {averageList.clear()}
                for(testName in snapshot.children) {
                    var currentTestName = testName.child("test name").getValue(String::class.java)
                    for(neededTest in testList) {
                        if(neededTest == currentTestName) {
                            var score = testName.child("average").child("averageScore").getValue(String::class.java)?.substringBefore('%')
                            var grade = testName.child("average").child("averageGrade").getValue(String::class.java)
                            var model = AverageModel(currentTestName, score!!, grade!!)
                            averageList.add(model)
                            Log.d("AVERAGELIST", averageList.size.toString())
                        }
                    }
                }
                invalidate()
                Log.d("BOTH", "${averageList.size} ${userList.size}")
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
                //invalidate()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        DATABASE_ROOT_USER.child("test attendance").addListenerForSingleValueEvent(userListener)
    }

    private fun invalidate() {
        //var barEntries1: ArrayList<BarEntry> = ArrayList()
        //var lineEntries1: ArrayList<Entry> = ArrayList()
        //var labels: ArrayList<String> = ArrayList()
        for(i in 0..userList.size - 1) {
            var score = userList[i].averageScore.toInt()
            var grade = userList[i].averageGrade.toFloat()
            var name = userList[i].testName
            //entries1.add(BarEntry(score.toFloat(), i)) //TODO старый вариант при версии 1.7.4
            barEntries1.add(BarEntry(i.toFloat(), grade.toFloat()))
            lineEntries1.add(Entry(i.toFloat(), score.toFloat()))
            labels.add(name)
            //labels.add(i.toString())
        }
        var formatter = object: ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return labels[value.toInt()]
            }
        }
        var barEntries2: ArrayList<BarEntry> = ArrayList()
        var lineEntries2: ArrayList<Entry> = ArrayList()
        for(j in 0..averageList.size - 1) {
            var score = averageList[j].averageScore.toInt()
            var grade = averageList[j].averageGrade.toFloat()
            var name = averageList[j].testName
            barEntries2.add(BarEntry(j.toFloat(), grade.toFloat()))
            lineEntries2.add(Entry(j.toFloat(), score.toFloat()))
        }
        barDataset1 = BarDataSet(barEntries1, "Моя средняя оценка")
        barDataset2= BarDataSet(barEntries2, "Средняя оценка")
        lineDataSet1 = LineDataSet(lineEntries1, "Мой средний процент")
        lineDataSet1.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet2 = LineDataSet(lineEntries2, "Средний процент")
        lineDataSet2.axisDependency = YAxis.AxisDependency.LEFT
        //var dataset1 = chart.data.getDataSetByIndex(0)
        //var dataset2 = chart.data.getDataSetByIndex(1)
        barDataset1.color = resources.getColor(R.color.red)
        barDataset2.color = resources.getColor(R.color.blue)
        lineDataSet1.color = resources.getColor(R.color.red)
        lineDataSet2.color = resources.getColor(R.color.blue)
        //var barData: BarData = BarData(labels, dataset)
        var barData1 = BarData(barDataset1/*, barDataset2*/)
        var dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(lineDataSet1)
        //dataSets.add(lineDataSet2)
        var lineData1 = LineData(dataSets)
        //var barData2 = BarData(labels, dataset2)
        var lineAxis = line.xAxis
        lineAxis.valueFormatter = formatter
        lineAxis.granularity = 1f
        var chartAxis = chart.xAxis
        chartAxis.valueFormatter = formatter
        chartAxis.granularity = 1f
        //axis.setCenterAxisLabels(true)
        barData1.barWidth = 0.2f
        line.data = lineData1
        chart.data = barData1
        //var barSpace = 0.04f
        //var groupSpace = 0.5f
        //chart.groupBars(-0.5f, groupSpace, barSpace)
        //chart.notifyDataSetChanged()
        //chart.data = barData2
        line.invalidate()
        chart.invalidate()
    }

    private fun invalidate2() {
        barDataset1.clear()
        barDataset2.clear()
        lineDataSet1.clear()
        lineDataSet2.clear()
        for(i in 0..userList.size - 1) {
            var score = userList[i].averageScore.toInt()
            var grade = userList[i].averageGrade.toFloat()
            var name = userList[i].testName
            //entries1.add(BarEntry(score.toFloat(), i)) //TODO старый вариант при версии 1.7.4
            barEntries1.add(BarEntry(i.toFloat(), grade.toFloat()))
            lineEntries1.add(Entry(i.toFloat(), score.toFloat()))
            labels.add(name)
            //labels.add(i.toString())
        }
        var formatter = object: ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return labels[value.toInt()]
            }
        }
        var barEntries2: ArrayList<BarEntry> = ArrayList()
        var lineEntries2: ArrayList<Entry> = ArrayList()
        for(j in 0..averageList.size - 1) {
            var score = averageList[j].averageScore.toInt()
            var grade = averageList[j].averageGrade.toFloat()
            var name = averageList[j].testName
            barEntries2.add(BarEntry(j.toFloat(), grade.toFloat()))
            lineEntries2.add(Entry(j.toFloat(), score.toFloat()))
        }
        barDataset1 = BarDataSet(barEntries1, "Моя средняя оценка")
        barDataset2= BarDataSet(barEntries2, "Средняя оценка")
        lineDataSet1 = LineDataSet(lineEntries1, "Мой средний процент")
        lineDataSet1.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet2 = LineDataSet(lineEntries2, "Средний процент")
        lineDataSet2.axisDependency = YAxis.AxisDependency.LEFT
        //var dataset1 = chart.data.getDataSetByIndex(0)
        //var dataset2 = chart.data.getDataSetByIndex(1)
        barDataset1.color = resources.getColor(R.color.red)
        barDataset2.color = resources.getColor(R.color.blue)
        lineDataSet1.color = resources.getColor(R.color.red)
        lineDataSet2.color = resources.getColor(R.color.blue)
        //var barData: BarData = BarData(labels, dataset)
        var barData1 = BarData(barDataset1, barDataset2)
        var dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(lineDataSet1)
        dataSets.add(lineDataSet2)
        var lineData1 = LineData(dataSets)
        //var barData2 = BarData(labels, dataset2)
        var lineAxis = line.xAxis
        lineAxis.valueFormatter = formatter
        lineAxis.granularity = 1f
        var chartAxis = chart.xAxis
        chartAxis.valueFormatter = formatter
        chartAxis.granularity = 1f
        //axis.setCenterAxisLabels(true)
        barData1.barWidth = 0.2f
        line.data = lineData1
        chart.data = barData1
        var barSpace = 0.04f
        var groupSpace = 0.5f
        chart.groupBars(-0.5f, groupSpace, barSpace)
        chart.notifyDataSetChanged()
        chart.data = barData1
        line.invalidate()
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