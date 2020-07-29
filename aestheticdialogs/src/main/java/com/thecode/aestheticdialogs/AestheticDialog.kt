package com.thecode.aestheticdialogs

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Aesthetic Dialog class
 * Use Builder to create a new instance.
 *
 * @author Gabriel The Code
 */

@Keep
class AestheticDialog {
    
    class Builder(
            //Necessary parameters
            @NonNull private val activity: Activity,
            @NonNull private val dialogStyle: String,
            @NonNull private val dialogType: String) {

        lateinit var alertDialog: AlertDialog
        private val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)

        // optional features
        private var title: String = "Title"
        private var message: String = "Message"
        private var isDarkMode: Boolean = false
        private var isCancelable: Boolean = true
        private var duration: Int = 0
        private var gravity: Int = Gravity.NO_GRAVITY
        private lateinit var layoutView: View
        private var onClickListener: OnDialogClickListener = object : OnDialogClickListener {
            override fun onClick(dialog: Builder) {
                dialog.dismiss()
            }
        }


        /**
         * Set dialog title
         *
         *
         * @param title
         */
        @NonNull
        fun setTitle(@NonNull title: String): Builder {
            this.title = title
            return this
        }

        /**
         * Set dialog message
         *
         *
         * @param message
         */
        @NonNull
        fun setMessage(@NonNull message: String): Builder {
            this.message = message
            return this
        }

        /**
         * Set dialog mode. Defined by default to false
         *
         *
         * @param isDarkMode
         */
        @NonNull
        fun setDarkMode(@NonNull isDarkMode: Boolean): Builder {
            this.isDarkMode = isDarkMode
            return this
        }

        /**
         * Set an OnClickListener to the dialog
         *
         *
         * @param onDialogClickListener interface for callback event on click of button.
         */
        @NonNull
        fun setOnClickListener(onDialogClickListener: OnDialogClickListener): Builder {
            this.onClickListener = onDialogClickListener
            return this
        }

        /**
         * Define if the dialog is cancelable
         *
         *
         * @param isCancelable
         */
        @NonNull
        fun setCancelable(isCancelable: Boolean): Builder {
            this.isCancelable = isCancelable
            return this
        }

        /**
         * Define the display duration of the dialog
         *
         *
         * @param duration in milliseconds
         */
        @NonNull
        fun setDuration(duration: Int): Builder {
            if(duration!=0) {
                this.duration = duration
                Handler().postDelayed({
                    this.dismiss()
                }, duration.toLong())
            }
            return this
        }

        /**
         * Set the gravity duration of the dialog
         *
         *
         * @param gravity in milliseconds
         */
        @NonNull
        fun setGravity(gravity: Int): Builder {
            this.gravity = gravity
            return this
        }


        /**
         * Dismiss the dialog
         *
         *
         */
        @NonNull
        fun dismiss(): AestheticDialog {
            if(alertDialog.isShowing){
                alertDialog.dismiss()
            }
            return AestheticDialog()
        }


        /**
         * Displays the dialog according to the parameters of the Builder
         *
         *
         */
        @NonNull
        fun show(): AestheticDialog {

            when (dialogStyle) {
                DialogStyle.EMOJI -> {
                    layoutView = activity.layoutInflater.inflate(R.layout.dialog_emoji, null)
                    val layoutDialog = layoutView.findViewById<RelativeLayout>(R.id.dialog_layout)
                    val imgClose: AppCompatImageView = layoutView.findViewById(R.id.image_close)
                    val icon: AppCompatImageView = layoutView.findViewById(R.id.dialog_icon)
                    val textTitle: AppCompatTextView = layoutView.findViewById(R.id.text_title)
                    val textMessage: AppCompatTextView = layoutView.findViewById(R.id.text_message)
                    textMessage.text = message
                    textTitle.text = title

                    if (dialogType == DialogType.SUCCESS) {
                        textTitle.setTextColor(ContextCompat.getColor(activity, R.color.dialog_success))
                        icon.setImageResource(R.drawable.thumbs_up_sign)
                    } else {
                        textTitle.setTextColor(ContextCompat.getColor(activity, R.color.dialog_error))
                        icon.setImageResource(R.drawable.man_shrugging)
                    }

                    if (isDarkMode) {
                        textMessage.setTextColor(ContextCompat.getColor(activity, R.color.md_white_1000))
                        layoutDialog = layoutView.findViewById(R.id.dialog_layout)
                        layoutDialog.setBackgroundColor(ContextCompat.getColor(activity, R.color.dark_background))
                    }
                    dialogBuilder.setView(layoutView)
                    alertDialog = dialogBuilder.create()
                    alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
                    alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    alertDialog.window!!.setGravity(Gravity.TOP)
                    alertDialog.show()
                    val height = activity.resources.getDimensionPixelSize(R.dimen.popup_height_emoji_dialog)
                    alertDialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, height)

                    imgClose.setOnClickListener { onClickListener.onClick(this) }

                    }


                DialogStyle.DRAKE -> {
                    layoutView = if (dialogType == DialogType.SUCCESS) {
                        activity.layoutInflater.inflate(R.layout.dialog_drake_success, null)
                    } else {
                        activity.layoutInflater.inflate(R.layout.dialog_drake_error, null)
                    }
                    dialogBuilder.setView(layoutView)
                    alertDialog = dialogBuilder.create()
                    alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
                    alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    alertDialog.window!!.setGravity(Gravity.CENTER)
                    alertDialog.show()
                    val height = activity.resources.getDimensionPixelSize(R.dimen.popup_height_drake)
                    alertDialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, height)
                }

