package com.example.meustock.data.mappers

import com.example.meustock.data.models.ProductDto
import com.example.meustock.domain.model.Product

fun ProductDto.toDomain(): Product = Product(
    id = id,
    idProduct = idProduct,
    imageUrl = imageUrl,
    name = name,
    description = description,
    barcodeSku = barcodeSku,
    costPrice = costPrice,
    sellingPrice = sellingPrice,
    currentStock = currentStock,
    minimumStock = minimumStock,
    category = category,
    brand = brand,
    unitOfMeasurement = unitOfMeasurement,
    supplier = supplier,
    stockLocation = stockLocation,
    status = status,
    notes = notes,
    registrationDate = registrationDate,
    lastUpdateDate = lastUpdateDate
)

fun Product.toDto(): ProductDto = ProductDto(
    id = id,
    idProduct = idProduct,
    imageUrl = imageUrl,
    name = name,
    description = description,
    barcodeSku = barcodeSku,
    costPrice = costPrice,
    sellingPrice = sellingPrice,
    currentStock = currentStock,
    minimumStock = minimumStock,
    category = category,
    brand = brand,
    unitOfMeasurement = unitOfMeasurement,
    supplier = supplier,
    stockLocation = stockLocation,
    status = status,
    notes = notes,
    registrationDate = registrationDate,
    lastUpdateDate = lastUpdateDate
)