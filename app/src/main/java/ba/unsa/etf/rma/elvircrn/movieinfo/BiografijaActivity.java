package ba.unsa.etf.rma.elvircrn.movieinfo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import ba.unsa.etf.rma.elvircrn.movieinfo.databinding.ActivityBiografijaBinding;

public class BiografijaActivity extends AppCompatActivity {
    Glumac glumac;

    private ActivityBiografijaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biografija);

        glumac = (Glumac)getIntent().getSerializableExtra("Glumac");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_biografija);
        binding.setGlumac(glumac);

        View root = findViewById(android.R.id.content);
        root.setBackgroundColor(ContextCompat.getColor(root.getContext(),
                glumac.getSpol() == Glumac.Spol.MUSKI ? R.color.colorMale : R.color.colorFemale));

        ImageButton imdbButton = (ImageButton)findViewById(R.id.imdbButton);
        imdbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(glumac.getImdbLink()));
                startActivity(i);
            }
        });

        ImageButton shareBio = (ImageButton)findViewById(R.id.shareButton);
        shareBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setData(Uri.parse(glumac.getBiografija()));
                startActivity(i);
            }
        });
    }
}
