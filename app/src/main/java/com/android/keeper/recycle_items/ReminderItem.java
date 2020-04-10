package com.android.keeper.recycle_items;

public class ReminderItem {

    private int reminderId;
    private String reminderTitle;
    private boolean reminderDone;

    public ReminderItem(int reminderId, String reminderTitle, boolean reminderDone) {
        this.reminderId = reminderId;
        this.reminderTitle = reminderTitle;
        this.reminderDone = reminderDone;
    }

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public String getReminderTitle() {
        return reminderTitle;
    }

    public void setReminderTitle(String reminderTitle) {
        this.reminderTitle = reminderTitle;
    }

    public boolean isReminderDone() {
        return reminderDone;
    }

    public void setReminderDone(boolean reminderDone) {
        this.reminderDone = reminderDone;
    }
}