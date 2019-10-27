package com.android.keeper.localdb.entities;

public class NotesEntities {

    private Integer note_id;
    private String note_title;
    private String note_description;

    public NotesEntities(Integer note_id, String note_title, String note_description) {
        this.note_id = note_id;
        this.note_title = note_title;
        this.note_description = note_description;
    }

    public Integer getNote_id() {
        return note_id;
    }

    public void setNote_id(Integer note_id) {
        this.note_id = note_id;
    }

    public String getNote_title() {
        return note_title;
    }

    public void setNote_title(String note_title) {
        this.note_title = note_title;
    }

    public String getNote_description() {
        return note_description;
    }

    public void setNote_description(String note_description) {
        this.note_description = note_description;
    }
}
