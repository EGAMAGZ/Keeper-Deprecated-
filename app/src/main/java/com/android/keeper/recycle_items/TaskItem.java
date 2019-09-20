package com.android.keeper.recycle_items;

public class TaskItem {
    private int imageResource;
    private int taskId;
    private String taskTitle;
    private String taskDetails;
    private boolean taskDone;

    public TaskItem(int imageResource,int taskId, String taskTitle, String taskDetails,boolean taskDone) {
        this.imageResource = imageResource;
        this.taskId=taskId;
        this.taskTitle = taskTitle;
        this.taskDetails = taskDetails;
        this.taskDone=taskDone;
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

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public boolean isTaskDone() {
        return taskDone;
    }

    public void setTaskDone(boolean taskDone) {
        this.taskDone = taskDone;
    }
}
