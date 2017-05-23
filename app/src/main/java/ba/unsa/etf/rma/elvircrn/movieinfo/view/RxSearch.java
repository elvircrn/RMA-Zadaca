package ba.unsa.etf.rma.elvircrn.movieinfo.view;

import android.support.annotation.NonNull;
import android.widget.SearchView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class RxSearch {
    public static Observable<String> fromSearchView(@NonNull final SearchView searchView) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull final ObservableEmitter<String> e) throws Exception {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        e.onComplete();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (!newText.isEmpty()) {
                            e.onNext(newText);
                        }
                        return true;
                    }
                });
            }
        });
    }

}
