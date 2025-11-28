package com.example.huerto.data.api

import com.example.huerto.data.local.db.entities.ProductEntity
import com.example.huerto.data.local.db.entities.UserEntity
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ========== PRODUCTOS ==========
    @GET("api/productos")
    suspend fun getProductos(): Response<List<ProductEntity>>

    @GET("api/productos/{id}")
    suspend fun getProductoById(@Path("id") id: Int): Response<ProductEntity>

    @POST("api/productos")
    suspend fun createProducto(@Body producto: ProductEntity): Response<ProductEntity>

    @PUT("api/productos/{id}")
    suspend fun updateProducto(
        @Path("id") id: Int,
        @Body producto: ProductEntity
    ): Response<ProductEntity>

    @DELETE("api/productos/{id}")
    suspend fun deleteProducto(@Path("id") id: Int): Response<Void>

    @GET("api/productos/buscar")
    suspend fun searchProductoByName(@Query("nombre") nombre: String): Response<List<ProductEntity>>

    // ========== USUARIOS ==========
    @GET("api/usuarios")
    suspend fun getUsuarios(): Response<List<UserEntity>>

    @GET("api/usuarios/{id}")
    suspend fun getUsuarioById(@Path("id") id: Long): Response<UserEntity>

    @GET("api/usuarios/email/{email}")
    suspend fun getUsuarioByEmail(@Path("email") email: String): Response<UserEntity>

    @POST("api/usuarios")
    suspend fun createUsuario(@Body usuario: UserEntity): Response<UserEntity>

    @PUT("api/usuarios/{id}")
    suspend fun updateUsuario(
        @Path("id") id: Long,
        @Body usuario: UserEntity
    ): Response<UserEntity>

    @DELETE("api/usuarios/{id}")
    suspend fun deleteUsuario(@Path("id") id: Long): Response<Void>

    @GET("api/usuarios/existe/{email}")
    suspend fun existeEmail(@Path("email") email: String): Response<Boolean>
}