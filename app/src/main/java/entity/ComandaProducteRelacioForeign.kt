package entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "comanda_producte",
    primaryKeys = ["comandaId", "producteId"],
    indices = [Index(value = ["producteId"])]
)
data class ComandaProducteRelacioForeign(
    val comandaId: Int,
    val producteId: Int,
    val quantitat: Int = 1
)
