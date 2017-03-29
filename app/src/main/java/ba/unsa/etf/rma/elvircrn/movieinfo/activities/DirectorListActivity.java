package ba.unsa.etf.rma.elvircrn.movieinfo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.adapters.DirectorAdapter;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.RecyclerViewHelpers;

public class DirectorListActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director_list);
        recyclerView = (RecyclerView)findViewById(R.id.directorsList);
        populateDirectors();
        setButtonOnClickListeners();
    }

    protected void populateDirectors() {
        RecyclerViewHelpers.initializeRecyclerView(recyclerView,
                new DirectorAdapter(DataProvider.getInstance().getDirectors()),
                null);
    }

    @Override
    protected void onResume() {
        super.onResume();

        DataProvider.getInstance().seed();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    protected void setButtonOnClickListeners() {
        (findViewById(R.id.actorsButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DirectorListActivity.this.startActivity(new Intent(DirectorListActivity.this, MainActivity.class));
                finish();
            }
        });

        (findViewById(R.id.genresButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DirectorListActivity.this.startActivity(new Intent(DirectorListActivity.this, GenreListActivity.class));
                finish();
            }
        });
    }
}
