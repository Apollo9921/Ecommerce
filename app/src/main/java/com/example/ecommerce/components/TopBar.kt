package com.example.ecommerce.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce.R
import com.example.ecommerce.core.TopBarColor
import com.example.ecommerce.core.Typography
import com.example.ecommerce.core.White
import com.example.ecommerce.utils.size.ScreenSizeUtils

@Composable
fun TopBar(title: String, isBack: Boolean = true, backStack: () -> Boolean) {
    val titleSize = ScreenSizeUtils.calculateCustomWidth(baseSize = 20).sp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(TopBarColor)
            .padding(horizontal = 10.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        if (isBack) {
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = null,
                colorFilter = ColorFilter.tint(White),
                modifier = Modifier.clickable { backStack() }
            )
            Spacer(modifier = Modifier.padding(10.dp))
        }
        Text(
            style = Typography.titleLarge.copy(fontSize = titleSize),
            text = title,
            textAlign = TextAlign.Start,
        )
    }
}