package com.thecode.aestheticdialogs

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.dialog_connectify_error.view.*
import kotlinx.android.synthetic.main.dialog_connectify_success.view.*
import kotlinx.android.synthetic.main.dialog_emoji.view.*
import kotlinx.android.synthetic.main.dialog_emotion.view.*
import kotlinx.android.synthetic.main.dialog_flash.view.*
import kotlinx.android.synthetic.main.dialog_flat.view.*
import kotlinx.android.synthetic.main.dialog_rainbow.view.*
import kotlinx.android.synthetic.main.dialog_toaster.view.*
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
        private var animation: Int = DialogAnimation.DEFAULT
        private lateinit var layoutView: View
        private var onClickListener: OnDialogClickListener = object : OnDialogClickListener {
            override fun onClick(dialog: Builder) {
                dialog.dismiss()
            }
        }


        /**
         * Set dialog title text
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
         * Set dialog message text
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
            if (duration != 0) {
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
         * Set the gravity duration of the dialog
         *
         *
         * @param gravity in milliseconds
         */
        @NonNull
        fun setAnimation(animation: Int): Builder {
            this.animation = animation
            return this
        }

        /**
         * Dismiss the dialog
         *
         *
         */
        @NonNull
        fun dismiss(): AestheticDialog {
            if (alertDialog.isShowing) {
                alertDialog.dismiss()
            }
            return AestheticDialog()
        }


        /**
         * Define the dialog animation
         *
         *
         */
        @NonNull
        private fun chooseAnimation() {
            if (animation == DialogAnimation.DEFAULT) {
                alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            } else {
                when (animation) {
                    DialogAnimation.ZOOM -> {
                        alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationZoom
                    }
                    DialogAnimation.FADE -> {
                        alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationFade
                    }
                    DialogAnimation.CARD -> {
                        alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationCard
                    }
                    DialogAnimation.SHRINK -> {
                        alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationShrink
                    }
                    DialogAnimation.SWIPE_LEFT -> {
                        alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationSwipeLeft
                    }
                    DialogAnimation.SWIPE_RIGHT -> {
                        alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationSwipeRight
                    }
                    DialogAnimation.IN_OUT -> {
                        alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationInOut
                    }
                    DialogAnimation.SPIN -> {
                        alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationSpin
                    }
                    DialogAnimation.SPLIT -> {
                        alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationSplit
                    }
                    DialogAnimation.DIAGONAL -> {
                        alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationDiagonal
                    }
                    DialogAnimation.WINDMILL -> {
                        alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationWindMill
                    }
                    DialogAnimation.SLIDE_UP -> {
                        alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationSlideUp
                    }
                    DialogAnimation.SLIDE_DOWN -> {
                        alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationSlideDown
                    }
                    DialogAnimation.SLIDE_LEFT -> {
                        alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationSlideLeft
                    }
                    DialogAnimation.SLIDE_RIGHT -> {
                        alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationSlideRight
                    }
                }
            }
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
                    val layoutDialog = layoutView.dialog_layout_emoji
                    val imgClose: AppCompatImageView = layoutView.image_close_emoji
                    val icon: AppCompatImageView = layoutView.dialog_icon_emoji
                    val textTitle: AppCompatTextView = layoutView.text_title_emoji
                    val textMessage: AppCompatTextView = layoutView.text_message_emoji
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
                        layoutDialog.setBackgroundColor(ContextCompat.getColor(activity, R.color.dark_background))
                    }
                    dialogBuilder.setView(layoutView)
                    alertDialog = dialogBuilder.create()
                    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    alertDialog.window?.setGravity(Gravity.TOP)
                    this.chooseAnimation()
                    alertDialog.show()
                    val height = activity.resources.getDimensionPixelSize(R.dimen.popup_height_emoji_dialog)
                    alertDialog.window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, height)

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
                    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    alertDialog.window?.setGravity(Gravity.CENTER)
                    this.chooseAnimation()
                    alertDialog.show()
                    val height = activity.resources.getDimensionPixelSize(R.dimen.popup_height_drake)
                    alertDialog.window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, height)
                }

                DialogStyle.TOASTER -> {
                    if (isDarkMode) {
                        layoutView = activity.layoutInflater.inflate(R.layout.dialog_toaster, null)
                        val layoutDialog = layoutView.dialog_layout_toaster
                        layoutDialog.setBackgroundColor(ContextCompat.getColor(activity, R.color.dark_background))
                        val imgClose: AppCompatImageView = layoutView.image_close_toaster
                        val icon: AppCompatImageView = layoutView.dialog_icon_toaster
                        val textTitle: AppCompatTextView = layoutView.text_title_toaster
                        val textMessage: AppCompatTextView = layoutView.text_message_toaster
                        textMessage.setTextColor(ContextCompat.getColor(activity, R.color.md_white_1000))
                        val verticalView = layoutView.vertical_view_toaster
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
                        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        alertDialog.window?.setGravity(Gravity.TOP)
                        this.chooseAnimation()
                        alertDialog.show()
                        val height = activity.resources.getDimensionPixelSize(R.dimen.popup_height_toaster)
                        alertDialog.window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, height)
                        imgClose.setOnClickListener { onClickListener.onClick(this) }

                    } else {

                        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)
                        layoutView = activity.layoutInflater.inflate(R.layout.dialog_toaster, null)
                        val imgClose: AppCompatImageView = layoutView.image_close_toaster
                        val icon: AppCompatImageView = layoutView.dialog_icon_toaster
                        val textTitle: AppCompatTextView = layoutView.text_title_toaster
                        val textMessage: AppCompatTextView = layoutView.text_message_toaster
                        val verticalView = layoutView.vertical_view_toaster
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
                        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        alertDialog.window?.setGravity(Gravity.TOP)
                        this.chooseAnimation()
                        alertDialog.show()
                        val height = activity.resources.getDimensionPixelSize(R.dimen.popup_height_toaster)
                        alertDialog.window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, height)
                        imgClose.setOnClickListener { onClickListener.onClick(this) }
                    }

                }

                DialogStyle.RAINBOW -> {
                    layoutView = activity.layoutInflater.inflate(R.layout.dialog_rainbow, null)
                    val icon: AppCompatImageView = layoutView.dialog_icon_rainbow
                    val layoutDialog = layoutView.dialog_layout_rainbow
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
                    val imgClose: AppCompatImageView = layoutView.image_close_rainbow
                    val textTitle: AppCompatTextView = layoutView.text_title_rainbow
                    val textMessage: AppCompatTextView = layoutView.text_message_rainbow
                    textMessage.text = message
                    textTitle.text = title
                    dialogBuilder.setView(layoutView)
                    alertDialog = dialogBuilder.create()
                    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    alertDialog.window?.setGravity(Gravity.TOP)
                    this.chooseAnimation()
                    alertDialog.show()
                    val height = activity.resources.getDimensionPixelSize(R.dimen.popup_height_emoji_dialog)
                    alertDialog.window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, height)
                    imgClose.setOnClickListener { onClickListener.onClick(this) }
                }

                DialogStyle.CONNECTIFY -> {
                    val imgClose: AppCompatImageView
                    val textTitle: AppCompatTextView
                    val textMessage: AppCompatTextView
                    val layoutDialog: LinearLayoutCompat
                    if (dialogType == DialogType.SUCCESS) {
                        layoutView = activity.layoutInflater.inflate(R.layout.dialog_connectify_success, null)
                        layoutDialog = layoutView.dialog_layout_connectify_success
                        imgClose = layoutView.image_close_connectify_success
                        textTitle = layoutView.text_title_connectify_success
                        textMessage = layoutView.text_message_connectify_success
                    } else {
                        layoutView = activity.layoutInflater.inflate(R.layout.dialog_connectify_error, null)
                        layoutDialog = layoutView.dialog_layout_connectify_error
                        imgClose = layoutView.image_close_connectify_error
                        textTitle = layoutView.text_title_connectify_error
                        textMessage = layoutView.text_message_connectify_error
                    }

                    textTitle.text = title
                    textMessage.text = message

                    if (isDarkMode) {
                        layoutDialog.setBackgroundColor(ContextCompat.getColor(activity, R.color.dark_background))
                        textMessage.setTextColor(ContextCompat.getColor(activity, R.color.md_white_1000))
                    }
                    dialogBuilder.setView(layoutView)
                    alertDialog = dialogBuilder.create()
                    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    alertDialog.window?.setGravity(Gravity.TOP)
                    this.chooseAnimation()
                    alertDialog.show()
                    alertDialog.window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
                    imgClose.setOnClickListener { onClickListener.onClick(this) }
                }


                DialogStyle.FLASH -> {
                    layoutView = activity.layoutInflater.inflate(R.layout.dialog_flash, null)
                    val btnOk: AppCompatButton = layoutView.btn_action_flash
                    val textTitle: AppCompatTextView = layoutView.dialog_title_flash
                    val textMessage: AppCompatTextView = layoutView.dialog_message_flash
                    val dialogFrame = layoutView.dialog_frame_flash
                    val icon: AppCompatImageView = layoutView.img_icon_flash
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
                    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    alertDialog.window?.setGravity(Gravity.CENTER)
                    this.chooseAnimation()
                    alertDialog.show()
                    val height = activity.resources.getDimensionPixelSize(R.dimen.popup_height)
                    val width = activity.resources.getDimensionPixelSize(R.dimen.popup_height)
                    alertDialog.window?.setLayout(width, height)
                    btnOk.setOnClickListener { onClickListener.onClick(this) }
                }

                DialogStyle.EMOTION -> {
                    layoutView = activity.layoutInflater.inflate(R.layout.dialog_emotion, null)
                    val icon: AppCompatImageView = layoutView.img_icon_emotion
                    val layoutDialog = layoutView.dialog_layout_emotion
                    val textTitle: AppCompatTextView = layoutView.dialog_title_emotion
                    val textMessage: AppCompatTextView = layoutView.dialog_message_emotion
                    val textHour: AppCompatTextView = layoutView.dialog_hour_emotion
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
                    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    alertDialog.window?.setGravity(Gravity.CENTER)
                    this.chooseAnimation()
                    alertDialog.show()
                    val height = activity.resources.getDimensionPixelSize(R.dimen.popup_height_emotion)
                    alertDialog.window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, height)
                }

                DialogStyle.FLAT -> {
                    if (isDarkMode) {
                        layoutView = activity.layoutInflater.inflate(R.layout.dialog_flat, null)
                        val btnOk: AppCompatButton = layoutView.btn_action_flat
                        val textTitle: AppCompatTextView = layoutView.dialog_title_flat
                        val textMessage: AppCompatTextView = layoutView.dialog_message_flat
                        val icon: AppCompatImageView = layoutView.dialog_icon_flat
                        val layoutDialog: LinearLayoutCompat = layoutView.dialog_layout_flat
                        val frameLayout = layoutView.dialog_frame_flat
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
                        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        alertDialog.window?.setGravity(Gravity.CENTER)
                        this.chooseAnimation()
                        alertDialog.show()
                        val height = activity.resources.getDimensionPixelSize(R.dimen.popup_height)
                        val width = activity.resources.getDimensionPixelSize(R.dimen.popup_height)
                        alertDialog.window?.setLayout(width, height)
                        btnOk.setOnClickListener { onClickListener.onClick(this) }

                    } else {
                        layoutView = activity.layoutInflater.inflate(R.layout.dialog_flat, null)
                        val btnOk: AppCompatButton = layoutView.btn_action_flat
                        val textTitle: AppCompatTextView = layoutView.dialog_title_flat
                        val textMessage: AppCompatTextView = layoutView.dialog_message_flat
                        val icon: AppCompatImageView = layoutView.dialog_icon_flat
                        val layoutDialog: LinearLayoutCompat = layoutView.dialog_layout_flat
                        val frameLayout = layoutView.dialog_frame_flat
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
                        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        alertDialog.window?.setGravity(Gravity.CENTER)
                        this.chooseAnimation()
                        alertDialog.show()
                        val height = activity.resources.getDimensionPixelSize(R.dimen.popup_height)
                        val width = activity.resources.getDimensionPixelSize(R.dimen.popup_height)
                        alertDialog.window?.setLayout(width, height)
                        btnOk.setOnClickListener { onClickListener.onClick(this) }
                    }
                }
            }

            alertDialog.setCancelable(isCancelable)
            if (gravity != Gravity.NO_GRAVITY) {
                alertDialog.window?.setGravity(gravity)
            }
            return AestheticDialog()
        }
    }
}