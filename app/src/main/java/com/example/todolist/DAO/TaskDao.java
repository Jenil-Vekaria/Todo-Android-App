package com.example.todolist.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.todolist.Entity.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Update
    void update(Task project);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM task_table")
    void deleteAllTasks();

    @Query("SELECT * FROM task_table ORDER BY taskID DESC")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM task_table WHERE projectID = :projectID ORDER BY taskID DESC")
    LiveData<List<Task>> getAllProjectTasks(int projectID);


    @Query("SELECT * FROM task_table WHERE isDone = 1 ORDER BY taskID DESC")
    LiveData<List<Task>> getAllCompletedTasks();

    @Query("SELECT * FROM task_table WHERE isDone = 1 AND projectID = :projectID ORDER BY taskID DESC")
    LiveData<List<Task>> getAllProjectCompletedTasks(int projectID);


    @Query("SELECT * FROM task_table WHERE isDone = 0 ORDER BY taskID DESC")
    LiveData<List<Task>> getAllIncompletedTasks();

}
