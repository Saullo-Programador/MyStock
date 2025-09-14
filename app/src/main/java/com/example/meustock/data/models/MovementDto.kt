package com.example.meustock.data.models

data class MovementDto(
    val id: String = "",                    // ID único da movimentação
    val productId: String = "",             // ID do produto relacionado
    val quantity: Int = 0,                  // Quantidade movimentada
    val type: String = "",                  // "entrada" ou "saida"
    val date: Long = 0L,                    // Timestamp
    val responsible: String? = null,        // Opcional: usuário ou responsável
    val notes: String? = null,              // Observações
    val createdBy: String = ""              // ID do usuário que criou o movimento
)