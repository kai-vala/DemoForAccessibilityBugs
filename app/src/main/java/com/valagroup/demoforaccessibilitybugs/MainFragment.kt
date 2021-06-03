package com.valagroup.demoforaccessibilitybugs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.valagroup.demoforaccessibilitybugs.databinding.FragmentMainBinding
import kotlin.concurrent.fixedRateTimer

class MainFragment<T> : Fragment() {

    lateinit var binding: FragmentMainBinding
    private val vm: MainViewModel<T> by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.titleText.text = "Main fragment"
        vm.getData().value?.let {
            binding.editText.setText(it as String)
        }

        binding.editText.doAfterTextChanged {
            if (vm.getData().value != it.toString()) {
                vm.storeData(it.toString() as T)
            }
        }

        vm.text.observe(viewLifecycleOwner, {
            if (binding.editText.text.toString() != it) {
                binding.editText.setText(it)
            }
        })

        binding.button.setOnClickListener {
            val fragment = ChildFragment<String>()
            this.childFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_modal_container, fragment, fragment::class.java.name)
                .also { it.addToBackStack(fragment::class.java.name) }
                .commitAllowingStateLoss()
        }

        binding.closeButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        super.onViewCreated(view, savedInstanceState)
    }
}

class ChildFragment<T> : Fragment() {

    lateinit var binding: FragmentMainBinding
    private val vm: MainViewModel<T> by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.titleText.text = "Child fragment"

        vm.getData().value?.let {
            binding.editText.setText(it as String)
        }

        binding.editText.doAfterTextChanged {
            vm.storeData(it.toString() as T)
        }

        binding.button.text = "Save"
        binding.button.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.closeButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        super.onViewCreated(view, savedInstanceState)
    }
}