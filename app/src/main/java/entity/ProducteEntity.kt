package entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productes")
data class ProducteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nom: String,
    val preu: Double,
    val categoria: String,
    val imatgeNom: String
)