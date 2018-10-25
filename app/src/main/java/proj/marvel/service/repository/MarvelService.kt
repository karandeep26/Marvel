package proj.marvel.service.repository

import android.os.SystemClock
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Single
import okhttp3.OkHttpClient
import proj.marvel.service.model.MarvelResponse
import retrofit2.Response
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.math.BigInteger
import java.security.MessageDigest


interface MarvelService {

    @GET("v1/public/characters")
    fun getData(@Query("offset") limit: Int): Single<Response<MarvelResponse>>


    companion object Factory {

        val getInstance: MarvelService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            buildRetrofit().create(MarvelService::class.java)

        }

        private const val BASE_URL: String = "https://gateway.marvel.com/"

        private fun buildRetrofit() = retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        private fun getOkHttpClient(): OkHttpClient {
            val timeStamp = SystemClock.currentThreadTimeMillis().toString()
            val md = MessageDigest.getInstance("MD5")
            val httpClient = OkHttpClient.Builder()

            val publicApiKey = "16d02aed2dba8ef778e86edc0ca004f0"
            val privateApiKey = "c72847183a8e61af0fb90127a08ccfb50fe2be9a"

            val stringToHash = "$timeStamp$privateApiKey$publicApiKey"

            val hash = BigInteger(1, md.digest(stringToHash.toByteArray())).toString(16).padStart(32, '0')

            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url()

                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("ts", timeStamp).addQueryParameter("hash", hash)
                    .addQueryParameter("apikey", publicApiKey)
                    .build()

                val requestBuilder = original.newBuilder()
                    .url(url)

                val request = requestBuilder.build()
                chain.proceed(request)
            }

            return httpClient.build()
        }
    }
}