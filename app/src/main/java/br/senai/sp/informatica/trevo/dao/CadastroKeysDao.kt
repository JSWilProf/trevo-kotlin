package br.senai.sp.informatica.trevo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.senai.sp.informatica.trevo.model.CadastroKeys

@Dao
interface CadastroKeysDao {
    @Query("select * from CadastroKeys where id= :id")
    suspend fun getCadastroKeys(id: Int): CadastroKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllCadastroKeys(cadastroKeys: List<CadastroKeys>)

    @Query("delete from CadastroKeys")
    suspend fun deleteAllCadastroKeys()
}