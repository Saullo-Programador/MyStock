package com.example.meustock.domain.usecase

import app.cash.turbine.test
import com.example.meustock.domain.model.Product
import com.example.meustock.domain.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ProductUseCaseTest {

    private lateinit var productRepository: ProductRepository
    private lateinit var saveProductUseCase: SaveProductUseCase
    private lateinit var getNextProductCodeUseCase: GetNextProductCodeUseCase
    private lateinit var detailProductUseCase: DetailProductUseCase
    private lateinit var getProductsUseCase: GetProductsUseCase
    private lateinit var deleteProductUseCase: DeleteProductUseCase
    private lateinit var updateProductUseCase: UpdateProductUseCase
    private lateinit var getProductByIdUseCase: GetProductByIdUseCase

    @Before
    fun setup() {
        productRepository = mockk()
        saveProductUseCase = SaveProductUseCase(productRepository)
        getNextProductCodeUseCase = GetNextProductCodeUseCase(productRepository)
        detailProductUseCase = DetailProductUseCase(productRepository)
        getProductsUseCase = GetProductsUseCase(productRepository)
        deleteProductUseCase = DeleteProductUseCase(productRepository)
        updateProductUseCase = UpdateProductUseCase(productRepository)
        getProductByIdUseCase = GetProductByIdUseCase(productRepository)
    }

    private fun createFakeProduct() = Product(
        createdBy = "user123",
        idProduct = "PROD001",
        name = "Test Product",
        costPrice = 10.0,
        sellingPrice = 20.0,
        currentStock = 100,
        minimumStock = 10,
        category = "Electronics",
        unitOfMeasurement = "Unit"
    )

    @Test
    fun `SaveProductUseCase should call addProduct in repository`() = runTest {
        val product = createFakeProduct()
        coEvery { productRepository.addProduct(product) } returns Unit

        saveProductUseCase(product)

        coVerify { productRepository.addProduct(product) }
    }

    @Test
    fun `GetNextProductCodeUseCase should return code from repository`() = runTest {
        val expectedCode = "PROD002"
        coEvery { productRepository.getNextProductCode() } returns expectedCode

        val result = getNextProductCodeUseCase()

        assertEquals(expectedCode, result)
        coVerify { productRepository.getNextProductCode() }
    }

    @Test
    fun `DetailProductUseCase should return product flow from repository`() = runTest {
        val productId = "PROD001"
        val product = createFakeProduct()
        coEvery { productRepository.detailProduct(productId) } returns flowOf(product)

        val result = detailProductUseCase(productId)

        // Flow cannot be compared directly with assertEquals(flowOf(product), result)
        // We must collect the flow and verify the item.
        result.test {
            assertEquals(product, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { productRepository.detailProduct(productId) }
    }

    @Test
    fun `GetProductsUseCase should return products list flow from repository`() = runTest {
        val products = listOf(createFakeProduct())
        coEvery { productRepository.getProducts() } returns flowOf(products)

        val result = getProductsUseCase()

        // Flow cannot be compared directly with assertEquals(flowOf(products), result)
        result.test {
            assertEquals(products, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { productRepository.getProducts() }
    }

    @Test
    fun `DeleteProductUseCase should call deleteProduct in repository`() = runTest {
        val product = createFakeProduct()
        coEvery { productRepository.deleteProduct(product) } returns Unit

        deleteProductUseCase(product)

        coVerify { productRepository.deleteProduct(product) }
    }

    @Test
    fun `UpdateProductUseCase should call updateProduct in repository`() = runTest {
        val product = createFakeProduct()
        coEvery { productRepository.updateProduct(product) } returns Unit

        updateProductUseCase(product)

        coVerify { productRepository.updateProduct(product) }
    }

    @Test
    fun `GetProductByIdUseCase should return product from repository`() = runTest {
        val productId = "PROD001"
        val product = createFakeProduct()
        coEvery { productRepository.getProductById(productId) } returns product

        val result = getProductByIdUseCase(productId)

        assertEquals(product, result)
        coVerify { productRepository.getProductById(productId) }
    }
}
