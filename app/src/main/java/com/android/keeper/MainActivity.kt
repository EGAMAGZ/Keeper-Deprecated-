package com.android.keeper

import android.content.Intent
import android.os.Bundle
import android.support.design.internal.NavigationMenuItemView
import android.support.design.widget.NavigationView
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.android.keeper.MainActivity
import com.android.keeper.fragments.NotesFragment
import com.android.keeper.fragments.RemindersFragment
import com.android.keeper.fragments.ScheduleFragment
import com.android.keeper.fragments.TasksFragment
import com.android.keeper.localdb.SQLiteConnection
import com.android.keeper.util.PreferenceUtil
import org.junit.runner.RunWith

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var tasksFragment: TasksFragment? = null
    private var remindersFragment: RemindersFragment? = null
    private var drawer: DrawerLayout? = null
    private var toolbar: Toolbar? = null
    private var backToast: Toast? = null
    private var navigationView: NavigationView? = null
    private var backPressedTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val conn = SQLiteConnection(this, "keeper_db", null, 1) //Creation of Database(SQLite)
        navigationView = findViewById<NavigationView?>(R.id.nav_view) //Gets Navigation View (Lateral Menu)
        navigationView.setNavigationItemSelectedListener(this) //Sets Item Listener to Navigation View
        tasksFragment = TasksFragment()
        remindersFragment = RemindersFragment()
        toolbar = findViewById<Toolbar?>(R.id.toolbar) //Gets Toolbar
        setSupportActionBar(toolbar)
        drawer = findViewById<DrawerLayout?>(R.id.drawer_layout) //Gets Drawer Layout (Root Container)
        val toogle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) // Menu Button to slide NavigationView
        drawer.addDrawerListener(toogle)
        toogle.syncState()

        /* Replace default fragment content*/
        val intent = intent
        val fragmentValue = intent.getStringExtra("fragment")
        if (fragmentValue != null) {
            when (fragmentValue) {
                "tasks" -> {
                    PreferenceUtil.Companion.getInstance(applicationContext).setLastFragment("tasks")
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, tasksFragment, "tasks_fragment").commit()
                    navigationView.setCheckedItem(R.id.nav_tasks)
                }
                else -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, NotesFragment(), "notes_fragment").commit()
                    navigationView.setCheckedItem(R.id.nav_notes)
                }
            }
        } else {
            if (PreferenceUtil.Companion.getInstance(applicationContext).getChangeLastFragment()) changeLastFragment() else {
                PreferenceUtil.Companion.getInstance(applicationContext).setLastFragment("notes")
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, NotesFragment(), "notes_fragment").commit()
                navigationView.setCheckedItem(R.id.nav_notes)
            }
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.thirdColor)
        setKeepScreenOn()
    }

    override fun onRestart() {
        super.onRestart()
        setKeepScreenOn()
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START) //Closes the drawer on back pressed
        } else {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                backToast.cancel()
                super.onBackPressed()
                return
            } else {
                backToast = Toast.makeText(applicationContext, "Press again to exit", Toast.LENGTH_SHORT)
                backToast.show()
            }
            backPressedTime = System.currentTimeMillis()
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        /* Navigation View Item Listener */
        var navigationMenuItemView: NavigationMenuItemView
        when (menuItem.itemId) {
            R.id.nav_notes -> {
                PreferenceUtil.Companion.getInstance(applicationContext).setLastFragment("notes")
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, NotesFragment(), "notes_fragment").commit()
            }
            R.id.nav_reminders -> {
                PreferenceUtil.Companion.getInstance(applicationContext).setLastFragment("reminders")
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, remindersFragment, "reminders_fragment").commit()
            }
            R.id.nav_tasks -> {
                PreferenceUtil.Companion.getInstance(applicationContext).setLastFragment("tasks")
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, tasksFragment, "tasks_fragment").commit()
            }
            R.id.nav_schedule -> {
                PreferenceUtil.Companion.getInstance(applicationContext).setLastFragment("schedules")
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ScheduleFragment(), "schedules_fragment").commit()
            }
            R.id.nav_settings -> {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        drawer.closeDrawer(GravityCompat.START) //After select item, automatically it closes the drawer
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        /* Creates items/menus on ToolBar*/
        menuInflater.inflate(R.menu.main_toolbar_menu, menu)
        val searchItem = menu.findItem(R.id.toolbar_search)
        val searchView = searchItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val fragmentManager = supportFragmentManager
                val currentFragment = fragmentManager.findFragmentById(R.id.fragment_container)
                when (currentFragment.getTag()) {
                    "reminders_fragment" -> remindersFragment.FilterReminder(newText)
                    "tasks_fragment" -> tasksFragment.FilterTask(newText)
                }
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        /* Listener for items in ToolBar  */
        when (item.getItemId()) {
            R.id.toolbar_search -> {
            }
        }
        return true
    }

    private fun changeLastFragment() {
        when (PreferenceUtil.Companion.getInstance(applicationContext).getLastFragment()) {
            "notes" -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, NotesFragment(), "notes_fragment").commit()
                navigationView.setCheckedItem(R.id.nav_notes)
            }
            "reminders" -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, remindersFragment, "reminders_fragment").commit()
                navigationView.setCheckedItem(R.id.nav_reminders)
            }
            "tasks" -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, tasksFragment, "tasks_fragment").commit()
                navigationView.setCheckedItem(R.id.nav_tasks)
            }
            "schedules" -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ScheduleFragment(), "schedules_fragment").commit()
                navigationView.setCheckedItem(R.id.nav_schedule)
            }
        }
    }

    private fun setKeepScreenOn() {
        if (PreferenceUtil.Companion.getInstance(applicationContext).getKeepScreenOn()) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}