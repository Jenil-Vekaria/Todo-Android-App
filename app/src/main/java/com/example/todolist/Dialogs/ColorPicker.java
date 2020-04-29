package com.example.todolist.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.todolist.R;

public class ColorPicker extends AppCompatDialogFragment {

    private int selectedColorCode;
    private String selectedColorName;
    private ColorPickerListener listener;
    private View view;

    private RadioGroup colorOptionGroup;
    private AlertDialog dialog;

    public ColorPicker(int chosenColor, String colorName){
        this.selectedColorCode = chosenColor;
        this.selectedColorName = colorName;
    }

    public void setUpColorOption(){
        LinearLayout layout = view.findViewById(R.id.displayColor);

        colorOptionGroup = new RadioGroup(getContext());
        colorOptionGroup.setPadding(15,15,15,15);
        colorOptionGroup.setOrientation(RadioGroup.VERTICAL);

        final int colorCode[] = getResources().getIntArray(R.array.colorCode);
        final String colorName[] = getResources().getStringArray(R.array.colorName);

        for(int i = 0; i < colorCode.length; i++){
            RadioButton btn = new RadioButton(getContext());

            if(i > 0) {
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 30, 0, 0);
                btn.setLayoutParams(params);
            }

            btn.setText(colorName[i]);
            btn.setPadding(15,10,0,0);
            btn.setTextSize(15);
            btn.setTextColor(getResources().getColor(R.color.cityLights));
//            btn.setTypeface(getResources().getFont(R.font.myfont));
//            btn.setBackgroundTintList(ColorStateList.valueOf(colorCode[i]));
            btn.setButtonTintList(ColorStateList.valueOf(colorCode[i]));
            colorOptionGroup.addView(btn);
        }

        colorOptionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedButton = group.findViewById(checkedId);
                //Get Color Name
                selectedColorName = checkedButton.getText().toString();
                //Get Color Code
                for(int i = 0; i < colorName.length; i++){
                    if(selectedColorName.equals(colorName[i])){
                        selectedColorCode = colorCode[i];
                        break;
                    }
                }
                listener.getColor(selectedColorCode,selectedColorName);
                dialog.hide();
            }
        });

        layout.addView(colorOptionGroup);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.color_picker,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        dialog = builder.create();

        setUpColorOption();

        return dialog;
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
