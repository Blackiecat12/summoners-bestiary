package com.example.summonapp.ui.monsterdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.summonapp.MonsterTopAppBar
import com.example.summonapp.R
import com.example.summonapp.capitalise
import com.example.summonapp.capitaliseWords
import com.example.summonapp.data.Monster
import com.example.summonapp.formatAsModifier
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
            .padding(24.dp)
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
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.image_24px),
                contentDescription = "Default Image",
                modifier = Modifier.size(80.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = monster.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = monster.alignment.displayString(),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = monster.size.toString().lowercase(),
                    style = MaterialTheme.typography.titleMedium
                )
                val creatureType = monster.creatureType.capitalise()
                val subtypes = if (monster.creatureSubtypes.isNotEmpty()) {
                    "(" + monster.creatureSubtypes.joinToString(", ") + ")"
                } else {
                    ""
                }
                Text(
                    text = "%s %s".format(creatureType, subtypes),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        SectionHeader(title = "Basic Info")
        Column(modifier = Modifier.fillMaxWidth()) {
            BoldLabelToValue("Initiative", monster.initiative.formatAsModifier())
            BoldLabelToValue("Perception", monster.perception.formatAsModifier())
            BoldLabelToValue("Senses", monster.senses.joinToString())
        }

        SectionHeader(title = "Defense")
        Column(modifier = Modifier.fillMaxWidth()) {
            BoldLabelToValue("AC", "${monster.armourClass.base} (touch ${monster.armourClass.touch}, flat-footed ${monster.armourClass.flatFooted})")
            BoldLabelToValue("Health", "${monster.health.total} (${monster.health.hitDice})")
            BoldLabelToValue("Resistances", "None")
            BoldLabelToValue("Immunities", "None")
        }

        SectionHeader(title = "Offense")
        Column(modifier = Modifier.fillMaxWidth()) {
            BoldLabelToValue("Speed", monster.speed.entries.joinToString { "${it.key} ${it.value} ft." })
            monster.meleeAttacks?.let { BoldLabelToValue("Melee", it) }
            monster.rangedAttacks?.let { BoldLabelToValue("Melee", it) }
            BoldLabelToValue("Resistances", monster.resistances ?: "None")
            BoldLabelToValue("Immunities", monster.immunities ?: "None")
        }

        SectionHeader(title = "Statistics")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BoldLabelToValue("Str", monster.abilityScores.strength.toString())
            BoldLabelToValue("Dex", monster.abilityScores.dexterity.toString())
            BoldLabelToValue("Con", monster.abilityScores.constitution.toString())
            BoldLabelToValue("Int", monster.abilityScores.intelligence.toString())
            BoldLabelToValue("Wis", monster.abilityScores.wisdom.toString())
            BoldLabelToValue("Cha", monster.abilityScores.charisma.toString())
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BoldLabelToValue("Fortitude", monster.savingThrows.fortitude.formatAsModifier())
            BoldLabelToValue("Reflex", monster.savingThrows.reflex.formatAsModifier())
            BoldLabelToValue("Will", monster.savingThrows.will.formatAsModifier())
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BoldLabelToValue("Base Atk", monster.attackBonus.base.formatAsModifier())
            BoldLabelToValue("CMB", monster.attackBonus.cmb.formatAsModifier())
            BoldLabelToValue("CMD", monster.attackBonus.cmd.toString())
        }

        SectionHeader(title = "Special Qualities")
        monster.specialQualities?.forEach { quality ->
            Text(
                text = quality.capitaliseWords(),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        SectionHeader(title = "Special Abilities")
        monster.specialAbilities?.forEach { (name, description) ->
            BoldLabelToValue("${name.capitaliseWords()}:", description)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier.padding(top = 12.dp, bottom = 12.dp)
    )
}

@Composable
private fun BoldLabelToValue(label: String, value: String) {
    Row {
        Text(
            buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("$label ")
            }
            append(value)
        },
            style = MaterialTheme.typography.bodySmall)
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
    SummonAppTheme {
        MonsterNotFoundMessage("Fire Drake")
    }
}