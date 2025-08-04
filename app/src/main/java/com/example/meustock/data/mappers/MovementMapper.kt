package com.example.meustock.data.mappers

import com.example.meustock.data.models.MovementDto
import com.example.meustock.domain.model.ProductMovement

fun MovementDto.toDomain(): ProductMovement {
    return ProductMovement(
        id = id,
        productId = productId,
        quantity = quantity,
        type = type,
        date = date,
        responsible = responsible,
        notes = notes
    )
}
