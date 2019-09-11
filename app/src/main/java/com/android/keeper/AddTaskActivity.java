package com.android.keeper;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.keeper.localdb.SQLiteConnection;
import com.android.keeper.localdb.utilities.TasksUtilities;

public class AddTaskActivity extends AppCompatActivity {

    private EditText TaskTitle,TaskDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        TaskTitle=(EditText) findViewById(R.id.task_title);
        TaskDescription=(EditText) findViewById(R.id.task_description);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);//Unables toolbar's title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Add return button

        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.thirdColor));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Creates items/menus on ToolBar*/
        getMenuInflater().inflate(R.menu.add_task_toolbar_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.toolbar_save:
                StoreNewTask();
                Toast.makeText(getApplicationContext(),"Saved2",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }

    private void StoreNewTask(){
        SQLiteConnection conn=new SQLiteConnection(this,"keeper_db",null,1);
        SQLiteDatabase db=conn.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(TasksUtilities.COLUMN_TASK_TITLE,TaskTitle.getText().toString());
        values.put(TasksUtilities.COLUMN_TASK_DESCRIPTION,TaskDescription.getText().toString());

        Long resultId=db.insert(TasksUtilities.TABLE_NAME,TasksUtilities.COLUMN_TASK_ID,values);
        //Toast.makeText(getApplicationContext(),"ID:"+resultId,Toast.LENGTH_SHORT);
    }
}
