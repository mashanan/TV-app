package com.example.tv_app.data.remote

import com.example.tv_app.data.model.Show
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvMazeApiService {

    @GET("shows")
    suspend fun getShows(@Query("page") page: Int = 0): List<Show>

    @GET("shows/{id}")
    suspend fun getShowDetail(
        @Path("id") id: Int,
        @Query("embed[]") embed: List<String> = listOf("episodes", "cast")
    ): Show
}
