package com.example.android.marsrealestate.clickListener

import com.example.android.marsrealestate.network.MarsProperty

interface DetailsClickListener {
    fun sendDetails(detailItems: MarsProperty)
}