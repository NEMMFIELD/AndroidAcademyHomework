package com.example.androidacademyhomework.data.model.viewholder

import androidx.fragment.app.Fragment

interface ClickListener {
        fun changeFragment(showedFragment: Fragment, movie: ResultsItem?)
}