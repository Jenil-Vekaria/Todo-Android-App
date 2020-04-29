package com.example.todolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.Adapter.PageViewAdapter;
import com.example.todolist.Entity.Project;
import com.example.todolist.Entity.Task;
import com.example.todolist.ViewModel.TodoViewModel;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ProjectActivity extends AppCompatActivity {

    public final static int ADD_TASK_REQUEST = 1;
    public static final int PROJECT_EDIT_REQUEST = 4;

    private Project currentProject;
    private TodoViewModel todoViewModel;
    private TabLayout tabLayout;

    private List<Project> allProjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        todoViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(TodoViewModel.class);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        Bundle data = getIntent().getExtras();

        if(data != null){
            currentProject = data.getParcelable("PROJECT");
            changeActionbarStyle();
        }
        displayTab();
        addTaskListener();
    }

    public void displayTab(){
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Tasks"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorColor(currentProject.getProjectColor());

        final ViewPager viewPager = findViewById(R.id.viewPager);
        final PageViewAdapter pageViewAdapter = new PageViewAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),currentProject.getProjectID());

        viewPager.setAdapter(pageViewAdapter);

        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void changeActionbarStyle(){
//      Change the action bar color
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(currentProject.getProjectColor()));


//      Changing the action bar title color
        Spannable text = new SpannableString(currentProject.getProjectName());
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);

//      Changing the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(currentProject.getProjectColor());
        }

        if(tabLayout != null)
            tabLayout.setSelectedTabIndicatorColor(currentProject.getProjectColor());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.project_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.edit_project:
                editProject();
                return true;
            case R.id.delete_project:
                deleteProject();
                return true;
            default:
                return false;

        }
    }

    private void deleteProject() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete this project?")
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                todoViewModel.delete(currentProject);
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void editProject() {

        Intent intent = new Intent(ProjectActivity.this,AddProjectActivity.class);
        intent.putExtra(AddProjectActivity.EXTRA_NAME,currentProject.getProjectName());
        intent.putExtra(AddProjectActivity.EXTRA_COLOR_NAME,currentProject.getColorName());
        intent.putExtra(AddProjectActivity.EXTRA_COLOR,currentProject.getProjectColor());
        intent.putExtra(AddProjectActivity.EXTRA_ID,currentProject.getProjectID());
        startActivityForResult(intent,PROJECT_EDIT_REQUEST);

    }

    private void addTaskListener(){
        FloatingActionButton addTask = findViewById(R.id.button_add_task);
        final FloatingActionMenu menu = findViewById(R.id.floatingActionMenu);

        todoViewModel.getAllProjects().observe(this, new Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> projects) {
                allProjects = projects;
            }
        });

//      ADDING TASK BUTTON LISTENER
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.close(true);

                Bundle data = new Bundle();
                data.putParcelableArrayList("ALL_PROJECTS", (ArrayList<? extends Parcelable>) allProjects);

                Intent intent = new Intent(ProjectActivity.this,AddTaskActivity.class);
                intent.putExtra("SELECT_PROJECTID",currentProject.getProjectID());
                intent.putExtras(data);

                startActivityForResult(intent,ADD_TASK_REQUEST);
            }
        });

    }

    private void updateProjectTaskCount(int projectID, int count){
        for(Project p: allProjects){
            if(p.getProjectID() == projectID){
                p.setTotalTasks(count);
                todoViewModel.update(p);
                break;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PROJECT_EDIT_REQUEST && resultCode == RESULT_OK){
            String projectName = data.getStringExtra(AddProjectActivity.EXTRA_NAME);
            String colorName = data.getStringExtra(AddProjectActivity.EXTRA_COLOR_NAME);
            int colorCode = data.getIntExtra(AddProjectActivity.EXTRA_COLOR,-1);
            int id = data.getIntExtra(AddProjectActivity.EXTRA_ID,-1);
            int totalTask = 0;

            Project updateProject = new Project(projectName,colorCode,totalTask,colorName);
            updateProject.setProjectID(id);
            todoViewModel.update(updateProject);

            currentProject = updateProject;
            changeActionbarStyle();
            Toast.makeText(this,"Project updated",Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK){
            String task = data.getStringExtra(AddTaskActivity.EXTRA_TASK);
            final int projectID = data.getIntExtra(AddTaskActivity.EXTRA_PROJECTID,-1);
            int color = data.getIntExtra(AddTaskActivity.EXTRA_COLOR, Color.parseColor("#74B9FF"));
            String colorName = data.getStringExtra(AddTaskActivity.EXTRA_COLOR_NAME);

            Task newTask = new Task(projectID,task,color,colorName,false);
            todoViewModel.insert(newTask);

//          If task added to project, update project totalTask count
            todoViewModel.getAllProjectTaskCount(projectID).observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    updateProjectTaskCount(projectID,integer.intValue());
                }
            });

            Toast.makeText(this,"Task saved",Toast.LENGTH_SHORT).show();
        }
    }
}
