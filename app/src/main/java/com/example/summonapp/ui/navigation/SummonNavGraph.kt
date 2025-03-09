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

package com.example.summonapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.summonapp.data.FavouriteMonster
import com.example.summonapp.ui.favouritemonster.FavouriteMonsterDestination
import com.example.summonapp.ui.favouritemonster.FavouriteMonsterScreen
import com.example.summonapp.ui.home.HomeDestination
import com.example.summonapp.ui.home.HomeScreen
import com.example.summonapp.ui.monsterdetails.MonsterDetailDestination
import com.example.summonapp.ui.monsterdetails.MonsterDetailScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun MonsterNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToMonsterInfo = {
                    navController.navigate("${MonsterDetailDestination.route}/$it")
                },
                onBottomBarClick = { navController.navigate(it) }
            )
        }
        composable(
            route = MonsterDetailDestination.routeWithArgs,
            arguments = listOf(navArgument(MonsterDetailDestination.monsterIdArg) {
                type = NavType.StringType
            })
        ){
            MonsterDetailScreen(
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = FavouriteMonsterDestination.route
        ) {
            FavouriteMonsterScreen(
                navigateToMonsterInfo = {
                    navController.navigate("${MonsterDetailDestination.route}/$it")
                },
                onBottomBarClick = { navController.navigate(it) }
            )
        }
    }
}
