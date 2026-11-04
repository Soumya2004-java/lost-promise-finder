package com.lostpromisefinder.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.People
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFFF7F7FF)
private val Muted = Color(0xFFB5BAD1)
private val Paper = Color(0xFF0B1020)
private val Indigo = Color(0xFF9B8CFF)
private val IndigoSoft = Color(0xFF8C7CFF).copy(alpha = 0.22f)
private val Mint = Color(0xFF72E3B0).copy(alpha = 0.2f)
private val MintInk = Color(0xFF8EF0C3)
private val Amber = Color(0xFFFFC766).copy(alpha = 0.2f)
private val AmberInk = Color(0xFFFFD27A)
private val Border = Color.White.copy(alpha = 0.14f)
private val Glass = Color.White.copy(alpha = 0.09f)
private val GlassStrong = Color.White.copy(alpha = 0.15f)
private val GlassBright = Color.White.copy(alpha = 0.22f)

data class Promise(
    val title: String,
    val owner: String,
    val date: String,
    val tag: String,
    val status: PromiseStatus,
    val color: Color
)

enum class PromiseStatus { OVERDUE, DUE_SOON, UPCOMING, DONE }

private val demoPromises = listOf(
    Promise("Send revised budget to Priya", "You", "Due today", "Budget review", PromiseStatus.DUE_SOON, Amber),
    Promise("Confirm room booking for showcase", "You", "Due tomorrow", "Student org", PromiseStatus.UPCOMING, IndigoSoft),
    Promise("Review Q3 campaign brief", "Priya", "Due Nov 08", "Campaign", PromiseStatus.UPCOMING, Color(0xFFE5F4FF)),
    Promise("Share notes from Monday sync", "You", "Completed", "Team sync", PromiseStatus.DONE, Mint)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.statusBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        setContent { LostPromiseFinderApp() }
    }
}

@Composable
fun LostPromiseFinderApp() {
    var selectedTab by remember { mutableStateOf(0) }
    var showAnalyzer by remember { mutableStateOf(false) }
    var showGetStarted by remember { mutableStateOf(true) }

    MaterialTheme(
        colorScheme = androidx.compose.material3.darkColorScheme(
            primary = Indigo,
            onPrimary = Color.White,
            background = Paper,
            surface = Paper,
            onSurface = Ink,
            onBackground = Ink
        ),
        typography = androidx.compose.material3.Typography().run {
            copy(
                headlineLarge = headlineLarge.copy(fontSize = 32.sp, fontWeight = FontWeight.Bold, letterSpacing = (-1).sp),
                headlineSmall = headlineSmall.copy(fontSize = 23.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp),
                titleLarge = titleLarge.copy(fontSize = 19.sp, fontWeight = FontWeight.SemiBold),
                titleMedium = titleMedium.copy(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
                bodyLarge = bodyLarge.copy(fontSize = 16.sp, lineHeight = 23.sp),
                bodyMedium = bodyMedium.copy(fontSize = 14.sp, lineHeight = 20.sp),
                labelLarge = labelLarge.copy(fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            )
        }
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = Paper) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF0B1020), Color(0xFF171A3A), Color(0xFF0B1020))
                        )
                    )
            ) {
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .align(Alignment.TopEnd)
                        .background(Color(0xFF6F5CFF).copy(alpha = 0.18f), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(190.dp)
                        .align(Alignment.BottomStart)
                        .background(Color(0xFF37D6AF).copy(alpha = 0.10f), CircleShape)
                )
            if (showGetStarted) {
                GetStartedScreen(onGetStarted = { showGetStarted = false })
            } else if (showAnalyzer) {
                AnalyzerScreen(onBack = { showAnalyzer = false })
            } else {
                Scaffold(
                    containerColor = Color.Transparent,
                    bottomBar = {
                        AppNavBar(selectedTab = selectedTab, onSelected = { selectedTab = it })
                    }
                ) { padding ->
                    when (selectedTab) {
                        0 -> HomeScreen(
                            padding = padding,
                            onNewAnalysis = { showAnalyzer = true },
                            onViewAll = { selectedTab = 1 }
                        )
                        1 -> PromisesScreen(padding = padding)
                        else -> SettingsScreen(padding = padding)
                    }
                }
            }
            }
        }
    }
}

