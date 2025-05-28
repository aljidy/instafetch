package com.adam.instafetch.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.adam.instafetch.R
import com.adam.instafetch.theme.InstaFetchTheme
import com.adam.instafetch.theme.Spacing

@Composable
fun GenericErrorMessage(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.error_message_title),
    subtitle: String = stringResource(R.string.error_message_subtitle),
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            tonalElevation = Spacing.xs,
            shadowElevation = Spacing.xs,
            modifier =
                Modifier
                    .padding(Spacing.xl),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(Spacing.xxl)
                        .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(Spacing.m))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GenericErrorMessagePreview() {
    InstaFetchTheme {
        GenericErrorMessage()
    }
}
