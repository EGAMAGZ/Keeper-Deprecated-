package com.android.keeper.recycle_items;

public class ReminderItem {

    private int reminderId;
    private String reminderTitle;
    private String reminderDate;
    private String reminderTime;
    private boolean reminderDone;

    public ReminderItem(int reminderId, String reminderTitle, String reminderDate, String reminderTime, boolean reminderDone) {
        this.reminderId = reminderId;
        this.reminderTitle = reminderTitle;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
        this.reminderDone = reminderDone;
    }

    public int getReminderId() {
        return reminderId;
    }

    public String getReminderTitle() {
        return reminderTitle;
    }

    public void setReminderTitle(String reminderTitle) {
        this.reminderTitle = reminderTitle;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public boolean isReminderDone() {
        return reminderDone;
    }

    public void setReminderDone(boolean reminderDone) {
        this.reminderDone = reminderDone;
    }
}