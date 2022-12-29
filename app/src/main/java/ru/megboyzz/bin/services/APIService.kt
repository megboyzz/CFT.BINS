package ru.megboyzz.bin.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ru.megboyzz.bin.entity.BINInfo

interface APIService {
    @GET("/{bin}")
    fun getData(@Path("bin") binNumber: Int): Call<BINInfo>
}