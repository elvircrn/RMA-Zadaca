package ba.unsa.etf.rma.elvircrn.movieinfo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.adapters.ActorAdapter;
import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.RecyclerViewHelpers;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataProvider.getInstance().seed();
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView)findViewById(R.id.actorsList);
        populateActors();
        setButtonOnClickListeners();
    }

    protected void populateActors() {
        RecyclerViewHelpers.initializeRecyclerView(recyclerView,
                new ActorAdapter(DataProvider.getInstance().getActors())
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        DataProvider.getInstance().seed();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    protected void setButtonOnClickListeners() {
        (findViewById(R.id.directorsButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this,
                        DirectorListActivity.class));
                finish();
            }
        });

        (findViewById(R.id.genresButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this,
                        GenreListActivity.class));
                finish();
            }
        });
    }
}
