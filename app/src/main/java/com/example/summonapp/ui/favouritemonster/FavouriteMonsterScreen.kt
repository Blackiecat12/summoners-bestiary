package com.example.summonapp.ui.favouritemonster

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.summonapp.MonsterBottomNavBar
import com.example.summonapp.MonsterTopAppBar
import com.example.summonapp.R
import com.example.summonapp.data.Monster
import com.example.summonapp.ui.AppViewModelProvider
import com.example.summonapp.ui.home.HomeDestination
import com.example.summonapp.ui.home.MonsterItem
import com.example.summonapp.ui.navigation.NavigationDestination

object FavouriteMonsterDestination : NavigationDestination {
    override val route = "favouriteMonsters"
    override val titleRes = R.string.favourite_monsters_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteMonsterScreen(
    navigateToMonsterInfo: (String) -> Unit,
    onBottomBarClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavouriteMonsterViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val favouritedMonsters by viewModel.favouritedMonsters.collectAsState()
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
        bottomBar = { MonsterBottomNavBar( onBottomBarClick, FavouriteMonsterDestination.route ) }
    ) { innerPadding ->
        FavouriteMonsterBody(
            monsterList = favouritedMonsters,
            onItemClick = navigateToMonsterInfo,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}

@Composable
private fun FavouriteMonsterBody(
    monsterList: List<Monster>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = monsterList) { monster ->
            MonsterItem(
                monster = monster,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onItemClick(monster.name) }
            )
        }
    }
}