package com.roguekingapps.bgdb.di

import com.roguekingapps.bgdb.data.network.BoardGamesService
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

@Module
object BoardGamesServiceModule {

    @Provides
    @JvmStatic
    fun provideBoardGamesService(): BoardGamesService =
        Retrofit.Builder()
            .baseUrl("http://www.boardgamegeek.com/")
            .addConverterFactory(TikXmlConverterFactory.create(TikXml.Builder().exceptionOnUnreadXml(false).build()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(BoardGamesService::class.java)

}