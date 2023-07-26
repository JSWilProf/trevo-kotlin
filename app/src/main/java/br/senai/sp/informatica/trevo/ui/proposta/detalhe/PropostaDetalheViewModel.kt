package br.senai.sp.informatica.trevo.ui.proposta.detalhe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.senai.sp.informatica.trevo.mediator.ProdutoPagingSource
import br.senai.sp.informatica.trevo.model.Produto
import br.senai.sp.informatica.trevo.service.TrevoService
import kotlinx.coroutines.flow.Flow

class PropostaDetalheViewModel(private val produtos: List<Int>)  : ViewModel() {
    private val service = TrevoService.create()
    private val pagingConfig = PagingConfig(
        pageSize = produtos.size,
        enablePlaceholders = false)
    val flow: Flow<PagingData<Produto>>
        get () {
            return Pager(
                config = pagingConfig,
                pagingSourceFactory = { ProdutoPagingSource(produtos, service) }
            ).flow.cachedIn(viewModelScope)
        }

    class Factory(private val produtos: List<Int>): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PropostaDetalheViewModel::class.java)) {
                return PropostaDetalheViewModel(produtos) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}