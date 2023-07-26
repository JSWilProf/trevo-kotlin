package br.senai.sp.informatica.trevo.model

data class ProdutosResponse(
    val content : List<Produto>,
    val pageable: Pageable,
    val totalPages: Int,
    val totalElements: Int,
    val last: Boolean,
    val size: Int,
    val number: Int,
    val sort: Sort,
    val numberOfElements: Int,
    val first: Boolean,
    val empty: Boolean
) {
    data class Pageable (
        val sort: Sort,
        val offset: Int,
        val pageNumber: Int,
        val pageSize: Int,
        val paged: Boolean,
        val unpaged: Boolean
    )

    data class Sort (
        val empty: Boolean,
        val sorted: Boolean,
        val unsorted: Boolean
    )
}
