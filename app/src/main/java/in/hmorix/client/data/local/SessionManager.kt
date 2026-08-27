package in.hmorix.client.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import in.hmorix.client.data.model.User

class SessionManager(context: Context) {
    private val prefs: SharedPreferences

    init {
        prefs = try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            EncryptedSharedPreferences.create(
                context,
                "hmorix_secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            context.getSharedPreferences("hmorix_client_prefs", Context.MODE_PRIVATE)
        }
    }

    private val gson = Gson()

    var authToken: String?
        get() = prefs.getString(KEY_AUTH_TOKEN, null)
        set(value) = prefs.edit().putString(KEY_AUTH_TOKEN, value).apply()

    var sessionCookie: String?
        get() = prefs.getString(KEY_SESSION_COOKIE, null)
        set(value) = prefs.edit().putString(KEY_SESSION_COOKIE, value).apply()

    var apiBaseUrl: String
        get() = prefs.getString(KEY_API_BASE_URL, "https://hmorix.in/api") ?: "https://hmorix.in/api"
        set(value) = prefs.edit().putString(KEY_API_BASE_URL, value).apply()

    var currentUser: User?
        get() {
            val json = prefs.getString(KEY_CURRENT_USER, null) ?: return null
            return try { gson.fromJson(json, User::class.java) } catch (e: Exception) { null }
        }
        set(value) {
            if (value == null) {
                prefs.edit().remove(KEY_CURRENT_USER).apply()
            } else {
                prefs.edit().putString(KEY_CURRENT_USER, gson.toJson(value)).apply()
            }
        }

    val isLoggedIn: Boolean
        get() = !authToken.isNullOrEmpty() || !sessionCookie.isNullOrEmpty()

    fun clearSession() {
        prefs.edit()
            .remove(KEY_AUTH_TOKEN)
            .remove(KEY_SESSION_COOKIE)
            .remove(KEY_CURRENT_USER)
            .apply()
    }

    companion object {
        private const val KEY_AUTH_TOKEN = "key_auth_token"
        private const val KEY_SESSION_COOKIE = "key_session_cookie"
        private const val KEY_API_BASE_URL = "key_api_base_url"
        private const val KEY_CURRENT_USER = "key_current_user"
    }
}
