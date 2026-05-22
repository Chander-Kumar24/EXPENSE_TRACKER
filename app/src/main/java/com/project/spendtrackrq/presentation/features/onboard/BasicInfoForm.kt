package com.project.spendtrackrq.presentation.features.onboard

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.project.spendtrackrq.R


@Composable
fun UIBasicInfoForm(
    userViewModel: UserViewModel,
    onNavigate: () -> Unit
) {
    val context = LocalContext.current


    LaunchedEffect(key1 = true) {
        userViewModel.eventFlow.collect { event ->
            when (event) {
                is RegistrationUiEvent.NavigateToHome -> {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    onNavigate()
                }

                is RegistrationUiEvent.showSnackbar -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val (banner, backIcon, title, doneIcon, card) = createRefs()

                Image(
                    painter = painterResource(R.drawable.homebanner),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .constrainAs(banner) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    contentScale = ContentScale.FillBounds
                )

                createHorizontalChain(backIcon, title, doneIcon, chainStyle = ChainStyle.SpreadInside)


                IconButton(
                    onClick = {  },
                    modifier = Modifier
                        .constrainAs(backIcon) {

                            top.linkTo(parent.top, margin = 16.dp)
                        }
                        .padding(start = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon),
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = "Onboarding",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .constrainAs(title) {

                            top.linkTo(backIcon.top)
                            bottom.linkTo(backIcon.bottom)
                        },
                    textAlign = TextAlign.Center,
                )

                IconButton(
                    onClick = { userViewModel.submitForm() },
                    modifier = Modifier
                        .constrainAs(doneIcon) {

                            top.linkTo(backIcon.top)
                            bottom.linkTo(backIcon.bottom)
                        }
                        .padding(end = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.done),
                        contentDescription = "Done",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
                UINameCardContainer(
                    modifier = Modifier.constrainAs(card) {
                        top.linkTo(banner.bottom, margin= (-106).dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.wrapContent
                    },
                    userViewModel
                )
            }
        }
    }
}

@Composable
fun UINameCardContainer(modifier: Modifier, userViewModel: UserViewModel)
{

    //This only give the state its immutable, its just dataclass
    //which will be sent after changing
    val uiState by userViewModel.uiState.collectAsState()

    Card(elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(Color.White),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text="Add Your Credentials",
                fontSize = 24.sp,
                color=Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.inter_medium))
            )
            Text(text="Create your account by filling the basic details",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color=Color(0xFF666666),
                fontFamily = FontFamily(Font(R.font.inter_medium)))
            // Name on card
            OutlinedTextField(
                value = uiState.name,
                onValueChange = { userViewModel.onNameChanged(it) },
                label = { Text("NAME") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { userViewModel.onEmailChanged(it) },
                label = { Text("EMAIL") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.phone,
                onValueChange = { newPhone ->
                    if (newPhone.matches(Regex("^[6-9]\\d{0,9}$|^$"))) {
                        userViewModel.onPhoneChanged(phone = newPhone)
                    }
                },
                label = { Text("PHONE NUMBER") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(Modifier.height(12.dp))
        }

    }
}
