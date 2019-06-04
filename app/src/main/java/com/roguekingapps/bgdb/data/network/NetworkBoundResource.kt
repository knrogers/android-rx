package com.roguekingapps.bgdb.data.network

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

abstract class NetworkBoundResource<RequestType, ResultType> {

    private val result: Observable<Resource<ResultType>>

    init {
        val diskObservable = Observable.defer { loadFromDb().subscribeOn(Schedulers.io()) }

        val networkObservable = Observable.defer {
            createCall()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext { request: Response<RequestType> ->
                    if (request.isSuccessful) saveCallResult(request.body())
                }
                .onErrorReturn { throwable: Throwable -> throw throwable }
                .flatMap { loadFromDb() }
        }

        result = Observable.merge(
            diskObservable
                .map<Resource<ResultType>> { Resource.success(it) }
                .onErrorReturn { Resource.error(it.message as String, null) }
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(Resource.loading(null)),
            networkObservable
                .map<Resource<ResultType>> { Resource.success(it) }
                .onErrorReturn { Resource.error(it.message as String, null) }
                .observeOn(AndroidSchedulers.mainThread())
        )

    }

    fun asObservable(): Observable<Resource<ResultType>> = result

    protected abstract fun saveCallResult(data: RequestType?)

    protected abstract fun loadFromDb(): Observable<ResultType>

    protected abstract fun createCall(): Observable<Response<RequestType>>
}