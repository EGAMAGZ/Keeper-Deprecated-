package com.android.keeper.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.keeper.R;
import com.android.keeper.localdb.SQLiteConnection;
import com.android.keeper.localdb.utilities.NotesUtilities;

public class NotesFragment extends Fragment {

    private SQLiteConnection conn;
    private ScrollView scrollView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView=inflater.inflate(R.layout.fragment_notes,container,false);
        conn=new SQLiteConnection(getContext(),"keeper_db",null,1);

        scrollView=fragmentView.findViewById(R.id.notes_scrollview);

        loadNotes();
        return fragmentView;
    }

    private void loadNotes(){
        String[] colums={NotesUtilities.COLUMN_NOTE_ID,NotesUtilities.COLUMN_NOTE_TITLE,NotesUtilities.COLUMN_NOTE_DESCRIPTION};

        SQLiteDatabase database=conn.getReadableDatabase();

        Cursor cursor=database.query(NotesUtilities.TABLE_NAME,colums,null,null,null,
                null, NotesUtilities.COLUMN_NOTE_ID+" DESC",null);
        /*while(cursor.moveToNext()){

        }*/
        Toast.makeText(getContext(),"TOTAL:"+cursor.getCount(),Toast.LENGTH_SHORT).show();
    }
}
