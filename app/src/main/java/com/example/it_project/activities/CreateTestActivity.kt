package com.example.it_project.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.*
import com.example.it_project.adapters.CategoryAdapter
import com.example.it_project.fragments.NewQuestionFragment
import com.example.it_project.models.CategoryModel
import com.example.it_project.models.GroupModel
import com.example.it_project.models.QuestionModel
import com.example.it_project.utilities.deleteTestIdWithName
import com.example.it_project.utilities.deleteTestWithName
import com.example.it_project.utilities.initFirebase
import com.example.it_project.values.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class CreateTestActivity : BaseActivity() {

    private lateinit var createNewQuestionButton: FloatingActionButton
    private lateinit var questionsRecyclerView: RecyclerView
    private lateinit var activity: Activity
    private lateinit var context: Context
    private lateinit var fragmentManager : FragmentManager
    private lateinit var adapter: CategoryAdapter
    private lateinit var commit: Button
    private lateinit var listData: ArrayList<CategoryModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_test)
        initToolbar(true)
        setToolbarTitle("Создание теста")
        initFirebase()
        init()
        enableUpButton()
        fragmentManager = this@CreateTestActivity.supportFragmentManager
        createNewQuestionButton.setOnClickListener {
            val dialogFragment = NewQuestionFragment()
            val manager = supportFragmentManager
            dialogFragment.show(manager, "MyDialog")
        }

        commit.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        }

    override fun onStart() {
        super.onStart()
        val extras: Bundle? = intent.extras
        if(extras != null) {setToolbarTitle(extras.getString("TestName").toString())}
        if(extras != null) {
            TEST_NAME = extras.getString("TestName").toString()}
        getDataFromDb()
    }

    override fun onResume() {
        super.onResume()
        if(CURRENT_TEST_NAME != null) {setToolbarTitle(CURRENT_TEST_NAME!!)}
        //val extras: Bundle? = intent.extras
        //if(NEW_QUESTION != null) {
            //var newCategory: String? = NEW_QUESTION
            //categoryList.add(CategoryModel(newCategory!!))
            //CATEGORY_LIST.add(CategoryModel(newCategory!!))
            //Toast.makeText(this@CreateTestActivity, "${CATEGORY_LIST.size}", Toast.LENGTH_SHORT).show()
            //Toast.makeText(this@CreateTestActivity, "${CATEGORY_LIST[0]}", Toast.LENGTH_SHORT).show()
        //}

        //questionsRecyclerView.layoutManager = LinearLayoutManager(this)
        //questionsRecyclerView.adapter = adapter
        //adapter.notifyItemInserted(categoryList.size - 1)
        //adapter.notifyItemInserted(CATEGORY_LIST.size - 1)
        //adapter.notifyDataSetChanged()
    }

    private fun init() {
        createNewQuestionButton = findViewById(R.id.create_new_question_button)
        questionsRecyclerView = findViewById(R.id.list_of_questions)
        context = applicationContext
        activity = this@CreateTestActivity
        listData = ArrayList()
        //adapter = CategoryAdapter(context, activity, CATEGORY_LIST)
        adapter = CategoryAdapter(context, activity, listData)
        questionsRecyclerView.layoutManager = LinearLayoutManager(this)
        questionsRecyclerView.adapter = adapter
        commit = findViewById(R.id.button_commit_create_test)
    }

    private fun getDataFromDb() {
        val categoryListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(listData.size > 0) {listData.clear()}
                for(questionsSnapshot: DataSnapshot in snapshot.children) {
                    var questionName: String? = questionsSnapshot.child(NODE_QUESTION_NAME).getValue(String::class.java)
                    Log.d("QUESTION_NAME", questionName!!)
                    //addNewGroupToList(groupInfo!!)
                    //Log.d("GROUP", "${GROUP_LIST.size}")
                    var newCategory: CategoryModel = CategoryModel(questionName!!)
                    if(questionName != null) {
                        listData.add(newCategory)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        if(CURRENT_TEST_PRIVACY == "Публичный") {
            DATABASE_ROOT_NEW_PUBLIC_TEST.child(CURRENT_TEST_NAME!!).child(NODE_QUESTIONS).addListenerForSingleValueEvent(categoryListener)
        }
        else {
            DATABASE_ROOT_NEW_PRIVATE_TEST.child(CURRENT_TEST_NAME!!).child(NODE_QUESTIONS).addListenerForSingleValueEvent(categoryListener)
        }
        //DATABASE_ROOT_NEW_PUBLIC_TEST.addListenerForSingleValueEvent(categoryListener)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //ActivityUtilities.getInstance()
                    //.invokeNewActivity(this@CreateTestActivity, MainActivity::class.java, true)
                openQuitDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openQuitDialog() {
        var quitDialog = AlertDialog.Builder(
            this@CreateTestActivity
        )
        quitDialog.setTitle("Вы уверены, что хотите отменить создание теста?")

        quitDialog.setPositiveButton("Да!"
        ) { dialog, which ->
            CATEGORY_LIST = ArrayList()
            startActivity(Intent(this@CreateTestActivity, MainActivity::class.java))
            if(CURRENT_TEST_NAME != null) {deleteTestWithName(CURRENT_TEST_NAME!!, CURRENT_TEST_PRIVACY!!)}
            if(CURRENT_TEST_ID != null) {deleteTestIdWithName(CURRENT_TEST_ID!!)}
            CURRENT_TEST_PRIVACY = null
            finish()
        }

        quitDialog.setNegativeButton("Нет"
        ) { dialog, which ->
            // TODO Auto-generated method stub
        }
        quitDialog.show()
    }

    override fun onBackPressed() {
        openQuitDialog()
    }
}