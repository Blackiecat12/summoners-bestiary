/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.summonapp.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.summonapp.MonsterTopAppBar
import com.example.summonapp.R
import com.example.summonapp.capitalise
import com.example.summonapp.data.Monster
import com.example.summonapp.models.AbilityScore
import com.example.summonapp.models.ArmourClass
import com.example.summonapp.models.AttackBonus
import com.example.summonapp.models.DamageResist
import com.example.summonapp.models.Health
import com.example.summonapp.models.SavingThrows
import com.example.summonapp.ui.AppViewModelProvider
import com.example.summonapp.ui.navigation.NavigationDestination
import com.example.summonapp.ui.theme.SummonAppTheme
import com.example.summonapp.models.enums.Alignment as CreatureAlignment
import com.example.summonapp.models.enums.Size as CreatureSize

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

/**
 * Entry route for Home screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToMonsterInfo: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MonsterTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        HomeBody(
            monsterList = homeUiState.monsterList,
            isExpandedSections = viewModel.expandedStates,
            toggleExpansion = { viewModel.toggleExpansion(it) },
            onItemClick = navigateToMonsterInfo,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}

@Composable
private fun HomeBody(
    monsterList: List<Monster>,
    isExpandedSections: Map<Int, Boolean>,
    toggleExpansion: (Int) -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (monsterList.isEmpty()) {
            Text(
                text = stringResource(R.string.empty_bestiary_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            SummonList(
                monsterList = monsterList,
                isExpandedSections = isExpandedSections,
                toggleExpansion = toggleExpansion,
                onItemClick = { onItemClick(it.name) },
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun SummonList(
    monsterList: List<Monster>,
    isExpandedSections: Map<Int, Boolean>,
    toggleExpansion: (Int) -> Unit,
    onItemClick: (Monster) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val groupedMonsters = remember(monsterList) {
        monsterList.groupBy { it.summonLevel }
            .toSortedMap() // Ensures summon levels are sorted
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        groupedMonsters.forEach { (summonLevel, monsters) ->
            // Section Header, default is not expanded
            val isExpanded = isExpandedSections[summonLevel] ?: false

            item {
                SummonLevelHeader(
                    summonLevel = summonLevel,
                    isExpanded = isExpanded,
                    onToggleExpand = {
                        toggleExpansion(summonLevel)
                    }
                )
            }
            // Monster List (sorted by name)
            item {
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        monsters.sortedBy { it.name }.forEach { monster ->
                            MonsterItem(
                                monster = monster,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(dimensionResource(id = R.dimen.padding_small))
                                    .clickable { onItemClick(monster) }
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun SummonLevelHeader(summonLevel: Int, isExpanded: Boolean, onToggleExpand: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onToggleExpand() } // Toggle expand/collapse
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Summon Level $summonLevel",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        if (isExpanded) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Expand/Collapse",
                modifier = Modifier.size(24.dp)
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.arrow_left_24px),
                contentDescription = "Expand/Collapse",
                modifier = Modifier.size(24.dp)
            )
        }

    }
}


@Composable
private fun MonsterItem(
    monster: Monster, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
        ) {
            // Name of monster
            Text(
                text = monster.name,
                style = MaterialTheme.typography.titleMedium,
            )
//            Spacer(modifier.padding(dimensionResource(id = R.dimen.padding_small)))
            Text(
                text = formatMonsterBasicDescription(monster),
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "HP: %d".format(monster.health.total),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "AC: %d".format(monster.armourClass.base),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Str %d".format(monster.abilityScores.strength),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Dex %d".format(monster.abilityScores.dexterity),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Con %d".format(monster.abilityScores.constitution),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Int %d".format(monster.abilityScores.intelligence),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Wis %d".format(monster.abilityScores.wisdom),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Cha %d".format(monster.abilityScores.charisma),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }
    }
}

fun formatMonsterBasicDescription(monster: Monster): String {
    val alignment = monster.alignment.displayString()
    val creatureType = monster.creatureType.capitalise()
    val size = monster.size.toString().lowercase()
    val subtypes = if (monster.creatureSubtypes.isNotEmpty()) {
        "(" + monster.creatureSubtypes.joinToString(", ") + ")"
    } else {
        ""
    }
    return "%s, %s, %s %s".format(alignment, size, creatureType, subtypes)
}

@Preview(showBackground = true)
@Composable
fun ThemePreview() {
    SummonAppTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Hello, SummonApp!",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun DirectFontTest() {
//    Text(
//        text = "Hello, ADLaM Display!",
//        style = TextStyle(
//            fontFamily = FontFamily(
//                Font(R.font.adlam_display)
//            ),
//            fontSize = 24.sp
//        )
//    )
//}


@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    SummonAppTheme() {
        HomeBody(
            listOf(
                getPreviewMonster(1),
                getPreviewMonster(2),
                getPreviewMonster(2)
            ),
            isExpandedSections = mapOf(1 to true),
            toggleExpansion = {},
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyEmptyListPreview() {
    SummonAppTheme {
        HomeBody(
            listOf(),
            isExpandedSections = mapOf(),
            toggleExpansion = {},
            onItemClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun MonsterItemPreview() {
    SummonAppTheme {
        MonsterItem(
            getPreviewMonster(1)
        )
    }
}

fun getPreviewMonster(summonLevel: Int): Monster {
    val previewMonster = Monster(
        name = "Fire Drake",
        summonLevel = summonLevel,
        cr = "5",
        size = CreatureSize.MEDIUM,
        alignment = CreatureAlignment.CHAOTIC_EVIL,
        creatureType = "Dragon",
        creatureSubtypes = listOf("Something", "Something else"),
        initiative = 3,
        perception = 10,
        senses = listOf("Darkvision 60ft", "Scent"),
        health = Health(total=100, hitDice = "8d8+4"),
        armourClass = ArmourClass(base = 18, touch = 12, flatFooted = 16),
        savingThrows = SavingThrows(1, 2, 3),
        damageResistance = DamageResist(5, "good"),
        spellResistance = 10,
        resistances = "Resistance to poison",
        immunities = "Immunity to fire",
        speed = mapOf("land" to 30, "fly" to 60),
        meleeAttacks = "Bite +10 (1d8+5), 2 Claws +8 (1d6+3)",
        rangedAttacks = "Fire Breath (30ft cone, 3d6 fire damage)",
        abilityScores = AbilityScore(
            strength = 18,
            dexterity = 14,
            constitution = 16,
            intelligence = 10,
            wisdom = 12,
            charisma = 14
        ),
        attackBonus = AttackBonus(base = 10, cmd = 18, cmb = 12),
        specialQualities = listOf("Fire Resistance 10", "Draconic Senses"),
        specialAbilities = mapOf(
            "Fire Breath" to "Deals 3d6 fire damage in a 30ft cone (once per 1d4 rounds)",
            "Wing Buffet" to "Can push enemies back 5ft when attacking with wings"
        )
    )
    return previewMonster
}
