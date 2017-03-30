package ba.unsa.etf.rma.elvircrn.movieinfo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.adapters.GenreAdapter;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.RecyclerViewHelpers;

public class GenreListActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_list);
        recyclerView = (RecyclerView)findViewById(R.id.genresList);
        populateGenres();
        setButtonOnClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        DataProvider.getInstance().seed();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    protected void populateGenres() {
        RecyclerViewHelpers.initializeRecyclerView(recyclerView,
                new GenreAdapter(DataProvider.getInstance().getGenres()));
    }

    protected void setButtonOnClickListeners() {
        (findViewById(R.id.actorsButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenreListActivity.this.startActivity(new Intent(GenreListActivity.this,
                        MainActivity.class));
                finish();
            }
        });

        (findViewById(R.id.directorsButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenreListActivity.this.startActivity(new Intent(GenreListActivity.this,
                        DirectorListActivity.class));
                finish();
            }
        });
    }
}
