package com.example.meustock.authentication

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthManager @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    // Login com email e senha
    suspend fun signIn(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Log.e("FirebaseAuthManager", "Erro ao fazer login", e)
            Result.failure(e)
        }
    }

    // Criação de usuário com email e senha
    suspend fun signUp(empresaName: String, username: String, email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("UID não encontrado"))

            val user = hashMapOf(
                "uid" to uid,
                "empresaName" to empresaName,
                "username" to username,
                "email" to email,
                "createdAt" to FieldValue.serverTimestamp()
            )

            firestore.collection("users").document(uid).set(user).await()

            Result.success(result.user!!)

        } catch (e: Exception) {
            Log.e("FirebaseAuthManager", "Erro ao criar usuário", e)
            Result.failure(e)
        }

    }

    fun logout() {
        auth.signOut()
    }

    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            Firebase.auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }




}