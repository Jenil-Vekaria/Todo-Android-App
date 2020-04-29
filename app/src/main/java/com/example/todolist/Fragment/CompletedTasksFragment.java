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


public class CompletedTasksFragment extends Fragment {

    public static final int EDIT_TASK_REQUEST = 2;
    private RecyclerView completedTaskRecylerView;
    private TodoViewModel todoViewModel;

    private List<Project> allProjects;

    private Task removedTask;

    private int projectID;

    private LinearLayout noTaskDisplay;


    public CompletedTasksFragment(int projectIDViewMode){
        this.projectID = projectIDViewMode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_completed_tasks, container, false);

        completedTaskRecylerView = view.findViewById(R.id.completedTaskRecyclerView);

        todoViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(TodoViewModel.class);

        noTaskDisplay = view.findViewById(R.id.no_tasks);


        displayCompletedTasks(view);

        return view;
    }

    public void displayNoTaskMessage(boolean display){
        if(display)
            noTaskDisplay.setVisibility(LinearLayout.VISIBLE);
        else
            noTaskDisplay.setVisibility(LinearLayout.INVISIBLE);
    }

    private void displayCompletedTasks(final View view) {

//      Set the Layout to vertical scroll
        completedTaskRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        completedTaskRecylerView.setHasFixedSize(true);

//      Get the displaying task adapter
        final TaskAdapter adapter = new TaskAdapter(projectID);
//      Attach the adapter to recyclerview
        completedTaskRecylerView.setAdapter(adapter);

//      This is so live displaying
        todoViewModel.getAllCompletedTasks().observe(this, new Observer<List<Task>>() {
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
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removedTask = adapter.getTaskAt(viewHolder.getAdapterPosition());

//              SWIPE RIGHT - INCOMPLETE TASK
                if(direction == ItemTouchHelper.RIGHT){
                    removedTask.setDone(false);
                    todoViewModel.update(removedTask);
                    Toast.makeText(getContext(),"Task Incomplete",Toast.LENGTH_SHORT).show();
                }
//              SWIPE LEFT - DELETE TASK
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
        }).attachToRecyclerView(completedTaskRecylerView);

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

            int id = data.getIntExtra(AddTaskActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(getContext(), "Task not saved", Toast.LENGTH_SHORT).show();
                return;
            }

            String task = data.getStringExtra(AddTaskActivity.EXTRA_TASK);
            final int projectID = data.getIntExtra(AddTaskActivity.EXTRA_PROJECTID,-1);
            int color = data.getIntExtra(AddTaskActivity.EXTRA_COLOR, Color.parseColor("#74B9FF"));
            String colorName = data.getStringExtra(AddTaskActivity.EXTRA_COLOR_NAME);

            Task newTask = new Task(projectID, task, color,colorName,true);
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
