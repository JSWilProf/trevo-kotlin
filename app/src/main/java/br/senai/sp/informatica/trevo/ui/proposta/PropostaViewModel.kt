package br.senai.sp.informatica.trevo.ui.proposta

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.senai.sp.informatica.trevo.constants.PAGE_Load_Size
import br.senai.sp.informatica.trevo.constants.PAGE_Max_Size
import br.senai.sp.informatica.trevo.constants.PAGE_Prefetch
import br.senai.sp.informatica.trevo.constants.PAGE_Size
import br.senai.sp.informatica.trevo.dao.ClienteDao
import br.senai.sp.informatica.trevo.mediator.PropostaPagingSource
import br.senai.sp.informatica.trevo.model.PropostaResponse
import br.senai.sp.informatica.trevo.service.TrevoService
import kotlinx.coroutines.flow.Flow

class PropostaViewModel(context: Context)  : ViewModel() {
    private val service = TrevoService.create()
    private var clienteDao = ClienteDao.instance(context)
    private val pagingConfig = PagingConfig(
        pageSize = PAGE_Size,
        enablePlaceholders = false,
        maxSize = PAGE_Max_Size,
        prefetchDistance = PAGE_Prefetch,
        initialLoadSize = PAGE_Load_Size)
    private val email: String
        get() {
            return clienteDao.getCliente()?.email ?: ""
        }
    val flow: Flow<PagingData<PropostaResponse>>
        get () {
            return Pager(
                config = pagingConfig,
                pagingSourceFactory = { PropostaPagingSource(email, service) }
            ).flow.cachedIn(viewModelScope)
        }

    class Factory(private val context: Context): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PropostaViewModel::class.java)) {
                return PropostaViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}