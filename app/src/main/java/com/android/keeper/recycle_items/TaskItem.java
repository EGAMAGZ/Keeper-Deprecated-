package com.android.keeper.recycle_items;

public class TaskItem {
    private int imageResource;
    private int taskId;
    private String taskTitle;
    private String taskDetails;

    public TaskItem(int imageResource,int taskId, String taskTitle, String taskDetails) {
        this.imageResource = imageResource;
        this.taskId=taskId;
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

    public int getTaskId() {
        return taskId;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public void setTaskDetails(String taskDetails) {
        this.taskDetails = taskDetails;
    }
}
