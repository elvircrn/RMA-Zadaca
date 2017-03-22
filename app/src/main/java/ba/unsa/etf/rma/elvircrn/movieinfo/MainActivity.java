package ba.unsa.etf.rma.elvircrn.movieinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataProvider.getInstance().seed();

        setContentView(R.layout.activity_main);

        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(new GlumacAdapter(this, DataProvider.getInstance().getGlumci()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(MainActivity.this, BiografijaActivity.class)
                        .putExtra("Glumac", DataProvider.getInstance().getGlumci().get(position));
                MainActivity.this.startActivity(myIntent);
            }
        });
    }
}
