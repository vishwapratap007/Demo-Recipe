package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
    viewModel: RecipeViewmodel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is RecipeUIState.Success -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    items = state.recipes,
                    key = { recipe -> recipe.id },
                    contentType = { "recipe_card" }
                ) { recipe ->
                    RecipeCard(recipe = recipe)
                }
            }
        }

        else -> {}
    }

}

@Composable
fun RecipeCard(recipe: Recipe) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = recipe.image,
                contentDescription = "Description for accessibility",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit
            )
            Column(modifier = Modifier)
            {
                Text(text = "Name:${recipe.name}")
                Text(text = "Ingredients:${recipe.ingredients.joinToString()}")
            }
        }
    }
}

/*
* 1. LaunchedEffect: suspend function and Composable function are used in LaunchedEffect.
    • What it is: Runs a suspend function bound to the lifecycle of a Composable.
    • When to use: For asynchronous work triggered by initial composition or when key values
      change (e.g., fetching data, showing a Snackbar, triggering animations).
    • Key Concept: Automatically cancels its coroutine when leaving composition or when any
      key changes.*/
@Composable
fun UserProfileScreen(userId: String, snackbarHostState: SnackbarHostState) {
    // Re-executes whenever `userId` changes
    LaunchedEffect(userId) {
//        val user = repository.getUser(userId)
//        if (user == null) {
            snackbarHostState.showSnackbar("User not found!")
//        }
    }
}

/*
2. rememberCoroutineScope
• What it is: Obtains a CoroutineScope bound to the Composable's lifecycle that can be called
    outside composition (in user event callbacks).
• When to use: To launch coroutines in response to user actions like onClick, button taps, or
    gesture callbacks.
• Senior Tip: Never launch coroutines directly in the Composable body using
    rememberCoroutineScope—use LaunchedEffect for that!*/
@Composable
fun ScrollToTopButton(listState: LazyListState) {
    val scope = rememberCoroutineScope()

    Button(onClick = {
        // Safe to call suspend functions inside event callbacks
        scope.launch {
            listState.animateScrollToItem(0)
        }
    }) {
        Text("Top")
    }
}

/*3. DisposableEffect
• What it is: A side-effect that requires cleanup when the keys change or when the Composable
    leaves composition.
• When to use: Registering/unregistering listeners, observers, receivers, or hardware callbacks
    (e.g., LifecycleObserver, BroadcastReceiver, LocationListener).
• Key Concept: Must always end with an onDispose { ... } block.*/
@Composable
fun SystemBackHandler(onBack: () -> Unit) {
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                // Handle broadcast
            }
        }
        ContextCompat.registerReceiver(
            context,
            receiver,
            IntentFilter("MY_ACTION"),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        // Cleanup block called on disposal
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }
}


/*4. SideEffect
• What it is: Executes a block of code on every successful recomposition.
• When to use: To publish state changes to non-Compose managed code (e.g., updating external
    analytics, legacy View properties, or system UI).
• Why not write raw code in the body? Code directly in the Composable body executes before
    composition completes and might execute even if composition fails or is discarded. SideEffect
    guarantees execution only after a successful composition.*/
@Composable
fun AnalyticsTracker(screenName: String, userStatus: String) {
    // Guarantees external tracking library gets updated ONLY on successful recomposition
    SideEffect {
//        LegacyAnalyticsLibrary.setCustomUserAttribute("status", userStatus)
    }
}

/*5. rememberUpdatedState
• What it is: Captures and references a value (like a lambda callback or state) in a long-running
    effect without restarting the effect when that value changes.
• When to use: When a LaunchedEffect or DisposableEffect contains a long-running operation
    (like a timer or delay) and uses a parameter that might change over time.*/
@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    // Capture the latest reference of onTimeout
    val currentOnTimeout by rememberUpdatedState(onTimeout)

    // Keyed to Unit so it only runs ONCE on launch and never restarts
    LaunchedEffect(Unit) {
        delay(3000L.milliseconds)
        currentOnTimeout() // Calls the latest lambda passed to SplashScreen!
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}