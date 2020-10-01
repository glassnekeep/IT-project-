package com.example.it_project.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.R
import com.example.it_project.adapters.GroupAdapter
import com.example.it_project.adapters.ParticipantsAdapter
import com.example.it_project.fragments.CreateGroupFragment
import com.example.it_project.fragments.NewParticipantFragment
import com.example.it_project.models.ParticipantModel
import com.example.it_project.models.User
import com.example.it_project.utilities.deleteTestWithName
import com.example.it_project.utilities.initFirebase
import com.example.it_project.utilities.invokeNewActivity
import com.example.it_project.values.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ParticipantsActivity : BaseActivity() {

    private lateinit var addParticipantButton: ImageButton

    private lateinit var context: Context

    private lateinit var participantsRecyclerView: RecyclerView

    private lateinit var activity: Activity

    private lateinit var fragmentManager : FragmentManager

    private lateinit var adapter: ParticipantsAdapter

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
            dialogFragment.show(manager, "MyDialog")
        }
        participantsRecyclerView.layoutManager = LinearLayoutManager(this)
        participantsRecyclerView.adapter = adapter
        val extras: Bundle? = intent.extras
        if(extras != null) {
            var name = extras.getString("name")
            var secName = extras.getString("secName")
            var id = extras.getString("id")
            var newParticipant = ParticipantModel("${name} ${secName}", id!!)
            PARTICIPANT_LIST.add(newParticipant)
            adapter.notifyItemInserted(GROUP_LIST.size - 1)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onStart() {
        super.onStart()
        if(NEW_PARTICIPANT_ID != null) {

        }
    }

    private fun init() {
        addParticipantButton = findViewById(R.id.new_participant)
        participantsRecyclerView = findViewById(R.id.list_of_participants)
        context = applicationContext
        activity = this
        adapter = ParticipantsAdapter(context, activity, PARTICIPANT_LIST)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //ActivityUtilities.getInstance()
                invokeNewActivity(this@ParticipantsActivity, CurrentGroupActivity::class.java, true)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        invokeNewActivity(this, CurrentGroupActivity::class.java, true)
    }
}