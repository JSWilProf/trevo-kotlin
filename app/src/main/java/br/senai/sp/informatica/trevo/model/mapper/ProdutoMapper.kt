package br.senai.sp.informatica.trevo.model.mapper

import br.senai.sp.informatica.trevo.model.ProdutoCache
import br.senai.sp.informatica.trevo.model.ProdutoItem
import br.senai.sp.informatica.trevo.model.Produto

class ProdutoMapper {
    companion object {
        fun toProdutoCache(produto: Produto): ProdutoCache {
            return ProdutoCache(
                idProduto = produto.idProduto,
                nome = produto.nome,
                descricao = produto.descricao,
                area = produto.area,
                imagem = produto.imagem,
                capa = produto.capa,
                maisVendido = produto.maisVendido,
                culturas = produto.culturas.joinToString(", ")
            )
        }

        fun toProdutoCarrinho(produto: ProdutoCache): ProdutoItem {
            return ProdutoItem(
                idProduto = produto.idProduto,
                nome = produto.nome,
                imagem = produto.imagem
            )
        }
    }
}