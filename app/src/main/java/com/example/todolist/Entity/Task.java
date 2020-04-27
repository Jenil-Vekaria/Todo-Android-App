package com.example.todolist.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
//foreignKeys = @ForeignKey(entity = Project.class,
//        parentColumns = "projectID",
//        childColumns = "projectID",
//        onDelete = ForeignKey.CASCADE)
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int taskID;

    private int projectID = -1;

    private String note;

    private int color;
    private String colorName;

    private boolean isDone = false;

    public Task(int projectID, String note, int color, String colorName, boolean isDone) {
        this.projectID = projectID;
        this.note = note;
        this.color = color;
        this.colorName = colorName;
        this.isDone = isDone;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
