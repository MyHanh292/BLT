package com.example.chamsocthucung2.view.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Healing
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.SystemUpdate
import androidx.compose.material.icons.outlined.Vaccines
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chamsocthucung2.R
import com.example.chamsocthucung2.navigation.Routes
import com.example.chamsocthucung2.ui.components.BottomNavigationBar
import com.example.chamsocthucung2.viewmodel.user.PetViewModel
import com.google.firebase.auth.FirebaseAuth



@Composable
fun HomeScreen(
    navController: NavController,
    petViewModel: PetViewModel = viewModel(),
    userEmail: String = FirebaseAuth.getInstance().currentUser?.email ?: "abcxyz@gmail.com"
) {
    val profileInfo by petViewModel.profileInfo.collectAsState()

    val doctors = remember {
        listOf(
            VetDoctor("Dr. Bình", "Chuyên gia nội khoa", R.drawable.doctor),
            VetDoctor("Dr. Hoa", "Chuyên gia ngoại khoa", R.drawable.doctor2)
        )
    }
    val primaryColor = MaterialTheme.colorScheme.primary
    val goldenAccent = Color(0xFFFFC107)
    val surfaceColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
    val lightBlue = Color(0xFFBBDEFB)

    val updatedServices = remember {
        listOf(
            Service("Tư vấn", Icons.Outlined.ChatBubbleOutline, "medical"),
            Service("Chăm sóc", Icons.Outlined.Healing, "care"),
            Service("Khám", Icons.Outlined.SystemUpdate, "examination"),
            Service("Spa", Icons.Outlined.Spa, "spa"),
            Service("Tiêm phòng", Icons.Outlined.Vaccines, "vaccine"),
            Service("Xét nghiệm", Icons.Outlined.Science, "test")
        )
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = WindowInsets.statusBars.getTop(LocalDensity.current).dp + 4.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(primaryColor.copy(alpha = 0.1f), Color.Transparent)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(primaryColor, primaryColor.copy(alpha = 0.3f))
                                    )
                                )
                                .border(
                                    width = 2.dp,
                                    color = goldenAccent,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.hi),
                                contentDescription = "Ảnh hồ sơ",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                "Hello,",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            Text(
                                userEmail.substringBefore("@"),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = primaryColor
                            )
                        }
                    }
                    IconButton(
                        onClick = { /* TODO: Handle notification click */ },
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = CircleShape
                            )
                    ) {
                        BadgedBox(badge = { Badge { Text("3") } }) {
                            Icon(
                                Icons.Filled.Notifications,
                                contentDescription = "Thông báo",
                                tint = primaryColor
                            )
                        }
                    }
                }
            }
        },
        bottomBar = { BottomNavigationBar(navController) },
        contentColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SectionTitle("Hồ sơ thú cưng", goldenAccent)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .offset(x = 2.dp, y = 2.dp)
                        .background(lightBlue.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                ) {
                    Card(
                        onClick = {
                            if (profileInfo.petName.isNullOrEmpty() || profileInfo.petType.isNullOrEmpty()) {
                                navController.navigate(Routes.TAO_HO_SO)
                            } else {
                                navController.navigate(Routes.PET_PROFILE_DETAIL)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = surfaceColor
                        ),
                        border = BorderStroke(1.dp, primaryColor.copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val petImage = when (profileInfo.petType?.lowercase()) {
                                        "chó" -> R.drawable.cat
                                        "mèo" -> R.drawable.dog
                                        else -> R.drawable.cat_dog
                                    }
                                    Image(
                                        painter = painterResource(id = R.drawable.cat_dog),
                                        contentDescription = "Hình ảnh thú cưng",
                                        modifier = Modifier
                                            .size(72.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                Spacer(modifier = Modifier.width(24.dp))
                                Column(horizontalAlignment = Alignment.Start) {
                                    Text(
                                        profileInfo.petName.takeIf { it?.isNotEmpty() == true } ?: "Chưa có hồ sơ",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        profileInfo.petType.takeIf { it?.isNotEmpty() == true } ?: "Thêm thông tin thú cưng",
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                            }
                            Icon(
                                Icons.Filled.ArrowForward,
                                contentDescription = "Xem hồ sơ",
                                tint = goldenAccent,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }

            SectionTitle("Dịch vụ Thú Cưng", goldenAccent)

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(updatedServices) { service ->
                    ServiceCardPremium(
                        service = service,
                        navController = navController,
                        modifier = Modifier.size(96.dp) // Increased size
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            SectionTitle("Đội Ngũ Bác Sĩ", goldenAccent)

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(doctors) { doctor ->
                    DoctorCardPremium(doctor = doctor, navController = navController)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionTitle(title: String, accentColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(20.dp)
                .background(accentColor, shape = RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            title,
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 16.sp),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )
    }
}

@Composable
fun ServiceCardPremium(
    service: Service,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { navController.navigate(service.route) },
        modifier = modifier,
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFE4D2)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp), // Increased padding inside the circle
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = service.icon,
                contentDescription = service.name,
                modifier = Modifier.size(36.dp), // Increased icon size
                tint = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                service.name,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF333333)
            )
        }
    }
}

@Composable
fun DoctorCardPremium(
    doctor: VetDoctor,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val goldenAccent = Color(0xFFFFC107)
    val doctorCardColor = Color(0xFFFFF8E1)
    val buttonColor = Color(0xFFFFC107)

    Card(
        onClick = { /* navigate nếu cần */ },
        modifier = modifier
            .width(200.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = doctorCardColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, goldenAccent.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(
                        width = 2.dp,
                        color = goldenAccent,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = doctor.imageRes),
                    contentDescription = "Doctor",
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                doctor.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Text(
                doctor.specialty,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(buttonColor)
                    .clickable { navController.navigate(Routes.BOOKING) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Đặt lịch", fontWeight = FontWeight.Medium)
            }
        }
    }
}

data class Service(val name: String, val icon: ImageVector, val route: String)
data class PetInfo(val name: String, val breed: String, val imageRes: Int)
data class VetDoctor(val name: String, val specialty: String, val imageRes: Int)




@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}
@Preview(showBackground = true)
@Composable
fun PreviewHomScreen() {
    val navController = rememberNavController()
    val petViewModel = PetViewModel()

    HomeScreen(navController = navController, petViewModel = petViewModel)
}