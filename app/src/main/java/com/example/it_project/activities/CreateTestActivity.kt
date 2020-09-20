package com.example.it_project.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.*
import com.example.it_project.models.CategoryModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CreateTestActivity : BaseActivity() {

    private lateinit var createNewQuestionButton: FloatingActionButton
    private lateinit var questionsRecyclerView: RecyclerView
    private lateinit var activity: Activity
    private lateinit var context: Context
    private lateinit var categoryList: ArrayList<CategoryModel>
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
        questionsRecyclerView.layoutManager = LinearLayoutManager(this)
        questionsRecyclerView.adapter = adapter
        fragmentManager = this@CreateTestActivity.supportFragmentManager
        createNewQuestionButton.setOnClickListener {
            //val transaction = fragmentManager.beginTransaction()
            //transaction.add(R.id.frameLayout, NewQuestionFragment()).commit()
            val dialogFragment = NewQuestionFragment()
            val manager = supportFragmentManager
            dialogFragment.show(manager, "MyDialog")
            }
        }

    override fun onStart() {
        super.onStart()
        val extras: Bundle? = intent.extras
        if(extras != null) {setToolbarTitle(extras.getString("TestName").toString())}
    }

    private fun init() {
        createNewQuestionButton = findViewById(R.id.create_new_question_button)
        questionsRecyclerView = findViewById(R.id.list_of_questions)
        context = applicationContext
        activity = this@CreateTestActivity
        categoryList = ArrayList()
        adapter = CategoryAdapter(context, activity, categoryList)
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
            startActivity(Intent(this@CreateTestActivity, MainActivity::class.java))
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