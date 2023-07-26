package br.senai.sp.informatica.trevo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProdutoCache(
    @PrimaryKey(autoGenerate = false)
    val idProduto: Int,
    val nome: String,
    val descricao: String,
    val area: String,
    val imagem: String,
    val capa: String,
    val maisVendido: Boolean,
    val culturas: String
)