package com.android.keeper.recycle_items;

public class TaskItem {
    private int imageResource;
    private String taskTitle;
    private String taskDetails;

    public TaskItem( String taskTitle, String taskDetails) {
        //this.imageResource = imageResource;
        this.taskTitle = taskTitle;
        this.taskDetails = taskDetails;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getTaskDetails() {
        return taskDetails;
    }
}
