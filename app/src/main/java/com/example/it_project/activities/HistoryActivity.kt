package com.example.it_project.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.R
import com.example.it_project.adapters.GroupAdapter
import com.example.it_project.adapters.HistoryAdapter
import com.example.it_project.models.GroupModel
import com.example.it_project.models.HistoryModel
import com.example.it_project.models.TableModel
import com.example.it_project.models.TotalModel
import com.example.it_project.values.DATABASE_ROOT_USER
import com.example.it_project.values.NODE_TEST_INFO
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class HistoryActivity : BaseActivity() {

    private lateinit var context: Context

    private lateinit var historyRecyclerView: RecyclerView

    private lateinit var activity: Activity

    //private lateinit var fragmentManager : FragmentManager

    private lateinit var adapter: HistoryAdapter

    private lateinit var listData: ArrayList<HistoryModel>

    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        initToolbar(true)
        init()
        enableUpButton()
        setToolbarTitle("История")
        getDataFromDb()
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun init() {
        historyRecyclerView = findViewById(R.id.historyRecyclerView)
        context = applicationContext
        activity = this@HistoryActivity
        listData = ArrayList()
        searchView = findViewById(R.id.searchViewHistory)
        adapter = HistoryAdapter(context, activity, listData)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.adapter = adapter
    }

    private fun getDataFromDb() {
        val historyListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(listData.size >0) {listData.clear()}
                for(historySnapshot: DataSnapshot in snapshot.children) {
                    //var testName = ""
                    for(name: DataSnapshot in historySnapshot.children) {
                        var testName = name.child(NODE_TEST_INFO).child("testName").getValue(String::class.java)
                        var privacy = name.child(NODE_TEST_INFO).child("privacy").getValue(String::class.java)
                        var testCreator = name.child(NODE_TEST_INFO).child("testCreator").getValue(String::class.java)
                        var subject = name.child(NODE_TEST_INFO).child("subject").getValue(String::class.java)
                        var time = name.child(NODE_TEST_INFO).child("time").getValue(String::class.java)
                        var total = name.child("total").getValue(TotalModel::class.java)
                        //var tableList = name.child("answerInfo").getValue(ArrayList<TableModel>()::class.java)
                        var tableList = ArrayList<TableModel>()
                        for(table: DataSnapshot in name.child("answerInfo").children) {
                            tableList.add(table.getValue(TableModel::class.java)!!)
                        }
                        var historyModel = HistoryModel(testName!!, privacy!!, testCreator!!, subject!!, time!!, total!!, tableList!!)
                        listData.add(historyModel)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        DATABASE_ROOT_USER.child("test attendance").addListenerForSingleValueEvent(historyListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this@HistoryActivity, MainActivity::class.java))
                this.finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@HistoryActivity, MainActivity::class.java))
        this.finish()
    }
}