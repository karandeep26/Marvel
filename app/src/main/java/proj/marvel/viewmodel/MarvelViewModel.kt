package proj.marvel.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import proj.marvel.service.repository.MarvelRepository

class MarvelViewModel(application: Application) : AndroidViewModel(application) {
    private var repository = MarvelRepository()
    var listObservable = repository.fetchData()
    fun fetchData() {
        repository.fetchData()
    }
}