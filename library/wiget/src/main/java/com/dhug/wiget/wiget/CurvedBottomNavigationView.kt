package com.dhug.wiget.wiget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import com.google.android.material.bottomnavigation.BottomNavigationView

class CurvedBottomNavigationView(context: Context, attrs: AttributeSet?) :
    BottomNavigationView(context, attrs) {

    private val path = Path()
    private val paint = Paint()

    // Bán kính của FAB
    private val fabRadius = 56f

    // Độ cao của đường cong
    private val curveHeight = 64f

    private var navigationBarWidth = 0
    private var navigationBarHeight = 0

    init {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = Color.WHITE
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        navigationBarWidth = width
        navigationBarHeight = height
        createCurvePath()
    }

    private fun createCurvePath() {
        val centerX = navigationBarWidth / 2f
        val fabMargin = fabRadius / 2

        path.reset()
        path.moveTo(0f, 0f)
        path.lineTo(centerX - fabRadius - fabMargin, 0f)

        // Vẽ đường cong
        path.cubicTo(
            centerX - fabRadius - fabMargin / 2, 0f,
            centerX - fabRadius, curveHeight,
            centerX, curveHeight
        )

        path.cubicTo(
            centerX + fabRadius, curveHeight,
            centerX + fabRadius + fabMargin / 2, 0f,
            centerX + fabRadius + fabMargin, 0f
        )

        path.lineTo(navigationBarWidth.toFloat(), 0f)
        path.lineTo(navigationBarWidth.toFloat(), navigationBarHeight.toFloat())
        path.lineTo(0f, navigationBarHeight.toFloat())
        path.close()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
    }
}
