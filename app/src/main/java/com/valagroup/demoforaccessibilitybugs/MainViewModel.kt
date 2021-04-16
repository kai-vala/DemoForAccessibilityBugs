package com.valagroup.demoforaccessibilitybugs

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.adapters.ListenerUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.transition.TransitionManager
import com.google.android.material.tabs.TabLayout

class MainViewModel : ViewModel() {
    val tabIndex = MutableLiveData<Int>().apply { value = 0 }
    val buttonState = MutableLiveData<Boolean>().apply { value = true }

    fun toggleButtonState() {
        buttonState.value = !buttonState.value!!
    }

    init {
        Log.d("MainViewModel", "init")
    }
}

@BindingAdapter("tabIndex")
fun tabIndexSet(tabLayout: TabLayout, index: Int) {
    Log.d("BindingAdapter", "Selecting tabIndex: $index")
    tabLayout.getTabAt(index)?.select()
}

@InverseBindingAdapter(attribute = "tabIndex")
fun tabIndex(tabLayout: TabLayout): Int {
    Log.d("InverseBindingAdapter", "selectedTabPosition: ${tabLayout.selectedTabPosition}")
    return tabLayout.selectedTabPosition
}

@BindingAdapter("visible")
fun visible(view: View, visible: Boolean?) {
    Log.d("BindingAdapter", "Changing visibility of view '${view.id}' to: $visible")
    view.visibility = if (visible == true) View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["tabIndexListener", "tabIndexAttrChanged"], requireAll = false)
fun setTabChangeListeners(
    tabLayout: TabLayout,
    tabSelectedListener: TabLayout.OnTabSelectedListener?,
    inverseBindingListener: InverseBindingListener?
) {

    val newListener: TabLayout.OnTabSelectedListener?

    if (inverseBindingListener == null) {
        newListener = tabSelectedListener
    } else {
        newListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                Log.d("BindingAdapter", "onTabUnselected: ${tab.text}")
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.d("BindingAdapter", "onTabSelected: ${tab.text}")
                tabLayout.findParent<ConstraintLayout>()?.let {
                    TransitionManager.beginDelayedTransition(it)
                }
                tabSelectedListener?.onTabSelected(tab)
                inverseBindingListener.onChange()
            }
        }
    }

    // remove old listener
    ListenerUtil.trackListener(tabLayout, newListener, R.id.tab_index_listener_id)?.apply {
        tabLayout.removeOnTabSelectedListener(this)
    }

    // add new listener
    newListener?.apply {
        tabLayout.addOnTabSelectedListener(this)
    }
}

inline fun <reified T : ViewGroup> View.findParent(): T? {
    var view = this.parent
    while (view != null) {
        if (view is T)
            return view
        view = view.parent
    }
    return null
}