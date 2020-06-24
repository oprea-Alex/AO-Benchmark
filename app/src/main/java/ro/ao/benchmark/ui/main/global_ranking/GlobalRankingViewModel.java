package ro.ao.benchmark.ui.main.global_ranking;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ro.ao.benchmark.model.benchmark.RemoteBenchmarkResult;
import ro.ao.benchmark.ui.BaseViewModel;
import ro.ao.benchmark.util.Constants;
import ro.ao.benchmark.util.SanitizeUtil;

public class GlobalRankingViewModel extends BaseViewModel {

    /* Benchmark scores live data */
    private MutableLiveData<List<RemoteBenchmarkResult>> topScoresLiveData;

    public GlobalRankingViewModel(Application app) {
        super(app);
        topScoresLiveData = new MutableLiveData<>();
        accountLiveData.postValue(FirebaseAuth.getInstance().getCurrentUser());
    }

    public MutableLiveData<List<RemoteBenchmarkResult>> getTopScoresLiveData() {
        return topScoresLiveData;
    }

    public void retrieveRemoteScores() {
        FirebaseDatabase
                .getInstance()
                .getReference(Constants.APP_TESTS_RESULTS_TABLE)
                .orderByChild("score")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(Constants.TAG_GLOBAL_RANKING, dataSnapshot.toString());

                        if (dataSnapshot.getValue() == null) {
                            exceptionLiveData.postValue(new Exception("No scores available right now!"));
                            return;
                        }

                        try {
                            List<RemoteBenchmarkResult> benchmarkResults = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                benchmarkResults.add(snapshot.getValue(RemoteBenchmarkResult.class));

                            /* Update UI */
                            topScoresLiveData.postValue(benchmarkResults);
                        } catch (Exception e) {
                            Log.e(Constants.TAG_GLOBAL_RANKING, "Failed to retrieve scores", e);
                            exceptionLiveData.postValue(e);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(Constants.TAG_GLOBAL_RANKING, "Failed to retrieve scores", databaseError.toException());
                        exceptionLiveData.postValue(databaseError.toException());
                    }
                });
    }

}