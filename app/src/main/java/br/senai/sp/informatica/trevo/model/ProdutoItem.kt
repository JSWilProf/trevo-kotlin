package br.senai.sp.informatica.trevo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ProdutoItem(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo()
    val idProduto: Int,
    val nome: String,
    val imagem: String,
)