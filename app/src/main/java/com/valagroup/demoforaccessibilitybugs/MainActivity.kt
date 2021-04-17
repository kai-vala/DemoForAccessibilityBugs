package com.valagroup.demoforaccessibilitybugs

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionManager
import com.valagroup.demoforaccessibilitybugs.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.vm = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.lifecycleOwner = this

        binding.toggleButton.setOnClickListener { v ->
            if (v != null) {
                Log.d("OnClick", "toggleButton")
                val viewModel = binding.vm as MainViewModel
                viewModel.toggleButtonState()
                binding.toggleButton.findParent<ConstraintLayout>()?.let {
                    // TODO/FIXME: The issue where edit texts are not enabled is caused by the transition
                    TransitionManager.beginDelayedTransition(it)
                }
            }
        }
    }
}

@BindingAdapter("visible")
fun visible(view: View, visible: Boolean?) {
    Log.d("BindingAdapter", "Changing visibility of view '${view.id}' to: $visible")
    view.visibility = if (visible == true) View.VISIBLE else View.GONE
}

private inline fun <reified T : ViewGroup> View.findParent(): T? {
    var view = this.parent
    while (view != null) {
        if (view is T)
            return view
        view = view.parent
    }
    return null
}
