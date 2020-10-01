package com.example.it_project.utilities

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.it_project.*
import com.example.it_project.models.*
import com.example.it_project.values.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.NullPointerException
import java.lang.reflect.Array

private var backPressed: Long = 0

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    CURRENT_UID = AUTH.uid.toString()
    DATABASE_ROOT_USER = REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
    DATABASE_ROOT_NEW_TEST = REF_DATABASE_ROOT.child(NODE_TEST)
    DATABASE_ROOT_NEW_GROUP = REF_DATABASE_ROOT.child(NODE_GROUP)
    DATABASE_ROOT_GROUP_IDS = REF_DATABASE_ROOT.child(NODE_GROUP_IDS)
    DATABASE_ROOT_TEST_IDS = REF_DATABASE_ROOT.child(NODE_TEST_IDS)
    USER = User()
}

fun initFirebaseVariant2() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    CURRENT_UID = AUTH.uid.toString()
    DATABASE_ROOT_USER = REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
    DATABASE_ROOT_NEW_TEST = REF_DATABASE_ROOT.child(NODE_TEST)
    DATABASE_ROOT_NEW_GROUP = REF_DATABASE_ROOT.child(NODE_GROUP)
    DATABASE_ROOT_GROUP_IDS = REF_DATABASE_ROOT.child(NODE_GROUP_IDS)
    DATABASE_ROOT_TEST_IDS = REF_DATABASE_ROOT.child(NODE_TEST_IDS)
    USER = User()
    initGroupList()
}

fun initParticipantList(group: String) {
    val participantListener = object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for(groupSnapshot: DataSnapshot in snapshot.children) {
                var participantInfo: ParticipantModel? = groupSnapshot.child(NODE_PARTICIPANT_INFO).getValue(ParticipantModel::class.java)
                //if(groupInfo != null) {GROUP_LIST.add(groupInfo)}
                //adapter.notifyItemInserted(GROUP_LIST.size - 1)
                //adapter.notifyDataSetChanged()
                //GROUP_LIST = ArrayList()
                if(participantInfo != null) {
                    addNewParticipantToList(participantInfo!!)
                }
                Log.d("PARTICIPANT", "${PARTICIPANT_LIST.size}")
            }
        }

        override fun onCancelled(error: DatabaseError) {

        }
    }
    DATABASE_ROOT_NEW_GROUP.child(group).child(NODE_PARTICIPANTS).addListenerForSingleValueEvent(participantListener)
}

fun addNewParticipantToList(newParticipant: ParticipantModel) {
    PARTICIPANT_LIST.add(newParticipant)
}

fun addParticipantToGroup(participant: ParticipantModel, participantID: String, groupName: String) {
    DATABASE_ROOT_NEW_GROUP.child(groupName).child(NODE_PARTICIPANTS).child(participantID).child(NODE_PARTICIPANT_INFO).setValue(participant)
}

fun showToast(context: Context?, message: String?) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun addNewGroupToList(newGroup: GroupModel) {
    GROUP_LIST.add(newGroup)
}

fun initGroupList() {
    val groupListener = object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for(groupSnapshot: DataSnapshot in snapshot.children) {
                var groupInfo: GroupModel? = groupSnapshot.child(NODE_GROUP_INFO).getValue(GroupModel::class.java)
                //if(groupInfo != null) {GROUP_LIST.add(groupInfo)}
                //adapter.notifyItemInserted(GROUP_LIST.size - 1)
                //adapter.notifyDataSetChanged()
                //GROUP_LIST = ArrayList()
                addNewGroupToList(groupInfo!!)
                Log.d("GROUP", "${GROUP_LIST.size}")
            }
        }

        override fun onCancelled(error: DatabaseError) {

        }
    }
    DATABASE_ROOT_NEW_GROUP.addListenerForSingleValueEvent(groupListener)
}

fun getCurrentUserName() {
    var nameListener = object: ValueEventListener {
        var name: String? = null
        override fun onDataChange(snapshot: DataSnapshot) {
            var user: User? = snapshot.getValue(User::class.java)
            name = user?.name
            pushUserName(name)
        }

        override fun onCancelled(error: DatabaseError) {

        }
    }
    DATABASE_ROOT_USER.addValueEventListener(nameListener)
}

fun getCurrentUserSecName() {
    var secNameListener = object: ValueEventListener {
        var secName: String? = null
        override fun onDataChange(snapshot: DataSnapshot) {
            var user: User? = snapshot.getValue(User::class.java)
            secName = user?.secName
            pushUserSecName(secName)
        }

        override fun onCancelled(error: DatabaseError) {

        }
    }
    DATABASE_ROOT_USER.addValueEventListener(secNameListener)
}

fun createTestIDWithName(testName: String) {
    var id = DATABASE_ROOT_TEST_IDS.push().key
    var idName: IdModel = IdModel(id!!, testName)
    DATABASE_ROOT_TEST_IDS.child(id!!).child(NODE_ID).setValue(idName)
    DATABASE_ROOT_NEW_TEST.child(testName).child(NODE_ID).setValue(id)
}

fun createGroupIDWithName(groupName: String, numberUsers: Int, creatorName: String) {
    var id = DATABASE_ROOT_GROUP_IDS.push().key
    var idName = IdModel(id!!, groupName)
    var newGroup = GroupModel(groupName, numberUsers, creatorName)
    DATABASE_ROOT_GROUP_IDS.child(id!!).child(NODE_ID).setValue(idName)
    DATABASE_ROOT_NEW_GROUP.child(groupName).child(NODE_ID).setValue(idName)
    DATABASE_ROOT_NEW_GROUP.child(groupName).child(NODE_GROUP_INFO).setValue(newGroup)
}

fun setGroupInfo(groupName: String, numberUsers: Int, creatorName: String) {
    var newGroup = GroupModel(groupName, numberUsers, creatorName)
    DATABASE_ROOT_NEW_GROUP.child(groupName).child(NODE_GROUP_INFO).setValue(newGroup)
}

fun deleteTestWithName(testName: String) {
    DATABASE_ROOT_NEW_TEST.child(testName).removeValue()
}

fun createQuestionInTest(testName : String, questionInfo: QuestionModel, answers: ArrayList<String>) {
    DATABASE_ROOT_NEW_TEST.child(testName).child(NODE_QUESTIONS).child(questionInfo.name).setValue(questionInfo)
    var questionOrder = 1
    for (answer in answers) {
        DATABASE_ROOT_NEW_TEST.child(testName).child(NODE_QUESTIONS).child(questionInfo.name).child(NODE_ANSWERS).child("Answer №${questionOrder}").setValue(answer)
        questionOrder++
    }
}

fun createUserInDatabase(name: String, secName: String, email: String, admin: String) {
    NEW_USER = User(name, secName, email, admin)
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).setValue(NEW_USER)
}



fun tapPromtToExit(activity: Activity) {
    if (backPressed + 2500 > System.currentTimeMillis()) {
        activity.finish()
        //super.onBackPressed()
    } else {
        showToast(activity.applicationContext, activity.resources.getString(R.string.tap_again))
    }
    backPressed = System.currentTimeMillis()
}

fun pushUserName(name: String?) {
    CURRENT_USER_NAME = name
}

fun pushUserSecName(secName: String?) {
    CURRENT_USER_SECNAME = secName
}



