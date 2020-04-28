package com.example.todolist.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "project_table")
public class Project implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int projectID;

    private String projectName;

    private int projectColor;
    private String colorName;

    private int totalTasks;

    private boolean isDone = false;

    public Project(String projectName, int projectColor, int totalTasks, String colorName) {
        this.projectName = projectName;
        this.projectColor = projectColor;
        this.totalTasks = totalTasks;
        this.colorName = colorName;
    }


    protected Project(Parcel in) {
        projectID = in.readInt();
        projectName = in.readString();
        projectColor = in.readInt();
        colorName = in.readString();
        totalTasks = in.readInt();
        isDone = in.readByte() != 0;
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getProjectColor() {
        return projectColor;
    }

    public void setProjectColor(int projectColor) {
        this.projectColor = projectColor;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(projectID);
        dest.writeString(projectName);
        dest.writeInt(projectColor);
        dest.writeString(colorName);
        dest.writeInt(totalTasks);
        dest.writeByte((byte) (isDone ? 1 : 0));
    }
}
