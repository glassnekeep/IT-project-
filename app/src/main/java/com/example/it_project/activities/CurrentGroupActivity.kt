package com.example.it_project.activities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.example.it_project.R
import com.example.it_project.values.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class CurrentGroupActivity : BaseActivity() {

    private lateinit var groupId: TextView
    private lateinit var participantsLayout: LinearLayout
    private var groupName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_group)
        initToolbar(true)
        init()
        enableUpButton()
        val extras: Bundle? = intent.extras
        if(extras != null) { groupName = extras.getString("groupName")!!}
        setToolbarTitle(groupName)
        getGroupId()
        //if(ADAPTER_GROUP_NAME != null){
            //setToolbarTitle(ADAPTER_GROUP_NAME!!)
            /*var IDListener = object: ValueEventListener {
                var ID: String? = null
                override fun onDataChange(snapshot: DataSnapshot) {
                    ID = snapshot.getValue(String::class.java)
                    groupId.text = ID
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }
            var groupName = ADAPTER_GROUP_NAME
            DATABASE_ROOT_NEW_GROUP.child(groupName!!).child(NODE_ID).child("id").addValueEventListener(IDListener)*/
        //}

        var clipboardManager: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        groupId.setOnClickListener {
            var text = groupId.text.toString()
            var clipData = ClipData.newPlainText("text", text)
            clipboardManager.setPrimaryClip(clipData)
        }


        participantsLayout.setOnClickListener {
            var intent: Intent = Intent(this, ParticipantsActivity::class.java)
            intent.putExtra("groupName", groupName)
            startActivity(intent)
            this.finish()
        }
    }

    private fun init() {
        groupId = findViewById(R.id.groupId)
        participantsLayout = findViewById(R.id.participantsLayout)
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
        DATABASE_ROOT_NEW_GROUP.child(groupName!!).child(NODE_ID).child("id").addValueEventListener(IDListener)
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