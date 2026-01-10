package database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import entity.ComandaProducteRelacioForeign
import entity.ComandaAProductes

@Dao
interface ComandaProducteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ref: ComandaProducteRelacioForeign)

    @Transaction
    @Query("SELECT * FROM comandas WHERE id = :comandaId")
    suspend fun getComandaAmbProductes(comandaId: Int): ComandaAProductes

    @Query("""
        SELECT producte.id, producte.nom, producte.preu, producte.imatgeNom, cp.quantitat
        FROM productes producte
        JOIN comanda_producte cp ON producte.id = cp.producteId
        WHERE cp.comandaId = :comandaId
    """)
    suspend fun getProductesQuantitatPerComanda(comandaId: Int): List<entity.ProducteComandaQuantitat>

    @Query("DELETE FROM comanda_producte WHERE comandaId = :comandaId")
    suspend fun eliminarPerComandaId(comandaId: Int)
}
