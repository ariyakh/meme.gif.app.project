package com.gigfa.ariya.DataLayer

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        var BASE_URL = "https://api.giphy.com/v1/gifs/"
        private var retrofit: Retrofit? = null

        fun getClient(): Retrofit {

            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }
    }
}
