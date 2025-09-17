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

    suspend fun updateUser(uid: String, empresaName: String, username: String, email: String,): Result<Unit> {
        return try {
            val user = hashMapOf(
                "empresaName" to empresaName,
                "username" to username,
                "email" to email,
                "updatedAt" to FieldValue.serverTimestamp()
            )
            firestore.collection("users").document(uid).update(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirebaseAuthManager", "Erro ao atualizar usuário", e)
            Result.failure(e)
        }
    }
    // Atualiza a senha do usuário
    suspend fun updatePassword(currentPassword: String, newPassword: String): Result<Unit> {
        val user = auth.currentUser ?: return Result.failure(Exception("Usuário não autenticado"))

        return try {
            // Reautenticação
            val credential = com.google.firebase.auth.EmailAuthProvider
                .getCredential(user.email!!, currentPassword)

            user.reauthenticate(credential).await()

            // Atualiza a senha no Firebase Auth
            user.updatePassword(newPassword).await()

            // (Opcional) salva apenas metadata no Firestore
            firestore.collection("users").document(user.uid)
                .update("updatedAt", FieldValue.serverTimestamp())
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Só verifica se a senha atual é válida
    suspend fun verifyPassword(currentPassword: String): Result<Unit> {
        val user = auth.currentUser ?: return Result.failure(Exception("Usuário não autenticado"))

        return try {
            val credential = com.google.firebase.auth.EmailAuthProvider
                .getCredential(user.email!!, currentPassword)

            user.reauthenticate(credential).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    // Obtém o usuário atual
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    suspend fun getUserData(uid: String): Map<String, Any>? {
        return try {
            val doc = firestore.collection("users").document(uid).get().await()
            doc.data
        } catch (e: Exception) {
            null
        }
    }


    // Faz logout do usuário
    fun logout() {
        auth.signOut()
    }

    // Envia um email de redefinição de senha
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            Firebase.auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }




}