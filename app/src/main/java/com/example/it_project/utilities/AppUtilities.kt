package com.example.it_project.utilities

import android.app.Activity
import android.content.Context
import android.renderscript.Sampler
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
    DATABASE_ROOT_NEW_PUBLIC_TEST = REF_DATABASE_ROOT.child(NODE_TEST_PUBLIC)
    DATABASE_ROOT_NEW_PRIVATE_TEST = REF_DATABASE_ROOT.child(NODE_TEST_PRIVATE)
    DATABASE_ROOT_NEW_GROUP = REF_DATABASE_ROOT.child(NODE_GROUP)
    DATABASE_ROOT_GROUP_IDS = REF_DATABASE_ROOT.child(NODE_GROUP_IDS)
    DATABASE_ROOT_TEST_IDS = REF_DATABASE_ROOT.child(NODE_TEST_IDS)
    USER = User()
    initPublicTestsList()
    getCurrentUser()
}

fun getCurrentUser() {
    val currentUserListener = object: ValueEventListener {
        var currentUser: User? = null
        override fun onDataChange(snapshot: DataSnapshot) {
            currentUser= snapshot.getValue(User::class.java)
            setCurrentUser(currentUser)
        }

        override fun onCancelled(error: DatabaseError) {

        }
    }
    DATABASE_ROOT_USER.addValueEventListener(currentUserListener)
}

fun createTestAttendanceByUserInTestNode(testName: String, privacy: String, time: String, total: TotalModel, tableList: ArrayList<TableModel>) {
    if(privacy == "Публичный") {
        DATABASE_ROOT_NEW_PUBLIC_TEST.child(testName).child(NODE_SOLUTIONS).child(CURRENT_UID).child(time).child("answerInfo").setValue(tableList)
        DATABASE_ROOT_NEW_PUBLIC_TEST.child(testName).child(NODE_SOLUTIONS).child(CURRENT_UID).child(time).child("total").setValue(total)
    } else {
        DATABASE_ROOT_NEW_PRIVATE_TEST.child(testName).child(NODE_SOLUTIONS).child(CURRENT_UID).child(time).child("answerInfo").setValue(tableList)
        DATABASE_ROOT_NEW_PRIVATE_TEST.child(testName).child(NODE_SOLUTIONS).child(CURRENT_UID).child(time).child("total").setValue(total)
        getPrivateTestAverage(testName)
        //TODO Не забудь проверить как эта штука работает!!!
    }
}

fun deleteParticipantFromGroup(groupName: String, id: String) {
    DATABASE_ROOT_NEW_GROUP.child(groupName).child("participants").child(id).removeValue()
    val listener = object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            var numberUsers = snapshot.getValue(Int::class.java)
            numberUsers = numberUsers!! - 1
            DATABASE_ROOT_NEW_GROUP.child(groupName).child("group info").child("numberUsers").setValue(numberUsers)
        }

        override fun onCancelled(error: DatabaseError) {

        }
    }
    DATABASE_ROOT_NEW_GROUP.child(groupName).child("group info").child("numberUsers").addListenerForSingleValueEvent(listener)
}

fun createTestAttendanceByUserInUserNode(testName: String, privacy: String, subject: String, testCreator: String, time: String, total: TotalModel, tableList: ArrayList<TableModel>) {
    /*DATABASE_ROOT_USER.child(NODE_TEST_ATTENDANCE).child(testName).child(time).child("answerInfo").setValue(tableList)
    DATABASE_ROOT_USER.child(NODE_TEST_ATTENDANCE).child(testName).child(time).child(NODE_TEST_INFO).child("privacy").setValue(privacy)
    DATABASE_ROOT_USER.child(NODE_TEST_ATTENDANCE).child(testName).child(time).child(NODE_TEST_INFO).child("subject").setValue(subject)
    DATABASE_ROOT_USER.child(NODE_TEST_ATTENDANCE).child(testName).child(time).child(NODE_TEST_INFO).child("testCreator").setValue(testCreator)
    DATABASE_ROOT_USER.child(NODE_TEST_ATTENDANCE).child(testName).child(time).child("total").setValue(total)*/
    DATABASE_ROOT_USER.child(NODE_TEST_ATTENDANCE).child(time).child(testName).child("answerInfo").setValue(tableList)
    DATABASE_ROOT_USER.child(NODE_TEST_ATTENDANCE).child(time).child(testName).child(NODE_TEST_INFO).child("privacy").setValue(privacy)
    DATABASE_ROOT_USER.child(NODE_TEST_ATTENDANCE).child(time).child(testName).child(NODE_TEST_INFO).child("subject").setValue(subject)
    DATABASE_ROOT_USER.child(NODE_TEST_ATTENDANCE).child(time).child(testName).child(NODE_TEST_INFO).child("testCreator").setValue(testCreator)
    DATABASE_ROOT_USER.child(NODE_TEST_ATTENDANCE).child(time).child(testName).child(NODE_TEST_INFO).child("testName").setValue(testName)
    DATABASE_ROOT_USER.child(NODE_TEST_ATTENDANCE).child(time).child(testName).child(NODE_TEST_INFO).child("time").setValue(time)
    DATABASE_ROOT_USER.child(NODE_TEST_ATTENDANCE).child(time).child(testName).child("total").setValue(total)
}

