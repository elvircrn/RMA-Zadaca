package ba.unsa.etf.rma.elvircrn.movieinfo;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class BiografijaActivity extends AppCompatActivity {
    Glumac glumac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biografija);

        glumac = (Glumac)getIntent().getSerializableExtra("Glumac");

        ((TextView)findViewById(R.id.imePrezimeTextView)).setText(glumac.getFullName());
        ((TextView)findViewById(R.id.godineTextView)).setText(glumac.getGodinaFormatted());
        ((TextView)findViewById(R.id.spolTextView)).setText(glumac.getSpol().toString());
        ((TextView)findViewById(R.id.mjestoTextView)).setText(glumac.getMjestoRodjenja());
        ((TextView)findViewById(R.id.bioTextView)).setText(glumac.getBiografija());

        View root = findViewById(android.R.id.content);
        root.setBackgroundColor(ContextCompat.getColor(root.getContext(),
                glumac.getSpol() == Glumac.Spol.MUSKI ? R.color.colorMale : R.color.colorFemale));


    }
}
