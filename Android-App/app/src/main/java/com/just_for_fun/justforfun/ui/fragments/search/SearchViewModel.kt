package com.just_for_fun.justforfun.ui.fragments.search

import androidx.lifecycle.ViewModel
import com.just_for_fun.justforfun.R

class SearchViewModel : ViewModel() {
    val mostSearchedItems = listOf(
        R.drawable.ddlj_poster,
        R.drawable.mm_poster,
        R.drawable.lagaan_poster,
        R.drawable.kranti_poster,
        R.drawable.baazigar_poster
    )

    val previousSearches = listOf(
        R.drawable.kranti_poster,
        R.drawable.mm_poster,
        R.drawable.ddlj_poster,
        R.drawable.baazigar_poster,
        R.drawable.lagaan_poster
    )

    val basedOnYourSearchItems = listOf(
        R.drawable.baazigar_poster,
        R.drawable.ddlj_poster,
        R.drawable.lagaan_poster,
        R.drawable.mm_poster,
        R.drawable.kranti_poster
    )
}