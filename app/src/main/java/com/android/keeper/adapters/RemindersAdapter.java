package com.android.keeper.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.android.keeper.recycle_items.ReminderItem;

import java.util.ArrayList;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.RemindersViewHolder>{

    private ArrayList<ReminderItem> remindersList;
    private ArrayList<ReminderItem> remindersListFull;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener{

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        itemClickListener=listener;
    }

    public static class RemindersViewHolder extends RecyclerView.ViewHolder {
        public RemindersViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
        }
    }

    public RemindersAdapter(ArrayList<ReminderItem> remindersList) {
        this.remindersList = remindersList;
        remindersListFull=new ArrayList<ReminderItem>(remindersList);
    }

    @NonNull
    @Override
    public RemindersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RemindersViewHolder remindersViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        //Length of items that will be added to the recyclerview
        return remindersList.size();
    }
}
