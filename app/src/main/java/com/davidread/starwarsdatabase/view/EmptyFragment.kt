package com.davidread.starwarsdatabase.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.davidread.starwarsdatabase.databinding.FragmentEmptyBinding

/**
 * Fragment representing an empty top-level category fragment destination.
 */
class EmptyFragment : Fragment() {

    /**
     * Binding object for this fragment's layout.
     */
    private val binding: FragmentEmptyBinding by lazy {
        FragmentEmptyBinding.inflate(layoutInflater)
    }

    /**
     * Arguments passed into this fragment.
     */
    private val arguments: EmptyFragmentArgs by navArgs()

    /**
     * Invoked when this fragment's view is to be created. It displays some detail text in the UI
     * and returns the fragment's view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.textView.text = getString(arguments.detailText)
        return binding.root
    }
}