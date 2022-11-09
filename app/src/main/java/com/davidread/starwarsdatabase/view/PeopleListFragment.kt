package com.davidread.starwarsdatabase.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.davidread.starwarsdatabase.di.ApplicationController
import com.davidread.starwarsdatabase.databinding.FragmentPeopleListBinding
import com.davidread.starwarsdatabase.viewmodel.PeopleListFragmentViewModel
import com.davidread.starwarsdatabase.viewmodel.PeopleListFragmentViewModelImpl
import javax.inject.Inject

/**
 * Fragment representing a list of entries in the people category.
 */
class PeopleListFragment : Fragment() {

    /**
     * Factory for instantiating `ViewModel` instances.
     */
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    /**
     * Binding object for this fragment's layout.
     */
    private val binding: FragmentPeopleListBinding by lazy {
        FragmentPeopleListBinding.inflate(layoutInflater)
    }

    /**
     * Exposes state to the UI and encapsulates business logic for this fragment.
     */
    private val viewModel: PeopleListFragmentViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PeopleListFragmentViewModelImpl::class.java]
    }

    /**
     * Invoked when this fragment is attached to it's associated activity. It just requests
     * dependency injection.
     */
    override fun onAttach(context: Context) {
        (activity?.application as ApplicationController).applicationGraph.inject(this)
        super.onAttach(context)
    }

    /**
     * Invoked when this fragment's view is to be created. Sets up some dummy UI for now.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupObserver()
        return binding.root
    }

    /**
     * Sets up an observer to populate the UI with a dummy string for now.
     */
    private fun setupObserver() {
        viewModel.testLiveData.observe(viewLifecycleOwner) {
            binding.textView.text = it
        }
    }
}