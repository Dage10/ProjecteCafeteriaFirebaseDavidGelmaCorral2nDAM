package viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import entity.ProducteEntity

class SharedViewModel : ViewModel() {

    private val _productesSeleccionats = MutableLiveData<MutableList<ProducteEntity>>(mutableListOf())
    val productesSeleccionats: LiveData<MutableList<ProducteEntity>> = _productesSeleccionats

    private val _preuTotal = MutableLiveData<Double>(0.0)
    val preuTotal: LiveData<Double> = _preuTotal

    
    private val _comandaRealitzada = MutableLiveData<Unit?>()
    val comandaRealitzada: LiveData<Unit?> = _comandaRealitzada

    private fun recalculaPreuTotal(llista: List<ProducteEntity>) {
        _preuTotal.value = llista.sumOf { it.preu }
    }

    fun afegirProducte(producte: ProducteEntity) {
        val llista = _productesSeleccionats.value ?: mutableListOf()
        llista.add(producte)
        _productesSeleccionats.value = llista
        recalculaPreuTotal(llista)
    }

    fun eliminarProductes() {
        val buida = mutableListOf<ProducteEntity>()
        _productesSeleccionats.value = buida
        recalculaPreuTotal(buida)
    }

    fun notificarComandaRealitzada() {
        _comandaRealitzada.value = Unit
    }

    fun buidarComandaRealitzada() {
        _comandaRealitzada.value = null
    }
}
