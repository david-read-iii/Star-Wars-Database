package com.davidread.starwarsdatabase.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.davidread.starwarsdatabase.databinding.FragmentPersonDetailBinding

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
     * Invoked when this fragment's view is to be created. Does nothing for now.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
}