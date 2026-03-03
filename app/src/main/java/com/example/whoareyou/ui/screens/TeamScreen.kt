package com.example.whoareyou.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whoareyou.model.Employee
import com.example.whoareyou.ui.theme.*

@Composable
fun TeamScreen(
    employees: List<Employee>,
    onNavigateToDetail: (Employee) -> Unit,
    onBack: () -> Unit
) {
    val groupedByTeam = remember(employees) {
        employees.groupBy { it.team }
    }

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
                text = "팀원보기",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            groupedByTeam.forEach { (teamName, teamMembers) ->
                item {
                    // Team header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = AccentBlue,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = teamName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${teamMembers.size}명",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }
                items(teamMembers) { employee ->
                    EmployeeRow(employee = employee, onClick = { onNavigateToDetail(employee) })
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = Background
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
