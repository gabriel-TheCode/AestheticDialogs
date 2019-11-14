package com.thecode.aestheticdialogs;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AestheticDialog {


    public final static String SUCCESS  = "TOASTER_SUCCESS";
    public final static String ERROR  = "TOASTER_ERROR";
    public final static String WARNING  = "TOASTER_WARNING";
    public final static String INFO  = "TOASTER_INFO";



    public static void showConnectify(Activity activity, String message, String dialogType){

        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(activity);
        View layoutView;
        if(dialogType.equals(AestheticDialog.SUCCESS)){
            layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_conectify_success, null);
        }else {
            layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_conectify_error, null);
        }
        ImageView imgClose = layoutView.findViewById(R.id.image_close);
        TextView textMessage = layoutView.findViewById(R.id.text_message);
        textMessage.setText(message);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialog.getWindow().setGravity(Gravity.TOP);
        alertDialog.show();
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        imgClose.setOnClickListener(view -> alertDialog.dismiss());
    }



    public static void showToaster(Activity activity, String title, String message, String dialogType){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(activity);
        View layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_toaster, null);
        ImageView imgClose = layoutView.findViewById(R.id.image_close);
        ImageView icon = layoutView.findViewById(R.id.dialog_icon);
        TextView textTitle = layoutView.findViewById(R.id.text_title);
        TextView textMessage = layoutView.findViewById(R.id.text_message);
        View verticalView = layoutView.findViewById(R.id.vertical_view);
        textMessage.setText(message);
        textTitle.setText(title);

        switch (dialogType){
            case ERROR:
                verticalView.setBackgroundColor(activity.getResources().getColor(R.color.toaster_error));
                icon.setImageResource(R.drawable.ic_error_red_24dp);
                break;
            case SUCCESS:
                verticalView.setBackgroundColor(activity.getResources().getColor(R.color.toaster_success));
                icon.setImageResource(R.drawable.ic_check_circle_green_24dp);
                break;
            case WARNING:
                verticalView.setBackgroundColor(activity.getResources().getColor(R.color.toaster_warning));
                icon.setImageResource(R.drawable.ic_warning_orange_24dp);
                break;
            case INFO:
                verticalView.setBackgroundColor(activity.getResources().getColor(R.color.toaster_info));
                icon.setImageResource(R.drawable.ic_info_blue_24dp);
                break;

        }

        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setGravity(Gravity.TOP);
        alertDialog.show();
        int height = activity.getResources().getDimensionPixelSize(R.dimen.popup_height_toaster);
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,height);

        imgClose.setOnClickListener(view -> alertDialog.dismiss());
    }


    public static void showEmoji(Activity activity, String title, String message, String dialogType){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(activity);
        View layoutView;

        if(dialogType.equals(AestheticDialog.SUCCESS)){
            layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_emoji_sucess, null);
        }else {
            layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_emoji_error, null);
        }
        ImageView imgClose = layoutView.findViewById(R.id.image_close);
        TextView textTitle = layoutView.findViewById(R.id.text_title);
        TextView textMessage = layoutView.findViewById(R.id.text_message);
        textMessage.setText(message);
        textTitle.setText(title);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setGravity(Gravity.TOP);
        alertDialog.show();
        int height = activity.getResources().getDimensionPixelSize(R.dimen.popup_height_emoji_dialog);
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,height);

        imgClose.setOnClickListener(view -> alertDialog.dismiss());
    }


    public static void showFlashDialog(Activity activity, String title, String message, String dialogType){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(activity);
        View layoutView;
        if(dialogType.equals(AestheticDialog.SUCCESS)){
            layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_message_success, null);
        }else {
            layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_message_error, null);
        }

        Button btnOk = layoutView.findViewById(R.id.btn_action);
        TextView textTitle = layoutView.findViewById(R.id.dialog_title);
        TextView textMessage = layoutView.findViewById(R.id.dialog_message);
        textMessage.setText(message);
        textTitle.setText(title);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.show();
        int height = activity.getResources().getDimensionPixelSize(R.dimen.popup_height);
        int width = activity.getResources().getDimensionPixelSize(R.dimen.popup_height);
        alertDialog.getWindow().setLayout(width,height);
        btnOk.setOnClickListener(view -> alertDialog.dismiss());
    }


    public static void showEmotion(Activity activity, String title, String message, String dialogType){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(activity);
        View layoutView;
        if(dialogType.equals(AestheticDialog.SUCCESS)){
            layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_emotion_success, null);
        }else {
            layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_emotion_error, null);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String hour = sdf.format(Calendar.getInstance().getTime());
        TextView textTitle = layoutView.findViewById(R.id.dialog_title);
        TextView textMessage = layoutView.findViewById(R.id.dialog_message);
        TextView textHour = layoutView.findViewById(R.id.dialog_hour);
        textMessage.setText(message);
        textTitle.setText(title);
        textHour.setText(hour);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.show();
        int height = activity.getResources().getDimensionPixelSize(R.dimen.popup_height_emotion);
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,height);

    }


    public static void showDrake(Activity activity, String dialogType){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(activity);
        View layoutView;
        if(dialogType.equals(AestheticDialog.SUCCESS)){
            layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_drake_success, null);
        }else {
            layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_drake_error, null);
        }

        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.show();
        int height = activity.getResources().getDimensionPixelSize(R.dimen.popup_height_drake);
        //int width = activity.getResources().getDimensionPixelSize(R.dimen.popup_width_drake);
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,height);

        //btnOk.setOnClickListener(view -> alertDialog.dismiss());

    }

}
