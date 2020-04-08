package com.android.keeper.adapters;

import java.util.ArrayList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.keeper.R;
import com.android.keeper.recycle_items.TaskItem;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder>  implements Filterable {

    private ArrayList<TaskItem> tasksList;
    private ArrayList<TaskItem> taskListFull;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener{
        void OnEditTask(int position);
        void OnTaskDoneClick(int position);
        //void onItemLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        itemClickListener=listener;
    }

    public static class TasksViewHolder extends RecyclerView.ViewHolder{
        /*This class will get all the elements from layout, and allow us to acces to them(in this case define them)
        * and also add them listeners
        * */
        public TextView taskTitleTextView,taskDetailsTextView;
        public ImageView taskImage;
        public View v;

        public TasksViewHolder(@NonNull View itemView, final OnItemClickListener listener) { //Because this class is static, we need to pass the interface OnItemClickListener
            super(itemView);
            v=itemView;
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

    public TasksAdapter(ArrayList<TaskItem> tasksList){
        this.tasksList=tasksList;
        taskListFull=new ArrayList<>(tasksList);
    }

    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //This method will directly access to the layout
        //viewgroup = parent
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_item,viewGroup,false);
        TasksViewHolder  tvh=new TasksViewHolder(view,itemClickListener);
        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder tasksViewHolder, int i) {
        /*At the moment that each item is inserted, this method will be executed
        *
        * i=position from the item
        * */
        TaskItem currentItem = tasksList.get(i);

        tasksViewHolder.taskTitleTextView.setText(currentItem.getTaskTitle());
        tasksViewHolder.taskDetailsTextView.setText(currentItem.getTaskDetails());
        tasksViewHolder.taskImage.setImageResource(currentItem.getImageResource());
    }

    @Override
    public int getItemCount() {
        //Length of items that will be added to the recyclerview
        return tasksList.size();
    }

    @Override
    public Filter getFilter() {
        return taskListFilter;
    }

    private Filter taskListFilter=new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<TaskItem> filteredList=new ArrayList<TaskItem>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(taskListFull);
            }else{
                String filterpattern= constraint.toString().toLowerCase().trim();

                for(TaskItem item: taskListFull){
                    if(item.getTaskTitle().toLowerCase().contains(filterpattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results=new FilterResults();
            results.values= filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            tasksList.clear();
            tasksList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };

    public void removeItem(int position){
        taskListFull.remove(position);
        //notifyDataSetChanged();
    }

    public void addItem(int index,TaskItem taskItem){
        taskListFull.add(index,taskItem);
    }

    public void editItem(int id,String task_title,String task_details){
        for (int i=0;i<tasksList.size();++i) {
            if(taskListFull.get(i).getTaskId()==id){
                taskListFull.get(i).setTaskTitle(task_title);
                taskListFull.get(i).setTaskDetails(task_details);
            }
        }
    }
}