                DialogStyle.TOASTER -> {
                    if(isDarkMode){
                        layoutView = activity.layoutInflater.inflate(R.layout.dialog_toaster, null)
                        val layoutDialog = layoutView.findViewById<RelativeLayout>(R.id.dialog_layout)
                        layoutDialog.setBackgroundColor(ContextCompat.getColor(activity, R.color.dark_background))
                        val imgClose: AppCompatImageView = layoutView.findViewById(R.id.image_close)
                        val icon: AppCompatImageView = layoutView.findViewById(R.id.dialog_icon)
                        val textTitle: AppCompatTextView = layoutView.findViewById(R.id.text_title)
                        val textMessage: AppCompatTextView = layoutView.findViewById(R.id.text_message)
                        textMessage.setTextColor(ContextCompat.getColor(activity, R.color.md_white_1000))
                        val verticalView = layoutView.findViewById<View>(R.id.vertical_view)
                        textMessage.text = message
                        textTitle.text = title
                        when (dialogType) {
                            DialogType.ERROR -> {
                                textTitle.setTextColor(ContextCompat.getColor(activity, R.color.dialog_error))
                                verticalView.setBackgroundColor(ContextCompat.getColor(activity, R.color.dialog_error))
                                icon.setImageResource(R.drawable.ic_error_red_24dp)
                            }
                            DialogType.SUCCESS -> {
                                textTitle.setTextColor(ContextCompat.getColor(activity, R.color.dialog_success))
                                verticalView.setBackgroundColor(ContextCompat.getColor(activity, R.color.dialog_success))
                                icon.setImageResource(R.drawable.ic_check_circle_green_24dp)
                            }
                            DialogType.WARNING -> {
                                textTitle.setTextColor(ContextCompat.getColor(activity, R.color.dialog_warning))
                                verticalView.setBackgroundColor(ContextCompat.getColor(activity, R.color.dialog_warning))
                                icon.setImageResource(R.drawable.ic_warning_orange_24dp)
                            }
                            DialogType.INFO -> {
                                textTitle.setTextColor(ContextCompat.getColor(activity, R.color.dialog_info))
                                verticalView.setBackgroundColor(ContextCompat.getColor(activity, R.color.dialog_info))
                                icon.setImageResource(R.drawable.ic_info_blue_24dp)
                            }
                        }
                        dialogBuilder.setView(layoutView)
                        alertDialog = dialogBuilder.create()
                        alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
                        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        alertDialog.window!!.setGravity(Gravity.TOP)
                        alertDialog.show()
                        val height = activity.resources.getDimensionPixelSize(R.dimen.popup_height_toaster)
                        alertDialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, height)
                        imgClose.setOnClickListener { onClickListener.onClick(this) }

                    }else{

                        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)
                        layoutView = activity.layoutInflater.inflate(R.layout.dialog_toaster, null)
                        val imgClose: AppCompatImageView = layoutView.findViewById(R.id.image_close)
                        val icon: AppCompatImageView = layoutView.findViewById(R.id.dialog_icon)
                        val textTitle: AppCompatTextView = layoutView.findViewById(R.id.text_title)
                        val textMessage: AppCompatTextView = layoutView.findViewById(R.id.text_message)
                        val verticalView = layoutView.findViewById<View>(R.id.vertical_view)
                        textMessage.text = message
                        textTitle.text = title
                        when (dialogType) {
                            DialogType.ERROR -> {
                                verticalView.setBackgroundColor(ContextCompat.getColor(activity, R.color.dialog_error))
                                icon.setImageResource(R.drawable.ic_error_red_24dp)
                            }
                            DialogType.SUCCESS -> {
                                verticalView.setBackgroundColor(ContextCompat.getColor(activity, R.color.dialog_success))
                                icon.setImageResource(R.drawable.ic_check_circle_green_24dp)
                            }
                            DialogType.WARNING -> {
                                verticalView.setBackgroundColor(ContextCompat.getColor(activity, R.color.dialog_warning))
                                icon.setImageResource(R.drawable.ic_warning_orange_24dp)
                            }
                            DialogType.INFO -> {
                                verticalView.setBackgroundColor(ContextCompat.getColor(activity, R.color.dialog_info))
                                icon.setImageResource(R.drawable.ic_info_blue_24dp)
                            }
                        }
                        dialogBuilder.setView(layoutView)
                        alertDialog = dialogBuilder.create()
                        alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
                        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        alertDialog.window!!.setGravity(Gravity.TOP)
                        alertDialog.show()
                        val height = activity.resources.getDimensionPixelSize(R.dimen.popup_height_toaster)
                        alertDialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, height)
                        imgClose.setOnClickListener { onClickListener.onClick(this) }
                    }

                }

                DialogStyle.RAINBOW -> {
                    layoutView = activity.layoutInflater.inflate(R.layout.dialog_rainbow, null)
                    val icon: AppCompatImageView = layoutView.findViewById(R.id.dialog_icon)
                    val layoutDialog = layoutView.findViewById<RelativeLayout>(R.id.dialog_layout)
                    when (dialogType) {
                        DialogType.ERROR -> {
                            layoutDialog.setBackgroundColor(ContextCompat.getColor(activity, R.color.dialog_error))
                            icon.setImageResource(R.drawable.ic_error_red_24dp)
                        }
                        DialogType.SUCCESS -> {
                            layoutDialog.setBackgroundColor(ContextCompat.getColor(activity, R.color.dialog_success))
                            icon.setImageResource(R.drawable.ic_check_circle_green_24dp)
                        }
                        DialogType.WARNING -> {
                            layoutDialog.setBackgroundColor(ContextCompat.getColor(activity, R.color.dialog_warning))
                            icon.setImageResource(R.drawable.ic_warning_orange_24dp)
                        }
                        DialogType.INFO -> {
                            layoutDialog.setBackgroundColor(ContextCompat.getColor(activity, R.color.dialog_info))
                            icon.setImageResource(R.drawable.ic_info_blue_24dp)
                        }
                    }
                    val imgClose: AppCompatImageView = layoutView.findViewById(R.id.image_close)
                    val textTitle: AppCompatTextView = layoutView.findViewById(R.id.text_title)
                    val textMessage: AppCompatTextView = layoutView.findViewById(R.id.text_message)
                    textMessage.text = message
                    textTitle.text = title
                    dialogBuilder.setView(layoutView)
                    alertDialog = dialogBuilder.create()
                    alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
                    alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    alertDialog.window!!.setGravity(Gravity.TOP)
                    alertDialog.show()
                    val height = activity.resources.getDimensionPixelSize(R.dimen.popup_height_emoji_dialog)
                    alertDialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, height)
                    imgClose.setOnClickListener { onClickListener.onClick(this) }
                }

                DialogStyle.CONNECTIFY -> {
                    layoutView = if (dialogType == DialogType.SUCCESS) {
                        activity.layoutInflater.inflate(R.layout.dialog_connectify_success, null)
                    } else {
                        activity.layoutInflater.inflate(R.layout.dialog_connectify_error, null)
                    }
                    val layoutDialog: LinearLayoutCompat = layoutView.findViewById(R.id.dialog_layout)
                    val imgClose: AppCompatImageView = layoutView.findViewById(R.id.image_close)
                    val textTitle: AppCompatTextView = layoutView.findViewById(R.id.text_title)
                    val textMessage: AppCompatTextView = layoutView.findViewById(R.id.text_message)
                    textTitle.text = title
                    textMessage.text = message

                    if (isDarkMode) {
                        layoutDialog.setBackgroundColor(ContextCompat.getColor(activity, R.color.dark_background))
                        textMessage.setTextColor(ContextCompat.getColor(activity, R.color.md_white_1000))
                    }
                        dialogBuilder.setView(layoutView)
                        alertDialog = dialogBuilder.create()
                        alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
                        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        alertDialog.window!!.setGravity(Gravity.TOP)
                        alertDialog.show()
                        alertDialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
                        imgClose.setOnClickListener { onClickListener.onClick(this) }
                }


                DialogStyle.FLASH -> {
                    layoutView = activity.layoutInflater.inflate(R.layout.dialog_flash, null)
                    val btnOk: AppCompatButton = layoutView.findViewById(R.id.btn_action)
                    val textTitle: AppCompatTextView = layoutView.findViewById(R.id.dialog_title)
                    val textMessage: AppCompatTextView = layoutView.findViewById(R.id.dialog_message)
                    val dialogFrame = layoutView.findViewById<FrameLayout>(R.id.dialog_frame)
                    val icon: AppCompatImageView = layoutView.findViewById(R.id.img_icon)
                    if (dialogType == DialogType.SUCCESS) {
                        dialogFrame.setBackgroundResource(R.drawable.rounded_green_gradient_bg)
                        icon.setImageResource(R.drawable.circle_validation_success)
                    } else {
                        dialogFrame.setBackgroundResource(R.drawable.rounded_red_gradient_bg)
                        icon.setImageResource(R.drawable.circle_validation_error)
                    }
                    textMessage.text = message
                    textTitle.text = title
                    dialogBuilder.setView(layoutView)
                    alertDialog = dialogBuilder.create()
                    alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
                    alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    alertDialog.window!!.setGravity(Gravity.CENTER)
                    alertDialog.show()
                    val height = activity.resources.getDimensionPixelSize(R.dimen.popup_height)
                    val width = activity.resources.getDimensionPixelSize(R.dimen.popup_height)
                    alertDialog.window!!.setLayout(width, height)
                    btnOk.setOnClickListener { onClickListener.onClick(this) }
                }

                DialogStyle.EMOTION -> {
                    layoutView = activity.layoutInflater.inflate(R.layout.dialog_emotion, null)
                    val icon: AppCompatImageView = layoutView.findViewById(R.id.img_icon)
                    val layoutDialog = layoutView.findViewById<RelativeLayout>(R.id.dialog_layout)
                    val textTitle: AppCompatTextView = layoutView.findViewById(R.id.dialog_title)
                    val textMessage: AppCompatTextView = layoutView.findViewById(R.id.dialog_message)
                    val textHour: AppCompatTextView = layoutView.findViewById(R.id.dialog_hour)
                    if (dialogType == DialogType.SUCCESS) {
                        icon.setImageResource(R.drawable.smiley_success)
                        layoutDialog.setBackgroundResource(R.drawable.background_emotion_success)
                    } else {
                        icon.setImageResource(R.drawable.smiley_error)
                        layoutDialog.setBackgroundResource(R.drawable.background_emotion_error)
                    }
                    val sdf = SimpleDateFormat("HH:mm")
                    val hour = sdf.format(Calendar.getInstance().time)
                    textMessage.text = message
                    textTitle.text = title
                    textHour.text = hour
                    dialogBuilder.setView(layoutView)
                    alertDialog = dialogBuilder.create()
                    alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
                    alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    alertDialog.window!!.setGravity(Gravity.CENTER)
                    alertDialog.show()
                    val height = activity.resources.getDimensionPixelSize(R.dimen.popup_height_emotion)
                    alertDialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, height)
                }

                DialogStyle.FLAT -> {
                    if (isDarkMode) {
                        layoutView = activity.layoutInflater.inflate(R.layout.dialog_flat, null)
                        val btnOk: AppCompatButton = layoutView.findViewById(R.id.btn_action)
                        val textTitle: AppCompatTextView = layoutView.findViewById(R.id.dialog_title)
                        val textMessage: AppCompatTextView = layoutView.findViewById(R.id.dialog_message)
                        val icon: AppCompatImageView = layoutView.findViewById(R.id.dialog_icon)
                        val layoutDialog: LinearLayoutCompat = layoutView.findViewById(R.id.dialog_layout)
                        val frameLayout = layoutView.findViewById<FrameLayout>(R.id.dialog_frame)
                        when (dialogType) {
                            DialogType.ERROR -> {
                                icon.setImageResource(R.drawable.ic_error_red_24dp)
                                btnOk.setBackgroundResource(R.drawable.btn_red_selector)
                                frameLayout.setBackgroundResource(R.drawable.rounded_rect_red)
                            }
                            DialogType.SUCCESS -> {
                                icon.setImageResource(R.drawable.ic_check_circle_green_24dp)
                                btnOk.setBackgroundResource(R.drawable.btn_green_selector)
                                frameLayout.setBackgroundResource(R.drawable.rounded_rect_green)
                            }
                            DialogType.WARNING -> {
                                icon.setImageResource(R.drawable.ic_warning_orange_24dp)
                                btnOk.setBackgroundResource(R.drawable.btn_yellow_selector)
                                frameLayout.setBackgroundResource(R.drawable.rounded_rect_yellow)
                            }
                            DialogType.INFO -> {
                                icon.setImageResource(R.drawable.ic_info_blue_24dp)
                                btnOk.setBackgroundResource(R.drawable.btn_blue_selector)
                                frameLayout.setBackgroundResource(R.drawable.rounded_rect_blue)
                            }
                        }
                        layoutDialog.setBackgroundResource(R.drawable.rounded_dark_bg)
                        textTitle.setTextColor(ContextCompat.getColor(activity, R.color.md_white_1000))
                        textMessage.setTextColor(ContextCompat.getColor(activity, R.color.md_white_1000))
                        textMessage.text = message
                        textTitle.text = title
                        dialogBuilder.setView(layoutView)
                        alertDialog = dialogBuilder.create()
                        alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
                        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        alertDialog.window!!.setGravity(Gravity.CENTER)

                        alertDialog.show()
                        val height = activity.resources.getDimensionPixelSize(R.dimen.popup_height)
                        val width = activity.resources.getDimensionPixelSize(R.dimen.popup_height)
                        alertDialog.window!!.setLayout(width, height)
                        btnOk.setOnClickListener { onClickListener.onClick(this) }

                    } else {
                        layoutView = activity.layoutInflater.inflate(R.layout.dialog_flat, null)
                        val btnOk: AppCompatButton = layoutView.findViewById(R.id.btn_action)
                        val textTitle: AppCompatTextView = layoutView.findViewById(R.id.dialog_title)
                        val textMessage: AppCompatTextView = layoutView.findViewById(R.id.dialog_message)
                        val icon: AppCompatImageView = layoutView.findViewById(R.id.dialog_icon)
                        val layoutDialog: LinearLayoutCompat = layoutView.findViewById(R.id.dialog_layout)
                        val frameLayout = layoutView.findViewById<FrameLayout>(R.id.dialog_frame)
                        when (dialogType) {
                            DialogType.ERROR -> {
                                icon.setImageResource(R.drawable.ic_error_red_24dp)
                                btnOk.setBackgroundResource(R.drawable.btn_red_selector)
                                frameLayout.setBackgroundResource(R.drawable.rounded_rect_red)
                            }
                            DialogType.SUCCESS -> {
                                icon.setImageResource(R.drawable.ic_check_circle_green_24dp)
                                btnOk.setBackgroundResource(R.drawable.btn_green_selector)
                                frameLayout.setBackgroundResource(R.drawable.rounded_rect_green)
                            }
                            DialogType.WARNING -> {
                                icon.setImageResource(R.drawable.ic_warning_orange_24dp)
                                btnOk.setBackgroundResource(R.drawable.btn_yellow_selector)
                                frameLayout.setBackgroundResource(R.drawable.rounded_rect_yellow)
                            }
                            DialogType.INFO -> {
                                icon.setImageResource(R.drawable.ic_info_blue_24dp)
                                btnOk.setBackgroundResource(R.drawable.btn_blue_selector)
                                frameLayout.setBackgroundResource(R.drawable.rounded_rect_blue)
                            }
                        }
                        layoutDialog.setBackgroundResource(R.drawable.rounded_white_bg)
                        textMessage.text = message
                        textTitle.text = title
                        dialogBuilder.setView(layoutView)
                        alertDialog = dialogBuilder.create()
                        alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
                        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        alertDialog.window!!.setGravity(Gravity.CENTER)
                        alertDialog.show()
                        val height = activity.resources.getDimensionPixelSize(R.dimen.popup_height)
                        val width = activity.resources.getDimensionPixelSize(R.dimen.popup_height)
                        alertDialog.window!!.setLayout(width, height)
                        btnOk.setOnClickListener { onClickListener.onClick(this) }
                    }
                }
            }
            alertDialog.setCancelable(isCancelable)
            if(gravity!=Gravity.NO_GRAVITY){
                alertDialog.window!!.setGravity(gravity)
            }
            return AestheticDialog()
        }
    }
}