package com.thecode.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.thecode.aestheticdialogs.AestheticDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_flash_dialog_success :
                AestheticDialog.showFlashDialog(this, "Succes", "L'operation s'est effectuée avec succes", AestheticDialog.SUCCESS);
                break;
            case R.id.btn_flash_dialog_error:
                AestheticDialog.showFlashDialog(this, "Erreur", "Une erreur s'est produit lors de l'envoi. Ressayez plus tard", AestheticDialog.ERROR);
                break;
            case R.id.btn_connectify_dialog_success:
                AestheticDialog.showConnectify(this,"Connexion retablie avec succes", AestheticDialog.SUCCESS);
                break;
            case R.id.btn_connectify_dialog_error:
                AestheticDialog.showConnectify(this, "Un erreur s'est produite lors de la connexion", AestheticDialog.ERROR);
                break;
            case R.id.btn_toaster_dialog_error:
                AestheticDialog.showToaster(this, "Erreur", "Un echec s'est produit au cours de l'enregistrement", AestheticDialog.ERROR);
                break;
            case R.id.btn_toaster_dialog_success:
                AestheticDialog.showToaster(this, "Succes", "L'envoi du message s'est effectué avec succes !", AestheticDialog.SUCCESS);
                break;
            case R.id.btn_toaster_dialog_warning:
                AestheticDialog.showToaster(this, "Avertissement", "Veuillez verifier que vous avez completé tous les champs", AestheticDialog.WARNING);
                break;
            case R.id.btn_toaster_dialog_info:
                AestheticDialog.showToaster(this, "Info", "Votre requete a été mise à jour", AestheticDialog.INFO);
                break;
            case R.id.btn_drake_dialog_success:
                AestheticDialog.showDrake(this, AestheticDialog.SUCCESS);
                break;
            case R.id.btn_drake_dialog_error:
                AestheticDialog.showDrake(this, AestheticDialog.ERROR);
                break;
            case R.id.btn_emoji_dialog_success:
                AestheticDialog.showEmoji(this,"Succes", "L'envoi du message s'est effectué avec succes !", AestheticDialog.SUCCESS);
                break;
            case R.id.btn_emoji_dialog_error:
                AestheticDialog.showEmoji(this, "Erreur", "Un echec s'est produit au cours de l'enregistrement", AestheticDialog.ERROR);
                break;
            case R.id.btn_emotion_dialog_success:
                AestheticDialog.showEmotion(this,"Succes", "L'envoi du message s'est effectué avec succes !", AestheticDialog.SUCCESS);
                break;
            case R.id.btn_emotion_dialog_error:
                AestheticDialog.showEmotion(this, "Erreur", "Un echec s'est produit au cours de l'enregistrement", AestheticDialog.ERROR);
                break;

        }
    }
}
