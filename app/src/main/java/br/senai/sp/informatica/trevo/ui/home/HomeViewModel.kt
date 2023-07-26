package br.senai.sp.informatica.trevo.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import br.senai.sp.informatica.trevo.constants.PAGE_Load_Size
import br.senai.sp.informatica.trevo.constants.PAGE_Max_Size
import br.senai.sp.informatica.trevo.constants.PAGE_Prefetch
import br.senai.sp.informatica.trevo.constants.PAGE_Size
import br.senai.sp.informatica.trevo.database.CadastroDatabase
import br.senai.sp.informatica.trevo.mediator.ProdutoMediator
import br.senai.sp.informatica.trevo.model.ProdutoCache
import br.senai.sp.informatica.trevo.service.TrevoService
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalPagingApi::class)
class HomeViewModel(context: Context) : ViewModel() {
    private val service = TrevoService.create()
    private var pesquisaAtual: String = ""
    private val cadastroDatabase = CadastroDatabase.instance(context)
    private val cadastroDao = cadastroDatabase.cadastroDao()
    private val produtoDao = cadastroDatabase.produtoDao()
    private val pagingConfig = PagingConfig(
        pageSize = PAGE_Size,
        enablePlaceholders = false,
        maxSize = PAGE_Max_Size,
        prefetchDistance = PAGE_Prefetch,
        initialLoadSize = PAGE_Load_Size,
        jumpThreshold = PAGE_Size * 3)
    private var mediator: ProdutoMediator? = null
        get() {
            if(field == null) {
                field = ProdutoMediator(pesquisaAtual, service, cadastroDatabase)
            }
            return field
        }
    val flow: Flow<PagingData<ProdutoCache>>
        get () {
            return Pager(
                config = pagingConfig,
                remoteMediator = mediator,
                pagingSourceFactory = { cadastroDao.getProdutos() }
            ).flow
        }

    fun pesquisar(pesquisa: String) {
        if(pesquisa != pesquisaAtual) {
            pesquisaAtual = pesquisa
            mediator?.atualiza(pesquisa)
        }
    }

    suspend fun existemProdutosNoOrcamento(): Boolean {
        return produtoDao.countProduto() > 0
    }

    class Factory(private val context: Context): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}