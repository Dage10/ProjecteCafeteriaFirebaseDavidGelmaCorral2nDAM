package viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import database.AppDatabase
import entity.ComandaFirebase
import kotlinx.coroutines.launch
import repository.FirebaseRepository
import repository.Repository

class HistorialViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository
    private val firebaseRepo = FirebaseRepository()
    private val _comandaNova = MutableLiveData(false)
    val comandaNova: LiveData<Boolean> = _comandaNova
    private val _comandasFirebase = MutableLiveData<List<ComandaFirebase>>()
    val comandasFirebase: LiveData<List<ComandaFirebase>> = _comandasFirebase

    init {
        val db = AppDatabase.getDatabase(application)
        repository = Repository(db.comandaDao(), db.producteDao(), db.comandaProducteDao())
    }

    fun getOrdresUsuariFirebase() {
        viewModelScope.launch {
            val uid = firebaseRepo.getUsuariActual()
            if (uid != null) {
                firebaseRepo.obtenirComandasUsuari(uid).onSuccess { comandas ->
                    _comandasFirebase.value = comandas
                }.onFailure {
                    _comandasFirebase.value = emptyList()
                }
            } else {
                _comandasFirebase.value = emptyList()
            }
        }
    }

    fun deleteComandaFirebase(comandaId: String) {
        viewModelScope.launch {
            val uid = firebaseRepo.getUsuariActual()
            if (uid != null) {
                firebaseRepo.eliminarComanda(uid, comandaId).onSuccess {
                    getOrdresUsuariFirebase()
                }
            }
        }
    }

    fun actualizarComandaFirebase(comandaId: String, nouTotal: Double) {
        viewModelScope.launch {
            val uid = firebaseRepo.getUsuariActual()
            if (uid != null) {
                val comandaActual = _comandasFirebase.value?.find { it.id == comandaId }
                comandaActual?.let { comanda ->
                    val comandaActualitzada = comanda.copy(total = nouTotal)
                    firebaseRepo.actualizarComanda(uid, comandaId, comandaActualitzada).onSuccess {
                        getOrdresUsuariFirebase()
                    }
                }
            }
        }
    }

    fun handleComandaRealitzada() {
        _comandaNova.value = true
    }

    fun clearComandaNova() {
        _comandaNova.value = false
    }
}