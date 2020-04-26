package com.example.todolist.Adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Entity.Task;
import com.example.todolist.R;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private List<Task> allTask = new ArrayList<Task>();

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.task_item,parent,false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task currentTask = allTask.get(position);
        holder.taskDescription.setText(currentTask.getNote());
        holder.dot.setBackgroundTintList(ColorStateList.valueOf(currentTask.getColor()));
    }

    @Override
    public int getItemCount() {
        return allTask.size();
    }

    public void setNotes(List<Task> allTask){
        this.allTask = allTask;
        notifyDataSetChanged();
    }

    class TaskHolder extends RecyclerView.ViewHolder{

        private Button dot;
        private TextView taskDescription;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            dot = itemView.findViewById(R.id.dot);
            taskDescription = itemView.findViewById(R.id.taskDescription);
        }
    }

}
