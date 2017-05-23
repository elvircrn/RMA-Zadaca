package ba.unsa.etf.rma.elvircrn.movieinfo.factories.services;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import ba.unsa.etf.rma.elvircrn.movieinfo.services.GenreService;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.PeopleService;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.SearchService;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces.IGenreService;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces.IPeopleService;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces.ISearchService;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "98616f6ba5eb339bef15a7d426f2897a";
    private final OkHttpClient SEARCH_CLIENT, BASIC_CLIENT;
    private final Retrofit SEARCH_RETROFIT, BASIC_RETROFIT;

    private ServiceFactory() {
        SEARCH_CLIENT = new OkHttpClient
                .Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        HttpUrl url = request.url().newBuilder()
                                .addQueryParameter("api_key", API_KEY)
                                .addQueryParameter("include_adult", "false")
                                .addQueryParameter("language", "en-US")
                                .build();
                        request = request.newBuilder().url(url).build();
                        Log.d("Http request url: ", request.url().toString());
                        return chain.proceed(request);
                    }
                })
                .build();

        BASIC_CLIENT = new OkHttpClient
                .Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        HttpUrl url = request.url().newBuilder()
                                .addQueryParameter("api_key", API_KEY)
                                .build();
                        request = request.newBuilder().url(url).build();
                        Log.d("Http request url: ", request.url().toString());
                        return chain.proceed(request);
                    }
                })
                .build();


        SEARCH_RETROFIT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(SEARCH_CLIENT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        BASIC_RETROFIT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(BASIC_CLIENT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private static class LazyHolder {
        static final ServiceFactory INSTANCE = new ServiceFactory();
    }

    public static ServiceFactory getInstance() {
        return LazyHolder.INSTANCE;
    }

    public ISearchService createSerachService() {
        return new SearchService(SEARCH_RETROFIT.create(ISearchService.class));
    }

    public IPeopleService createPeopleService() {
        return new PeopleService(BASIC_RETROFIT.create(IPeopleService.class));
    }

    public IGenreService createGenreService() {
        return new GenreService(BASIC_RETROFIT.create(IGenreService.class));
    }
}
