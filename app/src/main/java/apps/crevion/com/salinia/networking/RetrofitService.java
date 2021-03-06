package apps.crevion.com.salinia.networking;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by yusufaw on 1/24/18.
 */

public interface RetrofitService {

    @GET("notes")
    Observable<JsonObject> listLogs(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("notes")
    Observable<JsonObject> addNote(@Field("content") String content);

    @FormUrlEncoded
    @POST("users/login")
    Observable<JsonObject> userLogin(@Field("auth_code") String authCode);

    class Creator {
        private static RetrofitService INSTANCE;

        public static RetrofitService getInstance() {
            if(INSTANCE == null) {
                INSTANCE = RetrofitFactory.newInstance().create(RetrofitService.class);
            }
            return INSTANCE;
        }
    }
}
