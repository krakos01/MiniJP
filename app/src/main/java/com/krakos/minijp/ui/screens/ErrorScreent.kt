package com.krakos.minijp.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minijp.R

@Composable
fun ErrorScreen(modifier: Modifier = Modifier,) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(3f).fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.connection_error0),
                style = MaterialTheme.typography.headlineSmall,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.connection_error1)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier.padding(bottom = 10.dp).fillMaxWidth(0.9f),
            shape = RoundedCornerShape(15),
            onClick = { /*TODO*/ }) {
            Text(text = "Retry")
        }
    }
}


@Composable
@Preview(showSystemUi = true)
fun ErrorScreenPreview(){
    ErrorScreen(modifier = Modifier)
}