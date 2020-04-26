package com.example.todolist.Adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Entity.Project;
import com.example.todolist.R;

import java.util.ArrayList;
import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectHolder> {

    private List<Project> allProjects = new ArrayList<Project>();

    @NonNull
    @Override
    public ProjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.project_item,parent,false);
        return new ProjectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectHolder holder, int position) {
        Project currentProject = allProjects.get(position);

        holder.projectCard.setBackgroundTintList(ColorStateList.valueOf(currentProject.getProjectColor()));
        holder.projectName.setText(currentProject.getProjectName());
        holder.totalTasks.setText(currentProject.getTotalTasks() + " Tasks");

    }

    @Override
    public int getItemCount() {
        return allProjects.size();
    }

    public void setProject(List<Project> allProjects){
        this.allProjects = allProjects;
    }

    public class ProjectHolder extends RecyclerView.ViewHolder{

        private CardView projectCard;
        private TextView projectName;
        private TextView totalTasks;

        public ProjectHolder(@NonNull View itemView) {
            super(itemView);

            projectCard = itemView.findViewById(R.id.projectCard);
            projectName = itemView.findViewById(R.id.projectName);
            totalTasks = itemView.findViewById(R.id.totalTasks);
        }
    }
}
