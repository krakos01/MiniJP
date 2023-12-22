package com.krakos.minijp.ui.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krakos.minijp.model.Items
import com.krakos.minijp.model.Translation
import com.krakos.minijp.model.Word
import com.krakos.minijp.model.sampleWord


@Composable
fun HomeScreen(viewModel: MiniJpViewModel, miniJpUiState: MiniJpUiState) {
    when (miniJpUiState) {
        is MiniJpUiState.Loading -> LoadingScreen()
        is MiniJpUiState.Error -> ErrorScreen()
        is MiniJpUiState.SuccessDetailsScreen -> TODO() //DetailsScreen()
        is MiniJpUiState.SuccessHomeScreen -> WordsList(words = miniJpUiState.words)
    }
}


@Composable
fun WordCard(item: Word) {
    val word = item.japanese[0].word
    var reading = "【 ${item.japanese[0].reading} 】"

    Box( // Card
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .wrapContentHeight()
            .clickable {  } // todo
        //shape = RoundedCornerShape(5)

    ) {
        Column(
            modifier = Modifier
                .padding(8.dp, 4.dp)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                   //.weight(1f),
            ) {
                if (!word.isNullOrEmpty()) {
                    Text(
                        text = word,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                } else reading = reading.removeSurrounding("【 "," 】")

                if (reading.isNotEmpty()) {
                    Text(
                        text = reading,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            WordTranslations(
                translations = item.translations,
            )
        }
    }
} 


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlternativeWordCard(
    word: Word,
    onWordClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 4.dp),
        onClick = {  }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp, 4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                if (!word.japanese[0].reading.isNullOrEmpty()) {
                    Text(
                        text = word.japanese[0].reading!!,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
                if (!word.japanese[0].word.isNullOrEmpty()) {
                    Text(
                        text = word.japanese[0].word!!,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            WordTranslations(
                translations = word.translations,
                modifier = Modifier.weight(3f)
            )
        }
    }
}

@Composable
fun WordsList(
    words: Items,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(bottom = 4.dp)
    ) {
        this.items(words.words) { word ->
            //WordCard(word = word) { TODO("Details screen not yet implemented") }
            Divider(modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp))
            WordCard(item = word)
        }
    }
}


@Composable
fun WordTranslations(
    translations: List<Translation>,
    modifier: Modifier = Modifier
) {
    val goodTranslations =
        when (translations.size) {
            0,1 -> translations
            else -> translations
                .filterNot { it.partsOfSpeech.contains("Wikipedia definition") }
                .take(2)
        }


    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        goodTranslations.forEach {
            Column() {
                Text(
                    text = it.partsOfSpeech.toString().removeSurrounding("[","]"),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp
                )
                Row {
                    Text(
                        text = it.englishDefinitions.toString().removeSurrounding("[", "]"),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (translations.size>2 && it == goodTranslations.last()) {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "${translations.size - 2} more",
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }
        }
    }
}


//@Preview(showSystemUi = true)
@Composable
fun WordCardPreview() {
    WordCard(item = sampleWord)
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    Column {
        repeat(7) {
            WordCard(item = sampleWord)
            Divider(modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp))
        }
    }
}