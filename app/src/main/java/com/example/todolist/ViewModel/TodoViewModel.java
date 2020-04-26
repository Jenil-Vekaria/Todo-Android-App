package com.example.todolist.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todolist.Entity.Task;
import com.example.todolist.Repository.TaskRepository;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {

    private TaskRepository taskRepository;
    private LiveData<List<Task>> allTasks;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        allTasks = taskRepository.getAllTasks();
    }

    public void insert(Task task){
        taskRepository.insert(task);
    }
    public void update(Task task){
        taskRepository.update(task);
    }

    public void delete(Task task){
        taskRepository.delete(task);
    }

    public void deleteAllTasks(){
        taskRepository.deleteAllTasks();
    }

    public LiveData<List<Task>> getAllTasks(){
        return taskRepository.getAllTasks();
    }
}
