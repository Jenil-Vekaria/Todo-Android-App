package com.example.todolist.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.todolist.AllTasksFragment;
import com.example.todolist.CompletedTasksFragment;
import com.example.todolist.TasksFragment;

public class PageViewAdapter extends FragmentStatePagerAdapter {

    private int tabCount;

    public PageViewAdapter(FragmentManager fragmentManager, int tabCount){
        super(fragmentManager);
        this.tabCount = tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new TasksFragment();
            case 1:
                return new CompletedTasksFragment();
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
