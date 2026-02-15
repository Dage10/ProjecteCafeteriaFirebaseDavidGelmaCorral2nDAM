package repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import entity.ComandaFirebase
import entity.ProducteComandaFirebase
import entity.ProducteSeleccionat
import entity.UsuariFirebase
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun registrarUsuari(username: String, nom: String, password: String): Result<String> {
        return try {
            val email = "$username@example.com"
            val authResultat = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResultat.user?.uid ?: throw Exception("Error al crear usuari")
            val usuari = UsuariFirebase(uid = uid, nom = nom, created_at = System.currentTimeMillis())
            db.collection("usuaris")
                .document(uid)
                .set(usuari)
                .await()
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUsuari(username: String, password: String): Result<String> {
        return try {
            val email = "$username@example.com"
            val authResultat = auth.signInWithEmailAndPassword(email, password).await()
            val uid = authResultat.user?.uid ?: throw Exception("Error al fer login")
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUsuariActual(): String? = auth.currentUser?.uid

    suspend fun guardarComanda(usuari: String, total: Double, productes: List<ProducteSeleccionat>): Result<String> {
        return try {
            val uid = getUsuariActual() ?: throw Exception("Usuari no autenticat")
            val usuariRef = db.collection("usuaris").document(uid)


            val comandes = usuariRef.collection("comandas").get().await()

            val maxIndex = comandes.documents
                .mapNotNull { it.id.removePrefix("comanda").toIntOrNull() }
                .maxOrNull() ?: 0


            val idComanda = "comanda${maxIndex + 1}"


            val productesMap = mutableMapOf<String, ProducteComandaFirebase>()
            productes.forEachIndexed { index, producte ->
                productesMap["producte_$index"] = ProducteComandaFirebase(
                    nom = producte.producte.nom,
                    preu = producte.producte.preu,
                    quantitat = producte.quantitat
                )
            }


            val comanda = ComandaFirebase(
                id = idComanda,
                usuari = usuari,
                total = total,
                timestamp = System.currentTimeMillis(),
                productes = productesMap
            )


            usuariRef.collection("comandas").document(idComanda).set(comanda).await()

            Result.success(idComanda)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun obtenirComandasUsuari(uid: String): Result<List<ComandaFirebase>> {
        return try {
            val comandes = db.collection("usuaris")
                .document(uid).collection("comandas")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()
            val comandas = comandes.documents.mapNotNull { doc ->
                doc.toObject(ComandaFirebase::class.java)?.copy(id = doc.id)
            }
            Result.success(comandas)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarComanda(uidUsuari: String, comandaId: String): Result<Unit> {
        return try {
            db.collection("usuaris")
                .document(uidUsuari)
                .collection("comandas")
                .document(comandaId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarComanda(uidUsuari: String, comandaId: String, comanda: ComandaFirebase): Result<Unit> {
        return try {
            db.collection("usuaris")
                .document(uidUsuari)
                .collection("comandas")
                .document(comandaId)
                .set(comanda)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}