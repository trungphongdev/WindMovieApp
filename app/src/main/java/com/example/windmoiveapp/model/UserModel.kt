package com.example.windmoiveapp.model

import com.example.windmoiveapp.constant.AccountPermission
import com.example.windmoiveapp.constant.AccountType
import com.example.windmoiveapp.constant.GenderType
import com.example.windmoiveapp.constant.TypeLogin
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import java.util.*
import kotlin.collections.HashMap

data class UserModel(
    var uid: String? = "",
    var name: String? = "",
    var email: String? = "",
    var phone: String? = "",
    var gender: Int = GenderType.NOTHING.type,
    var photoUrl: String = DEFAULT_IMG_USER,
    var accountType: Int = AccountType.NORMAL.type,
    var accountPermission: Int = AccountPermission.USER.type,
    var password: String? = "",
    var typeLogin: String = ""
) {
    companion object {
        const val PREF_USER = "PREF_USER"
        const val DEFAULT_IMG_USER = "https://lh3.googleusercontent.com/a/ALm5wu3p26QqdgIsoCMAU_wX3gsMy24bgtSM7ajkDLd9"
    }

}


fun FirebaseUser.convertToUserModel(): UserModel {
    return UserModel(
        uid = this.uid,
        name = this.displayName ?: getUserWithUUID(),
        email = this.email,
        phone = this.phoneNumber ?: "",
        photoUrl = this.photoUrl?.toString() ?: UserModel.DEFAULT_IMG_USER,
        typeLogin = TypeLogin.FIREBASE.name,
    )
}

fun GoogleSignInAccount.convertToUserModel(): UserModel {
    return UserModel(
        uid = this.id,
        name = this.displayName,
        email = this.email,
        photoUrl = this.photoUrl?.toString() ?: UserModel.DEFAULT_IMG_USER,
        typeLogin = TypeLogin.GOOGLE.name
    )
}

fun updateUserModel(userModel: UserModel): HashMap<String, Any> {
    return hashMapOf(
        userModel::name.name to (userModel.name ?: ""),
        userModel::email.name to (userModel.email ?: ""),
        userModel::phone.name to (userModel.phone ?: ""),
        userModel::gender.name to (userModel.gender),
        userModel::photoUrl.name to (userModel.photoUrl),
    )
}

fun getUserWithUUID(): String {
    return "User ${UUID.randomUUID()}"
}


/*
*     private fun upDateDataForChart(listMarginDevelop: List<MarginDeveloperModel>) {
        binding?.cbMarginDevelopChart?.apply {
            data = listMarginDevelop.barDataChart(requireContext())
            xAxis.valueFormatter = IndexAxisValueFormatter(listMarginDevelop.labelChart())
            xAxis.setLabelCount(listMarginDevelop.labelChart().size, false)
            xAxis.setDrawAxisLine(false)
            setUpBarChart()
            invalidate()
        }
    }



	 private fun setUpBarChart() {
        binding?.cbMarginDevelopChart?.apply {
            val valueColor = ContextCompat.getColor(context, R.color.gray33646464)
            description.isEnabled = false
            legend.isEnabled = false
            setVisibleXRangeMaximum(5f)
            setDrawGridBackground(false)
            setPinchZoom(false)
            extraBottomOffset = 10F
            isDoubleTapToZoomEnabled = false

            xAxis.apply {
                spaceMin = 10F
                setDrawGridLines(true)
                gridLineWidth = 1f
                position = XAxis.XAxisPosition.BOTTOM
                axisMinimum += 0.1f
                axisMaximum += 0f
                granularity = 1f
                gridColor = valueColor
                axisLineColor = valueColor
                axisLineWidth = 1f
                textColor = Color.WHITE
                setDrawGridLines(false)
            }

            axisLeft.apply {
                axisMinimum = 0f
                gridColor = valueColor
                axisLineColor = valueColor
                gridLineWidth = 1f
                axisLineWidth = 1f
                textColor = Color.WHITE
            }

            axisRight.apply {
                axisMinimum = 0.1f
                setDrawLabels(false)
                gridColor = valueColor
                axisMinimum = 0f
                axisLineColor = valueColor
                gridColor = valueColor
                gridLineWidth = 1f
                axisLineWidth = 1f
            }
        }
    }



fun List<MarginDeveloperModel>.barDataChart(context: Context): BarData {
    val listBarEntry = this.barEntry()
    val dataSet: BarDataSet = BarDataSet(listBarEntry, "").apply {
        color = ContextCompat.getColor(context, R.color.yellowF18725)
        valueTextColor = ContextCompat.getColor(context, R.color.yellowF18725)
        valueTextSize = 8f
        valueFormatter = ValueFormatterBarDataSet()
    }
    return BarData(dataSet).apply {
        this.barWidth = if (listBarEntry.size > 3) 0.3f else 0.1f
    }
}

fun List<MarginDeveloperModel>.labelChart(): List<String> {
    return this.sortedBy { it.dateConvert }.map { it.reportDate.convertToDateStr(FORMAT_DD_MM_YYYY, FORMAT_DATE_NEWS)}
}

fun List<MarginDeveloperModel>.barEntry(): List<BarEntry> {
    return this.sortedBy { it.dateConvert }.mapIndexed { index, investorModel ->
        BarEntry(index.toFloat(), (investorModel.value).toFloat())
    }
}

class ValueFormatterBarDataSet : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return value.format(FORMATTER_NUMBER_DECIMAL)
    }
}*/