package com.example.todolist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.todolist.Adapter.TaskAdapter;
import com.example.todolist.Entity.Task;
import com.example.todolist.ViewModel.TodoViewModel;

import java.util.List;


public class TasksFragment extends Fragment {

    private TodoViewModel todoViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        todoViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(TodoViewModel.class);

        displayDailyTasks(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void displayDailyTasks(View view) {
        //Daily Task Recycler View
        RecyclerView recyclerViewTask = view.findViewById(R.id.tasksRecyclerView);
        recyclerViewTask.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewTask.setHasFixedSize(true);

        final TaskAdapter adapter = new TaskAdapter();
        recyclerViewTask.setAdapter(adapter);

        todoViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                //Update the Recylerview here
                adapter.setNotes(tasks);
            }
        });
    }
}
