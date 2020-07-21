package com.thecode.sample

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.thecode.aestheticdialogs.AestheticDialog
import com.thecode.aestheticdialogs.DialogThemes
import com.thecode.aestheticdialogs.DialogTypes
import com.thecode.aestheticdialogs.OnDialogClickListener

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var errorMessage: String = "A failure occurred during registration"
    var successMessage: String = "The message was sent successfully!"
    var warningMessage: String = "Please verify that you have completed all fields"
    var infoMessage: String = "Your request has been updated"
    var infoTitle: String = "Info"
    var successTitle: String = "Success"
    var errorTitle: String = "Error"
    var warningTitle: String = "Warning"
    private lateinit var rbEmojiLight: RadioButton
    private lateinit var rbConnectifyLight: RadioButton
    private lateinit var rbToasterLight: RadioButton
    private lateinit var rbFlatLight: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rbEmojiLight = findViewById(R.id.rb_emoji_light)
        rbConnectifyLight = findViewById(R.id.rb_connectify_light)
        rbToasterLight = findViewById(R.id.rb_toaster_light)
        rbFlatLight = findViewById(R.id.rb_flat_light)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_flash_dialog_success -> {
                AestheticDialog.Builder(this, DialogThemes.FLASH, DialogTypes.SUCCESS)
                        .setTitle(successTitle)
                        .setMessage(successMessage)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            }
            R.id.btn_flash_dialog_error -> {
                AestheticDialog.Builder(this, DialogThemes.FLASH, DialogTypes.ERROR)
                        .setTitle(errorTitle)
                        .setMessage(errorMessage)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            }
            R.id.btn_connectify_dialog_success -> if (rbConnectifyLight.isChecked) {
               AestheticDialog.Builder(this, DialogThemes.CONNECTIFY, DialogTypes.SUCCESS)
                        .setTitle(successTitle)
                        .setMessage(successMessage)
                        .show()
            } else {
                AestheticDialog.Builder(this, DialogThemes.CONNECTIFY, DialogTypes.SUCCESS)
                        .setTitle(successTitle)
                        .setMessage(successMessage)
                        .setDarkMode(true)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            }
            R.id.btn_connectify_dialog_error -> if (rbConnectifyLight.isChecked) {
                AestheticDialog.Builder(this, DialogThemes.CONNECTIFY, DialogTypes.ERROR)
                        .setTitle(errorTitle)
                        .setMessage(errorMessage)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            } else {
                AestheticDialog.Builder(this, DialogThemes.CONNECTIFY, DialogTypes.ERROR)
                        .setTitle(errorTitle)
                        .setMessage(errorMessage)
                        .setDarkMode(true)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()

            }
            R.id.btn_toaster_dialog_error -> if (rbToasterLight.isChecked) {
                AestheticDialog.Builder(this, DialogThemes.TOASTER, DialogTypes.ERROR)
                        .setTitle(errorTitle)
                        .setMessage(errorMessage)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()

            } else {
                AestheticDialog.Builder(this, DialogThemes.TOASTER, DialogTypes.ERROR)
                        .setTitle(errorTitle)
                        .setMessage(errorMessage)
                        .setDarkMode(true)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            }
            R.id.btn_toaster_dialog_success -> if (rbToasterLight.isChecked) {
                AestheticDialog.Builder(this, DialogThemes.TOASTER, DialogTypes.SUCCESS)
                        .setTitle(successTitle)
                        .setMessage(successMessage)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            } else {
                AestheticDialog.Builder(this, DialogThemes.TOASTER, DialogTypes.SUCCESS)
                        .setTitle(successTitle)
                        .setMessage(successMessage)
                        .setDarkMode(true)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            }
            R.id.btn_toaster_dialog_warning -> if (rbToasterLight.isChecked) {
                AestheticDialog.Builder(this, DialogThemes.TOASTER, DialogTypes.WARNING)
                        .setTitle(warningTitle)
                        .setMessage(warningMessage)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            } else {
                AestheticDialog.Builder(this, DialogThemes.TOASTER, DialogTypes.WARNING)
                        .setTitle(warningTitle)
                        .setMessage(warningMessage)
                        .setDarkMode(true)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            }
            R.id.btn_toaster_dialog_info -> if (rbToasterLight.isChecked) {
                AestheticDialog.Builder(this, DialogThemes.TOASTER, DialogTypes.INFO)
                        .setTitle(infoTitle)
                        .setMessage(infoMessage)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            } else {
                AestheticDialog.Builder(this, DialogThemes.TOASTER, DialogTypes.INFO)
                        .setTitle(infoTitle)
                        .setMessage(infoMessage)
                        .setDarkMode(true)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()

            }

            R.id.btn_drake_dialog_success -> {
                AestheticDialog.Builder(this, DialogThemes.DRAKE, DialogTypes.SUCCESS)
                        .setTitle(successTitle)
                        .setMessage(successMessage)
                        .setDarkMode(true)
                        .show()
            }

            R.id.btn_drake_dialog_error -> {
                AestheticDialog.Builder(this, DialogThemes.DRAKE, DialogTypes.ERROR)
                        .setTitle(errorTitle)
                        .setMessage(errorMessage)
                        .show()
            }
            R.id.btn_emoji_dialog_success -> if (rbEmojiLight.isChecked) {
                AestheticDialog.Builder(this, DialogThemes.EMOJI, DialogTypes.SUCCESS)
                        .setTitle(successTitle)
                        .setMessage(errorMessage)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            } else {
                AestheticDialog.Builder(this, DialogThemes.EMOJI, DialogTypes.SUCCESS)
                        .setTitle(errorTitle)
                        .setMessage(errorMessage)
                        .setDarkMode(true)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            }
            R.id.btn_emoji_dialog_error -> if (rbEmojiLight.isChecked) {
                AestheticDialog.Builder(this, DialogThemes.EMOJI, DialogTypes.ERROR)
                        .setTitle(errorTitle)
                        .setMessage(errorMessage)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            } else {
                AestheticDialog.Builder(this, DialogThemes.EMOJI, DialogTypes.ERROR)
                        .setTitle(errorTitle)
                        .setMessage(errorMessage)
                        .setDarkMode(true)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            }

            R.id.btn_emotion_dialog_success -> {
                AestheticDialog.Builder(this, DialogThemes.EMOTION, DialogTypes.SUCCESS)
                        .setTitle(successTitle)
                        .setMessage(successMessage)
                        .show()
            }

            R.id.btn_emotion_dialog_error -> {
                AestheticDialog.Builder(this, DialogThemes.EMOTION, DialogTypes.ERROR)
                        .setTitle(errorTitle)
                        .setMessage(errorMessage)
                        .show()
            }

            R.id.btn_rainbow_dialog_error -> {
                AestheticDialog.Builder(this, DialogThemes.RAINBOW, DialogTypes.ERROR)
                        .setTitle(errorTitle)
                        .setMessage(errorMessage)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            }

            R.id.btn_rainbow_dialog_success -> {
                AestheticDialog.Builder(this, DialogThemes.RAINBOW, DialogTypes.SUCCESS)
                        .setTitle(successTitle)
                        .setMessage(successMessage)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            }

            R.id.btn_rainbow_dialog_warning -> {
                AestheticDialog.Builder(this, DialogThemes.RAINBOW, DialogTypes.WARNING)
                        .setTitle(warningTitle)
                        .setMessage(warningMessage)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            }

            R.id.btn_rainbow_dialog_info -> {
                AestheticDialog.Builder(this, DialogThemes.RAINBOW, DialogTypes.INFO)
                        .setTitle(infoTitle)
                        .setMessage(infoMessage)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            }

            R.id.btn_flat_dialog_success -> if (rbFlatLight.isChecked) {
                AestheticDialog.Builder(this, DialogThemes.FLAT, DialogTypes.SUCCESS)
                        .setTitle(successTitle)
                        .setMessage(successMessage)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            } else {
                AestheticDialog.Builder(this, DialogThemes.FLAT, DialogTypes.SUCCESS)
                        .setTitle(successTitle)
                        .setMessage(successMessage)
                        .setDarkMode(true)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            }
            R.id.btn_flat_dialog_error -> if (rbFlatLight.isChecked) {
                AestheticDialog.Builder(this, DialogThemes.FLAT, DialogTypes.ERROR)
                        .setTitle(errorTitle)
                        .setMessage(errorMessage)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            } else {
                AestheticDialog.Builder(this, DialogThemes.FLAT, DialogTypes.ERROR)
                        .setTitle(errorTitle)
                        .setMessage(errorMessage)
                        .setDarkMode(true)
                        .show()
            }
            R.id.btn_flat_dialog_warning -> if (rbFlatLight.isChecked) {
                AestheticDialog.Builder(this, DialogThemes.FLAT, DialogTypes.WARNING)
                        .setTitle(warningTitle)
                        .setMessage(warningMessage)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            } else {
                AestheticDialog.Builder(this, DialogThemes.FLAT, DialogTypes.WARNING)
                        .setTitle(warningTitle)
                        .setMessage(warningMessage)
                        .setDarkMode(true)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                Toast.makeText(applicationContext, "test", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                        })
                        .show()
            }
            R.id.btn_flat_dialog_info -> if (rbFlatLight.isChecked) {
                AestheticDialog.Builder(this, DialogThemes.FLAT, DialogTypes.INFO)
                        .setTitle(infoTitle)
                        .setMessage(infoMessage)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            } else {
                AestheticDialog.Builder(this, DialogThemes.FLAT, DialogTypes.INFO)
                        .setTitle(infoTitle)
                        .setMessage(infoMessage)
                        .setDarkMode(true)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()
            }
        }
    }
}