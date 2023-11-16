package fr.eric.tp2.data


import fr.eric.tp2.Model.ApiResponse
import retrofit2.http.GET

interface RestApiDataSource {
    @GET("?inc=name")
    suspend fun getUserName(): ApiResponse

    @GET("?inc=picture")
    suspend fun getUserPicture(): ApiResponse

}