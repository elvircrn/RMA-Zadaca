package ba.unsa.etf.rma.elvircrn.movieinfo.controllers;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ba.unsa.etf.rma.elvircrn.movieinfo.services.TheMovieDBService;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.ActorSearchResponseDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces.ITheMovieDBService;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TheMovieDBController {
    static ITheMovieDBService service;
    private static final String apiKey = "98616f6ba5eb339bef15a7d426f2897a";
    private static final String baseUrl = "https://api.themoviedb.org/";

    private TheMovieDBController() {
    }

    static ITheMovieDBService createService() {
        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();

                        HttpUrl url = request.url().newBuilder()
                                .addQueryParameter("api_key", apiKey)
                                .addQueryParameter("include_adult", "false")
                                .addQueryParameter("language", "en-US")
                                .build();

                        request = request.newBuilder().url(url).build();

                        Log.d("Http request url: ", request.url().toString());


                        return chain.proceed(request);
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return new TheMovieDBService(retrofit.create(ITheMovieDBService.class));
    }

    private static ITheMovieDBService getService() {
        if (service == null)
            service = createService();
        return service;
    }

    public static rx.Observable<ActorSearchResponseDTO> searchActorByName(String name) {
        return getService().searchActorsByName(name);
    }
}
