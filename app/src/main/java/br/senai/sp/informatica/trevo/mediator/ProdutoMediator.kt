package br.senai.sp.informatica.trevo.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import br.senai.sp.informatica.trevo.database.CadastroDatabase
import br.senai.sp.informatica.trevo.model.ProdutoCache
import br.senai.sp.informatica.trevo.model.CadastroKeys
import br.senai.sp.informatica.trevo.model.ProdutosResponse
import br.senai.sp.informatica.trevo.model.mapper.ProdutoMapper
import br.senai.sp.informatica.trevo.service.TrevoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single
import kotlin.streams.toList

@OptIn(ExperimentalPagingApi::class)
class ProdutoMediator(
    private var pesquisa: String,
    private val service: TrevoService,
    cadastroDatabase: CadastroDatabase
): RemoteMediator<Int, ProdutoCache>() {
    private val cadastroDao = cadastroDatabase.cadastroDao()
    private val cadastroKeysDao = cadastroDatabase.cadastroKeysDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, ProdutoCache>): MediatorResult {
        return try {
            val position = when(loadType) {
                LoadType.REFRESH -> {
                    val cadastroKeys = getCadastroKeyClosestToCurrentPosition(state)
                    val nextPage = cadastroKeys?.nextPage?.minus(1) ?: 0
                    nextPage
                }
                LoadType.PREPEND -> {
                    val cadastroKeys = getCadastroKeyForFirstItem(state)
                    val prevPage = cadastroKeys?.prevPage
                        ?: return MediatorResult.Success(endOfPaginationReached = cadastroKeys != null)
                    prevPage
                }
                LoadType.APPEND -> {
                    val cadastroKeys = getCadastroKeyForLastItem(state)
                    val nextPage = cadastroKeys?.nextPage
                        ?: return MediatorResult.Success(endOfPaginationReached = cadastroKeys != null)
                    nextPage
                }
            }

            val response: ProdutosResponse = service.getProdutos(pesquisa, position)
                .flowOn(Dispatchers.IO)
                .single()

            val endOfPages = position >= response.totalPages - 1
            val prevPage = if (position <= 0) null else position - 1
            val nextPage = if (endOfPages) null else position + 1

            if(loadType == LoadType.REFRESH) {
                cadastroDao.deleteProdutos()
                cadastroKeysDao.deleteAllCadastroKeys()
            }

            cadastroDao.addProdutos(response.content.stream().map {
                ProdutoMapper.toProdutoCache(it)
            }.toList())
            cadastroKeysDao.addAllCadastroKeys(response.content.stream().map {
                CadastroKeys(
                    id = it.idProduto,
                    prevPage = prevPage,
                    nextPage = nextPage
                )
            }.toList())
            MediatorResult.Success(endOfPages)
        } catch (ex: Exception) {
            MediatorResult.Error(ex)
        }
    }

    private suspend fun getCadastroKeyClosestToCurrentPosition(state: PagingState<Int, ProdutoCache>): CadastroKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.idProduto?.let {
                cadastroKeysDao.getCadastroKeys(it)
            }
        }
    }

    private suspend fun getCadastroKeyForFirstItem(state: PagingState<Int, ProdutoCache>): CadastroKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { produto -> cadastroKeysDao.getCadastroKeys(produto.idProduto) }
    }

    private suspend fun getCadastroKeyForLastItem(state: PagingState<Int, ProdutoCache>): CadastroKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { produto -> cadastroKeysDao.getCadastroKeys(produto.idProduto) }
    }

    fun atualiza(pesquisa: String) {
        this.pesquisa = pesquisa
    }
}