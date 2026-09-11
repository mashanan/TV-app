package com.example.tv_app.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tv_app.data.model.Show
import com.example.tv_app.ui.common.UiState
import com.example.tv_app.util.formatRating

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowListScreen(
    onShowClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ShowListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("TV Shows") }) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Error -> ErrorContent(
                    message = state.message,
                    onRetry = viewModel::retry
                )
                is UiState.Success -> ShowList(
                    shows = state.data,
                    onShowClick = onShowClick
                )
            }
        }
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Failed to load shows")
        Text(text = message)
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
private fun ShowList(shows: List<Show>, onShowClick: (Int) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(shows, key = { it.id }) { show ->
            ShowRow(show = show, onClick = { onShowClick(show.id) })
        }
    }
}

@Composable
private fun ShowRow(show: Show, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AsyncImage(
            model = show.image?.medium,
            contentDescription = show.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 60.dp, height = 90.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Column {
            Text(text = show.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "★ ${formatRating(show.rating?.average)}")
        }
    }
}
