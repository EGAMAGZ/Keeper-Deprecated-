package com.android.keeper.view_holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.keeper.R;
import com.android.keeper.adapters.TasksAdapter;

public class TasksViewHolder extends RecyclerView.ViewHolder{
    /*This class will get all the elements from layout, and allow us to acces to them(in this case define them)
     * and also add them listeners
     * */
    public TextView taskTitleTextView,taskDetailsTextView;
    public ImageView taskImage;

    public TasksViewHolder(@NonNull View itemView, final TasksAdapter.OnItemClickListener listener) { //Because this class is static, we need to pass the interface OnItemClickListener
        super(itemView);
        /*At cause the adapter is getting the elements from the arraylist, the
         * getAdapterPosition() [position returned by interface] will be the same
         * position as the arraylist.
         * */
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(listener !=null){
                    int position=getAdapterPosition();//Get Adapter Position (ItemPosition)
                    if(position!=RecyclerView.NO_POSITION){
                        listener.OnEditTask(position);
                    }
                }
                return true;
            }
        });

        taskTitleTextView=itemView.findViewById(R.id.task_item_title);
        taskDetailsTextView=itemView.findViewById(R.id.task_item_details);
        taskImage=itemView.findViewById(R.id.task_item_image);

        taskImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener !=null){
                    int position=getAdapterPosition();//Get Adapter Position (ItemPosition)
                    if(position!=RecyclerView.NO_POSITION){
                        listener.OnTaskDoneClick(position);
                    }
                }
            }
        });
    }
}
