package entity

data class ProducteComandaQuantitat(
    val id: Int,
    val nom: String,
    val preu: Double,
    val imatgeNom: String?,
    val quantitat: Int
)
