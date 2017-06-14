package ba.unsa.etf.rma.elvircrn.movieinfo.view;

import android.support.annotation.NonNull;
import android.widget.SearchView;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

public class RxSearch {
    public static Observable<String> fromSearchView(@NonNull final SearchView searchView, String initialSearch) {
        final PublishSubject<String> subject = PublishSubject.<String>create();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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

        if (initialSearch != null && !Objects.equals(initialSearch, ""))
            subject.onNext(initialSearch);

        return subject;
    }

}
