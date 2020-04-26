package com.example.todolist.Database;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.todolist.DAO.TaskDao;
import com.example.todolist.Entity.Task;

@Database(entities = {Task.class}, version = 1)
public abstract class TodoDatabase extends RoomDatabase {

    private static TodoDatabase instance;

    public abstract TaskDao taskDao();


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


        private PopulateDBAsyncTask(TodoDatabase db){
            this.taskDao = db.taskDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.insert(new Task(-1,"Finish the App design on Adobe XD", Color.parseColor("#FF7675")));
            taskDao.insert(new Task(-1,"Add the unit testing", Color.parseColor("#74B9FF")));
            taskDao.insert(new Task(-1,"Create a GitHub repo", Color.parseColor("#55EFC4")));
            return null;
        }
    }
}
