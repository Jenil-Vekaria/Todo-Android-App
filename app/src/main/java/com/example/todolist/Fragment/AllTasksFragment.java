package com.example.todolist.Fragment;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.LinearLayout;
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


public class AllTasksFragment extends Fragment {

    public static final int EDIT_TASK_REQUEST = 2;

    private RecyclerView allTasksRecyclerView;
    private TodoViewModel todoViewModel;

    private List<Project> allProjects;

    private Task removedTask;

    private LinearLayout noTaskDisplay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_tasks, container, false);
        todoViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(TodoViewModel.class);

        noTaskDisplay = view.findViewById(R.id.no_tasks);

        displayAllTasks(view);

        // Inflate the layout for this fragment
        return view;
    }

    public void displayNoTaskMessage(boolean display){
        if(display)
            noTaskDisplay.setVisibility(LinearLayout.VISIBLE);
        else
            noTaskDisplay.setVisibility(LinearLayout.INVISIBLE);
    }

    private void displayAllTasks(final View view){

//      Get RecyclerView
        allTasksRecyclerView = view.findViewById(R.id.allTasksRecyclerView);
        allTasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allTasksRecyclerView.setHasFixedSize(true);

//      Task Adapter
        final TaskAdapter adapter = new TaskAdapter(-1);
//      Attack adapter to RecyclerView
        allTasksRecyclerView.setAdapter(adapter);

//      Live rendering
        todoViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.setTasks(tasks);

                if(adapter.getItemCount() > 0)
                    displayNoTaskMessage(false);
                else
                    displayNoTaskMessage(true);

            }
        });

        todoViewModel.getAllProjects().observe(this, new Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> projects) {
                allProjects = projects;
            }
        });



//      Swiping operation
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removedTask = adapter.getTaskAt(viewHolder.getAdapterPosition());

//              SWIPE LEFT - DELETE
                todoViewModel.delete(removedTask);
                Snackbar.make(view, "Task Deleted", Snackbar.LENGTH_SHORT).setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        todoViewModel.insert(removedTask);
                    }
                }).show();
            }
        }).attachToRecyclerView(allTasksRecyclerView);

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
                intent.putExtra(AddTaskActivity.EXTRA_IS_DONE,task.isDone());
                intent.putExtras(data);
                startActivityForResult(intent, EDIT_TASK_REQUEST);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EDIT_TASK_REQUEST && resultCode == RESULT_OK){

            int id = data.getIntExtra(AddTaskActivity.EXTRA_ID,-1);

            if(id == -1){
                Toast.makeText(getContext(), "Task not saved", Toast.LENGTH_SHORT).show();
                return;
            }

            String task = data.getStringExtra(AddTaskActivity.EXTRA_TASK);
            final int projectID = data.getIntExtra(AddTaskActivity.EXTRA_PROJECTID,-1);
            int color = data.getIntExtra(AddTaskActivity.EXTRA_COLOR, Color.parseColor("#74B9FF"));
            String colorName = data.getStringExtra(AddTaskActivity.EXTRA_COLOR_NAME);
            boolean isDone = data.getBooleanExtra(AddTaskActivity.EXTRA_IS_DONE,false);

            Task newTask = new Task(projectID, task, color,colorName,isDone);
            newTask.setTaskID(id);
            todoViewModel.update(newTask);

//          If task added to project, update project totalTask count
            todoViewModel.getAllProjectTaskCount(projectID).observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    updateProjectTaskCount(projectID,integer.intValue());
                }
            });

            Toast.makeText(getContext(), "Task updated", Toast.LENGTH_SHORT).show();

        }
    }
}
