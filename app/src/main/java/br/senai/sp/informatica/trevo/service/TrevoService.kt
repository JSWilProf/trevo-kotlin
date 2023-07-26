package br.senai.sp.informatica.trevo.service

import br.senai.sp.informatica.trevo.constants.URL
import br.senai.sp.informatica.trevo.model.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import tech.thdev.network.flowcalladapterfactory.FlowCallAdapterFactory

interface TrevoService {
    @GET("produtos")
    fun getProdutos(@Query("pesquisa") pesquisa: String, @Query("pageNo") page: Int): Flow<ProdutosResponse>

    @GET("produto/{id}")
    fun getProduto(@Path("id") id: Int): Flow<Produto>

    @POST("proposta")
    fun sendProposta(@Body proposta: Proposta): Flow<PropostaResponse>

    @GET("propostas")
    fun getPropostas(@Query("email") email: String, @Query("pageNo") page: Int): Flow<PropostasResponse>

    companion object {
        fun create(): TrevoService {
            val retrofit = Retrofit
                .Builder()
                .addCallAdapterFactory(FlowCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URL)
                .build()

            return retrofit.create(TrevoService::class.java)
        }
    }
}