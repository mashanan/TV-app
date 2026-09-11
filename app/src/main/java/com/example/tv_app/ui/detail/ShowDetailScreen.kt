package com.example.tv_app.ui.detail

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.compose.AsyncImage
import com.example.tv_app.data.model.CastCredit
import com.example.tv_app.data.model.Show
import com.example.tv_app.ui.common.UiState
import com.example.tv_app.util.htmlToPlainText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDetailScreen(
    showId: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ShowDetailViewModel = viewModel(
        factory = viewModelFactory {
            initializer { ShowDetailViewModel(showId) }
        }
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Show Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    val state = uiState
                    if (state is UiState.Success) {
                        IconButton(onClick = { shareShow(context, state.data) }) {
                            Icon(Icons.Filled.Share, contentDescription = "Share")
                        }
                    }
                }
            )
        }
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
                is UiState.Success -> ShowDetailContent(show = state.data)
            }
        }
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Failed to load show")
        Text(text = message)
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
private fun ShowDetailContent(show: Show) {
    val episodesBySeason = show.embedded?.episodes?.groupBy { it.season }?.toSortedMap()
    val cast = show.embedded?.cast

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            AsyncImage(
                model = show.image?.original,
                contentDescription = show.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item { Text(text = show.name, style = MaterialTheme.typography.headlineSmall) }
        item { Text(text = "Premiered: ${show.premiered ?: "Unknown"}") }
        item { Text(text = htmlToPlainText(show.summary)) }

        if (!episodesBySeason.isNullOrEmpty()) {
            item {
                Text(
                    text = "Episodes",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            episodesBySeason.forEach { (season, episodes) ->
                item {
                    Text(
                        text = "Season $season (${episodes.size} episodes)",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                items(episodes, key = { it.id }) { episode ->
                    Text(text = "Ep ${episode.number ?: "-"}: ${episode.name}")
                }
            }
        }

        if (!cast.isNullOrEmpty()) {
            item {
                Text(
                    text = "Cast",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(cast, key = { it.person.id }) { credit ->
                        CastMemberItem(credit)
                    }
                }
            }
        }
    }
}

@Composable
private fun CastMemberItem(credit: CastCredit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        AsyncImage(
            model = credit.person.image?.medium,
            contentDescription = credit.person.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(32.dp))
        )
        Text(
            text = credit.person.name,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1
        )
        Text(
            text = credit.character.name,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1
        )
    }
}

private fun shareShow(context: Context, show: Show) {
    val shareText = buildString {
        appendLine(show.name)
        appendLine()
        appendLine(htmlToPlainText(show.summary))
        appendLine()
        append(show.url.orEmpty())
    }
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, show.name)
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(Intent.createChooser(sendIntent, "Share via"))
}
