package com.davidread.starwarsdatabase.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.davidread.starwarsdatabase.R
import com.davidread.starwarsdatabase.databinding.FragmentPersonDetailBinding
import com.davidread.starwarsdatabase.model.view.DetailListItem

/**
 * Fragment representing the detail view of a person.
 */
class PersonDetailFragment : Fragment() {

    /**
     * Binding object for this fragment's layout.
     */
    private val binding: FragmentPersonDetailBinding by lazy {
        FragmentPersonDetailBinding.inflate(layoutInflater)
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
        val dummyDetailListItems = listOf(
            DetailListItem(getString(R.string.name_detail_label), "Luke Skywalker"),
            DetailListItem(getString(R.string.homeworld_detail_label), "Tatooine"),
            DetailListItem(getString(R.string.birth_year_detail_label), "19BBY"),
            DetailListItem(getString(R.string.species_detail_label), ""),
            DetailListItem(getString(R.string.gender_detail_label), "male"),
            DetailListItem(getString(R.string.height_detail_label), "172"),
            DetailListItem(getString(R.string.mass_detail_label), "77"),
            DetailListItem(getString(R.string.hair_color_detail_label), "blond"),
            DetailListItem(getString(R.string.eye_color_detail_label), "blue"),
            DetailListItem(getString(R.string.skin_color_detail_label), "fair"),
            DetailListItem(
                getString(R.string.films_detail_label),
                "A New Hope, The Empire Strikes Back, Return of the Jedi, Revenge of the Sith"
            ),
            DetailListItem(getString(R.string.starships_detail_label), "X-wing, Imperial shuttle"),
            DetailListItem(getString(R.string.vehicles_detail_label), "Snowspeeder, Imperial Speeder Bike")
        )
        binding.personDetailList.apply {
            adapter = DetailListAdapter(dummyDetailListItems)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        return binding.root
    }
}