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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.keeper.R;
import com.android.keeper.adapters.RemindersAdapter;
import com.android.keeper.dialog.AddNewReminderBottomSheet;
import com.android.keeper.localdb.SQLiteConnection;
import com.android.keeper.localdb.utilities.RemindersUtilities;
import com.android.keeper.recycle_items.ReminderItem;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RemindersFragment extends Fragment {

    private View fragmentView;
    private SQLiteConnection sqLiteConnection;
    private RecyclerView remindersRecyclerView;
    private RemindersAdapter remindersRecAdapter;
    private RecyclerView.LayoutManager remindersLayoutManager;
    private FloatingActionButton addReminderBtn;
    private ScrollView scrollView;
    private AddNewReminderBottomSheet addNewReminderBottomSheet;

    private ArrayList<ReminderItem> remindersList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO: REDUCE THE AMOUNT OF CODE
        fragmentView=inflater.inflate(R.layout.fragment_reminders,container,false);
        sqLiteConnection=new SQLiteConnection(getContext(),"keeper_db",null,1);
        remindersList=new ArrayList<ReminderItem>();

        addReminderBtn=fragmentView.findViewById(R.id.addreminder_flt_btn);

        remindersRecyclerView=fragmentView.findViewById(R.id.reminders_recyclerview);
        remindersLayoutManager=new LinearLayoutManager(getContext());

        scrollView=fragmentView.findViewById(R.id.reminder_scrollview);

        addReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewReminderBottomSheet=new AddNewReminderBottomSheet();
                addNewReminderBottomSheet.setBottomSheetListener(new AddNewReminderBottomSheet.AddNewReminderBottomSheetListener(){
                    @Override
                    public void onAddReminder(int reminder_id, String reminder_title,int year,int month,int day,int hour,int minute) {
                        AddReminder(reminder_id,reminder_title,year,month,day,hour,minute);
                    }

                    @Override
                    public void onEmptyReminderTitle() {
                        CustomToast("Reminder Title Empty",R.drawable.ic_close_white_24dp);
                    }

                    @Override
                    public void onReminderDateSelected() {

                    }
                });
                addNewReminderBottomSheet.show(getFragmentManager(),"addReminderBottomSheet");
            }
        });

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY<=oldScrollY){
                    //scrollView up
                    addReminderBtn.show();
                }else{
                    //ScrollView down
                    addReminderBtn.hide();
                }
            }
        });

        loadReminders();

        return fragmentView;
    }

    private void loadReminders(){
        String[] columns={RemindersUtilities.COLUMN_REMINDER_ID,RemindersUtilities.COLUMN_REMINDER_TITLE,
        RemindersUtilities.COLUMN_REMINDER_YEAR,RemindersUtilities.COLUMN_REMINDER_MONTH,
        RemindersUtilities.COLUMN_REMINDER_DAY,RemindersUtilities.COLUMN_REMINDER_HOUR,
        RemindersUtilities.COLUMN_REMINDER_MINUTE};

        SQLiteDatabase database=sqLiteConnection.getReadableDatabase();
        try{
            Cursor cursor=database.query(RemindersUtilities.TABLE_NAME,columns,null,null,null,null,
                    RemindersUtilities.COLUMN_REMINDER_ID+" DESC",null);
            while(cursor.moveToNext()){
                String date,time;
                int id=cursor.getInt(0);

                Calendar calendar=Calendar.getInstance();
                calendar.set(cursor.getInt(2),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5),cursor.getInt(6));

                String title=cursor.getString(1);
                if(cursor.getInt(2)==0 || cursor.getInt(3)==0 || cursor.getInt(4)==0){
                    date="";
                    time="";
                }else{
                    date= DateFormat.getDateInstance(DateFormat.LONG).format(calendar.getTime());
                    //Will change automatically between 12 and 24 format
                    time=DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
                }
                remindersList.add(new ReminderItem(id,title,date,time,false));
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

    private void AddReminder(int reminder_id, String reminder_title,int year,int month,int day,int hour,int minute){
        //The SQL PART IS AUTO EXECUTED ON AddNewReminderBottomSheet
        String date,time;
        Calendar calendar=Calendar.getInstance();
        calendar.set(year,month,day,hour,minute);
        if(year==0 || month==0 || day==0){
            date="";
            time="";
        }else{
            date= DateFormat.getDateInstance(DateFormat.LONG).format(calendar.getTime());

            //Will change automatically between 12 and 24 format
            time=DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        }
        /** It is important to add a new item in the adapter and in the array list because if it is not added it will throw
         * an IndexOutOfBoundsException when is notified to the adapter that a new item is inserted */
        remindersList.add(0,new ReminderItem(0,reminder_title,date,time,false));
        remindersRecAdapter.addItem(0,new ReminderItem(reminder_id,reminder_title,date,time,false));

        remindersRecAdapter.notifyItemInserted(0);
        CustomToast("Reminder Added",R.drawable.ic_done_white_24dp);
    }

    private void CustomToast(String text,int imageResource){
        LayoutInflater inflater=getLayoutInflater();

        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) fragmentView.findViewById(R.id.toast_root));

        TextView toastText = layout.findViewById(R.id.toast_text);
        ImageView toastImage = layout.findViewById(R.id.toast_image);

        toastText.setText(text);
        toastImage.setImageResource(imageResource);

        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);

        toast.show();
    }

}
