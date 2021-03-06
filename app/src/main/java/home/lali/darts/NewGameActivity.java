package home.lali.darts;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import home.lali.darts.model.DartsPlayer;
import home.lali.darts.model.OnlineMatches;


public class NewGameActivity extends AppCompatActivity {

    private LinearLayout scrollableLayout;

    private Spinner gameModes;
    private Spinner legsNumber;

    private EditText addedPlayerName;

    private Button startGame;
    private Button startOnlineGame;

    private ArrayList<String> playersList = new ArrayList<>();

    private boolean onlineGame;

    private DatabaseReference databaseRef;
    private FirebaseUser user;

    private String currentKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        databaseRef = FirebaseDatabase.getInstance().getReference().child("online_matches");

        Button addNewPlayer;

        user = FirebaseAuth.getInstance().getCurrentUser();

        Bundle bundle = getIntent().getExtras();
        onlineGame = bundle.getBoolean("ONLINE_GAME");
        if (user != null) {
            playersList.add(bundle.getString("CURRENT_USER"));
            scrollableLayout = findViewById(R.id.scrollNamesLayout);
            TextView player_tv = new TextView(getApplicationContext());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            layoutParams.setMargins(0, 10, 0, 10);

            player_tv.setLayoutParams(layoutParams);
            player_tv.setText(bundle.getString("CURRENT_USER"));
            player_tv.setTextSize(20);
            player_tv.setTextColor(getResources().getColor(R.color.black));
            player_tv.setGravity(Gravity.CENTER_HORIZONTAL);

            scrollableLayout.addView(player_tv);
        }

        gameModes = findViewById(R.id.gamemode_spinner);
        legsNumber = findViewById(R.id.legsnumber_spinner);