@Composable
private fun GetStartedScreen(onGetStarted: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(IndigoSoft)
                    .border(1.dp, Color.White.copy(alpha = 0.18f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = Indigo, modifier = Modifier.size(70.dp))
            }
            Spacer(modifier = Modifier.height(30.dp))
            Text("Keep every promise in sight.", color = Ink, style = MaterialTheme.typography.headlineLarge, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                "Capture commitments from your conversations, spot what needs attention, and follow through with confidence.",
                color = Muted,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = onGetStarted,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Indigo)
            ) {
                Text("Get started", color = Paper, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Rounded.ArrowForward, contentDescription = null, tint = Paper)
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text("Your commitments, made visible.", color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
private fun AppNavBar(selectedTab: Int, onSelected: (Int) -> Unit) {
    val items = listOf("Home", "Promises", "Settings")
    val icons = listOf(Icons.Rounded.Home, Icons.Rounded.Flag, Icons.Rounded.Settings)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 10.dp)
            .navigationBarsPadding()
            .shadow(18.dp, RoundedCornerShape(50.dp), ambientColor = Color.Black.copy(alpha = 0.34f), spotColor = Color.Black.copy(alpha = 0.34f))
            .clip(RoundedCornerShape(50.dp))
            .background(GlassStrong)
            .border(1.dp, Border, RoundedCornerShape(50.dp))
            .padding(7.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
        items.forEachIndexed { index, label ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(38.dp))
                    .background(if (selectedTab == index) GlassBright else Color.Transparent)
                    .clickable { onSelected(index) }
                    .padding(vertical = 9.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Icon(icons[index], contentDescription = label, tint = if (selectedTab == index) Color.White else Muted, modifier = Modifier.size(19.dp))
                Text(label, color = if (selectedTab == index) Color.White else Muted, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        }
    }
}

@Composable
private fun HomeScreen(padding: PaddingValues, onNewAnalysis: () -> Unit, onViewAll: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(start = 22.dp, end = 22.dp, top = 24.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Good morning", color = Muted, style = MaterialTheme.typography.bodyMedium)
                    Text("Make good on it.", color = Ink, style = MaterialTheme.typography.headlineLarge)
                }
                Box(
                    modifier = Modifier.size(48.dp).clip(CircleShape).background(GlassBright).border(1.dp, Border, CircleShape),
                    contentAlignment = Alignment.Center
                ) { Text("LP", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp) }
            }
        }
        item { HeroCard(onClick = onNewAnalysis) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                StatCard("4", "Open promises", IndigoSoft, Indigo, Modifier.weight(1f))
                StatCard("1", "Needs attention", Amber, AmberInk, Modifier.weight(1f))
                StatCard("12", "Closed this month", Mint, MintInk, Modifier.weight(1f))
            }
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("Needs your attention", style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                TextButton(onClick = onViewAll) { Text("View all", color = Indigo) }
            }
        }
        items(demoPromises.take(3), key = { it.title }) { promise -> PromiseCard(promise) }
        item { InsightCard() }
    }
}

@Composable
private fun HeroCard(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.12f)),
        shape = RoundedCornerShape(28.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.18f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.14f)).border(1.dp, Border, CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Rounded.AutoAwesome, null, tint = Color(0xFFFFD37A), modifier = Modifier.size(19.dp))
                    }
                    Spacer(Modifier.width(10.dp))
                    Text("AI NOTE ANALYSIS", color = Color(0xFFBFC8D7), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
                }
                Text("Turn messy notes\ninto momentum.", color = Color.White, style = MaterialTheme.typography.headlineSmall)
                Text("Find the promises hiding in your notes, then turn them into clear next steps.", color = Color(0xFFD0D3E4), style = MaterialTheme.typography.bodyMedium)
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.9f), contentColor = Color(0xFF25233E)),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Icon(Icons.Rounded.Add, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Analyze new notes")
                }
            }
            Icon(Icons.Rounded.ArrowForward, null, tint = Color(0xFF748093), modifier = Modifier.align(Alignment.TopEnd))
        }
    }
}

