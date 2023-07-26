package br.senai.sp.informatica.trevo.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.senai.sp.informatica.trevo.model.ProdutoCache

@Dao
interface CadastroDao {
    @Query("select * from ProdutoCache as p order by p.nome")
    fun getProdutos(): PagingSource<Int, ProdutoCache>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProdutos(produtos: List<ProdutoCache>)

    @Query("delete from ProdutoCache")
    suspend fun deleteProdutos(): Unit

    @Query("select * from ProdutoCache as p where p.idProduto = :id")
    suspend fun getProduto(id: Int): ProdutoCache
}