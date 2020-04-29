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
import com.example.todolist.R;

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

            Project noProject = new Project("",Color.parseColor("#FF7675"),0,"");
            noProject.setProjectID(-1);
            projectDao.insert(noProject);


            projectDao.insert(new Project("Sample Project No.3",Color.parseColor("#ff9f43"),0,"Double Dragon Skin"));
            projectDao.insert(new Project("Sample Project No.2",Color.parseColor("#e84393"),0,"Prunus Avium"));
            projectDao.insert(new Project("Sample Project No.1", Color.parseColor("#6c5ce7"),0,"Exodus Fruit"));

            taskDao.insert(new Task(-1,"CLICK HERE: Edit the task", Color.parseColor("#0984e3"),"Electron Blue",false));
            taskDao.insert(new Task(-1,"SWIPE LEFT: Delete Task", Color.parseColor("#a29bfe"),"Shy Moment",false));
            taskDao.insert(new Task(-1,"SWIPE RIGHT: Complete/Incomplete task", Color.parseColor("#feca57"),"Casandora Yellow",false));

            return null;
        }
    }
}
