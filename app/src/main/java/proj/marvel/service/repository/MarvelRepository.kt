package proj.marvel.service.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import proj.marvel.service.model.ResponseWrapper

class MarvelRepository {

    private val service = MarvelService.getInstance
    private var list: MutableLiveData<ResponseWrapper> = MutableLiveData()
    private var offset = 0
    private var totalItems = 0
    private val OFFSET_INCREASE_VALUE = 20

    @SuppressLint("CheckResult")
    fun fetchData(): LiveData<ResponseWrapper> {
        service.getData(offset).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { response ->
                if (response.isSuccessful) {
                    response.body()?.data?.results?.asSequence()?.filter {
                        it.description.isEmpty()
                    }?.map {
                        if (it.description.isEmpty()) {
                            it.description = "no description available"
                        }
                    }?.toList()
                }
                return@map response
            }
            .subscribe({
                if (it.isSuccessful) {
                    offset += OFFSET_INCREASE_VALUE
                    val body = it.body()
                    if (body != null) {
                        totalItems = body.data.total
                        list.postValue(ResponseWrapper(body.data.results, null, offset == totalItems))

                    } else {
                        list.postValue(ResponseWrapper(null, Exception(it.errorBody()?.string()), false))
                    }
                }
            }, {
                list.postValue(ResponseWrapper(null, Exception(it), false))
            })
        return list
    }
}