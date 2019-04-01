package edu.odu.cs.air411.wherearfthou;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface
{
        @GET("Register.php")
        Call<User>performRegistration(@Query("name") String Name,@Query("user_name") String UserName,@Query("user_password") String UserPassword);

        @GET("Login.php")
        Call<User>performLogin(@Query("user_name") String UserName,@Query("user_password") String UserPassword);
}
