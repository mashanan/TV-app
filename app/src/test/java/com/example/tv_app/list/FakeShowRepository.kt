package com.example.tv_app.list

import com.example.tv_app.data.model.Show
import com.example.tv_app.data.repository.ShowRepository

class FakeShowRepository(
    private val shows: List<Show> = emptyList(),
    private val error: Throwable? = null
) : ShowRepository {

    override suspend fun getShows(): List<Show> {
        error?.let { throw it }
        return shows
    }

    override suspend fun getShowDetail(id: Int): Show {
        error?.let { throw it }
        return shows.first { it.id == id }
    }
}
