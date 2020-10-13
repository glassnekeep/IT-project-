package com.example.it_project.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.R
import com.example.it_project.adapters.CategoryAdapter
import com.example.it_project.adapters.TestAdapter
import com.example.it_project.fragments.CreateGroupFragment
import com.example.it_project.fragments.CreateTestNameFragment
import com.example.it_project.models.TestInfoModel
import com.example.it_project.models.TestModel
import com.example.it_project.models.User
import com.example.it_project.utilities.*
import com.example.it_project.values.*
import com.google.firebase.database.*
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

class MainActivity : BaseActivity() {

    private lateinit var user: User
    private lateinit var adapter: TestAdapter
    private lateinit var activity: Activity
    private lateinit var context: Context
    private var toolbar: Toolbar? = null
    private var header: AccountHeader? = null
    private var drawer: Drawer? = null
    private lateinit var testsRecyclerView: RecyclerView
    private lateinit var search: SearchView
    private lateinit var mCurrentProfile: ProfileDrawerItem
    private lateinit var listData: ArrayList<TestModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        initFirebase()
        createHeader()
        //initFirebaseVariant2()
        getCurrentUserName()
        getCurrentUserSecName()
        val postListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                USER = snapshot.getValue(User::class.java)
                mCurrentProfile
                    .withName("${USER?.name} ${USER?.secName}")
                    .withEmail(USER?.email)
                header?.updateProfile(mCurrentProfile)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        DATABASE_ROOT_USER.addValueEventListener(postListener)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val adminListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ADMIN_STATUS = snapshot.getValue(String::class.java)
                if(ADMIN_STATUS == "admin") {
                    createAdministrationDrawer()
                } else {
                    createUserDrawer()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        DATABASE_ROOT_USER.child("admin").addValueEventListener(adminListener)
        /*if(ADMIN_STATUS == "admin") {
            createAdministrationDrawer()
        } else {
            createUserDrawer()
        }*/

        ///////////////////testsRecyclerView.layoutManager = LinearLayoutManager(this)
        //testsRecyclerView.adapter = adapter

        //adapter.notifyDataSetChanged()
        ///////////////////testsRecyclerView.adapter = adapter
        ///////////////////adapter.notifyDataSetChanged()
        //adapter.notifyItemInserted(PUBLIC_TESTS_LIST.size - 1)
        ///////////////////PUBLIC_TESTS_LIST = ArrayList()
        getDataFromDb()
    }

    private fun createHeader() {
        mCurrentProfile = ProfileDrawerItem()
            .withName(USER?.name)
            .withEmail(USER?.email)
            .withIdentifier(200)
        header = AccountHeaderBuilder()
            .withActivity(this)
            .withHeaderBackground(R.drawable.header)
            //.withTranslucentStatusBar(true)
            .addProfiles(
                mCurrentProfile
                //ProfileDrawerItem().withName("${name} ${secName}")
                    //.withEmail(email)
            ).build()
    }

