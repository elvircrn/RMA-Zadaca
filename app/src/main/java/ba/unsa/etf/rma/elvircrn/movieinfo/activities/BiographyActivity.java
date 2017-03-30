package ba.unsa.etf.rma.elvircrn.movieinfo.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;
import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.databinding.ActivityBiografijaBinding;

public class BiographyActivity extends AppCompatActivity {
    Actor actor;
    private ActivityBiografijaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biografija);
        actor = (Actor)getIntent().getSerializableExtra("Actor");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_biografija);
        binding.setActor(actor);
        addOnButtonClickListeners();

        ((TextView)findViewById(R.id.bioTextView)).setMovementMethod(new ScrollingMovementMethod());
    }

    void addOnButtonClickListeners() {
        ImageButton imdbButton = (ImageButton)findViewById(R.id.imdbButton);
        imdbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(actor.getImdbLink()));
                startActivity(i);
            }
        });

        ImageButton shareBio = (ImageButton)findViewById(R.id.shareButton);
        shareBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT, actor.getBiografija());
                i.setType("text/plain");

                // Provjera da li uopste postoji app koji podrzava ovu vrstu intenta. resolveActivity()
                // vraca null za slucaj da ne postoji.
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivity(i);
                }
            }
        });
    }
}
