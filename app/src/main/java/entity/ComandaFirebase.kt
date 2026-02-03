package entity

data class ComandaFirebase(
    val id: String = "",
    val usuari: String = "",
    val total: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis(),
    val productes: Map<String, ProducteComandaFirebase> = emptyMap()
)