package com.davidread.starwarsdatabase.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.davidread.starwarsdatabase.databinding.FragmentPersonDetailBinding
import com.davidread.starwarsdatabase.di.ApplicationController
import com.davidread.starwarsdatabase.viewmodel.PersonDetailViewModel
import com.davidread.starwarsdatabase.viewmodel.PersonDetailViewModelImpl
import javax.inject.Inject

/**
 * Fragment representing the detail view of a person.
 */
class PersonDetailFragment : Fragment() {

    /**
     * Factory for instantiating `ViewModel` instances.
     */
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    /**
     * Binding object for this fragment's layout.
     */
    private val binding: FragmentPersonDetailBinding by lazy {
        FragmentPersonDetailBinding.inflate(layoutInflater)
    }

    /**
     * Exposes state to the UI and encapsulates business logic for this fragment.
     */
    private val viewModel: PersonDetailViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PersonDetailViewModelImpl::class.java]
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
     * Invoked when this fragment's view is to be created. It sets up an observer to
     * [DetailListAdapter]'s dataset and returns the fragment's view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupObserver()
        // TODO: Find way to receive id from PeopleListFragment.
        if (savedInstanceState == null) {
            viewModel.getPerson(1)
        }
        return binding.root
    }

    /**
     * Sets up an observer to [DetailListAdapter]'s dataset.
     */
    private fun setupObserver() {
        viewModel.personDetailListItemsLiveData.observe(viewLifecycleOwner) { personDetailListItems ->
            binding.personDetailList.apply {
                adapter = DetailListAdapter(personDetailListItems)
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }
    }
}