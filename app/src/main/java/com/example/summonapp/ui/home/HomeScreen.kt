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

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.summonapp.MonsterTopAppBar
import com.example.summonapp.R
import com.example.summonapp.ui.AppViewModelProvider
import com.example.summonapp.data.Monster
import com.example.summonapp.models.AbilityScore
import com.example.summonapp.models.ArmourClass
import com.example.summonapp.models.AttackBonus
import com.example.summonapp.models.enums.Alignment as CreatureAlignment
import com.example.summonapp.models.enums.Size as CreatureSize
import com.example.summonapp.ui.navigation.NavigationDestination
import com.example.summonapp.ui.theme.SummonAppTheme
import java.util.Locale

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
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (String) -> Unit,
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .padding(
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateEndPadding(LocalLayoutDirection.current)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.item_entry_title)
                )
            }
        },
    ) { innerPadding ->
        HomeBody(
            itemList = homeUiState.itemList,
            onItemClick = { navigateToItemUpdate },
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}

@Composable
private fun HomeBody(
    itemList: List<Monster>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (itemList.isEmpty()) {
            Text(
                text = stringResource(R.string.empty_bestiary_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            SummonList(
                allMonsters = itemList,
                onItemClick = { onItemClick(it.name) },
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun SummonList(
    allMonsters: List<Monster>,
    onItemClick: (Monster) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val groupedMonsters = remember(allMonsters) {
        allMonsters.groupBy { it.summonLevel }
            .toSortedMap() // Ensures summon levels are sorted
    }
    val expandedStates = remember { mutableStateMapOf<Int, Boolean>() }

    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        groupedMonsters.forEach { (summonLevel, monsters) ->
            // Section Header
            val isExpanded = expandedStates[summonLevel] ?: true

            item {
                SummonLevelHeader(
                    summonLevel = summonLevel,
                    isExpanded = isExpanded,
                    onToggleExpand = {
                        expandedStates[summonLevel] = !isExpanded
                    }
                )
            }
            // Monster List (sorted by name)
            if (isExpanded) {
                items(monsters.sortedBy { it.name }) { monster ->
                    MonsterItem(
                        monster = monster,
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_small))
                            .clickable { onItemClick(monster) })
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
            fontSize = 20.sp,
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
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = monster.name,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = monster.summonLevel.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = monsterDescription(monster),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

fun monsterDescription(monster: Monster): String {
    val alignment = monster.alignment.toString().lowercase().replace("_", " ")
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    val creatureType = monster.creatureType.lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    val size = monster.size.toString().lowercase()
    val subtypes = if (monster.creatureSubtypes.isNotEmpty()) {
        "(" + monster.creatureSubtypes.joinToString(", ") + ")"
    } else {
        ""
    }
    return "%s %s %s %s".format(alignment, size, creatureType, subtypes)
}

@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    SummonAppTheme {
        HomeBody(listOf(
            getPreviewMonster()
        ), onItemClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyEmptyListPreview() {
    SummonAppTheme {
        HomeBody(listOf(), onItemClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun MonsterItemPreview() {
    SummonAppTheme {
        MonsterItem(
            getPreviewMonster()
        )
    }
}

fun getPreviewMonster(): Monster {
    val previewMonster = Monster(
        name = "Fire Drake",
        summonLevel = 5,
        cr = "5",
        size = CreatureSize.MEDIUM,
        alignment = CreatureAlignment.CHAOTIC_EVIL,
        creatureType = "Dragon",
        creatureSubtypes = listOf("Something", "Something else"),
        initiative = 3,
        perception = 10,
        senses = listOf("Darkvision 60ft", "Scent"),
        armourClass = ArmourClass(base = 18, touch = 12, flatFooted = 16),
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
