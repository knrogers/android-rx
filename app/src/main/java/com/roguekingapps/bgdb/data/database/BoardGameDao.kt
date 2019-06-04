package com.roguekingapps.bgdb.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.reactivex.Maybe

@Dao
interface BoardGameDao {

    @Query("SELECT * FROM boardgames")
    fun getBoardGames() : Maybe<List<BoardGame>>

    @Insert(onConflict = REPLACE)
    fun insertAll(boardGames: List<BoardGame>)

}