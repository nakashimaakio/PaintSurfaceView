package com.photograph.paintexample

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.photograph.paintexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding

	@SuppressLint("ClickableViewAccessibility")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val customSurfaceView = CustomSurfaceView(this, binding.surfaceView)
		binding.surfaceView.setOnTouchListener { _, event ->
			customSurfaceView.onTouch(event)
		}
	}
}