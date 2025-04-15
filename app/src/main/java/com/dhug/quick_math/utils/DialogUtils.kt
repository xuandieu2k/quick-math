package com.dhug.quick_math.utils

import androidx.appcompat.app.AppCompatActivity
import com.dhug.quick_math.presentation.dialog.GameOverDialog
import com.dhug.quick_math.presentation.dialog.WarningDialog
import javax.inject.Singleton

@Singleton
object DialogUtils {


    fun showDialogGameOver(
        activity: AppCompatActivity,
        sumOfQuestion: Int,
        highestQuestion: Int,
        onDone: (typeActionGameOver: GameOverDialog.Builder.Companion.TypeAction) -> Unit
    ): GameOverDialog.Builder = GameOverDialog.Builder(activity, sumOfQuestion, highestQuestion)
        .setListenerAction(object : GameOverDialog.Builder.OnActionGameOver {
            override fun onFishActionGameOver(typeActionGameOver: GameOverDialog.Builder.Companion.TypeAction) {
                onDone(typeActionGameOver)
            }

        })

    fun showDialogWarning(
        activity: AppCompatActivity,
        onDone: (isConfirm: Boolean) -> Unit
    ): WarningDialog.Builder = WarningDialog.Builder(activity)
        .setListenerAction(object : WarningDialog.Builder.OnActionDone {
            override fun onFinishAction(isConfirm: Boolean) {
                onDone(isConfirm)
            }

        })
}