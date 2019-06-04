package com.roguekingapps.bgdb.data.network

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET

interface BoardGamesService {

    @GET("xmlapi2/hot/")
    fun getBoardGames(): Observable<Response<BoardGamesDto>>

}