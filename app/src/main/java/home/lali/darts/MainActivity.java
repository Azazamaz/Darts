package home.lali.darts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newGameBtn;
        Button newOnlineGameBtn;
        Button liveGamesBtn;
        Button localStatBtn;
        Button globalStatBtn;

        newGameBtn = findViewById(R.id.newGame_btn);
        newOnlineGameBtn = findViewById(R.id.newOnlineGame_btn);
        liveGamesBtn = findViewById(R.id.liveGames_btn);
        localStatBtn = findViewById(R.id.statistic_btn);
        globalStatBtn = findViewById(R.id.globalStat_btn);

        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewGameActivity.class);
                intent.putExtra("ONLINE_GAME", false);

                startActivity(intent);
            }
        });

        newOnlineGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    //TODO: Bejelentkezés, flag
                    Intent intent = new Intent(MainActivity.this, NewGameActivity.class);
                    intent.putExtra("ONLINE_GAME", true);

                    startActivity(intent);

                } else {
                    noInternet().show();
                }
            }
        });

        liveGamesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    Intent intent = new Intent(MainActivity.this, LiveMatchActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Need internet", Toast.LENGTH_LONG).show();
                }
            }
        });

        localStatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LocalStatActivity.class);
                startActivity(intent);
            }
        });

        globalStatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    Intent intent = new Intent(MainActivity.this, GlobalStatActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Need internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.launcher)
                .setTitle("Exit")
                .setMessage("You will exit from the application. Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        } catch (Exception e) {
            Log.e("isOnline", e.getMessage());
            return false;
        }
    }

    private AlertDialog noInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(R.mipmap.offline_icon)
                .setTitle("No internet")
                .setMessage("Lost internet connection")
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isOnline()) {
                            //TODO: Bejelentkezés, intent
                            Intent intent = new Intent(MainActivity.this, NewGameActivity.class);
                            intent.putExtra("ONLINE_GAME", true);

                            startActivity(intent);
                        } else {
                            noInternet().show();
                        }
                    }
                })
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

}
