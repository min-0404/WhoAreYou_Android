package com.example.whoareyou.model

object EmployeeRepository {
    // 모든 컴포넌트(Activity, Service 등)에서 공유하는 임직원 목록
    val employees: List<Employee> get() = MockData.employees

    /**
     * 전화번호로 임직원을 찾습니다.
     * 하이픈 제거 후 비교하여 형식 차이를 무시합니다.
     */
    fun findByPhoneNumber(rawNumber: String): Employee? {
        val normalized = normalizePhone(rawNumber)
        return employees.firstOrNull { emp ->
            normalizePhone(emp.mobilePhone) == normalized ||
            normalizePhone(emp.internalPhone) == normalized
        }
    }

    private fun normalizePhone(phone: String): String {
        // 하이픈, 공백 제거 후 국가코드(+82, 0082) → 0 으로 변환
        var num = phone.replace(Regex("[^0-9]"), "")
        if (num.startsWith("82") && num.length > 10) {
            num = "0" + num.substring(2)
        }
        return num
    }
}
