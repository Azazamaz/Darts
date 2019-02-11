package home.lali.darts;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import home.lali.darts.database.DatabaseAccess;

public class LocalStatActivity extends AppCompatActivity {

    LinearLayout singleStat;
    LayoutInflater layoutInflater;

    private DatabaseAccess database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_stat);

        database = DatabaseAccess.getInstance(LocalStatActivity.this);

        singleStat = findViewById(R.id.single_stat_layout);
        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        setLayout();
    }

    private void setLayout() {
        ArrayList<String> p1Name;
        ArrayList<String> p1Leg;
        ArrayList<String> p1Avg;
        ArrayList<String> p2Name;
        ArrayList<String> p2Leg;
        ArrayList<String> p2Avg;
        TextView p1N, p1L, p1A, p2N, p2L, p2A;

        try {
            database.open();

            Cursor res = database.getLocalResults();

            if (res.getCount() == 0) {
                emptyStat().show();
                return;
            }

            p1Name = new ArrayList<>();
            p1Leg = new ArrayList<>();
            p1Avg = new ArrayList<>();
            p2Name = new ArrayList<>();
            p2Leg = new ArrayList<>();
            p2Avg = new ArrayList<>();

            while (res.moveToNext()) {
                p1Name.add(res.getString(1));
                p1Leg.add(res.getString(2));
                p1Avg.add(res.getString(3));
                p2Name.add(res.getString(4));
                p2Leg.add(res.getString(5));
                p2Avg.add(res.getString(6));
            }

            for (int i = 0; i < p1Name.size(); i++) {
                View localStatView = layoutInflater.inflate(R.layout.localstatview_layout, null, false);
                p1N = localStatView.findViewById(R.id.statP1Name_tv);
                p1L = localStatView.findViewById(R.id.statP1Leg_tv);
                p1A = localStatView.findViewById(R.id.statP1Avg_tv);
                p2N = localStatView.findViewById(R.id.statP2Name_tv);
                p2L = localStatView.findViewById(R.id.statP2Leg_tv);
                p2A = localStatView.findViewById(R.id.statP2Avg_tv);

                p1N.setText(p1Name.get(i));
                p1L.setText(p1Leg.get(i));
                p1A.setText(p1Avg.get(i));
                p2N.setText(p2Name.get(i));
                p2L.setText(p2Leg.get(i));
                p2A.setText(p2Avg.get(i));

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 50, 0, 0);

                singleStat.addView(localStatView, layoutParams);
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

        builder.setTitle("Empty Stats");
        builder.setMessage("No local match found!");

        return builder.create();
    }
}
