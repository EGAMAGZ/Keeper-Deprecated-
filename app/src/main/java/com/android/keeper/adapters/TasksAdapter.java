package com.android.keeper.adapters;

import java.util.ArrayList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import com.android.keeper.R;
import com.android.keeper.recycle_items.TaskItem;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private ArrayList<TaskItem> tasksList;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        itemClickListener=listener;
    }

    public static class TasksViewHolder extends RecyclerView.ViewHolder{

        public TextView taskTitleTextView,taskDetailsTextView;
        public ImageView taskImage;

        public TasksViewHolder(@NonNull View itemView, final OnItemClickListener listener) { //Because this class is static, we need to pass the interface OnItemClickListener
            super(itemView);

            taskTitleTextView=itemView.findViewById(R.id.task_item_title);
            taskDetailsTextView=itemView.findViewById(R.id.task_item_details);
            taskImage=itemView.findViewById(R.id.task_item_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener !=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public TasksAdapter(ArrayList<TaskItem> tasksList){
        this.tasksList=tasksList;
    }

    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //viewgroup = parent
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_item,viewGroup,false);
        TasksViewHolder  tvh=new TasksViewHolder(view,itemClickListener);
        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder tasksViewHolder, int i) {
        //i=position
        TaskItem currentItem = tasksList.get(i);

        tasksViewHolder.taskTitleTextView.setText(currentItem.getTaskTitle());
        tasksViewHolder.taskDetailsTextView.setText(currentItem.getTaskDetails());
        tasksViewHolder.taskImage.setImageResource(currentItem.getImageResource());
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

}
