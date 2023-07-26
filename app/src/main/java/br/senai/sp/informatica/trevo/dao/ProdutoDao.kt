package br.senai.sp.informatica.trevo.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.senai.sp.informatica.trevo.model.ProdutoItem

@Dao
interface ProdutoDao {
    @Query("select * from ProdutoItem as p order by p.nome")
    fun getProdutos(): PagingSource<Int, ProdutoItem>

    @Query("select * from ProdutoItem")
    suspend fun getListaDeProdutos(): List<ProdutoItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduto(produto: ProdutoItem)

    @Query("delete from ProdutoItem")
    suspend fun deleteProdutos()

    @Query("delete from ProdutoItem where idProduto = :id")
    suspend fun deleteProduto(id: Int)

    @Query("select * from ProdutoItem as p where p.idProduto = :id")
    suspend fun getProduto(id: Int): ProdutoItem?

    @Query("select count(*) from ProdutoItem")
    suspend fun countProduto(): Int
}