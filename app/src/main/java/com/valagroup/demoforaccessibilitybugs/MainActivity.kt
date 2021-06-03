package com.valagroup.demoforaccessibilitybugs

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.valagroup.demoforaccessibilitybugs.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        //binding.vm = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.lifecycleOwner = this

        binding.toggleButton.setOnClickListener { v ->
            if (v != null) {
                Log.d("OnClick", "toggleButton")
                showFragment(MainFragment<String>())
            }
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
