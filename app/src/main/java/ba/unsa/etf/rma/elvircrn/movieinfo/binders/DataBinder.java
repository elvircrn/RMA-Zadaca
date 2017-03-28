package ba.unsa.etf.rma.elvircrn.movieinfo.binders;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;

public class DataBinder {
    private DataBinder() {
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