        startGame = findViewById(R.id.startgame_btn);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playersList.size() == 2) {
                    Intent intent = new Intent(NewGameActivity.this, PlayGameActivity.class);
                    intent.putExtra("ONLINE_PLAY", false);
                    intent.putExtras(setInformation());
                    startActivity(intent);
                } else {
                    Toast.makeText(NewGameActivity.this, R.string.add_players, Toast.LENGTH_LONG).show();
                }
            }
        });

        addNewPlayer = findViewById(R.id.addplayer_btn);
        addNewPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playersList.size() < 2) {
                    addPlayerDialog();
                } else {
                    Toast.makeText(NewGameActivity.this, R.string.have_players, Toast.LENGTH_LONG).show();
                }
            }
        });

        startOnlineGame = findViewById(R.id.startOnlineGame_btn);
        startOnlineGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    if (playersList.size() == 2) {
                        try {

                            OnlineMatches onlineMatch = new OnlineMatches(
                                    new DartsPlayer(playersList.get(0), Integer.valueOf(gameModes.getSelectedItem().toString())),
                                    new DartsPlayer(playersList.get(1), Integer.valueOf(gameModes.getSelectedItem().toString())),
                                    true
                            );

                            currentKey = databaseRef.push().getKey();
                            databaseRef.child(currentKey).setValue(onlineMatch);
                            Log.i("SuccessUpload", "Match uploaded successfully!");

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notifications");
                            String notiKey = ref.push().getKey();
                            ref.child(notiKey).setValue(FirebaseInstanceId.getInstance().getToken());
                            ref.child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                            Log.i("TokenUpload", "Token uploaded successfully!");

                            Intent intent = new Intent(NewGameActivity.this, PlayGameActivity.class);
                            intent.putExtra("ONLINE_PLAY", true);
                            intent.putExtra("CURRENT_KEY", currentKey);
                            intent.putExtras(setInformation());
                            startActivity(intent);
                        } catch (Exception e) {
                            Log.e("UploadOnlineError", e.getMessage());
                        }
                    } else {
                        Toast.makeText(NewGameActivity.this, R.string.add_players, Toast.LENGTH_LONG).show();
                    }
                } else {
                    noInternet().show();
                }
            }
        });

        startGame.setEnabled(false);
        startOnlineGame.setEnabled(false);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (user != null && playersList.size() == 0) {
            playersList.add(user.getDisplayName());
            scrollableLayout = findViewById(R.id.scrollNamesLayout);
            TextView player_tv = new TextView(getApplicationContext());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            layoutParams.setMargins(0, 10, 0, 10);

            player_tv.setLayoutParams(layoutParams);
            player_tv.setText(user.getDisplayName());
            player_tv.setTextSize(20);
            player_tv.setTextColor(getResources().getColor(R.color.black));
            player_tv.setGravity(Gravity.CENTER_HORIZONTAL);

            scrollableLayout.addView(player_tv);
        }
        startGame.setEnabled(false);
        startOnlineGame.setEnabled(false);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (scrollableLayout != null) {
            scrollableLayout.removeAllViews();
        }
        playersList.clear();
    }

    /**
     * Adatok beállítása a játék Activity-hez.
     *
     * @return bundle
     */
    private Bundle setInformation() {
        Bundle bundle = new Bundle();

        bundle.putString("GAME_MODE", gameModes.getSelectedItem().toString());

        if (legsNumber.getSelectedItem().toString().contains("3")) {
            bundle.putInt("LEG_NUMBER", 3);
        } else if (legsNumber.getSelectedItem().toString().contains("5")) {
            bundle.putInt("LEG_NUMBER", 5);
        } else if (legsNumber.getSelectedItem().toString().contains("7")) {
            bundle.putInt("LEG_NUMBER", 7);
        } else if (legsNumber.getSelectedItem().toString().contains("9")) {
            bundle.putInt("LEG_NUMBER", 9);
        } else if (legsNumber.getSelectedItem().toString().contains("11")) {
            bundle.putInt("LEG_NUMBER", 11);
        } else if (legsNumber.getSelectedItem().toString().contains("13")) {
            bundle.putInt("LEG_NUMBER", 13);
        } else if (legsNumber.getSelectedItem().toString().contains("15")) {
            bundle.putInt("LEG_NUMBER", 15);
        } else if (legsNumber.getSelectedItem().toString().contains("17")) {
            bundle.putInt("LEG_NUMBER", 17);
        } else if (legsNumber.getSelectedItem().toString().contains("19")) {
            bundle.putInt("LEG_NUMBER", 19);
        }

        bundle.putStringArrayList("PLAYERS_LIST", playersList);

        return bundle;
    }

    /**
     * Új játékos hozzáadása.
     */
    private void addPlayerDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View playerDialogView = inflater.inflate(R.layout.addplayer_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();

        dialog.setView(playerDialogView);

        playerDialogView.findViewById(R.id.cancelLayout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        playerDialogView.findViewById(R.id.addplayerLayout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addedPlayerName = playerDialogView.findViewById(R.id.newplayerName_et);

                if (addedPlayerName.getText().length() < 3) {
                    Toast.makeText(NewGameActivity.this, R.string.min_name_length, Toast.LENGTH_LONG).show();
                } else {
                    scrollableLayout = findViewById(R.id.scrollNamesLayout);

                    TextView player_tv = new TextView(getApplicationContext());

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                    layoutParams.setMargins(0, 10, 0, 10);

                    player_tv.setLayoutParams(layoutParams);
                    player_tv.setText(addedPlayerName.getText());
                    player_tv.setTextSize(20);
                    player_tv.setTextColor(getResources().getColor(R.color.black));
                    player_tv.setGravity(Gravity.CENTER_HORIZONTAL);

                    scrollableLayout.addView(player_tv);

                    playersList.add(addedPlayerName.getText().toString());

                    if (playersList.size() == 2 && onlineGame) {
                        startOnlineGame.setEnabled(true);
                    } else if (playersList.size() == 2 && !onlineGame) {
                        startGame.setEnabled(true);
                    }

                    dialog.dismiss();
                }
            }
        });

        dialog.show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(NewGameActivity.this);

        builder.setIcon(R.mipmap.offline_icon)
                .setTitle(R.string.no_internet)
                .setMessage(R.string.lost_internet_connection)
                .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isOnline()) {
                            try {
                                if (playersList.size() == 2) {

                                    OnlineMatches onlineMatch = new OnlineMatches(
                                            new DartsPlayer(playersList.get(0), Integer.valueOf(gameModes.getSelectedItem().toString())),
                                            new DartsPlayer(playersList.get(1), Integer.valueOf(gameModes.getSelectedItem().toString())),
                                            true
                                    );

                                    currentKey = databaseRef.push().getKey();
                                    databaseRef.child(currentKey).setValue(onlineMatch);
                                    Log.i("trySuccessUpload", "Successful try again upload!");

                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notifications");
                                    String notiKey = ref.push().getKey();
                                    ref.child(notiKey).setValue(FirebaseInstanceId.getInstance().getToken());
                                    ref.child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                                    Log.i("TokenUpload", "Token uploaded successfully!");

                                    Intent intent = new Intent(NewGameActivity.this, PlayGameActivity.class);
                                    intent.putExtra("ONLINE_PLAY", true);
                                    intent.putExtra("CURRENT_KEY", currentKey);
                                    intent.putExtras(setInformation());
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(NewGameActivity.this, R.string.add_players, Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Log.e("UploadOnlineError", e.getMessage());
                            }
                        } else {
                            noInternet().show();
                        }
                    }
                })
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
}