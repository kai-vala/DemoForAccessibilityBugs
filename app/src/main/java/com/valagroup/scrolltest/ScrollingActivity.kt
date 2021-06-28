package com.valagroup.scrolltest

import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.valagroup.scrolltest.databinding.ActivityScrollingBinding

class ScrollingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScrollingBinding
    private var keyEventHandler: AppBarLayoutKeyboardHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title

        // 1. Create a handler that allows us to collapse/expand the layout with keyboard navigation
        keyEventHandler = AppBarLayoutKeyboardHandler.setupIfNeeded(binding.rootCoordinator, binding.appBar)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event != null) {
            // 2. Dispatch keyboard events to handler
            keyEventHandler?.handleKeyEvent(event)
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }
}