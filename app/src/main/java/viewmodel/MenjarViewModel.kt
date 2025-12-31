package viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import database.AppDatabase
import entity.ProducteEntity
import repository.Repository

class MenjarViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository

    val menjarProductes: LiveData<List<ProducteEntity>>

    private val _producteAfegit = MutableLiveData<ProducteEntity?>()
    val producteAfegit: LiveData<ProducteEntity?> = _producteAfegit

    init {
        val db = AppDatabase.getDatabase(application)
        repository = Repository(db.comandaDao(), db.producteDao(),db.comandaProducteDao())
        menjarProductes = repository.getProductesPerCategoria("menjar")
    }

    fun afegirProducte(producte: ProducteEntity) {
        _producteAfegit.value = producte
    }

    fun buidarProducteAfegit() {
        _producteAfegit.value = null
    }
}
