package br.senai.sp.informatica.trevo.ui.cliente

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.senai.sp.informatica.trevo.dao.ClienteDao
import br.senai.sp.informatica.trevo.model.Cliente

class ClienteViewModel(context: Context) : ViewModel() {
    private var clienteDao = ClienteDao.instance(context)

    fun cliente(): MutableLiveData<Cliente> {
        return MutableLiveData(clienteDao.getCliente()!!)
    }

    fun salvarDados(cliente: Cliente) {
        clienteDao.setCliente(cliente)
    }

    class Factory(private val context: Context): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ClienteViewModel::class.java)) {
                return ClienteViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}