package ro.ao.benchmark.networking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import ro.ao.benchmark.model.benchmark.synthetic_testing.FakeApiPhotoResponse;

public interface FakeApi {
    @GET("photos")
    Call<List<FakeApiPhotoResponse>> getFakeResponse();


}
