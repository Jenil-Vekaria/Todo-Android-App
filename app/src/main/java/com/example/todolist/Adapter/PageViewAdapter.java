package com.example.todolist.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.todolist.Fragment.AllTasksFragment;
import com.example.todolist.Fragment.CompletedTasksFragment;
import com.example.todolist.Fragment.TasksFragment;

public class PageViewAdapter extends FragmentStatePagerAdapter {

    private int tabCount;
    private int projectID;

    public PageViewAdapter(FragmentManager fragmentManager, int tabCount, int projectID){
        super(fragmentManager);
        this.tabCount = tabCount;
        this.projectID = projectID;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new TasksFragment(projectID);
            case 1:
                return new CompletedTasksFragment(projectID);
            case 2:
                return new AllTasksFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
