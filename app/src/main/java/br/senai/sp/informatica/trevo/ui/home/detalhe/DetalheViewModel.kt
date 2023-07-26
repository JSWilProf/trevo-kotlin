package br.senai.sp.informatica.trevo.ui.home.detalhe

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.senai.sp.informatica.trevo.database.CadastroDatabase
import br.senai.sp.informatica.trevo.model.ProdutoCache
import br.senai.sp.informatica.trevo.model.ProdutoItem

class DetalheViewModel(context: Context) : ViewModel() {
    private var cadastroDao = CadastroDatabase.instance(context).cadastroDao()
    private var carrinhoDao = CadastroDatabase.instance(context).produtoDao()

    suspend fun produto(id: Int): MutableLiveData<ProdutoCache> {
        return MutableLiveData(cadastroDao.getProduto(id))
    }

    suspend fun adicionaNoCarrinho(produto: ProdutoItem): Boolean {
        val item = carrinhoDao.getProduto(produto.idProduto)
        return if(item == null) {
            carrinhoDao.addProduto(produto)
            true
        } else {
            false
        }
    }

    class Factory(private val context: Context): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetalheViewModel::class.java)) {
                return DetalheViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}