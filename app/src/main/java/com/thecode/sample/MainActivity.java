package com.thecode.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.thecode.aestheticdialogs.AestheticDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String errorMessage, successMessage, warningMessage, infoMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorMessage = "A failure occurred during registration";
        successMessage = "The message was sent successfully!";
        infoMessage = "Your request has been updated";
        warningMessage = "Please verify that you have completed all fields";

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
                AestheticDialog.showConnectify(this,"Connection successfully restored", AestheticDialog.SUCCESS);
                break;
            case R.id.btn_connectify_dialog_error:
                AestheticDialog.showConnectify(this, "Internet connection has been interrupted", AestheticDialog.ERROR);
                break;
            case R.id.btn_toaster_dialog_error:
                AestheticDialog.showToaster(this, "Error", errorMessage, AestheticDialog.ERROR);
                break;
            case R.id.btn_toaster_dialog_success:
                AestheticDialog.showToaster(this, "Success", successMessage, AestheticDialog.SUCCESS);
                break;
            case R.id.btn_toaster_dialog_warning:
                AestheticDialog.showToaster(this, "Warning", warningMessage, AestheticDialog.WARNING);
                break;
            case R.id.btn_toaster_dialog_info:
                AestheticDialog.showToaster(this, "Info", infoMessage, AestheticDialog.INFO);
                break;
            case R.id.btn_drake_dialog_success:
                AestheticDialog.showDrake(this, AestheticDialog.SUCCESS);
                break;
            case R.id.btn_drake_dialog_error:
                AestheticDialog.showDrake(this, AestheticDialog.ERROR);
                break;
            case R.id.btn_emoji_dialog_success:
                AestheticDialog.showEmoji(this,"Success", successMessage, AestheticDialog.SUCCESS);
                break;
            case R.id.btn_emoji_dialog_error:
                AestheticDialog.showEmoji(this, "Error", errorMessage, AestheticDialog.ERROR);
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

        }
    }
}
