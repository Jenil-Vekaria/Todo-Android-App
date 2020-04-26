package com.example.todolist.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.todolist.DAO.ProjectDao;
import com.example.todolist.Database.TodoDatabase;
import com.example.todolist.Entity.Project;

import java.util.List;

public class ProjectRepository {
    private ProjectDao projectDao;
    private LiveData<List<Project>> allProjects;

    public ProjectRepository(Application application){
        TodoDatabase db = TodoDatabase.getInstance(application);
        projectDao = db.projectDao();
        allProjects = projectDao.getAllProject();
    }

    public void insert(Project project){
        new InsertProjectAsync(projectDao).execute(project);
    }

    public void update(Project project){
        new UpdateProjectAsync(projectDao).execute(project);
    }

    public void delete(Project project){
        new DeleteProjectAsync(projectDao).execute(project);
    }

    public void deleteAllProjects(){
        new DeleteAllProjectAsync(projectDao).execute();
    }

    public LiveData<List<Project>> getAllProjects(){
        return allProjects;
    }


    private static class InsertProjectAsync extends AsyncTask<Project,Void,Void>{

        private ProjectDao projectDao;

        public InsertProjectAsync(ProjectDao projectDao){
            this.projectDao = projectDao;
        }

        @Override
        protected Void doInBackground(Project... projects) {
            projectDao.insert(projects[0]);
            return null;
        }
    }

    private static class UpdateProjectAsync extends AsyncTask<Project,Void,Void>{

        private ProjectDao projectDao;

        public UpdateProjectAsync(ProjectDao projectDao){
            this.projectDao = projectDao;
        }

        @Override
        protected Void doInBackground(Project... projects) {
            projectDao.update(projects[0]);
            return null;
        }
    }

    private static class DeleteProjectAsync extends AsyncTask<Project,Void,Void>{

        private ProjectDao projectDao;

        public DeleteProjectAsync(ProjectDao projectDao){
            this.projectDao = projectDao;
        }

        @Override
        protected Void doInBackground(Project... projects) {
            projectDao.delete(projects[0]);
            return null;
        }
    }

    private static class DeleteAllProjectAsync extends AsyncTask<Void,Void,Void>{

        private ProjectDao projectDao;

        public DeleteAllProjectAsync(ProjectDao projectDao){
            this.projectDao = projectDao;
        }

        @Override
        protected Void doInBackground(Void... Void) {
            projectDao.deleteAllProjects();
            return null;
        }
    }



}
