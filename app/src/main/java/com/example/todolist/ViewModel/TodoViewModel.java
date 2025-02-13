package com.example.todolist.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todolist.Entity.Project;
import com.example.todolist.Entity.Task;
import com.example.todolist.Repository.ProjectRepository;
import com.example.todolist.Repository.TaskRepository;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {

    private TaskRepository taskRepository;
    private ProjectRepository projectRepository;

    private LiveData<List<Task>> allIncompletedTasks;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> allCompletedTasks;
    private LiveData<List<Project>> allProjects;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        projectRepository = new ProjectRepository(application);

        allTasks = taskRepository.getAllTasks();
        allCompletedTasks = taskRepository.getAllCompletedTask();
        allIncompletedTasks = taskRepository.getAllIncompletedTask();

        allProjects = projectRepository.getAllProjects();
    }

    public void insert(Task task){
        taskRepository.insert(task);
    }
    public void insert(Project project) { projectRepository.insert(project); }

    public void update(Task task){
        taskRepository.update(task);
    }
    public void update(Project project){
        projectRepository.update(project);
    }

    public void delete(Task task){
        taskRepository.delete(task);
    }
    public void delete(Project project){
        projectRepository.delete(project);
    }

    public void deleteAllTasks(){
        taskRepository.deleteAllTasks();
    }
    public void deleteAllProjects(){
        projectRepository.deleteAllProjects();
    }

    public LiveData<Integer> getAllProjectTaskCount(int projectId) { return taskRepository.getAllProjectTasksCount(projectId); }

    public LiveData<List<Task>> getAllIncompletedTasks(){ return allIncompletedTasks; }
    public LiveData<List<Task>> getAllTasks(){ return allTasks; }
    public LiveData<List<Task>> getAllCompletedTasks(){ return allCompletedTasks; }

    public LiveData<List<Project>> getAllProjects(){
        return allProjects;
    }

    public LiveData<Project> getProjectById(int projectID) { return projectRepository.getProjectById(projectID); }
}
