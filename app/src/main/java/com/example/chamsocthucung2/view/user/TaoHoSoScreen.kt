package com.example.chamsocthucung2.view.user


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chamsocthucung2.R
import com.example.chamsocthucung2.ui.components.BottomNavigationBar
import com.example.chamsocthucung2.viewmodel.AppViewModel
@Composable
fun TaoHoSoScreen(navController: NavController,appViewModel: AppViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                elevation = 0.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Image(
                        painter = painterResource(id = R.drawable.image1),
                        contentDescription = "User Avatar",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = "Hello,", fontSize = 14.sp, color = Color.Gray)
                        Text(text = "abcxyz.com", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_thongbao),
                            contentDescription = "Thông báo"
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Image(
                painter = painterResource(R.drawable.image1),
                contentDescription = "Thú cưng",
                modifier = Modifier
                    .size(250.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Chưa có hồ sơ thú cưng",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Bạn chưa tạo lập hồ sơ thú cưng.\nẤn Tiếp tục để cập nhật thông tin nhé!",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 20.dp),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            OutlinedButton(
                onClick = { navController.navigate("pet_info") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.Black)

            ) {
                Text(text = "+ Thêm hồ sơ", fontSize = 18.sp, color = Color.Black)
            }


        }}}
@Preview(showBackground = true)
@Composable
fun TaoHoSoScreenPreview() {
    val navController = rememberNavController()
    val appViewModel: AppViewModel = viewModel()
    TaoHoSoScreen(navController = navController, appViewModel = appViewModel)
}