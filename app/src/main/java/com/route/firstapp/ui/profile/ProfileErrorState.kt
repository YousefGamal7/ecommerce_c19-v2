package com.route.firstapp.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.route.firstapp.R
import com.route.firstapp.ui.theme.BrandColor
import com.route.firstapp.ui.theme.MyPreview

@Composable
fun ProfileErrorState(
    modifier: Modifier = Modifier,
    message: String = stringResource(R.string.generic_error_message),
    onRetryClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = BrandColor,
            textAlign = TextAlign.Center,
        )
        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = onRetryClick,
            colors = ButtonDefaults.buttonColors(containerColor = BrandColor),
        ) {
            Text(text = stringResource(R.string.try_again))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileErrorStatePreview() {
    MyPreview {
        ProfileErrorState(
            message = "Unable to connect to the server. Please check your internet connection.",
        )
    }
}
