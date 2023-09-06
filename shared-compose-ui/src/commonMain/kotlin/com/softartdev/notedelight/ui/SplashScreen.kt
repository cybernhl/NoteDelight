package com.softartdev.notedelight.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.softartdev.notedelight.MR
import com.softartdev.notedelight.shared.presentation.splash.SplashResult
import com.softartdev.notedelight.shared.presentation.splash.SplashViewModel
import com.softartdev.notedelight.ui.dialog.showError
import com.softartdev.theme.pref.PreferableMaterialTheme.themePrefs
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun SplashScreen(splashViewModel: SplashViewModel, navSignIn: () -> Unit, navMain: () -> Unit) {
    val splashResultState: State<SplashResult> = splashViewModel.resultStateFlow.collectAsState()
    DisposableEffect(splashViewModel) {
        splashViewModel.checkEncryption()
        onDispose(splashViewModel::onCleared)
    }
    when (val splashResult: SplashResult = splashResultState.value) {
        is SplashResult.Loading -> Unit//TODO: progress bar
        is SplashResult.NavSignIn -> navSignIn()
        is SplashResult.NavMain -> navMain()
        is SplashResult.ShowError -> themePrefs.dialogHolder.showError(splashResult.message)
    }
    SplashScreenBody()
}

@Composable
fun SplashScreenBody() = Box(
    modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background)
) {
    Image(
        painter = painterResource(MR.images.app_icon),
        contentDescription = null,
        modifier = Modifier
            .align(Alignment.Center)
            .background(color = MaterialTheme.colorScheme.background)
    )
    themePrefs.showDialogIfNeed()
}

@Preview
@Composable
fun PreviewSplashScreen() = SplashScreenBody()