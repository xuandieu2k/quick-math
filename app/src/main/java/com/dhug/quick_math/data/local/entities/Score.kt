package com.dhug.quick_math.data.local.entities


import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dhug.quick_math.R
import com.dhug.quick_math.utils.EnumConstants
import com.dhug.quick_math.utils.MoneyUtils
import com.dhug.quick_math.utils.StringUtils.capitalizeFirstLetter
import com.dhug.quick_math.utils.TimeUtils
import com.google.gson.annotations.SerializedName

@Entity("score")
data class Score(

    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @SerializedName("highestAnswer")
    var highestAnswer: Int = 0,

    @SerializedName("answerCorrect")
    var answerCorrect: Int = 0,

    @SerializedName("answerIncorrect")
    var answerIncorrect: Int = 0,

    @SerializedName("totalSpentTime")
    var totalSpentTime: Long = 0L,

    @SerializedName("createAt")
    var createAt: Long = System.currentTimeMillis(),

    @SerializedName("updateAt")
    var updateAt: Long = System.currentTimeMillis(),

    @SerializedName("type")
    var type: EnumConstants.PlayType = EnumConstants.PlayType.TRAINING
) {
    fun getQuestionText(context: Context): String =
        "${context.getString(R.string.question_)} ${MoneyUtils.formatBigDecimal(highestAnswer.toBigDecimal())}"

    fun getDateTimeCreated(): String = TimeUtils.formatLongToDateTime(createAt).capitalizeFirstLetter()
    fun getTextCorrect(context: Context): String = "${context.getString(R.string.correct)}: $answerCorrect"
    fun getTextIncorrect(context: Context): String = "${context.getString(R.string.incorrect)}: $answerIncorrect"
}