package br.senai.sp.informatica.trevo.mediator

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.senai.sp.informatica.trevo.model.Produto
import br.senai.sp.informatica.trevo.service.TrevoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.singleOrNull

class ProdutoPagingSource(
    private val produtos: List<Int>,
    private val service: TrevoService,
): PagingSource<Int, Produto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Produto> {
        return try {

            val lista = produtos.map {
                service.getProduto(it)
                    .flowOn(Dispatchers.IO)
                    .singleOrNull()!!
            }.toList()

            LoadResult.Page(
                itemsBefore = 0,
                data = lista,
                prevKey = null,
                nextKey = null
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Produto>): Int? {
        return null
    }
}
