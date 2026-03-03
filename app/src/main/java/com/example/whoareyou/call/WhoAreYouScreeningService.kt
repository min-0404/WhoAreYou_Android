package com.example.whoareyou.call

import android.content.Intent
import android.telecom.Call
import android.telecom.CallScreeningService
import com.example.whoareyou.model.EmployeeRepository

class WhoAreYouScreeningService : CallScreeningService() {

    override fun onScreenCall(callDetails: Call.Details) {
        // 수신 전화만 처리 (발신 전화는 무시)
        if (callDetails.callDirection != Call.Details.DIRECTION_INCOMING) {
            respondToCall(callDetails, CallResponse.Builder().build())
            return
        }

        val rawNumber = callDetails.handle?.schemeSpecificPart ?: run {
            respondToCall(callDetails, CallResponse.Builder().build())
            return
        }

        val employee = EmployeeRepository.findByPhoneNumber(rawNumber)

        if (employee != null) {
            // 임직원 번호 매칭 → 오버레이 팝업 실행
            val overlayIntent = Intent(this, CallOverlayActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                putExtra(CallOverlayActivity.EXTRA_EMPLOYEE_NAME, employee.name)
                putExtra(CallOverlayActivity.EXTRA_EMPLOYEE_TEAM, employee.team)
                putExtra(CallOverlayActivity.EXTRA_EMPLOYEE_JOB, employee.jobTitle)
                putExtra(CallOverlayActivity.EXTRA_PHONE_NUMBER, rawNumber)
            }
            startActivity(overlayIntent)
        }

        // 전화 자체는 정상 처리 (차단하지 않음)
        respondToCall(callDetails, CallResponse.Builder().build())
    }
}