@Composable
private fun StatCard(number: String, label: String, background: Color, numberColor: Color, modifier: Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = background.copy(alpha = 0.85f)), shape = RoundedCornerShape(20.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Border), elevation = CardDefaults.cardElevation(0.dp)) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(number, color = numberColor, fontSize = 23.sp, fontWeight = FontWeight.Bold)
            Text(label, color = Color.White.copy(alpha = 0.68f), fontSize = 11.sp, lineHeight = 14.sp)
        }
    }
}

@Composable
private fun PromiseCard(promise: Promise) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Glass),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Border),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(42.dp).clip(RoundedCornerShape(13.dp)).background(promise.color), contentAlignment = Alignment.Center) {
                Icon(if (promise.status == PromiseStatus.DONE) Icons.Rounded.CheckCircle else Icons.Rounded.Schedule, null, tint = if (promise.status == PromiseStatus.DONE) MintInk else if (promise.status == PromiseStatus.DUE_SOON) AmberInk else Indigo, modifier = Modifier.size(21.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(promise.title, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(promise.owner, color = Muted, style = MaterialTheme.typography.bodyMedium)
                    Box(modifier = Modifier.size(3.dp).clip(CircleShape).background(Color(0xFFB9BDC5)))
                    Text(promise.tag, color = Muted, style = MaterialTheme.typography.bodyMedium)
                }
            }
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Text(promise.date, color = if (promise.status == PromiseStatus.DUE_SOON) AmberInk else if (promise.status == PromiseStatus.DONE) MintInk else Muted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Icon(Icons.Rounded.KeyboardArrowRight, null, tint = Color(0xFFB1B6C0), modifier = Modifier.size(19.dp))
            }
        }
    }
}

@Composable
private fun InsightCard() {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF8C7CFF).copy(alpha = 0.15f)), shape = RoundedCornerShape(20.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Border), elevation = CardDefaults.cardElevation(0.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.Top) {
            Icon(Icons.Rounded.AutoAwesome, null, tint = Indigo, modifier = Modifier.size(21.dp))
            Spacer(Modifier.width(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("A small nudge", color = Indigo, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("You have 2 promises due in the next 48 hours. Clear them before they become follow-ups.", color = Ink, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun PromisesScreen(padding: PaddingValues) {
    var filter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Due soon", "Upcoming", "Done")
    val filtered = when (filter) {
        "Due soon" -> demoPromises.filter { it.status == PromiseStatus.DUE_SOON }
        "Upcoming" -> demoPromises.filter { it.status == PromiseStatus.UPCOMING }
        "Done" -> demoPromises.filter { it.status == PromiseStatus.DONE }
        else -> demoPromises
    }
    LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(22.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        item {
            Text("Your promises", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(6.dp))
            Text("Everything you said you would do, in one calm place.", color = Muted, style = MaterialTheme.typography.bodyMedium)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                filters.forEach { label ->
                    FilterChip(label, selected = filter == label, onClick = { filter = label })
                }
            }
        }
        items(filtered, key = { it.title }) { PromiseCard(it) }
        item {
            if (filtered.isEmpty()) EmptyState()
        }
    }
}

@Composable
private fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(onClick = onClick, color = if (selected) Color.White.copy(alpha = 0.22f) else Glass, shape = RoundedCornerShape(50), border = if (selected) androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)) else androidx.compose.foundation.BorderStroke(1.dp, Border)) {
        Text(label, color = if (selected) Color.White else Muted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 13.dp, vertical = 9.dp))
    }
}

