package com.thecode.aestheticdialogs

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.annotation.Keep
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.thecode.aestheticdialogs.databinding.DialogConnectifyErrorBinding
import com.thecode.aestheticdialogs.databinding.DialogConnectifySuccessBinding
import com.thecode.aestheticdialogs.databinding.DialogEmojiBinding
import com.thecode.aestheticdialogs.databinding.DialogEmotionBinding
import com.thecode.aestheticdialogs.databinding.DialogFlashBinding
import com.thecode.aestheticdialogs.databinding.DialogFlatBinding
import com.thecode.aestheticdialogs.databinding.DialogRainbowBinding
import com.thecode.aestheticdialogs.databinding.DialogToasterBinding
import java.text.SimpleDateFormat
import java.util.Calendar


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
        private val activity: Activity,
        private val dialogStyle: DialogStyle,
        private val dialogType: DialogType
    ) {

        private lateinit var alertDialog: AlertDialog
        private val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)

        private var title: String = "Title"
        private var message: String = "Message"

        // Optional features
        private var isDarkMode: Boolean = false
        private var isCancelable: Boolean = true
        private var duration: Int = 0
        private var gravity: Int = Gravity.NO_GRAVITY
        private var animation: DialogAnimation = DialogAnimation.DEFAULT
        private lateinit var layoutView: View
        private var onClickListener: OnDialogClickListener = object : OnDialogClickListener {
            override fun onClick(dialog: Builder) {
                dialog.dismiss()
            }
        }


        /**
         * Set dialog title text
         *
         * @param title
         * @return this, for chaining.
         */
        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        /**
         * Set dialog message text
         *
         * @param message
         * @return this, for chaining.
         */
        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

        /**
         * Set dialog mode. Defined by default to false
         *
         * @param isDarkMode
         * @return this, for chaining.
         */
        fun setDarkMode(isDarkMode: Boolean): Builder {
            this.isDarkMode = isDarkMode
            return this
        }

        /**
         * Set an OnClickListener to the dialog
         *
         * @param onDialogClickListener interface for callback event on click of button.
         * @return this, for chaining.
         */
        fun setOnClickListener(onDialogClickListener: OnDialogClickListener): Builder {
            this.onClickListener = onDialogClickListener
            return this
        }

        /**
         * Define if the dialog is cancelable
         *
         * @param isCancelable
         * @return this, for chaining.
         */
        fun setCancelable(isCancelable: Boolean): Builder {
            this.isCancelable = isCancelable
            return this
        }

        /**
         * Define the display duration of the dialog
         *
         * @param duration in milliseconds
         * @return this, for chaining.
         */
        fun setDuration(duration: Int): Builder {
            if (duration != 0) {
                this.duration = duration
                Handler(Looper.getMainLooper()).postDelayed({
                    this.dismiss()
                }, duration.toLong())
            }
            return this
        }

        /**
         * Set the gravity of the dialog
         *
         * @param gravity in milliseconds
         * @return this, for chaining.
         */
        fun setGravity(gravity: Int): Builder {
            this.gravity = gravity
            return this
        }

        /**
         * Set the animation of the dialog
         *
         * @param animation in milliseconds
         * @return this, for chaining.
         */
        fun setAnimation(animation: DialogAnimation): Builder {
            this.animation = animation
            return this
        }

        /**
         * Dismiss the dialog
         *
         * @return Aesthetic Dialog instance.
         */
        fun dismiss(): AestheticDialog {
            if (alertDialog.isShowing) {
                alertDialog.dismiss()
            }
            return AestheticDialog()
        }

        /**
         * Choose the dialog animation according to the parameter
         *
         */
        private fun chooseAnimation() {
            alertDialog.window?.attributes?.apply {
                when (animation) {
                    DialogAnimation.ZOOM -> {
                        windowAnimations = R.style.DialogAnimationZoom
                    }

                    DialogAnimation.FADE -> {
                        windowAnimations = R.style.DialogAnimationFade
                    }

                    DialogAnimation.CARD -> {
                        windowAnimations = R.style.DialogAnimationCard
                    }

                    DialogAnimation.SHRINK -> {
                        windowAnimations = R.style.DialogAnimationShrink
                    }

                    DialogAnimation.SWIPE_LEFT -> {
                        windowAnimations = R.style.DialogAnimationSwipeLeft
                    }

                    DialogAnimation.SWIPE_RIGHT -> {
                        windowAnimations = R.style.DialogAnimationSwipeRight
                    }

                    DialogAnimation.IN_OUT -> {
                        windowAnimations = R.style.DialogAnimationInOut
                    }

                    DialogAnimation.SPIN -> {
                        windowAnimations = R.style.DialogAnimationSpin
                    }

                    DialogAnimation.SPLIT -> {
                        windowAnimations = R.style.DialogAnimationSplit
                    }

                    DialogAnimation.DIAGONAL -> {
                        windowAnimations = R.style.DialogAnimationDiagonal
                    }

                    DialogAnimation.WINDMILL -> {
                        windowAnimations = R.style.DialogAnimationWindMill
                    }

                    DialogAnimation.SLIDE_UP -> {
                        windowAnimations = R.style.DialogAnimationSlideUp
                    }

                    DialogAnimation.SLIDE_DOWN -> {
                        windowAnimations = R.style.DialogAnimationSlideDown
                    }

                    DialogAnimation.SLIDE_LEFT -> {
                        windowAnimations = R.style.DialogAnimationSlideLeft
                    }

                    DialogAnimation.SLIDE_RIGHT -> {
                        windowAnimations = R.style.DialogAnimationSlideRight
                    }

                    DialogAnimation.DEFAULT -> {
                        windowAnimations = R.style.DialogAnimation
                    }
                }
            }
        }

        /**
         * Displays the dialog according to the parameters of the Builder
         *
         * @return Aesthetic Dialog instance.
         */
        fun show(): AestheticDialog {
            when (dialogStyle) {
                DialogStyle.EMOJI -> {
                    layoutView = activity.layoutInflater.inflate(R.layout.dialog_emoji, null)
                    DialogEmojiBinding.bind(layoutView).apply {
                        textMessageEmoji.text = message
                        textTitleEmoji.text = title
                        if (dialogType == DialogType.SUCCESS) {
                            textTitleEmoji.setTextColor(
                                ContextCompat.getColor(
                                    activity,
                                    R.color.dialog_success
                                )
                            )
                            dialogIconEmoji.setImageResource(R.drawable.thumbs_up_sign)
                        } else {
                            textTitleEmoji.setTextColor(
                                ContextCompat.getColor(
                                    activity,
                                    R.color.dialog_error
                                )
                            )
                            dialogIconEmoji.setImageResource(R.drawable.man_shrugging)
                        }

                        if (isDarkMode) {
                            textMessageEmoji.setTextColor(
                                ContextCompat.getColor(
                                    activity,
                                    R.color.md_white_1000
                                )
                            )
                            dialogLayoutEmoji.setBackgroundColor(
                                ContextCompat.getColor(
                                    activity,
                                    R.color.dark_background
                                )
                            )
                        }
                        imageCloseEmoji.setOnClickListener { onClickListener.onClick(this@Builder) }
                        dialogBuilder.setView(layoutView)
                        alertDialog = dialogBuilder.create()
                        alertDialog.apply {
                            chooseAnimation()
                            show()
                            window?.apply {
                                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                setGravity(Gravity.TOP)
                                setLayout(
                                    WindowManager.LayoutParams.WRAP_CONTENT,
                                    activity.resources.getDimensionPixelSize(R.dimen.popup_height_emoji_dialog)
                                )
                            }
                        }
                    }
                }

                DialogStyle.DRAKE -> {
                    layoutView = if (dialogType == DialogType.SUCCESS) {
                        activity.layoutInflater.inflate(R.layout.dialog_drake_success, null)
                    } else {
                        activity.layoutInflater.inflate(R.layout.dialog_drake_error, null)
                    }
                    dialogBuilder.setView(layoutView)
                    alertDialog = dialogBuilder.create()
                    alertDialog.apply {
                        chooseAnimation()
                        show()
                        window?.apply {
                            setGravity(Gravity.CENTER)
                            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            setLayout(
                                WindowManager.LayoutParams.WRAP_CONTENT,
                                activity.resources.getDimensionPixelSize(R.dimen.popup_height_drake)
                            )
                        }
                    }
                }

                DialogStyle.TOASTER -> {
                    val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)
                    layoutView = activity.layoutInflater.inflate(R.layout.dialog_toaster, null)
                    DialogToasterBinding.bind(layoutView).apply {
                        textMessageToaster.text = message
                        textTitleToaster.text = title
                        imageCloseToaster.setOnClickListener { onClickListener.onClick(this@Builder) }
                        dialogBuilder.setView(layoutView)
                        alertDialog = dialogBuilder.create()
                        alertDialog.apply {
                            chooseAnimation()
                            show()
                            window?.apply {
                                setGravity(Gravity.TOP)
                                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                setLayout(
                                    WindowManager.LayoutParams.WRAP_CONTENT,
                                    activity.resources.getDimensionPixelSize(R.dimen.popup_height_toaster)
                                )
                            }
                        }


                        when (dialogType) {
                            DialogType.ERROR -> {
                                textTitleToaster.setTextColor(
                                    ContextCompat.getColor(
                                        activity,
                                        R.color.dialog_error
                                    )
                                )
                                verticalViewToaster.setBackgroundColor(
                                    ContextCompat.getColor(
                                        activity,
                                        R.color.dialog_error
                                    )
                                )
                                dialogIconToaster.setImageResource(R.drawable.ic_error_red_24dp)
                            }

                            DialogType.SUCCESS -> {
                                textTitleToaster.setTextColor(
                                    ContextCompat.getColor(
                                        activity,
                                        R.color.dialog_success
                                    )
                                )
                                verticalViewToaster.setBackgroundColor(
                                    ContextCompat.getColor(
                                        activity,
                                        R.color.dialog_success
                                    )
                                )
                                dialogIconToaster.setImageResource(R.drawable.ic_check_circle_green_24dp)
                            }

                            DialogType.WARNING -> {
                                textTitleToaster.setTextColor(
                                    ContextCompat.getColor(
                                        activity,
                                        R.color.dialog_warning
                                    )
                                )
                                verticalViewToaster.setBackgroundColor(
                                    ContextCompat.getColor(
                                        activity,
                                        R.color.dialog_warning
                                    )
                                )
                                dialogIconToaster.setImageResource(R.drawable.ic_warning_orange_24dp)
                            }

                            DialogType.INFO -> {
                                textTitleToaster.setTextColor(
                                    ContextCompat.getColor(
                                        activity,
                                        R.color.dialog_info
                                    )
                                )
                                verticalViewToaster.setBackgroundColor(
                                    ContextCompat.getColor(
                                        activity,
                                        R.color.dialog_info
                                    )
                                )
                                dialogIconToaster.setImageResource(R.drawable.ic_info_blue_24dp)
                            }
                        }

                        if (isDarkMode) {
                            dialogLayoutToaster.setBackgroundColor(
                                ContextCompat.getColor(
                                    activity,
                                    R.color.dark_background
                                )
                            )
                            textMessageToaster.setTextColor(
                                ContextCompat.getColor(
                                    activity,
                                    R.color.md_white_1000
                                )
                            )
                        }
                    }
                }

                DialogStyle.RAINBOW -> {
                    layoutView = activity.layoutInflater.inflate(R.layout.dialog_rainbow, null)
                    DialogRainbowBinding.bind(layoutView).apply {
                        when (dialogType) {
                            DialogType.ERROR -> {
                                dialogLayoutRainbow.setBackgroundColor(
                                    ContextCompat.getColor(
                                        activity,
                                        R.color.dialog_error
                                    )
                                )
                                dialogIconRainbow.setImageResource(R.drawable.ic_error_red_24dp)
                            }

                            DialogType.SUCCESS -> {
                                dialogLayoutRainbow.setBackgroundColor(
                                    ContextCompat.getColor(
                                        activity,
                                        R.color.dialog_success
                                    )
                                )
                                dialogIconRainbow.setImageResource(R.drawable.ic_check_circle_green_24dp)
                            }

                            DialogType.WARNING -> {
                                dialogLayoutRainbow.setBackgroundColor(
                                    ContextCompat.getColor(
                                        activity,
                                        R.color.dialog_warning
                                    )
                                )
                                dialogIconRainbow.setImageResource(R.drawable.ic_warning_orange_24dp)
                            }

                            DialogType.INFO -> {
                                dialogLayoutRainbow.setBackgroundColor(
                                    ContextCompat.getColor(
                                        activity,
                                        R.color.dialog_info
                                    )
                                )
                                dialogIconRainbow.setImageResource(R.drawable.ic_info_blue_24dp)
                            }
                        }
                        textMessageRainbow.text = message
                        textTitleRainbow.text = title
                        imageCloseRainbow.setOnClickListener { onClickListener.onClick(this@Builder) }
                        dialogBuilder.setView(layoutView)
                        alertDialog = dialogBuilder.create()
                        alertDialog.apply {
                            chooseAnimation()
                            show()
                            window?.apply {
                                setGravity(Gravity.TOP)
                                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                setLayout(
                                    WindowManager.LayoutParams.WRAP_CONTENT,
                                    activity.resources.getDimensionPixelSize(R.dimen.popup_height_emoji_dialog)
                                )
                            }
                        }
                    }
                }

                DialogStyle.CONNECTIFY -> {
                    val imgClose: AppCompatImageView
                    val textTitle: AppCompatTextView
                    val textMessage: AppCompatTextView
                    val layoutDialog: LinearLayoutCompat
                    if (dialogType == DialogType.SUCCESS) {
                        layoutView = activity.layoutInflater.inflate(
                            R.layout.dialog_connectify_success,
                            null
                        )
                        val binding = DialogConnectifySuccessBinding.bind(layoutView)
                        binding.apply {
                            layoutDialog = dialogLayoutConnectifySuccess
                            imgClose = imageCloseConnectifySuccess
                            textTitle = textTitleConnectifySuccess
                            textMessage = textMessageConnectifySuccess
                        }

                    } else {
                        layoutView =
                            activity.layoutInflater.inflate(R.layout.dialog_connectify_error, null)
                        val binding = DialogConnectifyErrorBinding.bind(layoutView)
                        binding.apply {
                            layoutDialog = dialogLayoutConnectifyError
                            imgClose = imageCloseConnectifyError
                            textTitle = textTitleConnectifyError
                            textMessage = textMessageConnectifyError

                        }
                    }
                    textTitle.text = title
                    textMessage.text = message
                    imgClose.setOnClickListener { onClickListener.onClick(this@Builder) }
                    if (isDarkMode) {
                        layoutDialog.setBackgroundColor(
                            ContextCompat.getColor(
                                activity,
                                R.color.dark_background
                            )
                        )
                        textMessage.setTextColor(
                            ContextCompat.getColor(
                                activity,
                                R.color.md_white_1000
                            )
                        )
                    }
                    dialogBuilder.setView(layoutView)
                    alertDialog = dialogBuilder.create()
                    alertDialog.apply {
                        chooseAnimation()
                        show()
                        window?.apply {
                            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            setGravity(Gravity.TOP)
                            setLayout(
                                WindowManager.LayoutParams.WRAP_CONTENT,
                                WindowManager.LayoutParams.WRAP_CONTENT
                            )
                        }
                    }
                }

                DialogStyle.FLASH -> {
                    layoutView = activity.layoutInflater.inflate(R.layout.dialog_flash, null)
                    val binding = DialogFlashBinding.bind(layoutView)
                    binding.apply {
                        if (dialogType == DialogType.SUCCESS) {
                            dialogFrameFlash.setBackgroundResource(R.drawable.rounded_green_gradient_bg)
                            imgIconFlash.setImageResource(R.drawable.circle_validation_success)
                        } else {
                            dialogFrameFlash.setBackgroundResource(R.drawable.rounded_red_gradient_bg)
                            imgIconFlash.setImageResource(R.drawable.circle_validation_error)
                        }
                        dialogMessageFlash.text = message
                        dialogTitleFlash.text = title
                        btnActionFlash.setOnClickListener { onClickListener.onClick(this@Builder) }
                        dialogBuilder.setView(binding.root)
                        alertDialog = dialogBuilder.create()
                        alertDialog.apply {
                            chooseAnimation()
                            show()
                            window?.apply {
                                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                setGravity(Gravity.CENTER)
                                setLayout(
                                    activity.resources.getDimensionPixelSize(R.dimen.popup_width),
                                    activity.resources.getDimensionPixelSize(R.dimen.popup_height)
                                )
                            }
                        }
                    }
                }

                DialogStyle.EMOTION -> {
                    layoutView = activity.layoutInflater.inflate(R.layout.dialog_emotion, null)
                    val binding = DialogEmotionBinding.bind(layoutView)
                    binding.apply {
                        if (dialogType == DialogType.SUCCESS) {
                            imgIconEmotion.setImageResource(R.drawable.smiley_success)
                            dialogLayoutEmotion.setBackgroundResource(R.drawable.background_emotion_success)
                        } else {
                            imgIconEmotion.setImageResource(R.drawable.smiley_error)
                            dialogLayoutEmotion.setBackgroundResource(R.drawable.background_emotion_error)
                        }
                        val sdf = SimpleDateFormat("HH:mm")
                        val hour = sdf.format(Calendar.getInstance().time)
                        dialogMessageEmotion.text = message
                        dialogTitleEmotion.text = title
                        dialogHourEmotion.text = hour
                        dialogBuilder.setView(binding.root)
                        alertDialog = dialogBuilder.create()
                        alertDialog.apply {
                            chooseAnimation()
                            show()
                            window?.apply {
                                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                setGravity(Gravity.CENTER)

                                setLayout(
                                    WindowManager.LayoutParams.WRAP_CONTENT,
                                    activity.resources.getDimensionPixelSize(R.dimen.popup_height_emotion)
                                )
                            }
                        }
                    }
                }

                DialogStyle.FLAT -> {
                    layoutView = activity.layoutInflater.inflate(R.layout.dialog_flat, null)
                    DialogFlatBinding.bind(layoutView).apply {
                        dialogMessageFlat.text = message
                        dialogTitleFlat.text = title
                        btnActionFlat.setOnClickListener { onClickListener.onClick(this@Builder) }
                        when (dialogType) {
                            DialogType.ERROR -> {
                                dialogIconFlat.setImageResource(R.drawable.ic_error_red_24dp)
                                btnActionFlat.setBackgroundResource(R.drawable.btn_red_selector)
                                dialogFrameFlat.setBackgroundResource(R.drawable.rounded_rect_red)
                            }

                            DialogType.SUCCESS -> {
                                dialogIconFlat.setImageResource(R.drawable.ic_check_circle_green_24dp)
                                btnActionFlat.setBackgroundResource(R.drawable.btn_green_selector)
                                dialogFrameFlat.setBackgroundResource(R.drawable.rounded_rect_green)
                            }

                            DialogType.WARNING -> {
                                dialogIconFlat.setImageResource(R.drawable.ic_warning_orange_24dp)
                                btnActionFlat.setBackgroundResource(R.drawable.btn_yellow_selector)
                                dialogFrameFlat.setBackgroundResource(R.drawable.rounded_rect_yellow)
                            }

                            DialogType.INFO -> {
                                dialogIconFlat.setImageResource(R.drawable.ic_info_blue_24dp)
                                btnActionFlat.setBackgroundResource(R.drawable.btn_blue_selector)
                                dialogFrameFlat.setBackgroundResource(R.drawable.rounded_rect_blue)
                            }
                        }

                        if (isDarkMode) {
                            dialogLayoutFlat.setBackgroundResource(R.drawable.rounded_dark_bg)
                            dialogTitleFlat.setTextColor(
                                ContextCompat.getColor(
                                    activity,
                                    R.color.md_white_1000
                                )
                            )
                            dialogMessageFlat.setTextColor(
                                ContextCompat.getColor(
                                    activity,
                                    R.color.md_white_1000
                                )
                            )
                            dialogMessageFlat.text = message
                            dialogTitleFlat.text = title
                            btnActionFlat.setOnClickListener { onClickListener.onClick(this@Builder) }
                        }
                    }
                    dialogBuilder.setView(layoutView)
                    alertDialog = dialogBuilder.create()
                    alertDialog.apply {
                        chooseAnimation()
                        show()
                        window?.apply {
                            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            setGravity(Gravity.CENTER)
                            setLayout(
                                activity.resources.getDimensionPixelSize(R.dimen.popup_width),
                                activity.resources.getDimensionPixelSize(R.dimen.popup_height)
                            )
                        }
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

enum class DialogAnimation {
    DEFAULT,
    SLIDE_UP, SLIDE_DOWN,
    SLIDE_LEFT, SLIDE_RIGHT,
    SWIPE_LEFT, SWIPE_RIGHT,
    IN_OUT, CARD, SHRINK, SPLIT, DIAGONAL, SPIN, WINDMILL, FADE, ZOOM
}

enum class DialogStyle {
    EMOJI, DRAKE, TOASTER, CONNECTIFY, FLAT, RAINBOW, FLASH, EMOTION
}

enum class DialogType {
    SUCCESS, ERROR, WARNING, INFO
}