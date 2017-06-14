package ba.unsa.etf.rma.elvircrn.movieinfo.binders;

import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
        if (url != null && !url.isEmpty()) {
            Glide.with(imageView.getContext())
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(imageView);
        } else {
            imageView.setImageResource(imageView.getContext()
                    .getResources()
                    .getIdentifier("drawable/actordefault",
                            null,
                            imageView.getContext().getPackageName()));
        }
    }

    @BindingAdapter("localImageUrl")
    public static void setLocalImageUrl(ImageView imageView, String url) {
        imageView.setImageResource(imageView.getContext()
                .getResources()
                .getIdentifier("drawable/".concat(url),
                        null,
                        imageView.getContext().getPackageName()));
    }
}
