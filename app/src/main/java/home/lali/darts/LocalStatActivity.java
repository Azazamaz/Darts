package home.lali.darts;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import home.lali.darts.adapter.LocalStatAdapter;
import home.lali.darts.adapter.RecyclerItemDecorator;
import home.lali.darts.database.DatabaseAccess;
import home.lali.darts.model.OnlineMatches;

public class LocalStatActivity extends AppCompatActivity {

    private RecyclerView localMainList;

    private List<OnlineMatches> localMatchList;
    private LocalStatAdapter localAdapter;

    private DatabaseAccess database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_stat);

        database = DatabaseAccess.getInstance(LocalStatActivity.this);

        localMatchList = new ArrayList<>();
        localAdapter = new LocalStatAdapter(localMatchList);

        setLayout();

        localMainList = findViewById(R.id.localMainList);
        localMainList.setHasFixedSize(true);
        localMainList.addItemDecoration(new RecyclerItemDecorator(this));
        localMainList.setLayoutManager(new LinearLayoutManager(this));
        localMainList.setAdapter(localAdapter);
    }

    private void setLayout() {
        try {
            database.open();

            Cursor res = database.getLocalResults();

            if (res.getCount() == 0) {
                emptyStat().show();
                return;
            }

            while (res.moveToNext()) {
                String player1 = res.getString(1);
                int p1Leg = res.getInt(2);
                double p1Avg = res.getDouble(3);
                String player2 = res.getString(4);
                int p2Leg = res.getInt(5);
                double p2Avg = res.getDouble(6);
                localMatchList.add(new OnlineMatches(player1, p1Leg, p1Avg, player2, p2Leg, p2Avg, false));
            }

        } catch (Exception e) {
            Log.e("READ LOCAL", e.getMessage());
        } finally {
            database.close();
        }
    }

    private AlertDialog emptyStat() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LocalStatActivity.this);
        builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onBackPressed();
            }
        });

        builder.setTitle(R.string.empty_stats);
        builder.setMessage(R.string.no_local_match);

        return builder.create();
    }
}
