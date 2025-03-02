package com.example.summonapp.ui.monsterdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.summonapp.MonsterTopAppBar
import com.example.summonapp.R
import com.example.summonapp.data.Monster
import com.example.summonapp.ui.AppViewModelProvider
import com.example.summonapp.ui.home.getPreviewMonster
import com.example.summonapp.ui.navigation.NavigationDestination
import com.example.summonapp.ui.theme.SummonAppTheme

object MonsterDetailDestination : NavigationDestination {
    override val route = "monsterDetail"
    override val titleRes = R.string.monster_details
    const val monsterIdArg = "monsterID"
    val routeWithArgs = "$route/{$monsterIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonsterDetailScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MonsterDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.monster.collectAsState()
    Scaffold(
        topBar = {
            MonsterTopAppBar(
                title = stringResource(MonsterDetailDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        modifier = modifier
    ) {
        innerPadding ->
        val padding = Modifier
            .padding(
                start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                top = innerPadding.calculateTopPadding(),
                end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
            )
            .verticalScroll(rememberScrollState())
        if (uiState.monster != null) {
            MonsterDetailsBody(
                monster = uiState.monster!!,
                modifier = padding
            )
        } else {
            MonsterNotFoundMessage(
                monsterName = uiState.monsterId,
                modifier = padding
            )
        }

    }
}

@Composable
private fun MonsterDetailsBody(
    monster: Monster,
    modifier: Modifier = Modifier
) {
    Card (
        modifier = modifier
            .fillMaxWidth()
    )
    {
        Text(
            text = monster.name,
        )
    }
}

@Composable
fun MonsterNotFoundMessage(
    monsterName: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = "Error: Monster Not Found")
        Text(text = "The monster '$monsterName' could not be found in the database.")
    }
}

@Preview
@Composable
private fun MonsterDetailPreview() {
    val monster = getPreviewMonster(1)
    SummonAppTheme {
        MonsterDetailsBody(monster)
    }
}

@Preview
@Composable
private fun MonsterNotFoundPreview() {
    val monster = getPreviewMonster(1)
    SummonAppTheme {
        MonsterNotFoundMessage("Fire Drake")
    }
}