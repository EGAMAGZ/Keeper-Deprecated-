package com.android.keeper.dialog;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.keeper.R;
import com.android.keeper.localdb.SQLiteConnection;
import com.android.keeper.localdb.utilities.RemindersUtilities;

public class AddNewReminderBottomSheet extends BottomSheetDialogFragment {

    private View fragmentView;
    public AddNewReminderBottomSheetListener bottomSheetListener;
    private SQLiteConnection sqLiteConnection;
    private ImageButton saveReminderButton;
    private EditText titleEditText;

    private String reminder_title;
    private int reminder_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView=inflater.inflate(R.layout.bottom_sheet_add_new_reminder,container,false);
        sqLiteConnection=new SQLiteConnection(getContext(),"keeper_db",null,1);

        saveReminderButton=fragmentView.findViewById(R.id.reminder_save);

        titleEditText=fragmentView.findViewById(R.id.reminder_title);

        saveReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reminder_title=titleEditText.getText().toString();
                if(reminder_title.isEmpty()){
                    bottomSheetListener.onEmptyReminderTitle();
                }else{
                    //reminder_id=saveReminder();
                    reminder_id=0;
                    bottomSheetListener.onAddTask(reminder_id,reminder_title);
                    dismiss();
                }
            }
        });

        return fragmentView;
    }

    private int saveReminder(){
        //TODO: ADD TIME AND DATE PICKER
        int id;
        long returnedId;
        SQLiteDatabase database=sqLiteConnection.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(RemindersUtilities.COLUMN_REMINDER_TITLE,reminder_title);

        returnedId=database.insert(RemindersUtilities.TABLE_NAME,RemindersUtilities.COLUMN_REMINDER_ID,values);
        id=(int) returnedId;
        return id;
    }

    public interface AddNewReminderBottomSheetListener{
        void onAddTask(int reminder_id,String reminder_title);
        void onEmptyReminderTitle();
        void onReminderDateSelected();
    }

    public void setBottomSheetListener(AddNewReminderBottomSheetListener listener){
        bottomSheetListener=listener;
    }

}
