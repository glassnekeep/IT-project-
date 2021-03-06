package com.example.it_project.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.CommunicatorParticipant
import com.example.it_project.R
import com.example.it_project.adapters.GroupAdapter
import com.example.it_project.adapters.ParticipantsAdapter
import com.example.it_project.fragments.CreateGroupFragment
import com.example.it_project.fragments.NewParticipantFragment
import com.example.it_project.models.ParticipantModel
import com.example.it_project.models.User
import com.example.it_project.utilities.deleteParticipantFromGroup
import com.example.it_project.utilities.deleteTestWithName
import com.example.it_project.utilities.initFirebase
import com.example.it_project.utilities.invokeNewActivity
import com.example.it_project.values.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ParticipantsActivity : BaseActivity() /*CommunicatorParticipant*/ {

    private lateinit var addParticipantButton: FloatingActionButton

    private lateinit var context: Context

    private lateinit var participantsRecyclerView: RecyclerView

    private lateinit var activity: Activity

    private lateinit var fragmentManager : FragmentManager

    private lateinit var adapter: ParticipantsAdapter

    private lateinit var listData: ArrayList<ParticipantModel>

    private var groupName: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_participants)
        initToolbar(true)
        setToolbarTitle("Участники")
        init()
        enableUpButton()
        initFirebase()
        fragmentManager = this@ParticipantsActivity.supportFragmentManager
        addParticipantButton.setOnClickListener {
            val dialogFragment = NewParticipantFragment()
            val manager = supportFragmentManager
            val bundle = Bundle()
            bundle.putString("groupName", groupName)
            val transaction = this.supportFragmentManager.beginTransaction()
            dialogFragment.arguments = bundle
            dialogFragment.show(manager, "Dialog")
        }
        val extras: Bundle? = intent.extras
        if(extras != null) {groupName = extras.getString("groupName")}
        adapter = ParticipantsAdapter(context, activity, groupName!!, listData)
        participantsRecyclerView.layoutManager = LinearLayoutManager(this)
        participantsRecyclerView.adapter = adapter
        getDataFromDb()
    }

    private fun init() {
        addParticipantButton = findViewById(R.id.buttonAddParticipant)
        participantsRecyclerView = findViewById(R.id.list_of_participants)
        context = applicationContext
        activity = this
        listData = ArrayList()
        if(ADMIN_STATUS == "admin") {
            addParticipantButton.visibility = View.VISIBLE
        } else {
            addParticipantButton.visibility = View.GONE
        }
        //adapter = ParticipantsAdapter(context, activity, listData)
    }

    private fun getDataFromDb() {
        val participantListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(listData.size > 0) {listData.clear()}
                for(participant in snapshot.children) {
                    var participantModel = participant.child("participant info").getValue(ParticipantModel::class.java)
                    if(participantModel != null) {
                        listData.add(participantModel)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        DATABASE_ROOT_NEW_GROUP.child(groupName!!).child("participants").addValueEventListener(participantListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //ActivityUtilities.getInstance()
                //invokeNewActivity(this@ParticipantsActivity, CurrentGroupActivity::class.java, true)
                val intent = Intent(this, CurrentGroupActivity::class.java)
                intent.putExtra("groupName", groupName)
                startActivity(intent)
                this.finish()
                //PARTICIPANT_LIST = ArrayList()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        //invokeNewActivity(this, CurrentGroupActivity::class.java, true)
        //PARTICIPANT_LIST = ArrayList()
        val intent = Intent(this, CurrentGroupActivity::class.java)
        intent.putExtra("groupName", groupName)
        startActivity(intent)
        this.finish()
    }
}