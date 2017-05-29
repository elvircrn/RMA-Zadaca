package ba.unsa.etf.rma.elvircrn.movieinfo.view;

import android.support.annotation.NonNull;
import android.widget.SearchView;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class RxSearch {
    public static Observable<String> fromSearchView(@NonNull final SearchView searchView, String initialSearch) {
        final BehaviorSubject<String> subject = (initialSearch != null && !initialSearch.isEmpty())
                ? BehaviorSubject.createDefault(initialSearch)
                : BehaviorSubject.<String>create();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                subject.onComplete();
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
