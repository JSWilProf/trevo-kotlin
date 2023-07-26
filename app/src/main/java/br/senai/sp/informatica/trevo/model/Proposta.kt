package br.senai.sp.informatica.trevo.model

import java.time.LocalDate

data class Proposta(
    val idProposta: Int? = null,
    val data: LocalDate? = null,
    val cliente: Cliente,
    val produtos: List<Int>
)