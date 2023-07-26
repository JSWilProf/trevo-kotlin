package br.senai.sp.informatica.trevo.model

import br.senai.sp.informatica.trevo.model.mapper.LocalDateTypeAdapter
import com.google.gson.annotations.JsonAdapter
import java.time.LocalDate

data class PropostaResponse(
    val idProposta: Int,
    @JsonAdapter(LocalDateTypeAdapter::class)
    val data: LocalDate?,
    val cliente: Cliente,
    val produtos: List<Int>
)
