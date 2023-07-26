package br.senai.sp.informatica.trevo.mediator

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.senai.sp.informatica.trevo.constants.PAGE_Size
import br.senai.sp.informatica.trevo.model.PropostaResponse
import br.senai.sp.informatica.trevo.service.TrevoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single
import kotlin.math.max

class PropostaPagingSource(
    private var email: String,
    private val service: TrevoService,
): PagingSource<Int, PropostaResponse>() {
    private var pagePosition = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PropostaResponse> {
        pagePosition = params.key ?: 0

        return try {
            if (params is LoadParams.Refresh) {
                pagePosition = max(0, pagePosition - 1)
            }
            val response = service.getPropostas(email, pagePosition)
                .flowOn(Dispatchers.IO)
                .single()
            val nextKey = if (response.content.isEmpty() || response.content.size < PAGE_Size) {
                null
            } else {
                pagePosition + 1
            }
            LoadResult.Page(
                itemsBefore = pagePosition * PAGE_Size,
                data = response.content,
                prevKey = if (pagePosition <= 0) null else pagePosition - 1,
                nextKey = nextKey
            )
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PropostaResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val closestPage = state.closestPageToPosition(anchorPosition)?.prevKey ?: 0
            val refKey = if(anchorPosition>(closestPage)* PAGE_Size + PAGE_Size) closestPage + 1 else closestPage

            refKey
        }
    }
}
