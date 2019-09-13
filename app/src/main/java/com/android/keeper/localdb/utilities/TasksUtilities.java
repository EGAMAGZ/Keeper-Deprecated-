package com.android.keeper.localdb.utilities;

public class TasksUtilities {
    public static final String TABLE_NAME="TASKS_LIST";
    public static final String COLUMN_TASK_ID="task_id";
    public static final String COLUMN_TASK_TITLE="task_title";
    public static final String COLUMN_TASK_DESCRIPTION="task_details";

    public static final String CREATE_TASKS_TABLE="CREATE TABLE "+TABLE_NAME+"("+COLUMN_TASK_ID+" INTEGER PRIMARY KEY NULL,"+COLUMN_TASK_TITLE+" TEXT NOT NULL,"+COLUMN_TASK_DESCRIPTION+" TEXT NOT NULL)";
}
