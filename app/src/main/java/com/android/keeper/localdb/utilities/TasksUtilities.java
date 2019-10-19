package com.android.keeper.localdb.utilities;

import android.support.annotation.NonNull;

public class TasksUtilities {
    public static final String TABLE_NAME="TASKS_LIST";

    public static final String COLUMN_TASK_ID="task_id";
    public static final String COLUMN_TASK_TITLE="task_title";
    public static final String COLUMN_TASK_DETAILS="task_details";
    public static final String COLUMN_TASK_DONE="task_done";
    public static final String COLUMN_TASK_YEAR="task_year";
    public static final String COLUMN_TASK_MONTH="task_month";
    public static final String COLUMN_TASK_DAY="task_day";
    public static final String COLUMN_TASK_HOUR="task_hour";
    public static final String COLUMN_TASK_MINUTE="task_minute";

    public static final String CREATE_TASKS_TABLE="CREATE TABLE "+TABLE_NAME+"("+COLUMN_TASK_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NULL,"+COLUMN_TASK_TITLE+" TEXT NOT NULL,"+
            COLUMN_TASK_DETAILS+" TEXT NOT NULL,"+COLUMN_TASK_DONE+" INTEGER NULL DEFAULT 0,"+COLUMN_TASK_YEAR+" INTEGER NULL DEFAULT 0,"+COLUMN_TASK_MONTH+" INTEGER NULL DEFAULT 0,"+
            COLUMN_TASK_DAY+" INTEGER NULL DEFAULT 0,"+COLUMN_TASK_HOUR+" INTEGER NULL DEFAULT 0,"+COLUMN_TASK_MINUTE+" INTEGER NULL DEFAULT 0)";
}
