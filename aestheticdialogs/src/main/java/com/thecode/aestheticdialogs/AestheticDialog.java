package com.thecode.aestheticdialogs;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Aesthetic Dialog class
 * Call methods to display different styles of dialogs
 *
 * @author Gabriel The Code
 */

public class AestheticDialog {


    public final static String SUCCESS  = "DIALOG_SUCCESS";
    public final static String ERROR  = "DIALOG_ERROR";
    public final static String WARNING  = "DIALOG_WARNING";
    public final static String INFO  = "DIALOG_INFO";



    /**
     * Shows Connectify Dialog.
     *
     *
     * @param activity
     * @param title
     * @param message
     * @param dialogType
     */
    public static void showConnectify(Activity activity, String title, String message, String dialogType){

        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(activity);
        View layoutView;
        if(dialogType.equals(AestheticDialog.SUCCESS)){
            layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_connectify_success, null);
        }else {
            layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_connectify_error, null);
        }
        AppCompatImageView imgClose = layoutView.findViewById(R.id.image_close);
        AppCompatTextView textTitle = layoutView.findViewById(R.id.text_title);
        AppCompatTextView textMessage = layoutView.findViewById(R.id.text_message);
        textTitle.setText(title);
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


    /**
     * Shows Toaster Dialog.
     *
     *
     * @param activity
     * @param title
     * @param message
     * @param dialogType
     */
    public static void showToaster(Activity activity, String title, String message, String dialogType){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(activity);
        View layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_toaster, null);
        AppCompatImageView imgClose = layoutView.findViewById(R.id.image_close);
        AppCompatImageView icon = layoutView.findViewById(R.id.dialog_icon);
        AppCompatTextView textTitle = layoutView.findViewById(R.id.text_title);
        AppCompatTextView textMessage = layoutView.findViewById(R.id.text_message);
        View verticalView = layoutView.findViewById(R.id.vertical_view);
        textMessage.setText(message);
        textTitle.setText(title);

        switch (dialogType){
            case ERROR:
                verticalView.setBackgroundColor(activity.getResources().getColor(R.color.dialog_error));
                icon.setImageResource(R.drawable.ic_error_red_24dp);
                break;
            case SUCCESS:
                verticalView.setBackgroundColor(activity.getResources().getColor(R.color.dialog_success));
                icon.setImageResource(R.drawable.ic_check_circle_green_24dp);
                break;
            case WARNING:
                verticalView.setBackgroundColor(activity.getResources().getColor(R.color.dialog_warning));
                icon.setImageResource(R.drawable.ic_warning_orange_24dp);
                break;
            case INFO:
                verticalView.setBackgroundColor(activity.getResources().getColor(R.color.dialog_info));
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

    /**
     * Shows Emoji Dialog.
     *
     *
     * @param activity
     * @param title
     * @param message
     * @param dialogType
     */
    public static void showEmoji(Activity activity, String title, String message, String dialogType){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(activity);
        View layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_emoji, null);
        AppCompatImageView imgClose = layoutView.findViewById(R.id.image_close);
        AppCompatTextView textTitle = layoutView.findViewById(R.id.text_title);
        AppCompatTextView textMessage = layoutView.findViewById(R.id.text_message);
        AppCompatImageView icon = layoutView.findViewById(R.id.dialog_icon);

        if(dialogType.equals(AestheticDialog.SUCCESS)){
            textTitle.setTextColor(activity.getResources().getColor(R.color.dialog_success));
            icon.setImageResource(R.drawable.thumbs_up_sign);
        }else {
            textTitle.setTextColor(activity.getResources().getColor(R.color.dialog_error));
            icon.setImageResource(R.drawable.man_shrugging);
        }

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

    /**
     * Shows Rainbow Dialog.
     *
     *
     * @param activity
     * @param title
     * @param message
     * @param dialogType
     */
    public static void showRainbow(Activity activity, String title, String message, String dialogType){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(activity);
        View layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_rainbow, null);
        AppCompatImageView icon = layoutView.findViewById(R.id.dialog_icon);
        RelativeLayout layoutDialog = layoutView.findViewById(R.id.dialog_layout);

        switch (dialogType){
            case ERROR:
                layoutDialog.setBackgroundColor(activity.getResources().getColor(R.color.dialog_error));
                icon.setImageResource(R.drawable.ic_error_red_24dp);
                break;
            case SUCCESS:
                layoutDialog.setBackgroundColor(activity.getResources().getColor(R.color.dialog_success));
                icon.setImageResource(R.drawable.ic_check_circle_green_24dp);
                break;
            case WARNING:
                layoutDialog.setBackgroundColor(activity.getResources().getColor(R.color.dialog_warning));
                icon.setImageResource(R.drawable.ic_warning_orange_24dp);
                break;
            case INFO:
                layoutDialog.setBackgroundColor(activity.getResources().getColor(R.color.dialog_info));
                icon.setImageResource(R.drawable.ic_info_blue_24dp);
                break;

        }

        AppCompatImageView imgClose = layoutView.findViewById(R.id.image_close);
        AppCompatTextView textTitle = layoutView.findViewById(R.id.text_title);
        AppCompatTextView textMessage = layoutView.findViewById(R.id.text_message);
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

    /**
     * Shows Flash Dialog.
     *
     *
     * @param activity
     * @param title
     * @param message
     * @param dialogType
     */
    public static void showFlashDialog(Activity activity, String title, String message, String dialogType){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(activity);
        View layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_flash, null);
        AppCompatButton btnOk = layoutView.findViewById(R.id.btn_action);
        AppCompatTextView textTitle = layoutView.findViewById(R.id.dialog_title);
        AppCompatTextView textMessage = layoutView.findViewById(R.id.dialog_message);
        FrameLayout dialogFrame = layoutView.findViewById(R.id.dialog_frame);
        AppCompatImageView icon = layoutView.findViewById(R.id.img_icon);

        if(dialogType.equals(AestheticDialog.SUCCESS)){
            dialogFrame.setBackgroundResource(R.drawable.rounded_green_gradient_bg);
            icon.setImageResource(R.drawable.circle_validation_success);
        }else {
            dialogFrame.setBackgroundResource(R.drawable.rounded_red_gradient_bg);
            icon.setImageResource(R.drawable.circle_validation_error);
        }

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

    /**
     * Shows Emotion Dialog.
     *
     *
     * @param activity
     * @param title
     * @param message
     * @param dialogType
     */
    public static void showEmotion(Activity activity, String title, String message, String dialogType){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(activity);
        View  layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_emotion, null);
        AppCompatImageView icon = layoutView.findViewById(R.id.img_icon);
        RelativeLayout layoutDialog = layoutView.findViewById(R.id.dialog_layout);
        AppCompatTextView textTitle = layoutView.findViewById(R.id.dialog_title);
        AppCompatTextView textMessage = layoutView.findViewById(R.id.dialog_message);
        AppCompatTextView textHour = layoutView.findViewById(R.id.dialog_hour);

        if(dialogType.equals(AestheticDialog.SUCCESS)){
           icon.setImageResource(R.drawable.smiley_success);
           layoutDialog.setBackgroundResource(R.drawable.background_emotion_success);
        }else {
            icon.setImageResource(R.drawable.smiley_error);
            layoutDialog.setBackgroundResource(R.drawable.background_emotion_error);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String hour = sdf.format(Calendar.getInstance().getTime());
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

    /**
     * Shows Drake Dialog.
     *
     *
     * @param activity
     * @param dialogType
     */
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
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,height);

    }


    /**
     * Shows Flat Dialog.
     *
     *
     * @param activity
     * @param title
     * @param message
     * @param dialogType
     */
    public static void showFlat(Activity activity, String title, String message, String dialogType){

        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(activity);
        View layoutView;
        layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_flat, null);
        AppCompatButton btnOk = layoutView.findViewById(R.id.btn_action);
        AppCompatTextView textTitle = layoutView.findViewById(R.id.dialog_title);
        AppCompatTextView textMessage = layoutView.findViewById(R.id.dialog_message);
        AppCompatImageView icon = layoutView.findViewById(R.id.dialog_icon);
        LinearLayoutCompat layoutDialog = layoutView.findViewById(R.id.dialog_layout);
        FrameLayout frameLayout = layoutView.findViewById(R.id.dialog_frame);

        switch (dialogType){
            case ERROR:
                icon.setImageResource(R.drawable.ic_error_red_24dp);
                btnOk.setBackgroundResource(R.drawable.btn_red_selector);
                frameLayout.setBackgroundResource(R.drawable.rounded_rect_red);
                break;
            case SUCCESS:
                icon.setImageResource(R.drawable.ic_check_circle_green_24dp);
                btnOk.setBackgroundResource(R.drawable.btn_green_selector);
                frameLayout.setBackgroundResource(R.drawable.rounded_rect_green);
                break;
            case WARNING:
                icon.setImageResource(R.drawable.ic_warning_orange_24dp);
                btnOk.setBackgroundResource(R.drawable.btn_yellow_selector);
                frameLayout.setBackgroundResource(R.drawable.rounded_rect_yellow);
                break;
            case INFO:
                icon.setImageResource(R.drawable.ic_info_blue_24dp);
                btnOk.setBackgroundResource(R.drawable.btn_blue_selector);
                frameLayout.setBackgroundResource(R.drawable.rounded_rect_blue);
                break;
        }


        layoutDialog.setBackgroundResource(R.drawable.rounded_white_bg);
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


    /**
     * Shows Flat Dark Theme Dialog.
     *
     *
     * @param activity
     * @param title
     * @param message
     * @param dialogType
     */
    public static void showFlatDark(Activity activity, String title, String message, String dialogType){

        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(activity);
        View layoutView;
        layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_flat, null);
        AppCompatButton btnOk = layoutView.findViewById(R.id.btn_action);
        AppCompatTextView textTitle = layoutView.findViewById(R.id.dialog_title);
        AppCompatTextView textMessage = layoutView.findViewById(R.id.dialog_message);
        AppCompatImageView icon = layoutView.findViewById(R.id.dialog_icon);
        LinearLayoutCompat layoutDialog = layoutView.findViewById(R.id.dialog_layout);
        FrameLayout frameLayout = layoutView.findViewById(R.id.dialog_frame);

        switch (dialogType){
            case ERROR:
                icon.setImageResource(R.drawable.ic_error_red_24dp);
                btnOk.setBackgroundResource(R.drawable.btn_red_selector);
                frameLayout.setBackgroundResource(R.drawable.rounded_rect_red);
                break;
            case SUCCESS:
                icon.setImageResource(R.drawable.ic_check_circle_green_24dp);
                btnOk.setBackgroundResource(R.drawable.btn_green_selector);
                frameLayout.setBackgroundResource(R.drawable.rounded_rect_green);
                break;
            case WARNING:
                icon.setImageResource(R.drawable.ic_warning_orange_24dp);
                btnOk.setBackgroundResource(R.drawable.btn_yellow_selector);
                frameLayout.setBackgroundResource(R.drawable.rounded_rect_yellow);
                break;
            case INFO:
                icon.setImageResource(R.drawable.ic_info_blue_24dp);
                btnOk.setBackgroundResource(R.drawable.btn_blue_selector);
                frameLayout.setBackgroundResource(R.drawable.rounded_rect_blue);
                break;
        }


        layoutDialog.setBackgroundResource(R.drawable.rounded_dark_bg);
        textTitle.setTextColor(activity.getResources().getColor(R.color.md_white_1000));
        textMessage.setTextColor(activity.getResources().getColor(R.color.md_white_1000));
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

    /**
     * Shows Connectify Dark Theme Dialog.
     *
     *
     * @param activity
     * @param title
     * @param message
     * @param dialogType
     */
    public static void showConnectifyDark(Activity activity, String title, String message, String dialogType){

        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(activity);
        View layoutView;
        if(dialogType.equals(AestheticDialog.SUCCESS)){
            layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_connectify_success, null);
        }else {
            layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_connectify_error, null);
        }
        LinearLayoutCompat layoutDialog = layoutView.findViewById(R.id.dialog_layout);
        AppCompatImageView imgClose = layoutView.findViewById(R.id.image_close);
        AppCompatTextView textTitle = layoutView.findViewById(R.id.text_title);
        AppCompatTextView textMessage = layoutView.findViewById(R.id.text_message);
        layoutDialog.setBackgroundColor(activity.getResources().getColor(R.color.dark_background));
        textMessage.setTextColor(activity.getResources().getColor(R.color.md_white_1000));
        textTitle.setText(title);
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


    /**
     * Shows Toaster Dark Theme Dialog.
     *
     *
     * @param activity
     * @param title
     * @param message
     * @param dialogType
     */
    public static void showToasterDark(Activity activity, String title, String message, String dialogType){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(activity);
        View layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_toaster, null);
        RelativeLayout layoutDialog = layoutView.findViewById(R.id.dialog_layout);
        layoutDialog.setBackgroundColor(activity.getResources().getColor(R.color.dark_background));
        AppCompatImageView imgClose = layoutView.findViewById(R.id.image_close);
        AppCompatImageView icon = layoutView.findViewById(R.id.dialog_icon);
        AppCompatTextView textTitle = layoutView.findViewById(R.id.text_title);
        AppCompatTextView textMessage = layoutView.findViewById(R.id.text_message);
        textMessage.setTextColor(activity.getResources().getColor(R.color.md_white_1000));
        View verticalView = layoutView.findViewById(R.id.vertical_view);
        textMessage.setText(message);
        textTitle.setText(title);

        switch (dialogType){
            case ERROR:
                textTitle.setTextColor(activity.getResources().getColor(R.color.dialog_error));
                verticalView.setBackgroundColor(activity.getResources().getColor(R.color.dialog_error));
                icon.setImageResource(R.drawable.ic_error_red_24dp);
                break;
            case SUCCESS:
                textTitle.setTextColor(activity.getResources().getColor(R.color.dialog_success));
                verticalView.setBackgroundColor(activity.getResources().getColor(R.color.dialog_success));
                icon.setImageResource(R.drawable.ic_check_circle_green_24dp);
                break;
            case WARNING:
                textTitle.setTextColor(activity.getResources().getColor(R.color.dialog_warning));
                verticalView.setBackgroundColor(activity.getResources().getColor(R.color.dialog_warning));
                icon.setImageResource(R.drawable.ic_warning_orange_24dp);
                break;
            case INFO:
                textTitle.setTextColor(activity.getResources().getColor(R.color.dialog_info));
                verticalView.setBackgroundColor(activity.getResources().getColor(R.color.dialog_info));
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

    /**
     * Shows Emoji Dark Theme Dialog.
     *
     *
     * @param activity
     * @param title
     * @param message
     * @param dialogType
     */
    public static void showEmojiDark(Activity activity, String title, String message, String dialogType){
        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        dialogBuilder = new AlertDialog.Builder(activity);
        View layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_emoji, null);
        RelativeLayout layoutDialog = layoutView.findViewById(R.id.dialog_layout);
        layoutDialog.setBackgroundColor(activity.getResources().getColor(R.color.dark_background));
        AppCompatImageView imgClose = layoutView.findViewById(R.id.image_close);
        AppCompatImageView icon = layoutView.findViewById(R.id.dialog_icon);
        AppCompatTextView textTitle = layoutView.findViewById(R.id.text_title);
        AppCompatTextView textMessage = layoutView.findViewById(R.id.text_message);
        if(dialogType.equals(AestheticDialog.SUCCESS)){
            textTitle.setTextColor(activity.getResources().getColor(R.color.dialog_success));
            icon.setImageResource(R.drawable.thumbs_up_sign);
        }else {
            textTitle.setTextColor(activity.getResources().getColor(R.color.dialog_error));
            icon.setImageResource(R.drawable.man_shrugging);
        }

        textMessage.setTextColor(activity.getResources().getColor(R.color.md_white_1000));
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

}
