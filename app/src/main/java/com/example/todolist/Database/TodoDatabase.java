package com.example.todolist.Database;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.todolist.DAO.ProjectDao;
import com.example.todolist.DAO.TaskDao;
import com.example.todolist.Entity.Project;
import com.example.todolist.Entity.Task;

@Database(entities = {Task.class, Project.class}, version = 1)
public abstract class TodoDatabase extends RoomDatabase {

    private static TodoDatabase instance;

    public abstract TaskDao taskDao();
    public abstract ProjectDao projectDao();


    public static synchronized TodoDatabase getInstance(Context context){

        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TodoDatabase.class, "todo_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallBack)
                            .build();
        }

        return instance;
    }

//    Populate the database

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void,Void,Void>{
        private TaskDao taskDao;
        private ProjectDao projectDao;

        private PopulateDBAsyncTask(TodoDatabase db){
            this.taskDao = db.taskDao();
            this.projectDao = db.projectDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.insert(new Task(-1,"Finish the App design on Adobe XD", Color.parseColor("#FF7675"),"Red",false));
            taskDao.insert(new Task(-1,"Add the unit testing", Color.parseColor("#74B9FF"),"Green",true));
            taskDao.insert(new Task(-1,"Create a GitHub repo", Color.parseColor("#55EFC4"),"Blue",true));

            projectDao.insert(new Project("CPS847 Final Project",Color.parseColor("#FF7675"),0,"Red"));
            projectDao.insert(new Project("CPS616 Assignment 1",Color.parseColor("#74B9FF"),0,"Default Color"));
            projectDao.insert(new Project("Android Todo List App",Color.parseColor("#55EFC4"),0,"Green"));
            return null;
        }
    }
}
