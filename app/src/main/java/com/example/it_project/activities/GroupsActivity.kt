package com.example.it_project.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.example.it_project.utilities.deleteTestWithName
import com.example.it_project.utilities.initFirebase
import com.example.it_project.utilities.initFirebaseVariant2
import com.example.it_project.utilities.initGroupList
import com.example.it_project.values.CURRENT_TEST_NAME
import com.example.it_project.values.DATABASE_ROOT_NEW_GROUP
import com.example.it_project.values.GROUP_LIST
import com.example.it_project.values.NODE_GROUP_INFO
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)
        initToolbar(true)
        setToolbarTitle("Группы")
        init()
        initFirebase()
        enableUpButton()
        fragmentManager = this@GroupsActivity.supportFragmentManager
        createNewGroupButton.setOnClickListener {
            val dialogFragment = CreateGroupFragment()
            val manager = supportFragmentManager
            dialogFragment.show(manager, "MyDialog")
        }
        GROUP_LIST = ArrayList()
        /*val groupAddingListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(groupSnapshot: DataSnapshot in snapshot.children) {
                    var groupInfo: GroupModel? = groupSnapshot.child(NODE_GROUP_INFO).getValue(GroupModel::class.java)
                    //if(groupInfo != null) {GROUP_LIST.add(groupInfo)}
                    //adapter.notifyItemInserted(GROUP_LIST.size - 1)
                    //adapter.notifyDataSetChanged()
                    //GROUP_LIST = ArrayList()
                    addNewGroupToList(groupInfo!!)
                    Log.d("TAG", "${GROUP_LIST.size}")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        DATABASE_ROOT_NEW_GROUP.addListenerForSingleValueEvent(groupListener)*/
        //DATABASE_ROOT_NEW_GROUP.addValueEventListener(groupListener)
        adapter.notifyDataSetChanged()
        Log.d("TAG", "${GROUP_LIST.size}")
    }

    override fun onResume() {
        super.onResume()
        /*val groupAddingListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(groupSnapshot: DataSnapshot in snapshot.children) {
                    var groupInfo: GroupModel? = groupSnapshot.child(NODE_GROUP_INFO).getValue(GroupModel::class.java)
                    //if(groupInfo != null) {GROUP_LIST.add(groupInfo)}
                    //adapter.notifyItemInserted(GROUP_LIST.size - 1)
                    //adapter.notifyDataSetChanged()
                    //GROUP_LIST = ArrayList()
                    addNewGroupToList(groupInfo!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        DATABASE_ROOT_NEW_GROUP.addValueEventListener(groupListener)*/
        groupsRecyclerView.layoutManager = LinearLayoutManager(this)
        groupsRecyclerView.adapter = adapter
        adapter.notifyItemInserted(GROUP_LIST.size - 1)
        adapter.notifyDataSetChanged()
        Log.d("TAG", " FINAL ${GROUP_LIST.size}")
    }

    private fun init() {
        createNewGroupButton = findViewById(R.id.create_new_group_button)
        groupsRecyclerView = findViewById(R.id.groupsRecyclerView)
        context = applicationContext
        activity = this@GroupsActivity
        adapter = GroupAdapter(context, activity, GROUP_LIST)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                openQuitDialog()
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
        openQuitDialog()
    }

    /*private fun addNewGroupToList(newGroup: GroupModel) {
        GROUP_LIST.add(newGroup)
    }*/
}