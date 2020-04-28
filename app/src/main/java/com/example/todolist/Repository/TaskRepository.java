package com.example.todolist.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.todolist.DAO.TaskDao;
import com.example.todolist.Database.TodoDatabase;
import com.example.todolist.Entity.Task;

import java.util.List;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allIncompletedTask;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> allCompletedTask;

    private LiveData<List<Task>> allProjectTasks;
    private LiveData<List<Task>> allProjectCompletedTask;

    public TaskRepository(Application application){
        TodoDatabase database = TodoDatabase.getInstance(application);
        taskDao = database.taskDao();

        allTasks = taskDao.getAllTasks();
        allCompletedTask = taskDao.getAllCompletedTasks();
        allIncompletedTask = taskDao.getAllIncompletedTasks();

    }

    public void insert(Task task){
        new InsertTaskAsyncTask(taskDao).execute(task);
    }

    public void update(Task task){
        new UpdateTaskAsyncTask(taskDao).execute(task);
    }

    public void delete(Task task){
        new DeleteTaskAsyncTask(taskDao).execute(task);
    }

    public void deleteAllTasks(){
        new DeleteAllTaskAsyncTask(taskDao).execute();
    }

    public LiveData<List<Task>> getAllIncompletedTask() { return allIncompletedTask;}
    public LiveData<List<Task>> getAllTasks(){
        return allTasks;
    }
    public LiveData<List<Task>> getAllCompletedTask() { return allCompletedTask;}


    private static class InsertTaskAsyncTask extends AsyncTask<Task,Void,Void>{
        private TaskDao taskDao;

        private InsertTaskAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insert(tasks[0]);
            return null;
        }
    }


    private static class UpdateTaskAsyncTask extends AsyncTask<Task,Void,Void>{
        private TaskDao taskDao;

        private UpdateTaskAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.update(tasks[0]);
            return null;
        }
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<Task,Void,Void>{
        private TaskDao taskDao;

        private DeleteTaskAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.delete(tasks[0]);
            return null;
        }
    }

    private static class DeleteAllTaskAsyncTask extends AsyncTask<Void,Void,Void>{
        private TaskDao taskDao;

        private DeleteAllTaskAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.deleteAllTasks();
            return null;
        }
    }
}
