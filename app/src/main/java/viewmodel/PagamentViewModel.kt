package viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import database.AppDatabase
import kotlinx.coroutines.launch
import repository.FirebaseRepository
import repository.Repository

class PagamentViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository
    private val firebaseRepo = FirebaseRepository()
    private val _comandaFeta = MutableLiveData(false)
    val comandaFeta: LiveData<Boolean> = _comandaFeta

    init {
        val db = AppDatabase.getDatabase(application)
        repository = Repository(db.comandaDao(), db.producteDao(),db.comandaProducteDao())
    }

    fun pagar(usuari: String, total: Double, productes: List<entity.ProducteSeleccionat>) {
        viewModelScope.launch {
            val result = firebaseRepo.guardarComanda(usuari, total, productes)
            result.onSuccess {
                _comandaFeta.postValue(true)
            }
            result.onFailure {
                _comandaFeta.postValue(false)
            }
        }
    }

    fun clearComandaFeta() {
        _comandaFeta.value = false
    }
}