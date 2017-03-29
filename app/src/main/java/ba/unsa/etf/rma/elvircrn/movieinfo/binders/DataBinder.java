package ba.unsa.etf.rma.elvircrn.movieinfo.binders;

import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;

public class DataBinder {
    private DataBinder() {
    }

    @BindingAdapter("genderColor")
    public static void setGenderColor(RelativeLayout relativeLayout, Actor.Gender gender) {
        relativeLayout.setBackgroundColor(ContextCompat.getColor(relativeLayout.getContext(),
                gender == Actor.Gender.MALE ? R.color.colorMale : R.color.colorFemale));
    }

    // Custom Binder za ubacivanje slike sa Url-a u ImageView
    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        // Glide.with(context).load(url).into(imageView);
        imageView.setImageResource(imageView.getContext()
                .getResources()
                .getIdentifier("drawable/" + url, null, imageView.getContext().getPackageName()));
    }
}
