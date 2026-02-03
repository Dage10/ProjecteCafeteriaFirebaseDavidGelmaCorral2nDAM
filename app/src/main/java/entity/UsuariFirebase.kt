package entity

data class UsuariFirebase(
    val uid: String = "",
    val nom: String = "",
    val created_at: Long = System.currentTimeMillis()
)
