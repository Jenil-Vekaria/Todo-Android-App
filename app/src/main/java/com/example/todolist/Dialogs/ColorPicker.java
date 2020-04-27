package com.example.todolist.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.todolist.R;

public class ColorPicker extends AppCompatDialogFragment {

    private int chosenColor;
    private String colorName;
    private ColorPickerListener listener;

    public ColorPicker(int chosenColor, String colorName){
        this.chosenColor = chosenColor;
        this.colorName = colorName;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.color_picker,null);

        builder.setView(view)
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })
        .setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.getColor(chosenColor,colorName);
            }
        });

        colorListen(view);

        return builder.create();
    }

    public void colorListen(View view){

        final TextView green = view.findViewById(R.id.green);
        final TextView red = view.findViewById(R.id.red);
        final TextView defaultColor = view.findViewById(R.id.defaultColor);
        final TextView yellow = view.findViewById(R.id.yellow);
        final TextView purple = view.findViewById(R.id.purple);
        final TextView black = view.findViewById(R.id.black);
        final TextView orange = view.findViewById(R.id.orange);
        final TextView pink = view.findViewById(R.id.pink);

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenColor = getResources().getColor(R.color.green);
                colorName = "Green";
                green.setTypeface(null, Typeface.BOLD);
            }
        });
        
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenColor = getResources().getColor(R.color.red);
                colorName = "Red";
                red.setTypeface(null, Typeface.BOLD);
            }
        });

        defaultColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenColor = getResources().getColor(R.color.blue);
                colorName = "Default Color";
                defaultColor.setTypeface(null, Typeface.BOLD);
            }
        });

        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenColor = getResources().getColor(R.color.yellow);
                colorName = "Yellow";
                yellow.setTypeface(null, Typeface.BOLD);
            }
        });
        
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenColor = getResources().getColor(R.color.purple);
                colorName = "Purple";
                purple.setTypeface(null, Typeface.BOLD);
            }
        });

        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenColor = getResources().getColor(R.color.black);
                colorName = "Black";
                black.setTypeface(null, Typeface.BOLD);
            }
        });

        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenColor = getResources().getColor(R.color.orange);
                colorName = "Orange";
                orange.setTypeface(null, Typeface.BOLD);
            }
        });

        pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenColor = getResources().getColor(R.color.pink);
                colorName = "Pink";
                pink.setTypeface(null, Typeface.BOLD);
            }
        });

        listener.getColor(chosenColor,colorName);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (ColorPickerListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + "must implement ExampleDialogListener");
        }

    }

    public interface ColorPickerListener{
        void getColor(int chosenColor, String colorName);
    }
}
