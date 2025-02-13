package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.Dialogs.ColorPicker;

public class AddProjectActivity extends AppCompatActivity implements ColorPicker.ColorPickerListener {

    public static final String EXTRA_ID = "com.example.todolist.EXTRA_ID";
    public static final String EXTRA_NAME = "com.example.todolist.EXTRA_NAME";
    public static final String EXTRA_COLOR = "com.example.todolist.EXTRA_COLOR";
    public static final String EXTRA_COLOR_NAME = "com.example.todolist.EXTRA_COLOR_NAME";

    private EditText projectDescription;
    private TextView selectColor;
    private Button save;

    private Button dotColor;

    private int colorCode;
    private String colorName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);


        initComponents();
        listener();

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        if(intent.hasExtra(EXTRA_ID)){
            projectDescription.setText(intent.getStringExtra(EXTRA_NAME));
            selectColor.setText(intent.getStringExtra(EXTRA_COLOR_NAME));
            dotColor.setBackgroundTintList(ColorStateList.valueOf(intent.getIntExtra(EXTRA_COLOR,-1)));

            colorCode = intent.getIntExtra(EXTRA_COLOR,-1);
            colorName = intent.getStringExtra(EXTRA_COLOR_NAME);
            setTitle("Edit Project");
        }else{
            setTitle("Add Project");
        }
    }

    public void initComponents(){
        projectDescription = findViewById(R.id.projectDescription);
        selectColor = findViewById(R.id.selectColor);
        save = findViewById(R.id.button_save);
        dotColor = findViewById(R.id.dot);

        colorCode = getResources().getColor(R.color.greenDarnerTail);
        colorName = "Default Color";
    }

    public void listener(){
        selectColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicker colorPicker = new ColorPicker(colorCode,colorName);
                colorPicker.show(getSupportFragmentManager(),"Color Picker");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProject();
            }
        });
    }

    private void saveProject() {

        String project_description = projectDescription.getText().toString().trim();

        if(project_description.isEmpty()) {
            Toast.makeText(AddProjectActivity.this, "Please enter a project name", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();

        data.putExtra(EXTRA_NAME,project_description);
        data.putExtra(EXTRA_COLOR,colorCode);
        data.putExtra(EXTRA_COLOR_NAME,colorName);

        int id = getIntent().getIntExtra(EXTRA_ID,-1);
        if(id != -1)
            data.putExtra(EXTRA_ID,id);

        setResult(RESULT_OK,data);
        finish();

    }


    @Override
    public void getColor(int chosenColor, String colorName) {
        colorCode = chosenColor;
        this.colorName = colorName;

        selectColor.setText(colorName);
        dotColor.setBackgroundTintList(ColorStateList.valueOf(chosenColor));
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()){
//            case android.R.id.home:
//                finish();
//                return true;
//            default:
//                return false;
//        }
//    }
}
