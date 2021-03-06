package com.example.androidacademyhomework.data.model.viewholder

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidacademyhomework.network.API_KEY
import com.example.androidacademyhomework.network.LANGUAGE
import com.example.androidacademyhomework.network.MoviesApi

class MoviePagingSource(private val movieApiService:MoviesApi): PagingSource<Int, ResultsItem>() {
    override fun getRefreshKey(state: PagingState<Int, ResultsItem>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultsItem> {
        return try {
            val nextPage = params.key ?: 1
            val response = movieApiService.getNowPlaying(API_KEY, LANGUAGE, nextPage)
            LoadResult.Page(
                data = response.results as List<ResultsItem>,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = response.page?.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }
}
