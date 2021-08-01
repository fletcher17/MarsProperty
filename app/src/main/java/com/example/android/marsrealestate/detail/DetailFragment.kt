/*
 *  Copyright 2018, The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.android.marsrealestate.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.marsrealestate.R
import com.example.android.marsrealestate.databinding.FragmentDetailBinding

/**
 * This [Fragment] will show the detailed information about a selected piece of Mars real estate.
 */
class DetailFragment : Fragment() {
    lateinit var imagePhotoDetails: ImageView
    lateinit var propertyTypeDetails: TextView
    lateinit var valueDetails: TextView

    val args: DetailFragmentArgs by navArgs()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        @Suppress("UNUSED_VARIABLE")
        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        imagePhotoDetails = binding.mainPhotoImage
        propertyTypeDetails = binding.propertyTypeText
        valueDetails = binding.priceValueText



        val detailsReceived = args.marsDetails
        //val detailsReceived = DetailFragmentArgs.fromBundle(arguments!!).marsDetails

        Glide.with(context!!).load(detailsReceived?.imgSrcUrl).apply(RequestOptions().placeholder(R.drawable.loading_animation).error(R.drawable.ic_broken_image))
                .into(imagePhotoDetails)

        propertyTypeDetails.text = detailsReceived?.type
        valueDetails.text = detailsReceived?.price.toString()





        return binding.root
    }
}
