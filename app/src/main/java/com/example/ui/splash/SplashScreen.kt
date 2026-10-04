package com.example.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.PrimaryRed
import kotlinx.coroutines.delay

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import com.example.R

@Composable
fun SplashScreen(onNavigateToMain: () -> Unit) {
    val scale = remember { Animatable(1.0f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        // Alpha fade in
        alpha.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(durationMillis = 1000)
        )
        // Scale up animation
        scale.animateTo(
            targetValue = 1.3f,
            animationSpec = tween(durationMillis = 1500)
        )
        onNavigateToMain()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.royal_flix_new_logo_1791025395430),
            contentDescription = "Royal Flix Logo",
            modifier = Modifier
                .size(240.dp)
                .scale(scale.value),
            alpha = alpha.value
        )

        Text(
            text = "Created by Hamid Coder",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge.copy(
                shadow = androidx.compose.ui.graphics.Shadow(
                    color = PrimaryRed,
                    blurRadius = 8f
                )
            )
        )
    }
}
