package br.senai.sp.informatica.trevo.constants
const val IP = "172.30.10.10"  // TODO: Ajuste o IP de seu equipamento

const val URL = "http://$IP:8080/trevo/api/"
const val URL_Foto = URL + "produto/foto/"
const val URL_Capa = URL + "produto/capa/"

const val PAGE_Size = 10
const val PAGE_Max_Size = 30
const val PAGE_Prefetch = 5
const val PAGE_Load_Size = 40