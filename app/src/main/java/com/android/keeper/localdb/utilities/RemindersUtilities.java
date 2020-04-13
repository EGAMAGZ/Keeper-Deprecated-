package com.android.keeper.localdb.utilities;

public class RemindersUtilities {
    public static final String TABLE_NAME="REMINDERS_LIST";

    public static final String COLUMN_REMINDER_ID="reminder_id";
    public static final String COLUMN_REMINDER_TITLE="reminder_title";
    public static final String COLUMN_REMINDER_YEAR="reminder_year";
    public static final String COLUMN_REMINDER_MONTH="reminder_month";
    public static final String COLUMN_REMINDER_DAY="reminder_day";
    public static final String COLUMN_REMINDER_HOUR="reminder_hour";
    public static final String COLUMN_REMINDER_MINUTE="reminder_minute";
    public static final String COLUMN_REMINDER_DONE="reminder_done";

    public static final String CREATE_REMINDER_TABLE="CREATE TABLE "+TABLE_NAME+"("+COLUMN_REMINDER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NULL,"+
            COLUMN_REMINDER_TITLE+" TEXT NOT NULL,"+COLUMN_REMINDER_YEAR+" INTEGER NULL DEFAULT 0,"+COLUMN_REMINDER_MONTH+" INTEGER NULL DEFAULT 0,"+
            COLUMN_REMINDER_DAY+" INTEGER NULL DEFAULT 0,"+COLUMN_REMINDER_HOUR+" INTEGER NULL DEFAULT 0,"+COLUMN_REMINDER_MINUTE+" INTEGER NULL DEFAULT 0,"+
            COLUMN_REMINDER_DONE+" INTEGER NULL DEFAULT 0)";
}
