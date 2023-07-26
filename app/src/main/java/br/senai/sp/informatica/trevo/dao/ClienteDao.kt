package br.senai.sp.informatica.trevo.dao

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import br.senai.sp.informatica.trevo.model.Cliente

class ClienteDao {
    companion object {
        @Volatile
        private var preferences: SharedPreferences? = null

        fun instance(context: Context): ClienteDao {
            if(preferences == null) {
                preferences = PreferenceManager.getDefaultSharedPreferences(context)
            }
            return ClienteDao()
        }
    }

    fun getCliente(): Cliente? {
        preferences?.let {
            return Cliente(
                nome = it.getString("NOME", "")!!,
                email = it.getString("EMAIL", "")!!,
                telefone = it.getString("FONE", "")!!
            )
        }
        return null
    }

    fun setCliente(cliente: Cliente) {
        preferences?.let {
            it.edit().run {
                putString("NOME", cliente.nome)
                putString("EMAIL", cliente.email)
                putString("FONE", cliente.telefone)
                apply()
            }
        }
    }
}