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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krakos.minijp.model.Items
import com.krakos.minijp.model.Translation
import com.krakos.minijp.model.Word
import com.krakos.minijp.model.sampleWord
import com.krakos.minijp.ui.MiniJpUiState
import com.krakos.minijp.ui.MiniJpViewModel
import com.minijp.R


@Composable
fun HomeScreen(viewModel: MiniJpViewModel, miniJpUiState: MiniJpUiState) {
    val lastQueryExists = !viewModel.isSearchFileEmpty(context = LocalContext.current)

    when (miniJpUiState) {
        is MiniJpUiState.Loading -> LoadingScreen()
        is MiniJpUiState.SuccessDetailsScreen -> DetailsScreen(item = miniJpUiState.word)
        is MiniJpUiState.SuccessHomeScreen ->
            WordsList(words = miniJpUiState.words, onWordClick = viewModel::getDetails)
        is MiniJpUiState.Error ->
            ErrorScreen(lastQueryExists = lastQueryExists, onTryAgain = viewModel::retrySearch)
    }
}


@Composable
fun WordBox(
    item: Word,
    onWordClick: (Word) -> Unit
) {
    val word = item.japanese[0].word
    var reading =
        if (!item.japanese[0].reading.isNullOrEmpty()) "【 ${item.japanese[0].reading} 】"
        else ""

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .wrapContentHeight()
            .clickable { onWordClick(item) }

    ) {
        Column(
            modifier = Modifier
                .padding(8.dp, 4.dp)
                .fillMaxWidth(),
        ) {
            WordTagRepresentation(word = item)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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

@Composable
fun WordTagRepresentation( word: Word) {
    Row {
        if (word.isCommon)
            Text(text = "•", fontWeight = FontWeight.Bold, color = colorResource(id = R.color.common_tag))
        if (word.jlpt.isNotEmpty())
            Text(text = "•", fontWeight = FontWeight.Bold, color = colorResource(id = R.color.jlpt_tag))
        if (word.wanikaniLevel.isNotEmpty())
            Text(text = "•", fontWeight = FontWeight.Bold, color = colorResource(id = R.color.wanikani_tag))
    }
}



/* Will be deleted soon

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
*/


@Composable
fun WordsList(
    words: Items,
    modifier: Modifier = Modifier,
    onWordClick: (Word) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(bottom = 4.dp)
    ) {
        this.items(words.words) { word ->
            Divider(modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp))
            WordBox(item = word) { onWordClick(word) }
        }
    }
}


@Composable
fun WordTranslations(
    translations: List<Translation>,
    // modifier: Modifier = Modifier
) {
    val goodTranslations =
        when (translations.size) {
            0,1 -> translations
            else -> translations
                .filterNot { it.partsOfSpeech.contains("Wikipedia definition") }
                .take(2)
        }


    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        goodTranslations.forEach {
            Column(
            ) {
                Text(
                    text = it.partsOfSpeech.toString().removeSurrounding("[","]"),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = Color.Gray
                )
                Row {
                    Text(
                        text = it.englishDefinitions.toString().removeSurrounding("[", "]"),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (translations.size>2 && it == goodTranslations.last()) {
                        Text(
                            text = "${translations.size - 2} more",
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.width(70.dp),
                        )
                    }
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    Column {
        repeat(7) {
            WordBox(item = sampleWord) {}
            Divider(modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp))
        }
    }
}