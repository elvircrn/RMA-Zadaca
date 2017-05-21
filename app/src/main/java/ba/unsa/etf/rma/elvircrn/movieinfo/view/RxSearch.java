package ba.unsa.etf.rma.elvircrn.movieinfo.view;

import android.support.annotation.NonNull;
import android.widget.SearchView;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class RxSearch {
    public static Observable<String> fromSearchView(@NonNull final SearchView searchView) {

        // Emituje posljednji observani item svim subscriberima.
        final BehaviorSubject<String> subject = BehaviorSubject.create("");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                subject.onCompleted();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    subject.onNext(newText);
                }
                return true;
            }
        });

        return subject;
    }

}
