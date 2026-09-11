package com.example.tv_app.data.repository

import com.example.tv_app.data.model.Show
import com.example.tv_app.data.remote.NetworkModule
import com.example.tv_app.data.remote.TvMazeApiService

class ShowRepositoryImpl(
    private val api: TvMazeApiService = NetworkModule.api
) : ShowRepository {

    override suspend fun getShows(): List<Show> = api.getShows(page = 0)

    override suspend fun getShowDetail(id: Int): Show = api.getShowDetail(id)
}
