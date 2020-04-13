package com.android.keeper.view_holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.keeper.R;
import com.android.keeper.adapters.RemindersAdapter;

public class RemindersViewHolder extends RecyclerView.ViewHolder {

    public TextView reminderTitleTextView,reminderDateTextView;

    public RemindersViewHolder(@NonNull View itemView, final RemindersAdapter.OnItemClickListener listener) {
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