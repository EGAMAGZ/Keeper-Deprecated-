package com.android.keeper.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.keeper.R;
import com.android.keeper.adapters.RemindersAdapter;
import com.android.keeper.localdb.SQLiteConnection;
import com.android.keeper.localdb.utilities.RemindersUtilities;
import com.android.keeper.recycle_items.ReminderItem;

import java.util.ArrayList;

public class RemindersFragment extends Fragment {

    private View fragmentView;
    private SQLiteConnection sqLiteConnection;
    private RecyclerView remindersRecyclerView;
    private RemindersAdapter remindersRecAdapter;
    private RecyclerView.LayoutManager remindersLayoutManager;
    private FloatingActionButton addReminderBtn;

    private ArrayList<ReminderItem> remindersList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView=inflater.inflate(R.layout.fragment_reminders,container,false);
        sqLiteConnection=new SQLiteConnection(getContext(),"keeper_db",null,1);
        remindersList=new ArrayList<ReminderItem>();

        addReminderBtn=fragmentView.findViewById(R.id.addreminder_flt_btn);

        remindersRecyclerView=fragmentView.findViewById(R.id.reminders_recyclerview);
        remindersLayoutManager=new LinearLayoutManager(getContext());

        addReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Crear",Toast.LENGTH_SHORT).show();
            }
        });

        loadReminders();

        return fragmentView;
    }

    private void loadReminders(){
        String[] columns={RemindersUtilities.COLUMN_REMINDER_ID,RemindersUtilities.COLUMN_REMINDER_TITLE};

        SQLiteDatabase database=sqLiteConnection.getReadableDatabase();
        try{
            Cursor cursor=database.query(RemindersUtilities.TABLE_NAME,columns,null,null,null,null,
                    RemindersUtilities.COLUMN_REMINDER_ID+" DESC",null);
            while(cursor.moveToNext()){
                int id=cursor.getInt(0);
                String title=cursor.getString(1);
                remindersList.add(new ReminderItem(id,title,false));
            }
        }catch(Exception e){}
        finally {
            remindersRecAdapter=new RemindersAdapter(remindersList);
            remindersRecyclerView.setLayoutManager(remindersLayoutManager);
            remindersRecyclerView.setAdapter(remindersRecAdapter);
            remindersRecAdapter.setOnItemClickListener(new RemindersAdapter.OnItemClickListener() {
                @Override
                public void onEditReminder(int position) {

                }
            });
        }
        database.close();
    }
}
