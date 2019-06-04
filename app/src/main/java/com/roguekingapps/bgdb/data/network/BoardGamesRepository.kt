package com.roguekingapps.bgdb.data.network

import com.roguekingapps.bgdb.data.database.BoardGame
import com.roguekingapps.bgdb.data.database.BoardGameDao
import io.reactivex.Observable
import retrofit2.Response

class BoardGamesRepositoryImpl(
    private val service: BoardGamesService,
    private val boardGameDao: BoardGameDao
) : BoardGamesRepository {

    override fun getBoardGames(): Observable<Resource<List<BoardGame>>> {
        return object : NetworkBoundResource<BoardGamesDto, List<BoardGame>>() {

            override fun saveCallResult(data: BoardGamesDto?) {
                if (data == null) return
                val boardGames = data.boardGameDtos.map {
                    BoardGame(it.rank, it.id, it.name, it.year, it.thumbnailUrl)
                }
                boardGameDao.insertAll(boardGames)
            }

            override fun loadFromDb(): Observable<List<BoardGame>> = boardGameDao.getBoardGames().toObservable()

            override fun createCall(): Observable<Response<BoardGamesDto>> = service.getBoardGames()
        }.asObservable()
    }

}

interface BoardGamesRepository {

    fun getBoardGames() : Observable<Resource<List<BoardGame>>>

}