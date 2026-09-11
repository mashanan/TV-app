package com.example.tv_app.data.repository

import com.example.tv_app.data.model.Show

interface ShowRepository {
    suspend fun getShows(): List<Show>
    suspend fun getShowDetail(id: Int): Show
}
