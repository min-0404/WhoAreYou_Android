package com.example.whoareyou.call

import android.app.KeyguardManager
import android.content.Context
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class CallOverlayActivity : ComponentActivity() {

    companion object {
        const val EXTRA_EMPLOYEE_NAME = "employee_name"
        const val EXTRA_EMPLOYEE_TEAM = "employee_team"
        const val EXTRA_EMPLOYEE_JOB = "employee_job"
        const val EXTRA_PHONE_NUMBER = "phone_number"
    }

    private lateinit var telephonyManager: TelephonyManager
    private lateinit var phoneStateListener: PhoneStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 잠금 화면 위에 표시
        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
        }

        // 터치 이벤트가 아래 전화 수신 화면으로 전달되도록
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
        window.addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH)

        val name = intent.getStringExtra(EXTRA_EMPLOYEE_NAME) ?: ""
        val team = intent.getStringExtra(EXTRA_EMPLOYEE_TEAM) ?: ""
        val job = intent.getStringExtra(EXTRA_EMPLOYEE_JOB) ?: ""

        setContent {
            CallOverlayContent(name = name, team = team, job = job)
        }

        // 전화 상태 감지: 통화 종료(IDLE) 시 자동으로 Activity 닫기
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        phoneStateListener = object : PhoneStateListener() {
            @Suppress("DEPRECATION")
            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    finish()
                }
            }
        }
        @Suppress("DEPRECATION")
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        @Suppress("DEPRECATION")
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
    }
}

@Composable
fun CallOverlayContent(name: String, team: String, job: String) {
    val primary = Color(0xFFFF4545)
    val primaryLight = Color(0xFFFFEDED)
    val textPrimary = Color(0xFF1A1A1F)
    val textSecondary = Color(0xFF8C8C99)
    val badgeBackground = Color(0xFFE0EDFF)
    val badgeText = Color(0xFF1F47A6)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 중앙 팝업 카드
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // 이니셜 아바타
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(primaryLight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name.firstOrNull()?.toString() ?: "?",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = primary
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // 이름
                    Text(
                        text = name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    // 팀
                    Text(
                        text = team,
                        fontSize = 13.sp,
                        color = textSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    // 업무 배지
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(badgeBackground)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = job,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = badgeText
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // BC 로고 표시
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "BC",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
