package com.thecode.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.thecode.aestheticdialogs.AestheticDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String errorMessage, successMessage, warningMessage, infoMessage;
    RadioButton rbEmojiLight, rbConnectifyLight, rbToasterLight, rbFlatLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorMessage = "A failure occurred during registration";
        successMessage = "The message was sent successfully!";
        infoMessage = "Your request has been updated";
        warningMessage = "Please verify that you have completed all fields";

        rbEmojiLight = findViewById(R.id.rb_emoji_light);
        rbConnectifyLight = findViewById(R.id.rb_connectify_light);
        rbToasterLight = findViewById(R.id.rb_toaster_light);
        rbFlatLight = findViewById(R.id.rb_flat_light);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_flash_dialog_success :
                AestheticDialog.showFlashDialog(this, "Success", successMessage, AestheticDialog.SUCCESS);
                break;
            case R.id.btn_flash_dialog_error:
                AestheticDialog.showFlashDialog(this, "Error", "An error occurred while sending. Try again later", AestheticDialog.ERROR);
                break;
            case R.id.btn_connectify_dialog_success:
                if (rbConnectifyLight.isChecked()){
                    AestheticDialog.showConnectify(this,"Connection successfully restored", AestheticDialog.SUCCESS);
                }else{
                    AestheticDialog.showConnectifyDark(this,"Connection successfully restored", AestheticDialog.SUCCESS);
                }
                break;
            case R.id.btn_connectify_dialog_error:
                if (rbConnectifyLight.isChecked()){
                    AestheticDialog.showConnectify(this, "Internet connection has been interrupted", AestheticDialog.ERROR);
                }else{
                    AestheticDialog.showConnectifyDark(this, "Internet connection has been interrupted", AestheticDialog.ERROR);
                }
                break;
            case R.id.btn_toaster_dialog_error:
                if (rbToasterLight.isChecked()){
                    AestheticDialog.showToaster(this, "Error", errorMessage, AestheticDialog.ERROR);
                }else{
                    AestheticDialog.showToasterDark(this, "Error", errorMessage, AestheticDialog.ERROR);
                }
                break;
            case R.id.btn_toaster_dialog_success:
                if (rbToasterLight.isChecked()) {
                    AestheticDialog.showToaster(this, "Success", successMessage, AestheticDialog.SUCCESS);
                }else{
                    AestheticDialog.showToasterDark(this, "Success", successMessage, AestheticDialog.SUCCESS);
                }
                break;
            case R.id.btn_toaster_dialog_warning:
                if (rbToasterLight.isChecked()){
                    AestheticDialog.showToaster(this, "Warning", warningMessage, AestheticDialog.WARNING);
                }else{
                    AestheticDialog.showToasterDark(this, "Warning", warningMessage, AestheticDialog.WARNING);
                }
                break;
            case R.id.btn_toaster_dialog_info:
                if (rbToasterLight.isChecked()) {
                    AestheticDialog.showToaster(this, "Info", infoMessage, AestheticDialog.INFO);
                }else{
                    AestheticDialog.showToasterDark(this, "Info", infoMessage, AestheticDialog.INFO);
                }
                break;
            case R.id.btn_drake_dialog_success:
                AestheticDialog.showDrake(this, AestheticDialog.SUCCESS);
                break;
            case R.id.btn_drake_dialog_error:
                AestheticDialog.showDrake(this, AestheticDialog.ERROR);
                break;
            case R.id.btn_emoji_dialog_success:
                if (rbEmojiLight.isChecked()){
                    AestheticDialog.showEmoji(this,"Success", successMessage, AestheticDialog.SUCCESS);
                }else{
                    AestheticDialog.showEmojiDark(this,"Success", successMessage, AestheticDialog.SUCCESS);
                }
                break;
            case R.id.btn_emoji_dialog_error:
                if (rbEmojiLight.isChecked()){
                    AestheticDialog.showEmoji(this, "Error", errorMessage, AestheticDialog.ERROR);
                }else{
                    AestheticDialog.showEmojiDark(this, "Error", errorMessage, AestheticDialog.ERROR);
                }
                break;
            case R.id.btn_emotion_dialog_success:
                AestheticDialog.showEmotion(this,"Success", successMessage, AestheticDialog.SUCCESS);
                break;
            case R.id.btn_emotion_dialog_error:
                AestheticDialog.showEmotion(this, "Error", errorMessage, AestheticDialog.ERROR);
                break;
            case R.id.btn_rainbow_dialog_error:
                AestheticDialog.showRainbow(this, "Error", errorMessage, AestheticDialog.ERROR);
                break;
            case R.id.btn_rainbow_dialog_success:
                AestheticDialog.showRainbow(this, "Success", successMessage, AestheticDialog.SUCCESS);
                break;
            case R.id.btn_rainbow_dialog_warning:
                AestheticDialog.showRainbow(this, "Warning", warningMessage, AestheticDialog.WARNING);
                break;
            case R.id.btn_rainbow_dialog_info:
                AestheticDialog.showRainbow(this, "Info", infoMessage, AestheticDialog.INFO);
                break;
            case R.id.btn_flat_dialog_success:
                if (rbFlatLight.isChecked()) {
                    AestheticDialog.showFlat(this, "Success", successMessage, AestheticDialog.SUCCESS);
                }else{
                    AestheticDialog.showFlatDark(this, "Success", successMessage, AestheticDialog.SUCCESS);
                }
                break;
            case R.id.btn_flat_dialog_error:
                if (rbFlatLight.isChecked()) {
                    AestheticDialog.showFlat(this, "Error", errorMessage, AestheticDialog.ERROR);
                }else{
                    AestheticDialog.showFlatDark(this, "Error", errorMessage, AestheticDialog.ERROR);
                }
                break;
            case R.id.btn_flat_dialog_warning:
                if (rbFlatLight.isChecked()) {
                    AestheticDialog.showFlat(this, "Warning", warningMessage, AestheticDialog.WARNING);
                }else{
                    AestheticDialog.showFlatDark(this, "Warning", warningMessage, AestheticDialog.WARNING);
                }
                break;
            case R.id.btn_flat_dialog_info:
                if (rbFlatLight.isChecked()) {
                    AestheticDialog.showFlat(this, "Info", infoMessage, AestheticDialog.INFO);
                }else{
                    AestheticDialog.showFlatDark(this, "Info", infoMessage, AestheticDialog.INFO);
                }
                break;

        }
    }
}