fun getPrivateTestAverage(testName: String) {
    val averageListener = object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            var number = 0
            var grade = 0
            var score = 0
            for(user: DataSnapshot in snapshot.children) {
                for(attempt in user.children) {
                    number++
                    var currentGrade = attempt.child("total").child("grade").getValue(String::class.java)
                    var currentScore = attempt.child("total").child("totalScore").getValue(String::class.java)
                    currentScore = currentScore?.substringBefore('%')
                    grade = grade + currentGrade!!.toInt()
                    score = score + currentScore!!.toInt()
                }
            }
            if(number != 0) {
                DATABASE_ROOT_NEW_PRIVATE_TEST.child(testName).child("average").child("averageScore").setValue("${(score/number).toString()}%")
                if((grade.toFloat()/number).toString().length > 4) {
                    DATABASE_ROOT_NEW_PRIVATE_TEST.child(testName).child("average").child("averageGrade").setValue("${((grade.toFloat()/number).toString()).substring(0,4)}")
                } else {
                    DATABASE_ROOT_NEW_PRIVATE_TEST.child(testName).child("average").child("averageGrade").setValue("${((grade.toFloat()/number).toString())}")
                }
                //DATABASE_ROOT_NEW_PRIVATE_TEST.child(testName).child("average").child("averageGrade").setValue("${((grade.toFloat()/number).toString()).substring(0,4)}")
            }
        }

        override fun onCancelled(error: DatabaseError) {

        }
    }
    DATABASE_ROOT_NEW_PRIVATE_TEST.child(testName).child("solutions").addListenerForSingleValueEvent(averageListener)
}

fun setCurrentUser(user: User?) {
    CURRENT_USER = user
}

fun initFirebaseVariant2() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    CURRENT_UID = AUTH.uid.toString()
    DATABASE_ROOT_USER = REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
    DATABASE_ROOT_NEW_PUBLIC_TEST = REF_DATABASE_ROOT.child(NODE_TEST_PUBLIC)
    DATABASE_ROOT_NEW_PRIVATE_TEST = REF_DATABASE_ROOT.child(NODE_TEST_PRIVATE)
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
    REF_DATABASE_ROOT.child(NODE_USERS).child(participantID).child("in groups").child(groupName).setValue(groupName)
    val listener = object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            var numberUsers = snapshot.getValue(Int::class.java)
            numberUsers = numberUsers!! + 1
            DATABASE_ROOT_NEW_GROUP.child(groupName).child("group info").child("numberUsers").setValue(numberUsers)
        }

        override fun onCancelled(error: DatabaseError) {

        }
    }
    DATABASE_ROOT_NEW_GROUP.child(groupName).child("group info").child("numberUsers").addListenerForSingleValueEvent(listener)
}

fun showToast(context: Context?, message: String?) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun addNewGroupToList(newGroup: GroupModel) {
    GROUP_LIST.add(newGroup)
}

fun addNewPublicTestToList(newTest: TestModel) {
    PUBLIC_TESTS_LIST.add(newTest)
}

fun addNewPrivateTestToList(newTest: TestModel) {
    PRIVATE_TEST_LIST.add(newTest)
}

fun initGroupList() {
    val groupListener = object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for(groupSnapshot: DataSnapshot in snapshot.children) {
                var groupInfo: GroupModel? = groupSnapshot.child(NODE_GROUP_INFO).getValue(GroupModel::class.java)
                addNewGroupToList(groupInfo!!)
                Log.d("GROUP", "${GROUP_LIST.size}")
            }
        }

        override fun onCancelled(error: DatabaseError) {

        }
    }
    DATABASE_ROOT_NEW_GROUP.addListenerForSingleValueEvent(groupListener)
}

