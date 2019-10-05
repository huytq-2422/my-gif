package com.sun.mygif.service

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import com.sun.mygif.data.model.GifSmall
import com.sun.mygif.ui.main.MainActivity
import com.sun.mygif.utils.EXTRA_GIF_INFO
import com.sun.mygif.utils.screenHeight
import kotlinx.android.synthetic.main.layout_bubble.view.*
import kotlin.math.abs

private const val TAG = "GifHeadService"
private const val THRESHOLD_TIME_CLICK = 125L
private const val THRESHOLD_EXIT_DISTANCE_X = 200
private const val THRESHOLD_EXIT_DISTANCE_Y = 300

class GifHeadService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var gifHeadView: View
    private lateinit var exitView: View

    private val positionExitX = 0f
    private val positionExitY = screenHeight * 0.5

    private val paramsType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    } else {
        WindowManager.LayoutParams.TYPE_PHONE
    }
    private val gifHeadLayoutParams = WindowManager.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
        paramsType, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT
    )
    private val exitLayoutParams = WindowManager.LayoutParams(

        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
        paramsType, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT
    )

    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f
    private var timeStartTouch = 0L

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        initWindowManager()
        initViews()
        handleEvents()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val gif = intent?.getParcelableExtra<GifSmall>(EXTRA_GIF_INFO)
        gifHeadView.gifHeadIcon?.run {
            gif?.let {
                gifUrl = it.url
            } ?: run {
                visibility = View.GONE
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initWindowManager() {
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    @SuppressLint("InflateParams")
    private fun initViews() {
        gifHeadView = LayoutInflater.from(this).inflate(com.sun.mygif.R.layout.layout_bubble, null)
        windowManager.addView(gifHeadView, gifHeadLayoutParams)

        exitView = LayoutInflater.from(this).inflate(com.sun.mygif.R.layout.layout_trash_bubble, null)
    }

    private fun handleEvents() {
        gifHeadView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> onTouchDownGifHead(event.rawX, event.rawY)
                MotionEvent.ACTION_UP -> onTouchUpGifHead()
                MotionEvent.ACTION_MOVE -> onMoveGifHead(event.rawX, event.rawY)
            }
            true
        }
    }

    private fun onTouchDownGifHead(eventX: Float, eventY: Float) {
        initialX = gifHeadLayoutParams.x
        initialY = gifHeadLayoutParams.y
        initialTouchX = eventX
        initialTouchY = eventY
        timeStartTouch = System.currentTimeMillis()
        windowManager.addView(exitView, exitLayoutParams)
    }

    private fun onTouchUpGifHead() {
        if (isClick(timeStartTouch)) {
            startActivity(MainActivity.getIntent(this))
        } else if (isNearExitIcon()) {
            onDestroy()
        }
        windowManager.removeViewImmediate(exitView)
    }

    private fun onMoveGifHead(eventX: Float, eventY: Float) {
        gifHeadLayoutParams.x = initialX + (eventX - initialTouchX).toInt()
        gifHeadLayoutParams.y = initialY + (eventY - initialTouchY).toInt()
        windowManager.updateViewLayout(gifHeadView, gifHeadLayoutParams)
    }

    private fun isNearExitIcon() =
        abs(gifHeadLayoutParams.x - positionExitX) < THRESHOLD_EXIT_DISTANCE_X
                && abs(gifHeadLayoutParams.y - positionExitY) < THRESHOLD_EXIT_DISTANCE_Y

    private fun isClick(timeStart: Long) = System.currentTimeMillis() - timeStart < THRESHOLD_TIME_CLICK

    override fun onDestroy() {
        super.onDestroy()
        try {
            windowManager.removeViewImmediate(gifHeadView)
        } catch (e: Exception) {
            //Log.e(TAG, "onDestroy: $e")
            // view has been removed
        }
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context, gifSmall: GifSmall?) = Intent(context, GifHeadService::class.java).apply {
            gifSmall?.let {
                putExtra(EXTRA_GIF_INFO, it)
            }
        }

        @JvmStatic
        fun isRunning(context: Context): Boolean {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            return manager.getRunningServices(Integer.MAX_VALUE).any {
                it.service.className == GifHeadService::class.java.name
            }
        }
    }
}