    private fun createAdministrationDrawer() {
        drawer = DrawerBuilder()
            .withActivity(this)
            .withToolbar(toolbar!!)
            .withActionBarDrawerToggle(true)
            .withSelectedItem(-1)
            .withAccountHeader(header!!)
            .addDrawerItems(
                PrimaryDrawerItem().withIdentifier(100)
                    .withIconTintingEnabled(true)
                    .withName("О приложении")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_info_24),
                PrimaryDrawerItem().withIdentifier(101)
                    .withIconTintingEnabled(true)
                    .withName("Создать тест")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_add_24),
                PrimaryDrawerItem().withIdentifier(102)
                    .withIconTintingEnabled(true)
                    .withName("Пройти тест")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_check_24),
                PrimaryDrawerItem().withIdentifier(103)
                    .withIconTintingEnabled(true)
                    .withName("История")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_history_24),
                PrimaryDrawerItem().withIdentifier(104)
                    .withIconTintingEnabled(true)
                    .withName("Мои комнаты")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_group_24),
                PrimaryDrawerItem().withIdentifier(105)
                    .withIconTintingEnabled(true)
                    .withName("Создать комнату")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_group_add_24),
                PrimaryDrawerItem().withIdentifier(106)
                    .withIconTintingEnabled(true)
                    .withName("Статистика")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_bar_chart_24),
                PrimaryDrawerItem().withIdentifier(107)
                    .withIconTintingEnabled(true)
                    .withName("Настройки")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_settings_24)
            ).withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    when (position) {
                        1 -> ActivityUtilities.getInstance().invokeNewActivity(
                            this@MainActivity,
                            AboutDevActivity::class.java,
                            true
                        )
                        2 -> /*ActivityUtilities.getInstance().invokeNewActivity(
                            this@MainActivity,
                            CreateTestActivity::class.java,
                            true
                        )*/
                        {var fragmentManager = this@MainActivity.supportFragmentManager
                            //val transaction = fragmentManager.beginTransaction()
                            //transaction.add(R.id.frameLayout, NewQuestionFragment()).commit()
                            val dialogFragment = CreateTestNameFragment()
                            val manager = supportFragmentManager
                            dialogFragment.show(fragmentManager, "MyFirstDialog")
                            //ActivityUtilities.getInstance().invokeNewActivity(
                            //this@MainActivity,
                            //CreateTestActivity::class.java,
                            //true
                        }
                        3 -> ActivityUtilities.getInstance().invokeNewActivity(
                            this@MainActivity,
                            RegisterActivity::class.java,
                            true
                        )
                        5 -> invokeNewActivity(this@MainActivity, GroupsActivity::class.java, true)
                        6 -> {var fragmentManager = this@MainActivity.supportFragmentManager
                        //val transaction = fragmentManager.beginTransaction()
                        //transaction.add(R.id.frameLayout, NewQuestionFragment()).commit()
                            val dialogFragment = CreateGroupFragment()
                            val manager = supportFragmentManager
                            dialogFragment.show(fragmentManager, "MyFirstDialog")}
                        8 -> ActivityUtilities.getInstance().invokeNewActivity(
                            this@MainActivity,
                            SettingsActivity::class.java,
                            true
                        )
                    }
                    return false
                }
            })
            /*.withSavedInstance(savedInstanceState)
            *.withShowDrawerOnFirstLaunch(true)
            *.withShowDrawerUntilDraggedOpened(true)
            */.build()
    }

    private fun createUserDrawer() {
        drawer = DrawerBuilder()
            .withActivity(this)
            .withToolbar(toolbar!!)
            .withActionBarDrawerToggle(true)
            .withSelectedItem(-1)
            .withAccountHeader(header!!)
            .addDrawerItems(
                PrimaryDrawerItem().withIdentifier(100)
                    .withIconTintingEnabled(true)
                    .withName("О приложении")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_info_24),
                PrimaryDrawerItem().withIdentifier(101)
                    .withIconTintingEnabled(true)
                    .withName("Пройти тест")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_check_24),
                PrimaryDrawerItem().withIdentifier(102)
                    .withIconTintingEnabled(true)
                    .withName("История")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_history_24),
                PrimaryDrawerItem().withIdentifier(103)
                    .withIconTintingEnabled(true)
                    .withName("Мои комнаты")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_group_24),
                PrimaryDrawerItem().withIdentifier(104)
                    .withIconTintingEnabled(true)
                    .withName("Статистика")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_bar_chart_24),
                PrimaryDrawerItem().withIdentifier(105)
                    .withIconTintingEnabled(true)
                    .withName("Настройки")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_settings_24)
            ).withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    when (position) {
                        1 -> invokeNewActivity(
                            this@MainActivity,
                            AboutDevActivity::class.java,
                            true
                        )
                        2 -> {var fragmentManager = this@MainActivity.supportFragmentManager
                            //val transaction = fragmentManager.beginTransaction()
                            //transaction.add(R.id.frameLayout, NewQuestionFragment()).commit()
                            val dialogFragment = CreateTestNameFragment()
                            val manager = supportFragmentManager
                            dialogFragment.show(fragmentManager, "MyFirstDialog")
                        }
                        3 -> invokeNewActivity(
                            this@MainActivity,
                            RegisterActivity::class.java,
                            true
                        )
                        6 -> ActivityUtilities.getInstance().invokeNewActivity(
                            this@MainActivity,
                            SettingsActivity::class.java,
                            true
                        )
                    }
                    return false
                }
            })
            .build()
    }

    private fun init() {
        testsRecyclerView = findViewById(R.id.list_tests)
        search = findViewById(R.id.searchView)
        activity = this
        context = applicationContext
        //adapter = TestAdapter(context, activity, PUBLIC_TESTS_LIST)
        listData = ArrayList()
        adapter = TestAdapter(context, activity, listData)
        testsRecyclerView.layoutManager = LinearLayoutManager(this)
        testsRecyclerView.adapter = adapter
    }

    private fun getDataFromDb() {
            val publicTestListener = object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if(listData.size > 0) {listData.clear()}
                    for(testInfoSnapshot: DataSnapshot in snapshot.children) {
                        var testInfo: TestInfoModel? = testInfoSnapshot.child(NODE_TEST_INFO).getValue(
                            TestInfoModel::class.java)
                        var testName: String? = testInfoSnapshot.child(NODE_TEST_NAME).getValue(String::class.java)
                        var creatorName: String? = testInfo?.creatorName
                        var privacy: String? = testInfo?.privacy
                        var subject: String? = testInfo?.subject
                        var testId: String? = testInfoSnapshot.child(NODE_ID).getValue(String::class.java)
                        var testModel: TestModel = TestModel(testName!!, creatorName!!, privacy!!, subject!!, testId!!)
                        //addNewPublicTestToList(testModel)
                        listData.add(testModel)
                        //Log.d("TEST", "${PUBLIC_TESTS_LIST.size}")
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }
            DATABASE_ROOT_NEW_PUBLIC_TEST.addListenerForSingleValueEvent(publicTestListener)
    }

    private var backPressed: Long = 0

    override fun onBackPressed() {
        if (drawer != null && drawer!!.isDrawerOpen) {
            drawer!!.closeDrawer()
        } else {
            //tapPromtToExit(this)
            if (backPressed + 2500 > System.currentTimeMillis()) {
                //activity.finish()
                super.onBackPressed()
            } else {
                showToast(
                    activity.applicationContext,
                    activity.resources.getString(R.string.tap_again)
                )
            }
            backPressed = System.currentTimeMillis()
        }
    }
}