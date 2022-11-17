package com.colinjp.inblo.di

import com.colinjp.inblo.interactors.horse_list.*

class GetHorseListModule(
    val networkModule: NetworkModule
) {
    val searchHorses: SearchHorses by lazy {
        SearchHorses(
            networkModule.horseService
        )
    }

    val getUsernames: GetUsernames by lazy {
        GetUsernames(
            networkModule.userService
        )
    }

    val addHorse: AddHorse by lazy {
        AddHorse(
            networkModule.horseService
        )
    }

    val archiveHorse: ArchiveHorse by lazy {
        ArchiveHorse(
            networkModule.horseService
        )
    }

    val restoreArchivedHorses: RestoreArchivedHorse by lazy {
        RestoreArchivedHorse(
            networkModule.horseService
        )
    }
}