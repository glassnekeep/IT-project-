package com.example.it_project.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.*
import com.example.it_project.adapters.CategoryAdapter
import com.example.it_project.fragments.NewQuestionFragment
import com.example.it_project.models.CategoryModel
import com.example.it_project.utilities.deleteTestWithName
import com.example.it_project.utilities.initFirebase
import com.example.it_project.values.CATEGORY_LIST
import com.example.it_project.values.CURRENT_TEST_NAME
import com.example.it_project.values.NEW_QUESTION
import com.example.it_project.values.TEST_NAME
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CreateTestActivity : BaseActivity() {

    private lateinit var createNewQuestionButton: FloatingActionButton
    private lateinit var questionsRecyclerView: RecyclerView
    private lateinit var activity: Activity
    private lateinit var context: Context
    private lateinit var fragmentManager : FragmentManager
    private lateinit var adapter: CategoryAdapter

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
        }

    override fun onStart() {
        super.onStart()
        val extras: Bundle? = intent.extras
        if(extras != null) {setToolbarTitle(extras.getString("TestName").toString())}
        if(extras != null) {
            TEST_NAME = extras.getString("TestName").toString()}
    }

    override fun onResume() {
        super.onResume()
        if(CURRENT_TEST_NAME != null) {setToolbarTitle(CURRENT_TEST_NAME!!)}
        val extras: Bundle? = intent.extras
        if(NEW_QUESTION != null) {
            var newCategory: String? = NEW_QUESTION
            //categoryList.add(CategoryModel(newCategory!!))
            CATEGORY_LIST.add(CategoryModel(newCategory!!))
            Toast.makeText(this@CreateTestActivity, "${CATEGORY_LIST.size}", Toast.LENGTH_SHORT).show()
            Toast.makeText(this@CreateTestActivity, "${CATEGORY_LIST[0]}", Toast.LENGTH_SHORT).show()
        }

        questionsRecyclerView.layoutManager = LinearLayoutManager(this)
        questionsRecyclerView.adapter = adapter
        //adapter.notifyItemInserted(categoryList.size - 1)
        adapter.notifyItemInserted(CATEGORY_LIST.size - 1)
        adapter.notifyDataSetChanged()
    }

    private fun init() {
        createNewQuestionButton = findViewById(R.id.create_new_question_button)
        questionsRecyclerView = findViewById(R.id.list_of_questions)
        context = applicationContext
        activity = this@CreateTestActivity
        adapter = CategoryAdapter(context, activity, CATEGORY_LIST)
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
            if(CURRENT_TEST_NAME != null) {deleteTestWithName(CURRENT_TEST_NAME!!)}
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