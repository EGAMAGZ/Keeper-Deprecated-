package com.android.keeper;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.keeper.adapters.TasksAdapter;
import com.android.keeper.dialog.AddNewTaskBottomSheet;
import com.android.keeper.dialog.DatePickerDialogFragment;
import com.android.keeper.dialog.EditTaskBottomSheet;
import com.android.keeper.dialog.TimePickerDialogFragment;
import com.android.keeper.fragments.NotesFragment;
import com.android.keeper.fragments.RemindersFragment;
import com.android.keeper.fragments.TasksFragment;
import com.android.keeper.localdb.SQLiteConnection;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TasksFragment tasksFragment;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private Toast backToast;

    private long backPressedTime;
    private int selected_year,selected_month,selected_dayOfMonth,selected_hourOfDay,selected_minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteConnection conn=new SQLiteConnection(this,"keeper_db",null,1);//Creation of Database(SQLite)

        NavigationView navigationView=findViewById(R.id.nav_view); //Gets Navigation View (Lateral Menu)
        navigationView.setNavigationItemSelectedListener(this); //Sets Item Listener to Navigation View

        toolbar=findViewById(R.id.toolbar); //Gets Toolbar
        setSupportActionBar(toolbar);

        drawer=findViewById(R.id.drawer_layout); //Gets Drawer Layout (Root Container)

        ActionBarDrawerToggle toogle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);// Menu Button to slide NavigationView

        drawer.addDrawerListener(toogle);
        toogle.syncState();

        /* Replace default fragment content*/
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new NotesFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_notes);

        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.thirdColor));
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START); //Closes the drawer on back pressed
        }else{
            if(backPressedTime + 2000 > System.currentTimeMillis()){
                backToast.cancel();
                super.onBackPressed();
                return ;
            } else{
                backToast=Toast.makeText(getApplicationContext(),"Press again to exit",Toast.LENGTH_SHORT);
                backToast.show();
            }
            backPressedTime=System.currentTimeMillis();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        /* Navigation View Item Listener */
        NavigationMenuItemView navigationMenuItemView;
        switch(menuItem.getItemId()){
            case R.id.nav_notes:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new NotesFragment(),"notes_fragment").commit();
                //toolbar.setTitle("Keeper: Notes");
                break;
            case R.id.nav_reminders:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RemindersFragment(),"reminders_fragment").commit();
                //toolbar.setTitle("Keeper: Reminders");
                break;
            case R.id.nav_tasks:
                tasksFragment=new TasksFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,tasksFragment,"tasks_fragment").commit();
                //toolbar.setTitle("Keeper: Tasks");
                break;
            case R.id.nav_settings:
                Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START); //After select item, automatically it closes the drawer
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Creates items/menus on ToolBar*/
        getMenuInflater().inflate(R.menu.main_toolbar_menu,menu);

        MenuItem searchItem=menu.findItem(R.id.toolbar_search);
        SearchView searchView=(SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                FragmentManager fragmentManager=getSupportFragmentManager();
                Fragment currentFragment=fragmentManager.findFragmentById(R.id.fragment_container);
                if(currentFragment.getTag().equals("tasks_fragment")){
                    tasksFragment.FilterTask(newText);
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Listener for items in ToolBar  */
        switch(item.getItemId()){
            case R.id.toolbar_search:
                break;
        }
        return true;
    }

    /*
     * Fragment Listeners
     * */

    //DatePicker
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        selected_year=year;
        selected_month=month;
        selected_dayOfMonth=dayOfMonth;
        DialogFragment timePicker=new TimePickerDialogFragment();
        timePicker.show(getSupportFragmentManager(),"time picker");
    }

    //TimePicker
    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        selected_hourOfDay=hourOfDay;
        selected_minute=minute;

        tasksFragment.setTaskDate(selected_year,selected_month,selected_dayOfMonth,selected_hourOfDay,selected_minute);
    }
}
