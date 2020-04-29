package com.example.todolist.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todolist.Entity.Project;

import java.util.List;

@Dao
public interface ProjectDao {

    @Insert
    void insert(Project project);

    @Update
    void update(Project project);

    @Delete
    void delete(Project project);

    @Query("DELETE FROM project_table")
    void deleteAllProjects();

    @Query("SELECT * FROM project_table WHERE projectID <> -1 ORDER BY projectID DESC")
    LiveData<List<Project>> getAllProject();

    @Query("SELECT * FROM project_table WHERE projectID = :projectID")
    LiveData<Project> getProjectById(int projectID);

}
