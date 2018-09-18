package home.lali.darts;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class NewGameActivity extends AppCompatActivity {

    private LinearLayout scrollableLayout;

    private Spinner gameModes;
    private Spinner setsNumber;
    private Spinner legsNumber;

    private Button startGame;
    private Button addNewPlayer;

    private EditText addedPlayerName;

    private ArrayList<String> playersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        gameModes = findViewById(R.id.gamemode_spinner);
        legsNumber = findViewById(R.id.legsnumber_spinner);
        setsNumber = findViewById(R.id.sets_spinner);

        startGame = findViewById(R.id.startgame_btn);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playersList.size() < 2){
                    Toast.makeText(NewGameActivity.this,
                            "Add 2 players!", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(NewGameActivity.this, PlayGameActivity.class);
                    intent.putExtras(setInformation());
                    startActivity(intent);
                }
            }
        });

        addNewPlayer = findViewById(R.id.addplayer_btn);
        addNewPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPlayerDialog();
            }
        });
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
        }

        if (setsNumber.getSelectedItem().toString().contains("3")) {
            bundle.putInt("SET_NUMBER", 3);
        } else if (setsNumber.getSelectedItem().toString().contains("5")) {
            bundle.putInt("SET_NUMBER", 5);
        } else if (setsNumber.getSelectedItem().toString().contains("7")) {
            bundle.putInt("SET_NUMBER", 7);
        } else if (setsNumber.getSelectedItem().toString().contains("9")) {
            bundle.putInt("SET_NUMBER", 9);
        } else if (setsNumber.getSelectedItem().toString().contains("11")) {
            bundle.putInt("SET_NUMBER", 11);
        } else if (setsNumber.getSelectedItem().toString().contains("13")) {
            bundle.putInt("SET_NUMBER", 13);
        }

        bundle.putStringArrayList("PLAYERS_LIST", playersList);

        return bundle;
    }


    /**
     * Új játékos hozzáadása.
     * */
    private void addPlayerDialog(){
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

                if (addedPlayerName.getText().length() < 3){
                    Toast.makeText(NewGameActivity.this, "Name must be at least 3 charachters!", Toast.LENGTH_LONG).show();
                } else {
                    scrollableLayout = findViewById(R.id.scrollNamesLayout);

                    TextView player_tv = new TextView(getApplicationContext());

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                    layoutParams.setMargins(0,10,0,10);

                    player_tv.setLayoutParams(layoutParams);
                    player_tv.setText(addedPlayerName.getText());
                    player_tv.setTextSize(20);
                    player_tv.setTextColor(getResources().getColor(R.color.black));
                    player_tv.setGravity(Gravity.CENTER_HORIZONTAL);

                    scrollableLayout.addView(player_tv);

                    playersList.add(addedPlayerName.getText().toString());

                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }
}