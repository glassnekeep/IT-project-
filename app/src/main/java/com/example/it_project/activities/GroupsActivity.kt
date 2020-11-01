package com.example.it_project.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.R
import com.example.it_project.adapters.GroupAdapter
import com.example.it_project.fragments.CreateGroupFragment
import com.example.it_project.fragments.NewQuestionFragment
import com.example.it_project.models.GroupModel
import com.example.it_project.models.User
import com.example.it_project.utilities.*
import com.example.it_project.values.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class GroupsActivity : BaseActivity() {

    private lateinit var createNewGroupButton: FloatingActionButton

    private lateinit var context: Context

    private lateinit var groupsRecyclerView: RecyclerView

    private lateinit var activity: Activity

    private lateinit var fragmentManager : FragmentManager

    private lateinit var adapter: GroupAdapter

    private lateinit var listData: ArrayList<GroupModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)
        initToolbar(true)
        setToolbarTitle("Группы")
        init()
        initFirebase()
        enableUpButton()
        getDataFromDb()
        fragmentManager = this@GroupsActivity.supportFragmentManager
        createNewGroupButton.setOnClickListener {
            val dialogFragment = CreateGroupFragment()
            val manager = supportFragmentManager
            dialogFragment.show(manager, "MyDialog")
        }
        Log.d("TAG", "${GROUP_LIST.size}")
        //groupsRecyclerView.layoutManager = LinearLayoutManager(this)
        //groupsRecyclerView.adapter = adapter
    }

    private fun init() {
        createNewGroupButton = findViewById(R.id.create_new_group_button)
        groupsRecyclerView = findViewById(R.id.groupsRecyclerView)
        context = applicationContext
        activity = this@GroupsActivity
        //adapter = GroupAdapter(context, activity, GROUP_LIST)
        listData = ArrayList()
        adapter = GroupAdapter(context, activity, listData)
        groupsRecyclerView.layoutManager = LinearLayoutManager(this)
        groupsRecyclerView.adapter = adapter
    }

    private fun getDataFromDb() {
        val groupListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(listData.size >0) {listData.clear()}
                for(groupSnapshot: DataSnapshot in snapshot.children) {
                    var groupInfo: GroupModel? = groupSnapshot.child(NODE_GROUP_INFO).getValue(GroupModel::class.java)
                    //addNewGroupToList(groupInfo!!)
                    //Log.d("GROUP", "${GROUP_LIST.size}")
                    if(groupInfo != null) {
                        listData.add(groupInfo!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        DATABASE_ROOT_NEW_GROUP.addListenerForSingleValueEvent(groupListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this@GroupsActivity, MainActivity::class.java))
                finish()
                GROUP_LIST = ArrayList()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openQuitDialog() {
        var quitDialog = AlertDialog.Builder(
            this@GroupsActivity
        )
        quitDialog.setTitle("Вы уверены, что хотите отменить создание теста?")

        quitDialog.setPositiveButton("Да!"
        ) { dialog, which ->
            startActivity(Intent(this@GroupsActivity, MainActivity::class.java))
            finish()
        }

        quitDialog.setNegativeButton("Нет"
        ) { dialog, which ->
        }
        quitDialog.show()
    }

    override fun onBackPressed() {
        startActivity(Intent(this@GroupsActivity, MainActivity::class.java))
        GROUP_LIST = ArrayList()
        finish()
    }

    /*private fun addVeryNewGroupToList(newGroup: GroupModel) {
        GROUP_LIST.add(newGroup)
    }*/

}