package br.senai.sp.informatica.trevo.ui.orcamento

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.senai.sp.informatica.trevo.constants.PAGE_Size
import br.senai.sp.informatica.trevo.dao.ClienteDao
import br.senai.sp.informatica.trevo.database.CadastroDatabase
import br.senai.sp.informatica.trevo.model.ProdutoItem
import br.senai.sp.informatica.trevo.model.Proposta
import br.senai.sp.informatica.trevo.service.TrevoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.singleOrNull

class OrcamentoViewModel(context: Context)  : ViewModel() {
    private val service = TrevoService.create() // para o envio do orcamaneto ao backend
    private val clienteDao = ClienteDao.instance(context)
    private val cadastroDatabase = CadastroDatabase.instance(context)
    private val produtoDao = cadastroDatabase.produtoDao()
    private val pagingConfig = PagingConfig(
        pageSize = PAGE_Size,
        enablePlaceholders = false)
    val flow: Flow<PagingData<ProdutoItem>>
        get () {
            return Pager(
                config = pagingConfig,
                pagingSourceFactory = { produtoDao.getProdutos() }
            ).flow.cachedIn(viewModelScope)
        }

    fun existeCadastro() : Boolean {
        val cliente = clienteDao.getCliente()
        if(cliente != null) {
            cliente.let {
                return it.nome.isNotEmpty() && it.email.isNotEmpty() && it.telefone.isNotEmpty()
            }
        } else {
            return false
        }
    }

    suspend fun existemProdutosNoOrcamento(): Boolean {
        return produtoDao.countProduto() > 0
    }

    suspend fun removeItem(id: Int) {
        produtoDao.deleteProduto(id)
    }

    suspend fun enviaSolicitacao(): Boolean {
        val proposta = Proposta(
            cliente = clienteDao.getCliente()!!,
            produtos = produtoDao.getListaDeProdutos().map(ProdutoItem::idProduto).toList()
        )
        return try {
            val resposta = service.sendProposta(proposta)
                .flowOn(Dispatchers.IO)
                .singleOrNull()
            if (resposta != null) {
                produtoDao.deleteProdutos()
                true
            } else {
                false
            }
        } catch (ex: Exception) {
            false
        }
    }

    class Factory(private val context: Context): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OrcamentoViewModel::class.java)) {
                return OrcamentoViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}