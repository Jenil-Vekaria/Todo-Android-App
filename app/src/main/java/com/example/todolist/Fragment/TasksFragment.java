package com.example.todolist.Fragment;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.todolist.Adapter.TaskAdapter;
import com.example.todolist.AddTaskActivity;
import com.example.todolist.Entity.Project;
import com.example.todolist.Entity.Task;
import com.example.todolist.R;
import com.example.todolist.ViewModel.TodoViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class TasksFragment extends Fragment {

    public static final int EDIT_TASK_REQUEST = 2;

    private TodoViewModel todoViewModel;

    private Task removedTask;

    private List<Project> allProjects;

    private int projectID;

    public TasksFragment(int projectIDViewMode){
        this.projectID = projectIDViewMode;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        todoViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(TodoViewModel.class);

        displayDailyTasks(view);

        // Inflate the layout for this fragment
        return view;
    }



    private void displayDailyTasks(final View view) {
        //Daily Task Recycler View
        RecyclerView recyclerViewTask = view.findViewById(R.id.tasksRecyclerView);
        recyclerViewTask.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewTask.setHasFixedSize(true);

        final TaskAdapter adapter = new TaskAdapter(projectID);
        recyclerViewTask.setAdapter(adapter);

        todoViewModel.getAllIncompletedTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                //Update the RecyclerView here
                adapter.setTasks(tasks);
            }
        });

        todoViewModel.getAllProjects().observe(this, new Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> projects) {
                allProjects = projects;
            }
        });


//      SWIPING OPERATION
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                removedTask = adapter.getTaskAt(viewHolder.getAdapterPosition());

//              Task Complete - SWIPE RIGHT
                if(direction == ItemTouchHelper.RIGHT){
                    removedTask.setDone(true);
                    todoViewModel.update(removedTask);
                    Toast.makeText(getContext(),"Task Completed",Toast.LENGTH_SHORT).show();
                }
//              Delete Task - SWIPE LEFT
                else{
                    todoViewModel.delete(removedTask);
                    Snackbar.make(view, "Task Deleted", Snackbar.LENGTH_SHORT).setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            todoViewModel.insert(removedTask);
                        }
                    }).show();
                }
            }
        }).attachToRecyclerView(recyclerViewTask);

//      FOR EDITING EACH TASK WHEN USER CLICKS ON IT
        adapter.setOnItemClickListener(new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onTaskClick(Task task) {

                Bundle data = new Bundle();

                data.putParcelableArrayList("ALL_PROJECTS", (ArrayList<? extends Parcelable>) allProjects);

                Intent intent = new Intent(getContext(), AddTaskActivity.class);
                intent.putExtra(AddTaskActivity.EXTRA_ID, task.getTaskID());
                intent.putExtra(AddTaskActivity.EXTRA_TASK, task.getNote());
                intent.putExtra(AddTaskActivity.EXTRA_COLOR, task.getColor());
                intent.putExtra(AddTaskActivity.EXTRA_PROJECTID, task.getProjectID());
                intent.putExtra(AddTaskActivity.EXTRA_COLOR_NAME,task.getColorName());
                intent.putExtras(data);
                startActivityForResult(intent, EDIT_TASK_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_TASK_REQUEST && resultCode == RESULT_OK) {

            int id = data.getIntExtra(AddTaskActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(getContext(), "Task not saved", Toast.LENGTH_SHORT).show();
                return;
            }

            String task = data.getStringExtra(AddTaskActivity.EXTRA_TASK);
            int projectID = data.getIntExtra(AddTaskActivity.EXTRA_PROJECTID,-1);
            int color = data.getIntExtra(AddTaskActivity.EXTRA_COLOR, Color.parseColor("#74B9FF"));
            String colorName = data.getStringExtra(AddTaskActivity.EXTRA_COLOR_NAME);

            Task newTask = new Task(projectID, task, color,colorName,false);
            newTask.setTaskID(id);
            todoViewModel.update(newTask);

            Toast.makeText(getContext(), "Task updated", Toast.LENGTH_SHORT).show();

        }
    }
}
