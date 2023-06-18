package com.davidread.starwarsdatabase.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.davidread.starwarsdatabase.databinding.FragmentEmptyDetailsBinding

/**
 * Fragment representing a resource details screen where no resource has been selected yet.
 */
class EmptyDetailsFragment : Fragment() {

    /**
     * Binding object for this fragment's layout.
     */
    private val binding: FragmentEmptyDetailsBinding by lazy {
        FragmentEmptyDetailsBinding.inflate(layoutInflater)
    }

    /**
     * Invoked when this fragment's view is to be created. It just returns the fragment's view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root
}