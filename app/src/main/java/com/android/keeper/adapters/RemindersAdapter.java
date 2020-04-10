package com.android.keeper.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.keeper.R;
import com.android.keeper.recycle_items.ReminderItem;

import java.util.ArrayList;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.RemindersViewHolder>{

    private ArrayList<ReminderItem> remindersList;
    private ArrayList<ReminderItem> remindersListFull;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener{
        void onEditReminder(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        itemClickListener=listener;
    }

    public static class RemindersViewHolder extends RecyclerView.ViewHolder {
        //TODO: PASS THIS CLASS TO AN INDEPENDENT CLASS

        public TextView reminderTitleTextView,reminderDateTextView;

        public RemindersViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(listener !=null){
                        int position=getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onEditReminder(position);
                        }
                    }

                    return true;
                }
            });

            reminderTitleTextView=itemView.findViewById(R.id.reminder_item_title);
            reminderDateTextView=itemView.findViewById(R.id.reminder_item_date);

        }
    }

    public RemindersAdapter(ArrayList<ReminderItem> remindersList) {
        this.remindersList = remindersList;
        remindersListFull=new ArrayList<ReminderItem>(remindersList);
    }

    @NonNull
    @Override
    public RemindersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        /*This method will directly access to the layout
        *   viewgroup -> parent*/
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reminder_item,viewGroup,false);
        RemindersViewHolder rvh=new RemindersViewHolder(view,itemClickListener);

        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RemindersViewHolder remindersViewHolder, int i) {
        /*At the moment that each item is inserted, this method will be executed
         *
         * i=position from the item
         * so, i is the same position to the array list and adapter( getAdapterPosition() )
         * */
        ReminderItem currentItem=remindersList.get(i);
        remindersViewHolder.reminderTitleTextView.setText(currentItem.getReminderTitle());
    }

    @Override
    public int getItemCount() {
        //Length of items that will be added to the recyclerview
        return remindersList.size();
    }

    public void removeItem(int position){
        remindersListFull.remove(position);
    }
    public void addItem(int index,ReminderItem reminderItem){
        remindersListFull.add(index,reminderItem);
    }

    public void editItem(int reminder_position,String reminder_title){
        remindersListFull.get(reminder_position).setReminderTitle(reminder_title);
    }
}
