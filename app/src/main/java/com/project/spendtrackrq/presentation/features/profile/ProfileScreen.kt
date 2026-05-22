package com.project.spendtrackrq.presentation.features.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.project.spendtrackrq.R
import com.project.spendtrackrq.data.local.entities.user.UserEntity
import com.project.spendtrackrq.presentation.common.UICommonBottomBar
import com.project.spendtrackrq.presentation.features.onboard.RegistrationUiEvent
import com.project.spendtrackrq.presentation.features.onboard.UserViewModel
import com.project.spendtrackrq.presentation.navigation.components.NavigationScreen

@Composable
fun ProfileScreen(
    navController: NavHostController,
    userViewModel: UserViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val user by userViewModel.getUserState.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }
    var editedName by remember(user?.id) { mutableStateOf(user?.name.orEmpty()) }
    var editedEmail by remember(user?.id) { mutableStateOf(user?.email.orEmpty()) }
    var editedPhone by remember(user?.id) { mutableStateOf(user?.phoneNumber.orEmpty()) }

    LaunchedEffect(user) {
        if (user != null && !showEditDialog) {
            editedName = user?.name.orEmpty()
            editedEmail = user?.email.orEmpty()
            editedPhone = user?.phoneNumber.orEmpty()
        }
    }

    LaunchedEffect(Unit) {
        userViewModel.eventFlow.collect { event ->
            when (event) {
                is RegistrationUiEvent.NavigateToHome -> {
                    navController.navigate(NavigationScreen.Home.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

                is RegistrationUiEvent.showSnackbar -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { UICommonBottomBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ProfileHeroSection(
                user = user,
                onEditClick = { showEditDialog = true },
                onHomeClick = {
                    navController.navigate(NavigationScreen.Home.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

            ProfileDetailsSection(
                user = user,
                onGoHome = {
                    navController.navigate(NavigationScreen.Home.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onGoBudget = {
                    navController.navigate(NavigationScreen.Budget.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onGetStarted = {
                    navController.navigate(NavigationScreen.OnBoarding.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }

    if (showEditDialog && user != null) {
        ProfileEditDialog(
            user = user!!,
            name = editedName,
            email = editedEmail,
            phone = editedPhone,
            onNameChange = { editedName = it },
            onEmailChange = { editedEmail = it },
            onPhoneChange = { editedPhone = it },
            onDismiss = { showEditDialog = false },
            onSave = {
                userViewModel.updateProfile(user!!, editedName, editedEmail, editedPhone)
                showEditDialog = false
            }
        )
    }
}

@Composable
private fun ProfileHeroSection(
    user: UserEntity?,
    onEditClick: () -> Unit,
    onHomeClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(270.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3F8782),
                        Color(0xFF69AEA9)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Profile",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onHomeClick) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Go to home",
                        tint = Color.White
                    )
                }
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit profile",
                        tint = Color.White
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(94.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.user_fill_1),
                        contentDescription = "User avatar",
                        tint = Color.White,
                        modifier = Modifier.size(46.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = user?.name?.takeIf { it.isNotBlank() } ?: "Welcome back",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = user?.email?.takeIf { it.isNotBlank() } ?: "Your profile details live here",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 13.sp,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun ProfileDetailsSection(
    user: UserEntity?,
    onGoHome: () -> Unit,
    onGoBudget: () -> Unit,
    onGetStarted: () -> Unit,
) {
    val statusText = if (user?.completedOnboarding == true) "Completed" else "Pending"
    val statusColor = if (user?.completedOnboarding == true) Color(0xFF1E8E5A) else Color(0xFFE09B00)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = (-28).dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Account details",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.inter_medium))
                    )
                    Text(
                        text = "View and update your personal information",
                        color = Color(0xFF6B7280),
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.inter_medium))
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(statusColor.copy(alpha = 0.12f))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = statusText,
                        color = statusColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily(Font(R.font.inter_medium))
                    )
                }
            }

            if (user == null) {
                EmptyProfileState(onGetStarted = onGetStarted)
            } else {
                ProfileInfoCard(
                    icon = Icons.Default.Person,
                    label = "Full name",
                    value = user.name
                )
                ProfileInfoCard(
                    icon = Icons.Default.Email,
                    label = "Email address",
                    value = user.email
                )
                ProfileInfoCard(
                    icon = Icons.Default.Phone,
                    label = "Phone number",
                    value = user.phoneNumber
                )

                ProfileActionButtons(
                    onGoHome = onGoHome,
                    onGoBudget = onGoBudget
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
private fun ProfileInfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7FAFC)),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFF3F8782)
            )
            Spacer(modifier = Modifier.size(12.dp))
            Column {
                Text(
                    text = label,
                    color = Color(0xFF6B7280),
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.inter_medium))
                )
                Text(
                    text = value.ifBlank { "Not provided" },
                    color = Color(0xFF111827),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily(Font(R.font.inter_medium))
                )
            }
        }
    }
}

@Composable
private fun ProfileActionButtons(
    onGoHome: () -> Unit,
    onGoBudget: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onGoHome,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F8782))
        ) {
            Text(
                text = "Go home",
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontWeight = FontWeight.SemiBold
            )
        }

        Button(
            onClick = onGoBudget,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEFF6F5), contentColor = Color(0xFF3F8782))
        ) {
            Text(
                text = "Open budget",
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun EmptyProfileState(onGetStarted: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "No profile found yet.",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily(Font(R.font.inter_medium))
        )
        Text(
            text = "Complete onboarding to create your account details.",
            color = Color(0xFF6B7280),
            fontSize = 13.sp,
            fontFamily = FontFamily(Font(R.font.inter_medium))
        )

        Button(
            onClick = onGetStarted,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F8782))
        ) {
            Text(
                text = "Go to onboarding",
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ProfileEditDialog(
    user: UserEntity,
    name: String,
    email: String,
    phone: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit ${user.name}'s profile",
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = onPhoneChange,
                    label = { Text("Phone") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onSave) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

