package br.senai.sp.informatica.trevo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CadastroKeys(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val prevPage: Int?,
    val nextPage: Int?
)
