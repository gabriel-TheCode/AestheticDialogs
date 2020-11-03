package com.thecode.aestheticdialogs

import androidx.annotation.Keep

@Keep
interface OnDialogClickListener {
    fun onClick(dialog: AestheticDialog.Builder)
}