@Composable
private fun EmptyState() {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 44.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(modifier = Modifier.size(62.dp).clip(CircleShape).background(Mint), contentAlignment = Alignment.Center) { Icon(Icons.Rounded.TaskAlt, null, tint = MintInk, modifier = Modifier.size(28.dp)) }
        Text("Nothing here", style = MaterialTheme.typography.titleLarge)
        Text("You’re all caught up.", color = Muted, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun SettingsScreen(padding: PaddingValues) {
    Column(modifier = Modifier.fillMaxSize().padding(padding).padding(22.dp), verticalArrangement = Arrangement.spacedBy(22.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineLarge)
        Text("Make Lost Promise Finder work the way you do.", color = Muted, style = MaterialTheme.typography.bodyMedium)
        SettingsRow(Icons.Rounded.People, "Your profile", "Rudrajit Dey")
        SettingsRow(Icons.Rounded.CalendarToday, "Reminder rhythm", "Gentle · 9:00 AM")
        SettingsRow(Icons.Rounded.Description, "Privacy", "Your notes stay on this device")
        androidx.compose.material3.HorizontalDivider(color = Border)
        Text("About", color = Muted, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Text("Lost Promise Finder", style = MaterialTheme.typography.titleMedium)
        Text("A calm place for the things you said you would do.", color = Muted, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun SettingsRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(42.dp).clip(RoundedCornerShape(13.dp)).background(IndigoSoft).border(1.dp, Border, RoundedCornerShape(13.dp)), contentAlignment = Alignment.Center) { Icon(icon, null, tint = Indigo, modifier = Modifier.size(20.dp)) }
        Spacer(Modifier.width(13.dp))
        Column(modifier = Modifier.weight(1f)) { Text(title, style = MaterialTheme.typography.titleMedium); Text(value, color = Muted, style = MaterialTheme.typography.bodyMedium) }
        Icon(Icons.Rounded.KeyboardArrowRight, null, tint = Color(0xFFB1B6C0))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnalyzerScreen(onBack: () -> Unit) {
    var notes by remember { mutableStateOf("") }
    var analyzed by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(22.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack) { Icon(Icons.Rounded.ArrowBack, "Back", tint = Ink) }
            Text("Analyze notes", style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
            IconButton(onClick = onBack) { Icon(Icons.Rounded.Close, "Close", tint = Muted) }
        }
        Spacer(Modifier.height(20.dp))
        Text("What did you say you’d do?", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text("Paste notes from a meeting, class, or chat. We’ll surface the promises worth remembering.", color = Muted, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it; analyzed = false },
            modifier = Modifier.fillMaxWidth().height(190.dp),
            placeholder = { Text("e.g. I’ll send the revised budget to Priya by Friday…") },
            shape = RoundedCornerShape(18.dp),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Border, focusedBorderColor = Indigo, unfocusedContainerColor = Glass, focusedContainerColor = GlassStrong, unfocusedTextColor = Ink, focusedTextColor = Ink, unfocusedPlaceholderColor = Muted, focusedPlaceholderColor = Muted),
            maxLines = 7
        )
        Spacer(Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Description, null, tint = Muted, modifier = Modifier.size(17.dp))
            Text("Your notes stay private on this device", color = Muted, fontSize = 12.sp)
        }
        Spacer(Modifier.height(22.dp))
        Button(onClick = { analyzed = true }, enabled = notes.isNotBlank(), modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Indigo)) {
            Icon(Icons.Rounded.AutoAwesome, null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(9.dp))
            Text("Find my promises")
        }
        if (analyzed) {
            Spacer(Modifier.height(28.dp))
            Text("We found 3 promises", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(12.dp))
            ResultRow("Send revised budget to Priya", "Due Friday", Amber)
            ResultRow("Share the final deck", "Owner: You", IndigoSoft)
            ResultRow("Confirm the review time", "Missing deadline", Color(0xFFE5F4FF))
            Spacer(Modifier.height(16.dp))
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF8C7CFF).copy(alpha = 0.15f)), shape = RoundedCornerShape(18.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Border), elevation = CardDefaults.cardElevation(0.dp)) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Rounded.Send, null, tint = Indigo, modifier = Modifier.size(19.dp))
                    Spacer(Modifier.width(10.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text("Suggested follow-up", color = Indigo, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("“I’ll send the revised budget by Friday. Does Wednesday still work for the review?”", color = Ink, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultRow(title: String, detail: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(color))
        Spacer(Modifier.width(12.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
        Text(detail, color = Muted, fontSize = 12.sp)
    }
}
