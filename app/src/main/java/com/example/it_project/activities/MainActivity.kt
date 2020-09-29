package com.example.it_project.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.example.it_project.R
import com.example.it_project.fragments.CreateTestNameFragment
import com.example.it_project.models.User
import com.example.it_project.utilities.ActivityUtilities
import com.example.it_project.utilities.initFirebase
import com.example.it_project.utilities.invokeNewActivity
import com.example.it_project.utilities.showToast
import com.example.it_project.values.ADMIN_STATUS
import com.example.it_project.values.DATABASE_ROOT_USER
import com.example.it_project.values.USER
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
    private lateinit var activity: Activity
    private var context: Context? = null
    private var toolbar: Toolbar? = null
    private var header: AccountHeader? = null
    private var drawer: Drawer? = null
    private lateinit var mCurrentProfile: ProfileDrawerItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFirebase()
        createHeader()
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
                        /*1 -> ActivityUtilities.getInstance().invokeNewActivity(
                            this@MainActivity,
                            SplashActivity::class.java,
                            true
                        )*/
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
                        //ActivityUtilities.getInstance().invokeNewActivity(
                            //this@MainActivity,
                            //CreateTestActivity::class.java,
                            //true
                        }
                        //)
                        3 -> invokeNewActivity(
                            this@MainActivity,
                            RegisterActivity::class.java,
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