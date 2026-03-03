package com.example.whoareyou.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whoareyou.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToInfo: () -> Unit,
    onNavigateToPhoneSettings: () -> Unit
) {
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showResetDbDialog by remember { mutableStateOf(false) }
    var showResetUserDialog by remember { mutableStateOf(false) }
    var isUpdating by remember { mutableStateOf(false) }
    var updateDone by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "뒤로가기", tint = TextPrimary)
            }
            Text(
                text = "설정",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 비씨후아유 섹션
            SettingsSectionHeader(title = "비씨후아유")

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SettingsRow(
                        icon = Icons.Default.Phone,
                        iconColor = AccentGreen,
                        title = "전화번호 설정",
                        subtitle = "발신자 표시 설정",
                        trailingContent = {
                            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = TextSecondary)
                        },
                        onClick = onNavigateToPhoneSettings
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Background)
                    SettingsRow(
                        icon = Icons.Default.Info,
                        iconColor = AccentBlue,
                        title = "가이드",
                        subtitle = "앱 사용 방법 안내",
                        trailingContent = {
                            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = TextSecondary)
                        },
                        onClick = onNavigateToInfo
                    )
                }
            }

            // 앱 데이터 관리 섹션
            SettingsSectionHeader(title = "앱 데이터 관리")

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SettingsRow(
                        icon = Icons.Default.Refresh,
                        iconColor = Primary,
                        title = "데이터베이스 업데이트",
                        subtitle = if (updateDone) "업데이트 완료" else "최신 임직원 정보로 업데이트",
                        trailingContent = {
                            if (isUpdating) {
                                CircularProgressIndicator(color = Primary, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = TextSecondary)
                            }
                        },
                        onClick = { if (!isUpdating) showUpdateDialog = true }
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Background)
                    SettingsRow(
                        icon = Icons.Default.Delete,
                        iconColor = AccentOrange,
                        title = "데이터베이스 초기화",
                        subtitle = "전화번호 정보 모두 삭제",
                        trailingContent = {
                            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = TextSecondary)
                        },
                        onClick = { showResetDbDialog = true }
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Background)
                    SettingsRow(
                        icon = Icons.Default.Person,
                        iconColor = Color.Red,
                        title = "사용자정보 초기화",
                        subtitle = "로그아웃됩니다",
                        titleColor = Color.Red,
                        trailingContent = {
                            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.Red.copy(alpha = 0.5f))
                        },
                        onClick = { showResetUserDialog = true }
                    )
                }
            }
        }
    }

    // Update confirmation dialog
    if (showUpdateDialog) {
        AlertDialog(
            onDismissRequest = { showUpdateDialog = false },
            title = { Text("알림") },
            text = { Text("전화번호부를 업데이트하시겠습니까?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showUpdateDialog = false
                        isUpdating = true
                        scope.launch {
                            delay(2000)
                            isUpdating = false
                            updateDone = true
                        }
                    }
                ) {
                    Text("확인", color = Primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showUpdateDialog = false }) {
                    Text("취소", color = TextSecondary)
                }
            }
        )
    }

    // Reset DB confirmation dialog
    if (showResetDbDialog) {
        AlertDialog(
            onDismissRequest = { showResetDbDialog = false },
            title = { Text("알림") },
            text = { Text("데이터베이스를 초기화하시겠습니까?\n전화번호 정보가 모두 삭제됩니다.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetDbDialog = false
                        updateDone = false
                    }
                ) {
                    Text("확인", color = Primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDbDialog = false }) {
                    Text("취소", color = TextSecondary)
                }
            }
        )
    }

    // Reset user confirmation dialog
    if (showResetUserDialog) {
        AlertDialog(
            onDismissRequest = { showResetUserDialog = false },
            title = { Text("알림") },
            text = { Text("사용자 정보를 초기화하시겠습니까?\n로그아웃됩니다.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetUserDialog = false
                        onLogout()
                    }
                ) {
                    Text("확인", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetUserDialog = false }) {
                    Text("취소", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextSecondary,
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
    )
}

@Composable
fun SettingsRow(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String,
    titleColor: Color = TextPrimary,
    trailingContent: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(iconColor.copy(alpha = 0.12f), shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = titleColor)
            Text(text = subtitle, fontSize = 12.sp, color = TextSecondary)
        }
        trailingContent()
    }
}
