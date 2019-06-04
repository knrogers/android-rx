package com.roguekingapps.bgdb.ui.boardgame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.roguekingapps.bgdb.data.database.BoardGame
import com.roguekingapps.bgdb.data.network.BoardGamesRepository
import com.roguekingapps.bgdb.data.network.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class BoardGamesViewModel(private val boardGamesRepository: BoardGamesRepository) : ViewModel() {

    private lateinit var subscriber : Disposable

    private val _boardGames  = MutableLiveData<Resource<List<BoardGame>>>()
    val boardGames : LiveData<Resource<List<BoardGame>>>
            get() = _boardGames

    fun getBoardGames() {
        subscriber = boardGamesRepository.getBoardGames()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _boardGames.value = it }
    }

    override fun onCleared() {
        super.onCleared()
        subscriber.dispose()
    }

}