package br.senai.sp.informatica.trevo.model

data class Produto(
    val idProduto: Int,
    val nome: String,
    val descricao: String,
    val area: String,
    val imagem: String,
    val capa: String,
    val maisVendido: Boolean,
    var culturas: List<String>
)
