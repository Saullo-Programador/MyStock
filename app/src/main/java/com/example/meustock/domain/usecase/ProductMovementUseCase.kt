package com.example.meustock.domain.usecase

import com.example.meustock.domain.repository.ProductMovementRepository
import javax.inject.Inject



class GetProductMovementsUseCase @Inject constructor(
    private val productRepository: ProductMovementRepository
){
    suspend operator fun invoke(productId: String) = productRepository.getProductMovements(productId)
}

class AddProductStockUseCase @Inject constructor(
    private val repository: ProductMovementRepository
){
    suspend operator fun invoke(
        productId: String,
        quantity: Int,
        responsible: String? = null,
        notes: String? = null
    ) {
        repository.addProductStock(productId, quantity)
        repository.registerProductMovement(
            productId = productId,
            quantity = quantity,
            type = "entrada",
            responsible = responsible,
            notes = notes
        )
    }
}

class RemoveProductStockUseCase @Inject constructor(
    private val repository: ProductMovementRepository
){
    suspend operator fun invoke(
        productId: String,
        quantity: Int,
        responsible: String? = null,
        notes: String? = null
    ) {
        repository.removeProductStock(productId, quantity)
        repository.registerProductMovement(
            productId = productId,
            quantity = quantity,
            type = "saida",
            responsible = responsible,
            notes = notes
        )
    }
}


class GetProductsByCodeOrNameUseCase @Inject constructor(
    private val productRepository: ProductMovementRepository
){
    suspend operator fun invoke(query: String) = productRepository.getProductsByCodeOrName(query)
}


class GetAllProductsUseCase @Inject constructor(
    private val productRepository: ProductMovementRepository
) {
    suspend operator fun invoke() = productRepository.getAllProducts()
}

class ListenProductByIdUseCase @Inject constructor(
    private val productRepository: ProductMovementRepository
) {
    operator fun invoke(productId: String) = productRepository.listenProductById(productId)
}