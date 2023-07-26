package br.senai.sp.informatica.trevo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.senai.sp.informatica.trevo.dao.CadastroDao
import br.senai.sp.informatica.trevo.dao.CadastroKeysDao
import br.senai.sp.informatica.trevo.dao.ProdutoDao
import br.senai.sp.informatica.trevo.model.ProdutoCache
import br.senai.sp.informatica.trevo.model.CadastroKeys
import br.senai.sp.informatica.trevo.model.ProdutoItem

@Database(entities = [ProdutoCache::class, CadastroKeys::class, ProdutoItem::class], version = 1, exportSchema = false)
abstract class CadastroDatabase: RoomDatabase() {
    abstract fun cadastroDao(): CadastroDao
    abstract fun cadastroKeysDao(): CadastroKeysDao
    abstract fun produtoDao(): ProdutoDao

    companion object {
        private var database: CadastroDatabase? = null

        fun instance(context: Context): CadastroDatabase {
            if (database == null) {
                database = Room.databaseBuilder(
                    context,
                    CadastroDatabase::class.java,
                    "cadastrodb")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return database!!
        }
    }
}