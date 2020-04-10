package com.android.keeper.localdb.entities;

public class TasksEntities {
    //TODO: DELETE ALL ENTITIES BECAUSE THEY AREN'T USE
    private Integer task_id;
    private String task_title;
    private String task_details;
    private Boolean task_done;

    public TasksEntities(Integer task_id, String task_title, String task_details, Boolean task_done) {
        this.task_id = task_id;
        this.task_title = task_title;
        this.task_details = task_details;
        this.task_done = task_done;
    }

    public Boolean getTask_done() {
        return task_done;
    }

    public void setTask_done(Boolean task_done) {
        this.task_done = task_done;
    }

    public String getTask_title() {
        return task_title;
    }

    public void setTask_title(String task_title) {
        this.task_title = task_title;
    }

    public String getTask_details() {
        return task_details;
    }

    public void setTask_details(String task_details) {
        this.task_details = task_details;
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }
}