fun initPublicTestsList() {
    val publicTestListener = object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for(testInfoSnapshot: DataSnapshot in snapshot.children) {
                var testInfo: TestInfoModel? = testInfoSnapshot.child(NODE_TEST_INFO).getValue(TestInfoModel::class.java)
                var testName: String? = testInfoSnapshot.child(NODE_TEST_NAME).getValue(String::class.java)
                var creatorName: String? = testInfo?.creatorName
                var privacy: String? = testInfo?.privacy
                var subject: String? = testInfo?.subject
                var testId: String? = testInfoSnapshot.child(NODE_ID).getValue(String::class.java)
                var testModel: TestModel = TestModel(testName!!, creatorName!!, privacy!!, subject!!, testId!!)
                addNewPublicTestToList(testModel)
                Log.d("TEST", "${PUBLIC_TESTS_LIST.size}")
            }
        }

        override fun onCancelled(error: DatabaseError) {

        }
    }
    DATABASE_ROOT_NEW_PUBLIC_TEST.addListenerForSingleValueEvent(publicTestListener)
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

fun createTestIDWithName(testName: String): String? {
    var id = DATABASE_ROOT_TEST_IDS.push().key
    var idName: IdModel = IdModel(id!!, testName)
    DATABASE_ROOT_TEST_IDS.child(id!!).child(NODE_ID).setValue(idName)
    return id
}

fun createTestWithName(testName: String, subject: String, privacy: String, id: String, time: String) {
    //var id = DATABASE_ROOT_TEST_IDS.push().key
    var testCreator: User? = null
    testCreator = CURRENT_USER
    var creatorName = "${testCreator?.name} ${testCreator?.secName}"
    var testInfo: TestInfoModel = TestInfoModel(subject, privacy, creatorName, time)
    if(privacy == "Публичный") {
        DATABASE_ROOT_NEW_PUBLIC_TEST.child(testName).child(NODE_ID).setValue(id)
        DATABASE_ROOT_NEW_PUBLIC_TEST.child(testName).child(NODE_TEST_NAME).setValue(testName)
        DATABASE_ROOT_NEW_PUBLIC_TEST.child(testName).child(NODE_TEST_INFO).setValue(testInfo)
        DATABASE_ROOT_USER.child("tests").child(NODE_TEST_PUBLIC).child(testName).child(NODE_ID).setValue(id)
        DATABASE_ROOT_USER.child("tests").child(NODE_TEST_PUBLIC).child(testName).child(NODE_TEST_NAME).setValue(testName)
        DATABASE_ROOT_USER.child("tests").child(NODE_TEST_PUBLIC).child(testName).child(NODE_TEST_INFO).setValue(testInfo)
    }
    else {
        DATABASE_ROOT_NEW_PRIVATE_TEST.child(testName).child(NODE_ID).setValue(id)
        DATABASE_ROOT_NEW_PRIVATE_TEST.child(testName).child(NODE_TEST_NAME).setValue(testName)
        DATABASE_ROOT_NEW_PRIVATE_TEST.child(testName).child(NODE_TEST_INFO).setValue(testInfo)
        DATABASE_ROOT_USER.child("tests").child(NODE_TEST_PRIVATE).child(testName).child(NODE_ID).setValue(id)
        DATABASE_ROOT_USER.child("tests").child(NODE_TEST_PRIVATE).child(testName).child(NODE_TEST_NAME).setValue(testName)
        DATABASE_ROOT_USER.child("tests").child(NODE_TEST_PRIVATE).child(testName).child(NODE_TEST_INFO).setValue(testInfo)
    }
}

fun createGroupIDWithName(groupName: String, numberUsers: Int, creatorName: String) {
    var id = DATABASE_ROOT_GROUP_IDS.push().key
    var idName = IdModel(id!!, groupName)
    var newGroup = GroupModel(groupName, numberUsers, creatorName)
    DATABASE_ROOT_GROUP_IDS.child(id!!).child(NODE_ID).setValue(idName)
    DATABASE_ROOT_NEW_GROUP.child(groupName).child(NODE_ID).setValue(idName)
    DATABASE_ROOT_NEW_GROUP.child(groupName).child(NODE_GROUP_INFO).setValue(newGroup)
    DATABASE_ROOT_USER.child(NODE_GROUP).child(groupName).child(NODE_ID).setValue(idName)
    DATABASE_ROOT_USER.child(NODE_GROUP).child(groupName).child(NODE_GROUP_INFO).setValue(newGroup)
    //TODO в GroupsActivity должны браться данные из DATABASE_ROOT_USER, потому что у каждого админа свой список групп
    //TODO кроме того, тесты должны получаться для добавления в группу тоже из DATABASE_ROOT_USER
}

