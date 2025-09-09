package com.example.meustock.di

import com.example.meustock.authentication.FirebaseAuthManager
import com.example.meustock.data.repository.ProductMovementRepositoryImpl
import com.example.meustock.data.repository.ProductRepositoryImpl
import com.example.meustock.domain.repository.ProductMovementRepository
import com.example.meustock.domain.repository.ProductRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFireStore(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        iml: ProductRepositoryImpl
    ): ProductRepository = iml


    @Provides
    @Singleton
    fun provideMovementRepository(
        iml: ProductMovementRepositoryImpl
    ): ProductMovementRepository = iml


}