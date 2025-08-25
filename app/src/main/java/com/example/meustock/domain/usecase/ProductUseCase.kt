package com.example.meustock.domain.usecase

import com.example.meustock.domain.model.Product
import com.example.meustock.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
){
    suspend operator fun invoke(product: Product){
        productRepository.addProduct(product)
    }
}

class GetNextProductCodeUseCase @Inject constructor(
    private val productRepository: ProductRepository
){
    suspend operator fun invoke(): String{
        return productRepository.getNextProductCode()
    }
}

class DetailProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
){
    suspend operator fun invoke(productId: String): Flow<Product?>{
        return productRepository.detailProduct(productId)
    }
}

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
){
    suspend operator fun invoke(): Flow<List<Product>>{
        return productRepository.getProducts()
    }
}

class DeleteProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
){
    suspend operator fun invoke(product: Product){
        productRepository.deleteProduct(product)
    }
}

class UpdateProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
){
    suspend operator fun invoke(product: Product){
        productRepository.updateProduct(product)
    }
}

class GetProductByIdUseCase @Inject constructor(
    private val productRepository: ProductRepository
){
    suspend operator fun invoke(id: String): Product?{
        return productRepository.getProductById(id)
    }
}
