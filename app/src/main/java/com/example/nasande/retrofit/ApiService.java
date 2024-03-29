package com.example.nasande.retrofit;

import com.example.nasande.model.LoginData;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
	
	@POST("user/login?_format=json")
    @Headers({"Content-type: application/json"})
    Call<ResponseBody> loginRequest(@Body LoginData body);


    @POST ("file/upload/node/article/field_image?_format=json")
    Call<ResponseBody> postFile (@Header("Authorization") String authorization,@Header("X-CSRF-Token") String x_csrf_token, @Body RequestBody field_image);
	
	//@POST("file/upload/node/article/field_image?_format=hal_json")
	//Call<ResponseBody> addNode(@Header("Authorization") String user_auth, @Header("X-CSRF-Token") String x_csrf_token, @Body Node node);

	
}
