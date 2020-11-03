package com.example.it_project.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.R
import com.example.it_project.adapters.CreateNewTestInGroupAdapter
import com.example.it_project.adapters.TestAdapter
import com.example.it_project.models.TestInfoModel
import com.example.it_project.models.TestModel
import com.example.it_project.values.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class NewTestInGroupActivity : BaseActivity() {

    private lateinit var adapter: CreateNewTestInGroupAdapter
    private lateinit var activity: Activity
    private lateinit var context: Context
    //private var toolbar: Toolbar? = null
    private lateinit var testsRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var listData: ArrayList<TestModel>

    private var groupName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_test_ingroup)
        init()
        initToolbar(true)
        enableUpButton()
        val extras: Bundle? = intent.extras
        if(extras != null) {groupName = extras.getString("groupName")!!}
        setToolbarTitle("Новый тест для группы ${groupName}")
        adapter = CreateNewTestInGroupAdapter(context, activity, groupName, listData)
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
        testsRecyclerView.layoutManager = LinearLayoutManager(this)
        testsRecyclerView.adapter = adapter
    }
    private fun init() {
        listData = ArrayList()
        context = applicationContext
        activity = this
        testsRecyclerView = findViewById(R.id.list_tests)
        searchView = findViewById(R.id.searchView)
    }

    private fun getDataFromDb() {
        val publicTestListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(listData.size > 0) {listData.clear()}
                for(testInfoSnapshot: DataSnapshot in snapshot.children) {
                    var testInfo: TestInfoModel? = testInfoSnapshot.child(NODE_TEST_INFO).getValue(
                        TestInfoModel::class.java)
                    var testName: String? = testInfoSnapshot.child(NODE_TEST_NAME).getValue(String::class.java)
                    var creatorName: String? = testInfo?.creatorName
                    var privacy: String? = testInfo?.privacy
                    var subject: String? = testInfo?.subject
                    var testId: String? = testInfoSnapshot.child(NODE_ID).getValue(String::class.java)
                    var time: String? = testInfo?.time
                    var testModel: TestModel = TestModel(testName!!, creatorName!!, privacy!!, subject!!, testId!!, time!!)
                    //addNewPublicTestToList(testModel)
                    listData.add(testModel)
                    //Log.d("TEST", "${PUBLIC_TESTS_LIST.size}")
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        DATABASE_ROOT_USER.child("tests").child("private tests").addListenerForSingleValueEvent(publicTestListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, CurrentGroupActivity::class.java)
                intent.putExtra("groupName", groupName)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val intent = Intent(this, CurrentGroupActivity::class.java)
        intent.putExtra("groupName", groupName)
        startActivity(intent)
    }
    //TODO Нужно, чтобы при отказе проходить тест, возвращало не в MainActivity, а в CurrentGroupActivity
}