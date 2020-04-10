package com.android.keeper.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.android.keeper.recycle_items.ReminderItem;

import java.util.ArrayList;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.RemindersViewHolder>{

    private ArrayList<ReminderItem> remindersListFull;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener{

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        itemClickListener=listener;
    }

    public static class RemindersViewHolder extends RecyclerView.ViewHolder {
        public RemindersViewHolder(@NonNull View itemView) {
            super(itemView);
        }
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
        return 0;
    }
}
