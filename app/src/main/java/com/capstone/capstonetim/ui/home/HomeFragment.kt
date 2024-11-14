package com.capstone.capstonetim.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.capstonetim.R
import com.capstone.capstonetim.databinding.FragmentHomeBinding
import com.capstone.capstonetim.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.toList

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private lateinit var placesAdapter: PlacesAdapter

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var pref: SessionManager
    private lateinit var token: String
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(LayoutInflater.from(requireActivity()))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = SessionManager(requireContext())
        token = pref.fetchAuthToken().toString()
        username = pref.getUsername().toString()

        initSwipeRefreshLayout()
        initAction()
        initStoryRecyclerView()
        getAllPlaces()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAction() {

        binding?.buttonMenu?.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_navigation_profile)
        )

    }


    private fun getAllPlaces() {
        lifecycleScope.launchWhenResumed {
            try {
                val result = homeViewModel.getPlaces().toList().first()

                result.onSuccess { placesResult ->
                    placesAdapter.setPlaces(placesResult)
                }

                result.onFailure { exception ->

                }
            } catch (e: Exception) {

            } finally {
                 binding?.swipeRefresh?.isRefreshing = false
            }
        }
    }

    private fun initSwipeRefreshLayout() {
        binding!!.swipeRefresh.setOnRefreshListener {
            getAllPlaces()
        }
    }


    private fun initStoryRecyclerView() {
        val horizontalLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        placesAdapter = PlacesAdapter()

        binding!!.rvPlaces.apply {
            adapter = placesAdapter
            layoutManager = horizontalLayoutManager
        }

        placesAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                horizontalLayoutManager.scrollToPosition(0)
            }
        })
    }


}