fun addNewTestToGroup(groupName: String, testName: String, subject: String, privacy: String, id: String, time: String) {
    var testCreator: User? = null
    testCreator = CURRENT_USER
    var creatorName = "${testCreator?.name} ${testCreator?.secName}"
    var testInfo: TestInfoModel = TestInfoModel(subject, privacy, creatorName, time)
    DATABASE_ROOT_USER.child(NODE_GROUP).child(groupName).child("tests")/*.child(privacy)*/.child(testName).child(NODE_ID).setValue(id)
    DATABASE_ROOT_USER.child(NODE_GROUP).child(groupName).child("tests")/*.child(privacy)*/.child(testName).child(NODE_TEST_NAME).setValue(testName)
    DATABASE_ROOT_USER.child(NODE_GROUP).child(groupName).child("tests")/*.child(privacy)*/.child(testName).child(NODE_TEST_INFO).setValue(testInfo)
    DATABASE_ROOT_NEW_GROUP.child(groupName).child("tests")/*.child(privacy)*/.child(testName).child(NODE_ID).setValue(id)
    DATABASE_ROOT_NEW_GROUP.child(groupName).child("tests")/*.child(privacy)*/.child(testName).child(NODE_TEST_NAME).setValue(testName)
    DATABASE_ROOT_NEW_GROUP.child(groupName).child("tests")/*.child(privacy)*/.child(testName).child(NODE_TEST_INFO).setValue(testInfo)
}

fun setGroupInfo(groupName: String, numberUsers: Int, creatorName: String) {
    var newGroup = GroupModel(groupName, numberUsers, creatorName)
    DATABASE_ROOT_NEW_GROUP.child(groupName).child(NODE_GROUP_INFO).setValue(newGroup)
}

fun deleteTestWithName(testName: String, privacy: String) {
    if(privacy == "Публичный") {
        DATABASE_ROOT_NEW_PUBLIC_TEST.child(testName).removeValue()
    }
    else {
        DATABASE_ROOT_NEW_PRIVATE_TEST.child(testName).removeValue()
    }
}

fun deleteTestIdWithName(testId: String) {
    DATABASE_ROOT_TEST_IDS.child(testId).removeValue()
}

fun createQuestionInTest(testName : String, questionInfo: QuestionModel, answers: ArrayList<String>, privacy: String) {
    if(privacy == "Публичный") {
        DATABASE_ROOT_NEW_PUBLIC_TEST.child(testName).child(NODE_QUESTIONS).child(questionInfo.name).setValue(questionInfo)
        var questionOrder = 1
        for (answer in answers) {
            DATABASE_ROOT_NEW_PUBLIC_TEST.child(testName).child(NODE_QUESTIONS).child(questionInfo.name).child(NODE_ANSWERS).child("Answer №${questionOrder}").setValue(answer)
            questionOrder++
        }
    }
    else {
        DATABASE_ROOT_NEW_PRIVATE_TEST.child(testName).child(NODE_QUESTIONS).child(questionInfo.name).setValue(questionInfo)
        var questionOrder = 1
        for (answer in answers) {
            DATABASE_ROOT_NEW_PRIVATE_TEST.child(testName).child(NODE_QUESTIONS).child(questionInfo.name).child(NODE_ANSWERS).child("Answer №${questionOrder}").setValue(answer)
            questionOrder++
        }
    }
}

fun createComparisonQuestionInTest(testName: String, questionInfo: QuestionModel, firstPartList: ArrayList<String>, secondPartList: ArrayList<String>, privacy: String) {
    if(privacy == "Публичный") {
        DATABASE_ROOT_NEW_PUBLIC_TEST.child(testName).child(NODE_QUESTIONS).child(questionInfo.name).setValue(questionInfo)
        var questionOrder = 1
        for (answer in firstPartList) {
            DATABASE_ROOT_NEW_PUBLIC_TEST.child(testName).child(NODE_QUESTIONS).child(questionInfo.name).child("firstParts").child("Answer №${questionOrder}").setValue(answer)
            questionOrder++
        }
        for (answer in secondPartList) {
            DATABASE_ROOT_NEW_PUBLIC_TEST.child(testName).child(NODE_QUESTIONS).child(questionInfo.name).child("secondParts").child("Answer №${questionOrder}").setValue(answer)
            questionOrder++
        }
    }
    else {
        DATABASE_ROOT_NEW_PRIVATE_TEST.child(testName).child(NODE_QUESTIONS).child(questionInfo.name).setValue(questionInfo)
        var questionOrder = 1
        for (answer in firstPartList) {
            DATABASE_ROOT_NEW_PRIVATE_TEST.child(testName).child(NODE_QUESTIONS).child(questionInfo.name).child("firstParts").child("Answer №${questionOrder}").setValue(answer)
            questionOrder++
        }
        for (answer in secondPartList) {
            DATABASE_ROOT_NEW_PRIVATE_TEST.child(testName).child(NODE_QUESTIONS).child(questionInfo.name).child("secondParts").child("Answer №${questionOrder}").setValue(answer)
            questionOrder++
        }
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



