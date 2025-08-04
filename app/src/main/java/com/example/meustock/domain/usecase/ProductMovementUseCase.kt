package com.example.meustock.domain.usecase

import com.example.meustock.domain.repository.ProductMovementRepository
import javax.inject.Inject



class GetProductMovementsUseCase @Inject constructor(
    private val productRepository: ProductMovementRepository
){
    suspend operator fun invoke(productId: String) = productRepository.getProductMovements(productId)
}

class AddProductStockUseCase @Inject constructor(
    private val productRepository: ProductMovementRepository
){
    suspend operator fun invoke(productId: String, quantity: Int){
        productRepository.addProductStock(productId, quantity)
    }
}

class RemoveProductStockUseCase @Inject constructor(
    private val productRepository: ProductMovementRepository
){
    suspend operator fun invoke(productId: String, quantity: Int){
        productRepository.removeProductStock(productId, quantity)
    }
}


class GetProductsByCodeOrNameUseCase @Inject constructor(
    private val productRepository: ProductMovementRepository
){
    suspend operator fun invoke(query: String) = productRepository.getProductsByCodeOrName(query)
}