package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.Dialogs.ColorPicker;
import com.example.todolist.Entity.Project;

import java.util.ArrayList;
import java.util.List;

public class AddTaskActivity extends AppCompatActivity implements ColorPicker.ColorPickerListener {

    public static final String EXTRA_ID = "com.example.todolist.EXTRA_ID";
    public static final String EXTRA_IS_DONE = "com.example.todolist.EXTRA_IS_DONE";
    public static final String EXTRA_TASK = "com.example.todolist.EXTRA_TASK";
    public static final String EXTRA_PROJECTID = "com.example.todolist.EXTRA_PROJECT";
    public static final String EXTRA_COLOR = "com.example.todolist.EXTRA_COLOR";
    public static final String EXTRA_COLOR_NAME = "com.example.todolist.EXTRA_COLOR_NAME";

    private EditText taskDescription;
    private Spinner selectProject;
    private TextView selectColor;
    private Button colorDot;
    private Button save;

    private boolean isDone;
    private int dotColor;
    private String colorName;
    private int selectedProjectID;
    private int currentProjectSelectionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        initComponents();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            taskDescription.setText(intent.getStringExtra(EXTRA_TASK));
            colorDot.setBackgroundTintList(ColorStateList.valueOf(intent.getIntExtra(EXTRA_COLOR, -1)));
            selectColor.setText(intent.getStringExtra(EXTRA_COLOR_NAME));

            isDone = intent.getBooleanExtra(EXTRA_IS_DONE, false);
            dotColor = intent.getIntExtra(EXTRA_COLOR, -1);
            colorName = intent.getStringExtra(EXTRA_COLOR_NAME);

            selectedProjectID = intent.getIntExtra(EXTRA_PROJECTID, -1);

        } else {
            setTitle("Add Task");
        }

        listener();
        displayProjectList();


    }

    public void initComponents() {
        taskDescription = findViewById(R.id.taskDescription);
        selectProject = findViewById(R.id.selectProject);
        selectColor = findViewById(R.id.selectColor);
        colorDot = findViewById(R.id.dot);
        save = findViewById(R.id.button_save);

        dotColor = getResources().getColor(R.color.greenDarnerTail);
        colorName = "Default Color";
    }

    public void listener() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
        selectColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicker colorPicker = new ColorPicker(dotColor, colorName);
                colorPicker.show(getSupportFragmentManager(), "Color Picker");
            }
        });
    }


    public void displayProjectList() {

        List<String> list = new ArrayList<>();
        list.add("Select Project");

        Bundle data = getIntent().getExtras();

        assert data != null;
        final List<Project> allProjects = data.getParcelableArrayList("ALL_PROJECTS");

        if(data != null){
            for (int i = 0; i < allProjects.size(); i++) {

                Project project = allProjects.get(i);
                list.add(project.getProjectName());

                if(project.getProjectID() == selectedProjectID){
                    currentProjectSelectionIndex = i+1;
                }


            }
        }




        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner, list);
        selectProject.setAdapter(adapter);
        final List<Project> finalAllProjects = allProjects;
        selectProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position > 0 && allProjects != null)
                    selectedProjectID = allProjects.get(position-1).getProjectID();
                else
                    selectedProjectID = -1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });




        /* This is for edit mode
           Set the select project to the Project Name
        * */
        selectProject.setSelection(currentProjectSelectionIndex);
    }


    public void saveTask() {
        String task = taskDescription.getText().toString();
        int projectID = selectedProjectID;
        int color = dotColor;

        if (task.trim().isEmpty()) {
            Toast.makeText(AddTaskActivity.this, "Please enter a task", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TASK, task);
        data.putExtra(EXTRA_PROJECTID, projectID);
        data.putExtra(EXTRA_COLOR, color);
        data.putExtra(EXTRA_COLOR_NAME, colorName);
        data.putExtra(EXTRA_IS_DONE, isDone);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }


    @Override
    public void getColor(int chosenColor, String colorName) {
        this.dotColor = chosenColor;
        this.colorName = colorName;

        colorDot.setBackgroundTintList(ColorStateList.valueOf(dotColor));
        selectColor.setText(this.colorName);
    }
}
