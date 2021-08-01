/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.marsrealestate.R
import com.example.android.marsrealestate.adaper.MarsAdapter
import com.example.android.marsrealestate.clickListener.DetailsClickListener
import com.example.android.marsrealestate.databinding.FragmentOverviewBinding
import com.example.android.marsrealestate.network.MarsApiFilter
import com.example.android.marsrealestate.network.MarsProperty

/**
 * This fragment shows the the status of the Mars real-estate web services transaction.
 */
class OverviewFragment : Fragment(), DetailsClickListener {


    lateinit var marsRecyclerView : RecyclerView
    lateinit var marsAdapter: MarsAdapter
    /**
     * Lazily initialize our [OverviewViewModel].
     */
    private val viewModel: OverviewViewModel by lazy {
        ViewModelProvider(this).get(OverviewViewModel::class.java)
    }

    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentOverviewBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        marsRecyclerView = binding.recyclerViewOverview


        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel


        viewModel.response.observe(this, Observer {
            marsAdapter = MarsAdapter(this)
            marsAdapter.newListOfMars(it)
            marsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            marsRecyclerView.adapter = marsAdapter
        })

        viewModel.status.observe(this, Observer {
            loadingStatus(binding.ErrorImage, it)
        })

        viewModel.response

        setHasOptionsMenu(true)
        return binding.root
    }

    /**
     * Inflates the overflow menu that contains filtering options.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun loadingStatus(statusImageView: ImageView, statusState: OverviewViewModel.MarsResponseState) {
        when(statusState) {
            OverviewViewModel.MarsResponseState.LOADING -> {
                statusImageView.visibility = View.VISIBLE
                statusImageView.setImageResource(R.drawable.loading_animation)
            }
            OverviewViewModel.MarsResponseState.FAILURE -> {
                statusImageView.visibility = View.VISIBLE
                statusImageView.setImageResource((R.drawable.ic_connection_error))
            }
            OverviewViewModel.MarsResponseState.SUCCESS -> {
                statusImageView.visibility = View.GONE
            }
        }
    }

    override fun sendDetails(detailItems: MarsProperty) {
        viewModel.displayMarsDetails(detailItems)

        viewModel.navigateDetails.observe(this, Observer {
            if(it != null) {
                val action = OverviewFragmentDirections.actionShowDetail(it)
                findNavController().navigate(action)
                viewModel.displayMarsDetailsComplete()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
                when(item.itemId) {
                    R.id.show_rent_menu -> MarsApiFilter.SHOW_RENT
                    R.id.show_buy_menu -> MarsApiFilter.SHOW_BUY
                    else -> MarsApiFilter.SHOW_ALL
                }
        )
        return true
    }
}

//Glide.with(context!!).load(it.imgSrcUrl).apply(RequestOptions().placeholder(R.drawable.loading_animation)
//.error(R.drawable.ic_broken_image)).into(binding.marsImages)
