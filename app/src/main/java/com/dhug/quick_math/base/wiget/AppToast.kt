package com.dhug.quick_math.base.wiget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.dhug.quick_math.databinding.ToastCustomBinding
import androidx.core.graphics.toColorInt


@Suppress("DEPRECATION")
class AppToast(
    private val context: Context,
    private val message: String,
    private val lengthShort: Int,
    private val backgroundColor: String = "#252C32",
    private val gravity: Int = Gravity.BOTTOM
) : Toast(context) {
    init {
        val binding = ToastCustomBinding.inflate(LayoutInflater.from(context))
        binding.textToast = message
        view = binding.root
        view?.backgroundTintList = ColorStateList.valueOf(backgroundColor.toColorInt())
        setGravity(gravity, 0, 0)
        duration = lengthShort
    }
}
