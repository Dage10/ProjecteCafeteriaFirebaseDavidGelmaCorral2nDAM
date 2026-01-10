package viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import entity.ProducteEntity
import entity.ProducteSeleccionat

class SharedViewModel : ViewModel() {

    private val _productesSeleccionats = MutableLiveData<MutableList<ProducteSeleccionat>>(mutableListOf())
    val productesSeleccionats: LiveData<MutableList<ProducteSeleccionat>> = _productesSeleccionats

    private val _preuTotal = MutableLiveData<Double>(0.0)
    val preuTotal: LiveData<Double> = _preuTotal

    
    private val _comandaRealitzada = MutableLiveData<Unit?>()
    val comandaRealitzada: LiveData<Unit?> = _comandaRealitzada

    private fun recalculaPreuTotal(llista: List<ProducteSeleccionat>) {
        _preuTotal.value = llista.sumOf { it.producte.preu * it.quantitat }
    }

    fun afegirProducte(producte: ProducteEntity) {
        val llista = _productesSeleccionats.value ?: mutableListOf()
        val existent = llista.find { it.producte.id == producte.id }
        if (existent != null) {
            existent.quantitat += 1
        } else {
            llista.add(ProducteSeleccionat(producte = producte, quantitat = 1))
        }
        _productesSeleccionats.value = llista
        recalculaPreuTotal(llista)
    }

    fun eliminarProductes() {
        val buida = mutableListOf<ProducteSeleccionat>()
        _productesSeleccionats.value = buida
        recalculaPreuTotal(buida)
    }

    fun eliminarProducte(producteId: Int) {
        val llista = _productesSeleccionats.value ?: return
        val nova = llista.filter { it.producte.id != producteId }.toMutableList()
        _productesSeleccionats.value = nova
        recalculaPreuTotal(nova)
    }

    fun setQuantitat(producteId: Int, quantitat: Int) {
        val llista = _productesSeleccionats.value ?: return
        val existent = llista.find { it.producte.id == producteId } ?: return
        if (quantitat <= 0) {
            eliminarProducte(producteId)
            return
        }
        existent.quantitat = quantitat
        _productesSeleccionats.value = llista
        recalculaPreuTotal(llista)
    }

    fun notificarComandaRealitzada() {
        _comandaRealitzada.value = Unit
    }

    fun buidarComandaRealitzada() {
        _comandaRealitzada.value = null
    }
}
