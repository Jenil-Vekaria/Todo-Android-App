package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.Dialogs.ColorPicker;

public class AddTaskActivity extends AppCompatActivity implements ColorPicker.ColorPickerListener {

    public static final String EXTRA_ID = "com.example.todolist.EXTRA_ID";
    public static final String EXTRA_TASK = "com.example.todolist.EXTRA_TASK";
    public static final String EXTRA_PROJECT = "com.example.todolist.EXTRA_PROJECT";
    public static final String EXTRA_COLOR = "com.example.todolist.EXTRA_COLOR";
    public static final String EXTRA_COLOR_NAME = "com.example.todolist.EXTRA_COLOR_NAME";

    private EditText taskDescription;
    private TextView selectProject;
    private TextView selectColor;
    private Button colorDot;
    private Button save;


    private int dotColor;
    private String colorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        initComponents();
        listener();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            Toast.makeText(this, "ID:" + intent.getIntExtra(EXTRA_ID,-1), Toast.LENGTH_SHORT).show();
            taskDescription.setText(intent.getStringExtra(EXTRA_TASK));
            colorDot.setBackgroundTintList(ColorStateList.valueOf(intent.getIntExtra(EXTRA_COLOR,-1)));
            selectColor.setText(intent.getStringExtra(EXTRA_COLOR_NAME));

            dotColor = intent.getIntExtra(EXTRA_COLOR,-1);
            colorName = intent.getStringExtra(EXTRA_COLOR_NAME);

        }else{
            setTitle("Add Task");
        }



    }

    private void listener(){
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
        selectColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicker colorPicker = new ColorPicker(dotColor,colorName);
                colorPicker.show(getSupportFragmentManager(),"Color Picker");
            }
        });
    }

    private void initComponents() {
        taskDescription = findViewById(R.id.taskDescription);
        selectProject = findViewById(R.id.selectProject);
        selectColor = findViewById(R.id.selectColor);
        colorDot = findViewById(R.id.dot);
        save = findViewById(R.id.button_save);

        dotColor = getResources().getColor(R.color.blue);
        colorName = "Default Color";
    }

    public void saveTask(){
        String task = taskDescription.getText().toString();
        String project = selectProject.getText().toString();
        int color = dotColor;

        if(task.trim().isEmpty()) {
            Toast.makeText(AddTaskActivity.this, "Please enter a task", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TASK,task);
        data.putExtra(EXTRA_PROJECT,project);
        data.putExtra(EXTRA_COLOR,color);
        data.putExtra(EXTRA_COLOR_NAME,colorName);

        int id = getIntent().getIntExtra(EXTRA_ID,-1);
        if(id != -1){
            data.putExtra(EXTRA_ID,id);
        }
        setResult(RESULT_OK,data);
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
