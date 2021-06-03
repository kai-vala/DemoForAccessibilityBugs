package com.valagroup.demoforaccessibilitybugs

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.valagroup.demoforaccessibilitybugs.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val vm: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        binding.openButton.setOnClickListener { v ->
            if (v != null) {
                showFragment(MainFragment())
            }
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs).also {
            vm.fragmentCount.observe(this, {
                if (it > 0) {
                    binding.container.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS
                } else {
                    binding.container.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_AUTO
                }
            })
        }
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.modal_container, fragment, fragment::class.java.name)
            .also { if (addToBackStack) it.addToBackStack(fragment::class.java.name) }
            .commitAllowingStateLoss()
    }
}