package com.photograph.paintexample

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.*

@SuppressLint("ViewConstructor")
class CustomSurfaceView(context: Context, surfaceView: SurfaceView) :
    SurfaceView(context), SurfaceHolder.Callback {
    private var surfaceHolder: SurfaceHolder = surfaceView.holder
    private var paint: Paint = Paint()
    private var path: Path = Path()
    private var bitmap: Bitmap
    private var canvas: Canvas

    init {
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT)
        surfaceView.setZOrderOnTop(true)
        surfaceHolder.addCallback(this)
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.isAntiAlias = true
        paint.strokeWidth = 30F

        val size = getSize()
        bitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        val c = surfaceHolder.lockCanvas()
        c.drawBitmap(bitmap, 0F, 0F, null)
        surfaceHolder.unlockCanvasAndPost(c)

    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    override fun surfaceDestroyed(holder: SurfaceHolder) {}

    private fun draw() {
        val c = surfaceHolder.lockCanvas()
        c.drawBitmap(bitmap, 0F, 0F, null)
        c.drawPath(path, paint)
        surfaceHolder.unlockCanvasAndPost(c)
    }

    fun onTouch(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchDown(event.x, event.y)
            MotionEvent.ACTION_MOVE -> touchMove(event.x, event.y)
            MotionEvent.ACTION_UP -> touchUp(event.x, event.y)
        }
        return true
    }

    private fun touchDown(x: Float, y: Float) {
        path = Path()
        path.moveTo(x, y)
    }

    private fun touchMove(x: Float, y: Float) {
        path.lineTo(x, y)
        draw()
    }

    private fun touchUp(x: Float, y: Float) {
        path.lineTo(x, y)
        draw()
        canvas.drawPath(path, paint)
    }

    @Suppress("DEPRECATION")
    private fun getSize(): Point {
        val windowManager = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)

        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val windowInsets = windowManager.currentWindowMetrics.windowInsets
            var insets: Insets = windowInsets.getInsets(WindowInsets.Type.navigationBars())
            windowInsets.displayCutout?.run {
                insets = Insets.max(
                    insets,
                    Insets.of(safeInsetLeft, safeInsetTop, safeInsetRight, safeInsetBottom)
                )
            }
            val insetsWidth = insets.right + insets.left
            val insetsHeight = insets.top + insets.bottom
            Point(
                windowManager.currentWindowMetrics.bounds.width() - insetsWidth,
                windowManager.currentWindowMetrics.bounds.height() - insetsHeight
            )
        } else {
            Point().apply {
                windowManager.defaultDisplay.getSize(this)
            }
        }
    }
}