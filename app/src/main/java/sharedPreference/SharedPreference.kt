package sharedPreference

import android.content.Context

class SharedPreference(private val context: Context) {
    private val prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    private fun userKey(username: String) = "user_$username"

   
    fun registrarUsuari(usuari: String, contrasenya: String): Boolean {
        val key = userKey(usuari)
        if (prefs.contains(key)) {
            return false
        }
        prefs.edit().putString(key, contrasenya).apply()
        return true
    }

    
    fun validarCredencials(usuari: String, contrasenya: String): Boolean {
        val key = userKey(usuari)
        val guardat = prefs.getString(key, null)
        return guardat != null && guardat == contrasenya
    }

    
    fun setSessioUsuari(usuari: String) {
        prefs.edit()
            .putBoolean("estaLogat", true)
            .putString("usuari_actual", usuari)
            .apply()
    }

    fun estaLogat(): Boolean {
        return prefs.getBoolean("estaLogat", false)
    }


    fun getUsuari(): String? {
        return prefs.getString("usuari_actual", null)
    }
   
    fun logout() {
        prefs.edit().putBoolean("estaLogat", false).remove("usuari_actual").apply()
    }
}