package com.example.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.data.model.CastMember
import com.example.data.model.Movie
import com.example.ui.navigation.Details
import com.example.ui.theme.DarkGrey
import com.example.ui.theme.PrimaryRed
import androidx.navigation.toRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    movieId: Int,
    onBack: () -> Unit,
    onWatchNow: (Int) -> Unit,
    viewModel: DetailsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.fetchDetails(movieId)
    }

    Scaffold(
        containerColor = Color.Black
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = uiState) {
                is DetailsUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = PrimaryRed)
                }
                is DetailsUiState.Success -> {
                    val movie = state.movie
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            DetailsHeader(movie, onBack)
                        }
                        item {
                            MovieInfo(movie)
                        }
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                            CastSection(state.cast)
                        }
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                            OverviewSection(movie.overview)
                        }
                        item {
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                    
                    // Fixed Play Button at Bottom
                    Button(
                        onClick = { onWatchNow(movie.id) },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Watch Now", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
                is DetailsUiState.Error -> {
                    Text(
                        state.message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun DetailsHeader(movie: Movie, onBack: () -> Unit) {
    Box(modifier = Modifier.height(500.dp).fillMaxWidth()) {
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = 800f
                    )
                )
        )
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(top = 48.dp, start = 16.dp)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
        }
    }
}

@Composable
fun MovieInfo(movie: Movie) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = movie.releaseDate?.take(4) ?: "", color = Color.Gray)
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .background(Color.DarkGray, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(text = "HD", color = Color.LightGray, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Rating: ${movie.voteAverage}", color = PrimaryRed, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CastSection(cast: List<CastMember>) {
    Column {
        Text(
            text = "Cast",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(cast) { member ->
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(80.dp)) {
                    AsyncImage(
                        model = member.profileUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = member.name,
                        color = Color.White,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun OverviewSection(overview: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Storyline",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = overview,
            color = Color.LightGray,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 24.sp
        )
    }
}
