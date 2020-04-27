package com.example.todolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.todolist.Adapter.PageViewAdapter;
import com.example.todolist.Adapter.ProjectAdapter;
import com.example.todolist.Entity.Project;
import com.example.todolist.Entity.Task;
import com.example.todolist.ViewModel.TodoViewModel;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    public final static int ADD_TASK_REQUEST = 1;
    public final static int ADD_PROJECT_REQUEST = 3;

    private TodoViewModel todoViewModel;
    FloatingActionMenu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(TodoViewModel.class);

        menu = findViewById(R.id.floatingActionMenu);


        buttonListener();
        displayProjects();
        displayDailyTask();
    }

    private void buttonListener() {
        FloatingActionButton addTask = findViewById(R.id.button_add_task);
        FloatingActionButton addProject = findViewById(R.id.button_add_project);

//      ADDING TASK BUTTON LISTENER
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.close(true);
                Intent intent = new Intent(MainActivity.this,AddTaskActivity.class);
                startActivityForResult(intent,ADD_TASK_REQUEST);
            }
        });

//      ADDING PROJECT BUTTON LISTENER
        addProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.close(true);
                Intent intent = new Intent(MainActivity.this,AddProjectActivity.class);
                startActivityForResult(intent,ADD_PROJECT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK){
            String task = data.getStringExtra(AddTaskActivity.EXTRA_TASK);
            String project = data.getStringExtra(AddTaskActivity.EXTRA_PROJECT);
            int color = data.getIntExtra(AddTaskActivity.EXTRA_COLOR, Color.parseColor("#74B9FF"));
            String colorName = data.getStringExtra(AddTaskActivity.EXTRA_COLOR_NAME);

            Task newTask = new Task(-1,task,color,colorName,false);
            todoViewModel.insert(newTask);

            Toast.makeText(this,"Task saved",Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == ADD_PROJECT_REQUEST && resultCode == RESULT_OK){
            String projectName = data.getStringExtra(AddProjectActivity.EXTRA_NAME);
            int colorCode = data.getIntExtra(AddProjectActivity.EXTRA_COLOR,-1);
            String colorName = data.getStringExtra(AddProjectActivity.EXTRA_COLOR_NAME);
            int totalTask = 0;

            Project newProject = new Project(projectName,colorCode,totalTask,colorName);
            todoViewModel.insert(newProject);
            Toast.makeText(this,"Project saved",Toast.LENGTH_SHORT).show();
        }
    }

    private void displayProjects() {
        //Project Recycler View
        RecyclerView  recyclerViewProjects = findViewById(R.id.recyclerViewProjects);
        recyclerViewProjects.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewProjects.setHasFixedSize(true);

        final ProjectAdapter projectAdapter = new ProjectAdapter();
        recyclerViewProjects.setAdapter(projectAdapter);

        todoViewModel.getAllProjects().observe(this, new Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> projects) {
                projectAdapter.setProject(projects);
                Toast.makeText(MainActivity.this,"Change Observed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayDailyTask(){
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Tasks"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed"));
        tabLayout.addTab(tabLayout.newTab().setText("All Tasks"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.viewPager);
        final PageViewAdapter pageViewAdapter = new PageViewAdapter(getSupportFragmentManager(),tabLayout.getTabCount());

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


}
