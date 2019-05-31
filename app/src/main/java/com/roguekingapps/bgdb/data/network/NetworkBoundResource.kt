package com.roguekingapps.bgdb.data.network

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

abstract class NetworkBoundResource<RequestType, ResultType> {

    private val result: Flowable<Resource<ResultType>>

    init {
        val diskObservable = Flowable.defer { loadFromDb().subscribeOn(Schedulers.io()) }

        val networkObservable = Flowable.defer {
            createCall()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext { request: Response<RequestType> ->
                    if (request.isSuccessful) saveCallResult(request.body())
                }
                .onErrorReturn { throwable: Throwable -> throw throwable }
                .flatMap { loadFromDb() }
        }

        result = Flowable.merge(
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

    fun asFlowable(): Flowable<Resource<ResultType>> = result

    protected abstract fun saveCallResult(data: RequestType?)

    protected abstract fun loadFromDb(): Flowable<ResultType>

    protected abstract fun createCall(): Flowable<Response<RequestType>>
}