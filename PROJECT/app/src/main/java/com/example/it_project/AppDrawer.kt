package com.example.it_project

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

class AppDrawer {
    /*fun create() {
        createHeader()
        createDrawer()
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
                    clickToItem(position)
                    return false
                    }
            })
            /*.withSavedInstance(savedInstanceState)
            *.withShowDrawerOnFirstLaunch(true)
            *.withShowDrawerUntilDraggedOpened(true)
            */.build()
    }

    private fun clickToItem(position: Int) {
        when(position) {
            7 -> startActivity()
        }
    }*/
}