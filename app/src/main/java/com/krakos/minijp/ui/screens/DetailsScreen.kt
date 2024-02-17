package com.krakos.minijp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krakos.minijp.model.Word
import com.krakos.minijp.model.sampleWord
import com.minijp.R

@Composable
fun DetailsScreen(
    item: Word,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
    ) {
        TagList(
            item = item,
            modifier = Modifier.fillMaxWidth()
        )
        WordWithReading(
            item = item,
            modifier = Modifier.padding(16.dp)
        )
        Divider()
        Translations(item = item)
    }
}



@Composable
fun Translations(
    item: Word,
    modifier: Modifier = Modifier
) {
    val translations = item.translations
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .padding(4.dp)
            .fillMaxSize()
    ) {
        items(translations) {
            val pos = it.partsOfSpeech.toString().removeSurrounding("[", "]")
            val enDef = it.englishDefinitions

            Column {
                Text(
                    text = pos,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
                enDef.forEach {def ->
                    Text(
                        text = "- $def",
                        style = MaterialTheme.typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}



@Composable
fun WordWithReading(
    item: Word,
    modifier: Modifier
) {
    val word = item.japanese[0].word ?: ""
    val reading = "${item.japanese[0].reading}"

    Column(
        modifier = modifier
    ) {
        Text(
            text = reading,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
        )
        Text(
            text = word,
            style = MaterialTheme.typography.displaySmall,
        )
    }
}

@Composable
fun TagList(item: Word, modifier: Modifier = Modifier) {
    val wanikaniTag = if (item.wanikaniLevel.isNotEmpty()) item.wanikaniLevel[0] else ""
    val jlptTag = if (item.jlpt.isNotEmpty()) item.jlpt[0] else ""

    Row(
        modifier = modifier.wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Tag(commonTag = item.isCommon, bgColor = R.color.common_tag)
        Tag(tag = jlptTag, bgColor = R.color.jlpt_tag)
        Tag(tag = wanikaniTag, bgColor = R.color.wanikani_tag)
    }
}


@Composable
fun Tag(modifier: Modifier = Modifier, tag: String = "", commonTag: Boolean = false, bgColor: Int) {
    val formattedTag: String
    formattedTag = if (tag.contains("wanikani")) {
        tag.takeWhile { !it.isDigit() } + " " + tag.takeLastWhile { it.isDigit() }
    }
    else if (commonTag) stringResource(id = R.string.common)
    else tag.replace('-',' ')

    if (formattedTag.isEmpty()) return
    Card(
        colors = CardDefaults
            .cardColors(containerColor = colorResource(id = bgColor)),
        modifier = modifier.width(110.dp)
    ) {
        Text(
            text = formattedTag,
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 2.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Black
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun DetailsScreenPreview() {
    DetailsScreen(item = sampleWord)
}