package com.davidread.starwarsdatabase.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.davidread.starwarsdatabase.R
import com.davidread.starwarsdatabase.databinding.FragmentPersonDetailBinding
import com.davidread.starwarsdatabase.di.ApplicationController
import com.davidread.starwarsdatabase.model.view.DetailListItem
import com.davidread.starwarsdatabase.model.view.PersonListItem
import com.davidread.starwarsdatabase.viewmodel.PeopleViewModel
import com.davidread.starwarsdatabase.viewmodel.PeopleViewModelImpl
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
    private val viewModel: PeopleViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PeopleViewModelImpl::class.java]
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
     * Invoked when this fragment's view is to be created. It initializes the details list with
     * dummy data and returns the fragment's view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val personItemPosition = viewModel.selectedPersonItemPosition
        val personListItems = viewModel.personListItemsLiveData.value
        if (personItemPosition != null && personListItems != null) {
            val personItem = personListItems[personItemPosition] as PersonListItem.PersonItem
            val detailListItems = listOf(
                DetailListItem(getString(R.string.name_detail_label), personItem.name),
                DetailListItem(getString(R.string.homeworld_detail_label), personItem.homeworld),
                DetailListItem(getString(R.string.birth_year_detail_label), personItem.birthYear),
                DetailListItem(getString(R.string.species_detail_label), personItem.species.toString()),
                DetailListItem(getString(R.string.gender_detail_label), personItem.gender),
                DetailListItem(getString(R.string.height_detail_label), personItem.height),
                DetailListItem(getString(R.string.mass_detail_label), personItem.mass),
                DetailListItem(getString(R.string.hair_color_detail_label), personItem.hairColor),
                DetailListItem(getString(R.string.eye_color_detail_label), personItem.eyeColor),
                DetailListItem(getString(R.string.skin_color_detail_label), personItem.skinColor),
                DetailListItem(getString(R.string.films_detail_label), personItem.films.toString()),
                DetailListItem(getString(R.string.starships_detail_label), personItem.starships.toString()),
                DetailListItem(getString(R.string.vehicles_detail_label), personItem.vehicles.toString())
            )
            binding.personDetailList.apply {
                adapter = DetailListAdapter(detailListItems)
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }
        return binding.root
    }
}