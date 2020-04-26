package com.example.todolist.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "project_table")
public class Project {

    @PrimaryKey(autoGenerate = true)
    private int projectID;

    private String projectName;

    private int projectColor;

    private int totalTasks;

    private boolean isDone;

    public Project(String projectName, int projectColor, int totalTasks, boolean isDone) {
        this.projectName = projectName;
        this.projectColor = projectColor;
        this.totalTasks = totalTasks;
        this.isDone = isDone;
    }

    public int getProjectID() {
        return projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public int getProjectColor() {
        return projectColor;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public boolean isDone() {
        return isDone;
    }
}
