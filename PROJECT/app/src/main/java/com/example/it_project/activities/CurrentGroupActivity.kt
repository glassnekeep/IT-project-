package com.example.it_project.activities

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.R
import com.example.it_project.adapters.CreateNewTestInGroupAdapter
import com.example.it_project.adapters.TestAdapter
import com.example.it_project.models.TestInfoModel
import com.example.it_project.models.TestModel
import com.example.it_project.utilities.invokeNewActivity
import com.example.it_project.values.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class CurrentGroupActivity : BaseActivity() {

    private lateinit var groupId: TextView
    private lateinit var participantsLayout: LinearLayout
    private lateinit var addNewTestToGroup: FloatingActionButton
    private var groupName: String = ""

    private lateinit var testsRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var listData: ArrayList<TestModel>
    private lateinit var adapter: TestAdapter
    private lateinit var activity: Activity
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_group)
        initToolbar(true)
        init()
        enableUpButton()
        val extras: Bundle? = intent.extras
        if(extras != null) {groupName = extras.getString("groupName")!!}
        setToolbarTitle(groupName)
        getGroupId()
        getDataFromDb()
        if(ADMIN_STATUS != "admin") {
            addNewTestToGroup.visibility = View.GONE
        }

        var clipboardManager: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        groupId.setOnClickListener {
            var text = groupId.text.toString()
            var clipData = ClipData.newPlainText("text", text)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(context, "Скопировано в буфер обмена", Toast.LENGTH_SHORT).show()
        }

        addNewTestToGroup.setOnClickListener {
            val intent = Intent(this, NewTestInGroupActivity::class.java)
            intent.putExtra("groupName", groupName)
            startActivity(intent)
            this.finish()
        }

        participantsLayout.setOnClickListener {
            var intent: Intent = Intent(this, ParticipantsActivity::class.java)
            intent.putExtra("groupName", groupName)
            startActivity(intent)
            this.finish()
        }

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

    override fun onRestart() {
        super.onRestart()
        getGroupId()
    }

    private fun init() {
        groupId = findViewById(R.id.groupId)
        participantsLayout = findViewById(R.id.participantsLayout)
        addNewTestToGroup = findViewById(R.id.buttonAddGroupTest)
        context = applicationContext
        activity = this
        listData = ArrayList()
        searchView = findViewById(R.id.searchView)
        testsRecyclerView = findViewById(R.id.list_of_group_tests)
        adapter = TestAdapter(context, activity, listData)
    }

    private fun getGroupId() {
        var IDListener = object: ValueEventListener {
            var Id: String? = null
            override fun onDataChange(snapshot: DataSnapshot) {
                Id = snapshot.getValue(String::class.java)
                groupId.text = Id
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        DATABASE_ROOT_NEW_GROUP.child(groupName!!).child(NODE_ID).child("id").addListenerForSingleValueEvent(IDListener)
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
        DATABASE_ROOT_NEW_GROUP.child(groupName).child("tests").addListenerForSingleValueEvent(publicTestListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                intent()
                PARTICIPANT_LIST = ArrayList()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        intent()
        PARTICIPANT_LIST = ArrayList()
    }

    private fun intent() {
        var intent = Intent(this@CurrentGroupActivity, GroupsActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}