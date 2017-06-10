package ba.unsa.etf.rma.elvircrn.movieinfo.view;


import android.support.annotation.NonNull;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class RxCheckBox {
    public static Observable<Boolean> fromCheckBox(@NonNull final CheckBox checkBox, boolean checked) {
        final BehaviorSubject<Boolean> subject = BehaviorSubject.createDefault(checked);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                subject.onNext(isChecked);
            }
        });

        return subject;
    }

    public static Observable<Boolean> fromCheckBox(@NonNull final CheckBox checkBox) {
        final BehaviorSubject<Boolean> subject = BehaviorSubject.create();
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                subject.onNext(isChecked);
            }
        });
        return subject;
    }

}
