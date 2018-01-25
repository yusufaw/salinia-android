package apps.crevion.com.salinia;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yusufaw on 1/24/18.
 */

public class RetrofitFactory {
    public static OkHttpClient client;

    public static Retrofit newInstance() {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        client = clientBuilder.build();

        return new Retrofit.Builder()
                .client(client)
                .baseUrl("https://salinia-api.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
