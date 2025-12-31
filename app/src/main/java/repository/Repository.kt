package repository

import androidx.lifecycle.LiveData
import database.ComandaDAO
import database.ComandaProducteDAO
import database.ProducteDAO
import entity.ComandaEntity
import entity.ComandaProducteRelacioForeign
import entity.ProducteEntity

class Repository(
    private val comandaDao: ComandaDAO,
    private val producteDao: ProducteDAO,
    private val comandaProducteDao: ComandaProducteDAO
) {

    fun getOrdresUsuari(usuari: String): LiveData<List<ComandaEntity>> =
        comandaDao.obtenirOrdresUsuari(usuari)


    suspend fun insertComandaAmbProductes(comanda: ComandaEntity, productes: List<ProducteEntity>): Long {
        val id = comandaDao.insertarComanda(comanda)
        val comandaId = id.toInt()
        for (producte in productes) {
            comandaProducteDao.insert(ComandaProducteRelacioForeign(comandaId = comandaId, producteId = producte.id))
        }
        return id
    }
    suspend fun updateComanda(comanda: ComandaEntity) = comandaDao.updateComanda(comanda)
    suspend fun deleteComanda(comanda: ComandaEntity) = comandaDao.deleteComanda(comanda)

    fun getProductesPerCategoria(categoria: String): LiveData<List<ProducteEntity>> =
        producteDao.getProductesCategoria(categoria)

}
