package com.android.keeper.localdb.utilities;

public class NotesUtilities {

    public static final String TABLE_NAME="NOTES_LIST";

    public static final String COLUMN_NOTE_ID="note_id";
    public static final String COLUMN_NOTE_TITLE="note_title";
    public static final String COLUMN_NOTE_DESCRIPTION="note_description";

    public static final String CREATE_NOTES_TABLE="CREATE TABLE "+TABLE_NAME+" ("+COLUMN_NOTE_ID+" PRIMARY KEY AUTOINCREMENT NULL,"+COLUMN_NOTE_TITLE+" TEXT NOT NULL,"+
            COLUMN_NOTE_DESCRIPTION+" TEXT NULL)";
}
