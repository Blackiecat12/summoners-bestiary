package com.example.summonapp.ui.monsterdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = monster.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = "CR ${monster.cr}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        Text(text = "XP ???", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        Text(text = "${monster.alignment} ${monster.size} ${monster.creatureType}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Init +${monster.initiative}; Senses ${monster.senses.joinToString()}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "Languages ???", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "DEFENSE", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Text(text = "AC ${monster.armourClass.base}, touch ${monster.armourClass.touch}, flat-footed ${monster.armourClass.flatFooted}",
            style = MaterialTheme.typography.bodyMedium)
        Text(text = "HP ${monster.health.total} (${monster.health.hitDice})", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "OFFENSE", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Text(text = "Speed ${monster.speed.entries.joinToString { "${it.key} ${it.value} ft." }}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Melee ${monster.meleeAttacks}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Ranged ${monster.rangedAttacks}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "STATISTICS", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Text(text = "Str ${monster.abilityScores.strength}, Dex ${monster.abilityScores.dexterity}, Con ${monster.abilityScores.constitution}, Int ${monster.abilityScores.intelligence}, Wis ${monster.abilityScores.wisdom}, Cha ${monster.abilityScores.charisma}",
            style = MaterialTheme.typography.bodyMedium)
        Text(text = "Base Atk +${monster.attackBonus.base}; CMB +${monster.attackBonus.cmb}; CMD ${monster.attackBonus.cmd}",
            style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Feats", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "Special Qualities", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        monster.specialQualities?.forEach { quality ->
            Text(text = "- $quality", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Special Abilities", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        monster.specialAbilities?.forEach { (name, description) ->
            Text(text = "$name: $description", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(16.dp))
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

@Preview(showBackground = true)
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