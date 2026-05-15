package com.example.gramayatri

data class Route(
    val name: String = "",
    val timing: String = "",
    val stops: List<String> = listOf()
)