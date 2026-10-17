package com.panel.cheat
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.*
import android.widget.TextView
import android.widget.Switch
import android.widget.Toast

class FloatingService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: View
    private lateinit var params: WindowManager.LayoutParams
    private var initialX = 0; private var initialY = 0
    private var initialTouchX = 0f; private var initialTouchY = 0f

    companion object { init { System.loadLibrary("native-lib") } }
    external fun getNativeStatus(): String
    external fun activateAimbot(isActive: Boolean)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        floatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating, null)
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP or Gravity.START; params.x = 100; params.y = 200
        windowManager.addView(floatingView, params)

        Toast.makeText(this, getNativeStatus(), Toast.LENGTH_LONG).show()

        floatingView.findViewById<TextView>(R.id.btnCloseMenu).setOnClickListener {
            windowManager.removeView(floatingView); stopSelf()
        }
        val rootLayout = floatingView.findViewById<View>(R.id.root_floating)
        rootLayout.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = params.x; initialY = params.y; initialTouchX = event.rawX; initialTouchY = event.rawY; true
                }
                MotionEvent.ACTION_MOVE -> {
                    params.x = initialX + (event.rawX - initialTouchX).toInt(); params.y = initialY + (event.rawY - initialTouchY).toInt()
                    windowManager.updateViewLayout(floatingView, params); true
                }
                else -> false
            }
        }
        floatingView.findViewById<Switch>(R.id.swAimbot).setOnCheckedChangeListener { _, isChecked -> activateAimbot(isChecked) }
    }
    override fun onDestroy() { super.onDestroy(); if (::floatingView.isInitialized) windowManager.removeView(floatingView) }
}
