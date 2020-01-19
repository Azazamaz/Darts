package home.lali.darts;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import home.lali.darts.database.DatabaseAccess;
import home.lali.darts.model.DartsPlayer;

public class PlayGameActivity extends AppCompatActivity {

    private TextView[] playerName = new TextView[2];
    private TextView[] score = new TextView[2];
    private TextView[] legsWon = new TextView[2];
    private TextView[] legAvg = new TextView[2];
    private TextView[] matchAvg = new TextView[2];
    private TextView[] checkout = new TextView[2];

    private EditText[] enterScore = new EditText[2];

    private Button num1;
    private Button num2;
    private Button num3;
    private Button num4;
    private Button num5;
    private Button num6;
    private Button num7;
    private Button num8;
    private Button num9;
    private Button num0;
    private Button clear;
    private Button ok_btn;

    private ImageButton reset;

    private DartsPlayer player1;
    private DartsPlayer player2;

    private int gameMode;
    private int legNumber;
    private static int okBtnPress = 0;

    private int[] matchRounds = {0, 0};
    private int[] legRounds = {0, 0};

    private double[] legAvg_help = {0, 0};
    private double[] matchAvg_help = {0, 0};

    private boolean onlinePlay;

    private String currentKey;

    private DatabaseAccess databaseAccess;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        databaseRef = FirebaseDatabase.getInstance().getReference().child("online_matches");

        initFields();

        databaseAccess = DatabaseAccess.getInstance(PlayGameActivity.this);

        getInformation();
        updateEnterScore();

        enterScore[0].requestFocus();

        ok_btn.setOnClickListener(okBtnFunction);
        reset.setOnClickListener(resetScore);
    }

    @Override
    protected void onResume() {
        super.onResume();
        okBtnPress = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (onlinePlay) {
            databaseRef.child(currentKey).child("live").setValue(false);
        }
    }

    /**
     * Mező inicializáló metódus
     */
    protected void initFields() {
        LinearLayout scoreLayout = findViewById(R.id.score_layout);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View scoreboardLayout = inflater.inflate(R.layout.scoreboard_layout, null);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params1.setMargins(40, 0, 30, 0);
        scoreboardLayout.setLayoutParams(params1);

        final View scoreboardLayout2 = inflater.inflate(R.layout.scoreboard_layout, null);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params2.setMargins(40, 0, 30, 0);
        scoreboardLayout2.setLayoutParams(params2);

        scoreLayout.addView(scoreboardLayout);
        scoreLayout.addView(scoreboardLayout2);

        playerName[0] = scoreboardLayout.findViewById(R.id.playerName_tv);
        score[0] = scoreboardLayout.findViewById(R.id.score_tv);
        enterScore[0] = scoreboardLayout.findViewById(R.id.enterScore_et);
        legsWon[0] = scoreboardLayout.findViewById(R.id.legsWon_tv);
        legAvg[0] = scoreboardLayout.findViewById(R.id.avgLeg_tv);
        matchAvg[0] = scoreboardLayout.findViewById(R.id.avgMatch_tv);
        checkout[0] = scoreboardLayout.findViewById(R.id.checkout_tv);

        playerName[1] = scoreboardLayout2.findViewById(R.id.playerName_tv);
        score[1] = scoreboardLayout2.findViewById(R.id.score_tv);
        enterScore[1] = scoreboardLayout2.findViewById(R.id.enterScore_et);
        legsWon[1] = scoreboardLayout2.findViewById(R.id.legsWon_tv);
        legAvg[1] = scoreboardLayout2.findViewById(R.id.avgLeg_tv);
        matchAvg[1] = scoreboardLayout2.findViewById(R.id.avgMatch_tv);
        checkout[1] = scoreboardLayout2.findViewById(R.id.checkout_tv);

        num1 = findViewById(R.id.number1_btn);
        num2 = findViewById(R.id.number2_btn);
        num3 = findViewById(R.id.number3_btn);
        num4 = findViewById(R.id.number4_btn);
        num5 = findViewById(R.id.number5_btn);
        num6 = findViewById(R.id.number6_btn);
        num7 = findViewById(R.id.number7_btn);
        num8 = findViewById(R.id.number8_btn);
        num9 = findViewById(R.id.number9_btn);
        num0 = findViewById(R.id.number0_btn);
        clear = findViewById(R.id.remove_btn);
        reset = findViewById(R.id.reset_btn);
        ok_btn = findViewById(R.id.ok_btn);

        player1 = new DartsPlayer();
        player2 = new DartsPlayer();

        player1.setLegStart(true);

        enterScore[0].setHintTextColor(getResources().getColor(R.color.buttonStroke));
        enterScore[1].setHintTextColor(getResources().getColor(R.color.header_background));
    }

    /**
     * Adatok lekérdezése az előző Activity-ről.
     * Játékosok inicializálása
     */
    private void getInformation() {
        Bundle bundle = getIntent().getExtras();
        ArrayList<String> players;

        if (bundle != null) {
            onlinePlay = bundle.getBoolean("ONLINE_PLAY");
            gameMode = Integer.parseInt(bundle.getString("GAME_MODE"));
            legNumber = bundle.getInt("LEG_NUMBER");
            players = bundle.getStringArrayList("PLAYERS_LIST");

            Log.i("Játékmód: ", Integer.toString(gameMode));
            Log.i("Legek száma: ", Integer.toString(legNumber));
            Log.i("OnlinePlayValue", String.valueOf(onlinePlay));

            player1.setPlayerName(players.get(0));
            player1.setScore(gameMode);

            player2.setPlayerName(players.get(1));
            player2.setScore(gameMode);

            playerName[0].setText(player1.getPlayerName());
            score[0].setText(String.valueOf(player1.getScore()));
            playerName[1].setText(player2.getPlayerName());
            score[1].setText(String.valueOf(player2.getScore()));

            if (onlinePlay) {
                currentKey = bundle.getString("CURRENT_KEY");
                Log.d("Current key", currentKey);
            }
        }
    }


    /**
     * Gombok lenyomása után beírjuk a számot a ScoreET-be
     */
    private void updateEnterScore() {
        appendNumberToEnterScore(num1, "1");
        appendNumberToEnterScore(num2, "2");
        appendNumberToEnterScore(num3, "3");
        appendNumberToEnterScore(num4, "4");
        appendNumberToEnterScore(num5, "5");
        appendNumberToEnterScore(num6, "6");
        appendNumberToEnterScore(num7, "7");
        appendNumberToEnterScore(num8, "8");
        appendNumberToEnterScore(num9, "9");
        appendNumberToEnterScore(num0, "0");

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((okBtnPress % 2) == 0) {
                    enterScore[0].setText("");
                } else if ((okBtnPress % 2) == 1) {
                    enterScore[1].setText("");
                }
            }
        });
    }

    private void appendNumberToEnterScore(Button button, final String number) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendNumberToEnterScore(number);
            }
        });
    }

    private void appendNumberToEnterScore(String number) {
        if ((okBtnPress % 2) == 0) {
            enterScore[0].append(number);
        } else if ((okBtnPress % 2) == 1) {
            enterScore[1].append(number);
        }
    }


    /**
     * Elvégezzük a scoreboard frissítését.
     */
    View.OnClickListener okBtnFunction = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onlinePlay) {
                if ((okBtnPress % 2) == 0) {
                    onlinePlayer1Round();
                } else if ((okBtnPress % 2) == 1) {
                    onlinePlayer2Round();
                }
            } else if (!onlinePlay) {
                if ((okBtnPress % 2) == 0) {
                    player1Round();
                } else if ((okBtnPress % 2) == 1) {
                    player2Round();
                }
            }
        }
    };

    private void getPlayerCheckout(@NonNull DartsPlayer player, TextView score, TextView checkout) {
        if (player.getScore() < 180) {
            databaseAccess.open();
            String checkO = databaseAccess.getCheckout(String.valueOf(score.getText()));
            databaseAccess.close();
            checkout.setText(checkO);
        }
    }

    private void player1Round() {
        try {
            int score_helper;
            score_helper = Integer.parseInt(String.valueOf(enterScore[0].getText()));

            if (score_helper > 180) {
                Toast.makeText(PlayGameActivity.this, R.string.invalid3darts, Toast.LENGTH_LONG).show();
            } else {
                if (player1.getScore() - score_helper < 0) {
                    Toast.makeText(PlayGameActivity.this, R.string.not_possible, Toast.LENGTH_LONG).show();
                } else if (player1.getScore() - score_helper == 0 && notCheckOut(player1.getScore())) {
                    Toast.makeText(PlayGameActivity.this,
                            getString(R.string.nocheckout, player1.getScore()), Toast.LENGTH_LONG).show();
                } else {
                    matchRounds[0]++;
                    legRounds[0]++;
                    okBtnPress++;

                    player1.setScore(player1.getScore() - score_helper);
                    score[0].setText(String.valueOf(player1.getScore()));
                    player1.setLastScore(score_helper);

                    ///Kiszállózás
                    getPlayerCheckout(player1, score[0], checkout[0]);

                    legAvg_help[0] += score_helper;
                    matchAvg_help[0] += score_helper;

                    player1.setLegAvg(legAvg_help[0] / legRounds[0]);
                    player1.setMatchAvg(matchAvg_help[0] / matchRounds[0]);

                    updateAvgViews(player1, legAvg[0], matchAvg[0]);

                    setEnterScoreFocus(enterScore[0], enterScore[1]);
                    setEnterScoreHintTextColor(enterScore[0], enterScore[1]);
                }
            }

            //Legek végét figyelő kódrész.
            if (Integer.parseInt(String.valueOf(score[0].getText())) == 0) {
                legWinnerDialog(player1).show();
                legRounds[0] = 0;
                legAvg_help[0] = 0;
                player1.setLegW(player1.getLegW() + 1);
                legsWon[0].setText(String.valueOf(player1.getLegW()));
                score[0].setText(String.valueOf(gameMode));
                player1.setScore(gameMode);
                score[1].setText(String.valueOf(gameMode));
                player2.setScore(gameMode);
                checkout[1].setText("");

                setLegStartPlayer(player1, player2);
            }

            if (player1.getLegW() == (legNumber / 2 + 1)) {
                matchWinnerDialog(player1).show();

                double p1Avg = numberRound(player1.getMatchAvg(), 2);
                double p2Avg = numberRound(player2.getMatchAvg(), 2);

                databaseAccess.open();
                databaseAccess.saveLocalGameStat(player1.getPlayerName(), player1.getLegW(), p1Avg,
                        player2.getPlayerName(), player2.getLegW(), p2Avg);
                databaseAccess.close();
            }

        } catch (Exception ex) {
            Log.e("DARTS", ex.getMessage());
        }
    }

    private void player2Round() {
        try {
            int score_helper;
            score_helper = Integer.parseInt(String.valueOf(enterScore[1].getText()));

            if (score_helper > 180) {
                Toast.makeText(PlayGameActivity.this, R.string.invalid3darts, Toast.LENGTH_LONG).show();
            } else {
                if (player2.getScore() - score_helper < 0) {
                    Toast.makeText(PlayGameActivity.this, R.string.not_possible, Toast.LENGTH_LONG).show();
                } else if (player2.getScore() - score_helper == 0 && notCheckOut(player2.getScore())) {
                    Toast.makeText(PlayGameActivity.this,
                            getString(R.string.nocheckout, player2.getScore()), Toast.LENGTH_LONG).show();
                } else {
                    matchRounds[1]++;
                    legRounds[1]++;
                    okBtnPress++;

                    player2.setScore(player2.getScore() - score_helper);
                    score[1].setText(String.valueOf(player2.getScore()));
                    player2.setLastScore(score_helper);

                    ///Kiszállók
                    getPlayerCheckout(player2, score[1], checkout[1]);

                    legAvg_help[1] += score_helper;
                    matchAvg_help[1] += score_helper;

                    player2.setLegAvg(legAvg_help[1] / legRounds[1]);
                    player2.setMatchAvg(matchAvg_help[1] / matchRounds[1]);

                    updateAvgViews(player2, legAvg[1], matchAvg[1]);

                    setEnterScoreFocus(enterScore[1], enterScore[0]);
                    setEnterScoreHintTextColor(enterScore[1], enterScore[0]);
                }
            }

            //Legek végét figyelő kódrész.
            if (Integer.parseInt(String.valueOf(score[1].getText())) == 0) {
                legWinnerDialog(player2).show();
                legRounds[1] = 0;
                legAvg_help[1] = 0;
                player2.setLegW(player2.getLegW() + 1);
                legsWon[1].setText(String.valueOf(player2.getLegW()));
                score[1].setText(String.valueOf(gameMode));
                player2.setScore(gameMode);

                score[0].setText(String.valueOf(gameMode));
                player1.setScore(gameMode);
                checkout[0].setText("");

                setLegStartPlayer(player1, player2);
            }

            if (player2.getLegW() == (legNumber / 2 + 1)) {
                matchWinnerDialog(player2).show();

                double p1Avg = numberRound(player1.getMatchAvg(), 2);
                double p2Avg = numberRound(player2.getMatchAvg(), 2);

                databaseAccess.open();
                databaseAccess.saveLocalGameStat(player1.getPlayerName(), player1.getLegW(), p1Avg,
                        player2.getPlayerName(), player2.getLegW(), p2Avg);
                databaseAccess.close();

            }
        } catch (Exception ex) {
            Log.e("DARTS", ex.getMessage());
        }
    }

    private void onlinePlayer1Round() {
        if (isOnline()) {
            try {
                int score_helper;
                score_helper = Integer.parseInt(String.valueOf(enterScore[0].getText()));

                if (score_helper > 180) {
                    Toast.makeText(PlayGameActivity.this, R.string.invalid3darts, Toast.LENGTH_LONG).show();
                } else {

                    if (player1.getScore() - score_helper < 0) {
                        Toast.makeText(PlayGameActivity.this, R.string.not_possible, Toast.LENGTH_LONG).show();
                    } else if (player1.getScore() - score_helper == 0 && notCheckOut(player1.getScore())) {
                        Toast.makeText(PlayGameActivity.this,
                                getString(R.string.nocheckout, player1.getScore()), Toast.LENGTH_LONG).show();
                    } else {
                        matchRounds[0]++;
                        legRounds[0]++;
                        okBtnPress++;

                        player1.setScore(player1.getScore() - score_helper);
                        score[0].setText(String.valueOf(player1.getScore()));
                        player1.setLastScore(score_helper);

                        ///Kiszállózás
                        getPlayerCheckout(player1, score[0], checkout[0]);

                        legAvg_help[0] += score_helper;
                        matchAvg_help[0] += score_helper;

                        player1.setLegAvg(legAvg_help[0] / legRounds[0]);
                        player1.setMatchAvg(matchAvg_help[0] / matchRounds[0]);

                        updateAvgViews(player1, legAvg[0], matchAvg[0]);

                        setEnterScoreFocus(enterScore[0], enterScore[1]);
                        setEnterScoreHintTextColor(enterScore[0], enterScore[1]);

                        databaseRef.child(currentKey).child("score1").setValue(player1.getScore());
                        double p1Avg = numberRound(player1.getMatchAvg(), 2);
                        databaseRef.child(currentKey).child("avg_1").setValue(p1Avg);
                    }
                }

                //Legek végét figyelő kódrész.
                if (Integer.parseInt(String.valueOf(score[0].getText())) == 0) {
                    legWinnerDialog(player1).show();
                    legRounds[0] = 0;
                    legAvg_help[0] = 0;
                    player1.setLegW(player1.getLegW() + 1);
                    legsWon[0].setText(String.valueOf(player1.getLegW()));
                    score[0].setText(String.valueOf(gameMode));
                    player1.setScore(gameMode);
                    score[1].setText(String.valueOf(gameMode));
                    player2.setScore(gameMode);
                    checkout[1].setText("");

                    databaseRef.child(currentKey).child("legW_1").setValue(player1.getLegW());
                    databaseRef.child(currentKey).child("score1").setValue(gameMode);
                    databaseRef.child(currentKey).child("score2").setValue(gameMode);

                    setLegStartPlayer(player1, player2);
                }


                if (player1.getLegW() == (legNumber / 2 + 1)) {
                    matchWinnerDialog(player1).show();

                    double p1Avg = numberRound(player1.getMatchAvg(), 2);
                    double p2Avg = numberRound(player2.getMatchAvg(), 2);

                    databaseAccess.open();
                    databaseAccess.saveLocalGameStat(player1.getPlayerName(), player1.getLegW(), p1Avg,
                            player2.getPlayerName(), player2.getLegW(), p2Avg);
                    databaseAccess.close();

                    databaseRef.child(currentKey).child("live").setValue(false);
                }

            } catch (Exception ex) {
                Log.e("OnlinePlay", ex.getMessage());
            }
        } else {
            noInternetPlayer1().show();
        }
    }

    private void onlinePlayer2Round() {
        if (isOnline()) {
            try {
                int score_helper;
                score_helper = Integer.parseInt(String.valueOf(enterScore[1].getText()));

                if (score_helper > 180) {
                    Toast.makeText(PlayGameActivity.this, R.string.invalid3darts, Toast.LENGTH_LONG).show();
                } else {
                    if (player2.getScore() - score_helper < 0) {
                        Toast.makeText(PlayGameActivity.this, R.string.not_possible, Toast.LENGTH_LONG).show();
                    } else if (player2.getScore() - score_helper == 0 && notCheckOut(player2.getScore())) {
                        Toast.makeText(PlayGameActivity.this,
                                getString(R.string.nocheckout, player2.getScore()), Toast.LENGTH_LONG).show();
                    } else {
                        matchRounds[1]++;
                        legRounds[1]++;
                        okBtnPress++;

                        player2.setScore(player2.getScore() - score_helper);
                        score[1].setText(String.valueOf(player2.getScore()));
                        player2.setLastScore(score_helper);

                        ///Kiszállók
                        getPlayerCheckout(player2, score[1], checkout[1]);

                        legAvg_help[1] += score_helper;
                        matchAvg_help[1] += score_helper;

                        player2.setLegAvg(legAvg_help[1] / legRounds[1]);
                        player2.setMatchAvg(matchAvg_help[1] / matchRounds[1]);

                        updateAvgViews(player2, legAvg[1], matchAvg[1]);

                        setEnterScoreFocus(enterScore[1], enterScore[0]);
                        setEnterScoreHintTextColor(enterScore[1], enterScore[0]);

                        databaseRef.child(currentKey).child("score2").setValue(player2.getScore());
                        double p2Avg = numberRound(player2.getMatchAvg(), 2);
                        databaseRef.child(currentKey).child("avg_2").setValue(p2Avg);
                    }
                }

                //Legek végét figyelő kódrész.
                if (Integer.parseInt(String.valueOf(score[1].getText())) == 0) {
                    legWinnerDialog(player2).show();
                    legRounds[1] = 0;
                    legAvg_help[1] = 0;
                    player2.setLegW(player2.getLegW() + 1);
                    legsWon[1].setText(String.valueOf(player2.getLegW()));
                    score[1].setText(String.valueOf(gameMode));
                    player2.setScore(gameMode);

                    score[0].setText(String.valueOf(gameMode));
                    player1.setScore(gameMode);
                    checkout[0].setText("");

                    databaseRef.child(currentKey).child("legW_2").setValue(player2.getLegW());
                    databaseRef.child(currentKey).child("score1").setValue(gameMode);
                    databaseRef.child(currentKey).child("score2").setValue(gameMode);

                    setLegStartPlayer(player1, player2);
                }

                if (player2.getLegW() == (legNumber / 2 + 1)) {
                    matchWinnerDialog(player2).show();

                    double p1Avg = numberRound(player1.getMatchAvg(), 2);
                    double p2Avg = numberRound(player2.getMatchAvg(), 2);

                    databaseAccess.open();
                    databaseAccess.saveLocalGameStat(player1.getPlayerName(), player1.getLegW(), p1Avg,
                            player2.getPlayerName(), player2.getLegW(), p2Avg);
                    databaseAccess.close();

                    databaseRef.child(currentKey).child("live").setValue(false);

                }
            } catch (Exception ex) {
                Log.e("OnlinePlay", ex.getMessage());
            }
        } else {
            noInternetPlayer2().show();
        }
    }

    private void updateAvgViews(DartsPlayer player, TextView legAvg, TextView matchAvg) {
        legAvg.setText(String.valueOf(player.getLegAvg()));
        matchAvg.setText(String.valueOf(player.getMatchAvg()));
    }

    private void setEnterScoreHintTextColor(EditText backgroundColorView, EditText buttonStrokeView) {
        backgroundColorView.setHintTextColor(getResources().getColor(R.color.background));
        buttonStrokeView.setHintTextColor(getResources().getColor(R.color.buttonStroke));
    }

    private void setEnterScoreFocus(EditText emptyText, EditText focusRequest) {
        emptyText.setText("");
        focusRequest.requestFocus();
    }

    private void setLegStartPlayer(@NonNull DartsPlayer p1, @NonNull DartsPlayer p2) {
        if (p1.getLegStart()) {
            p1.setLegStart(false);
            p2.setLegStart(true);
            enterScore[0].setHintTextColor(getResources().getColor(R.color.background));
            enterScore[1].setHintTextColor(getResources().getColor(R.color.buttonStroke));
            if ((okBtnPress % 2) == 0) {
                okBtnPress--;
            }
        } else if (p2.getLegStart()) {
            p2.setLegStart(false);
            p1.setLegStart(true);
            enterScore[0].setHintTextColor(getResources().getColor(R.color.buttonStroke));
            enterScore[1].setHintTextColor(getResources().getColor(R.color.background));
            if ((okBtnPress % 2) == 1) {
                okBtnPress--;
            }
        }
    }

    private boolean notCheckOut(int n) {
        int[] nco = getResources().getIntArray(R.array.noCheckOuts);

        for (int i = 0; i < nco.length; i++) {
            if (n == nco[i]) {
                return true;
            }
        }
        return false;
    }

    View.OnClickListener resetScore = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LayoutInflater inflater = LayoutInflater.from(PlayGameActivity.this);
            final View resetScoreDialogView = inflater.inflate(R.layout.resetscore_layout, null);
            final AlertDialog dialog = new AlertDialog.Builder(PlayGameActivity.this).create();

            if ((okBtnPress - 1) % 2 == 0) {
                TextView tv = resetScoreDialogView.findViewById(R.id.resetText_tv);
                tv.setText(getString(R.string.reset_score_str, player1.getPlayerName()));
            } else if ((okBtnPress - 1) % 2 == 1) {
                TextView tv = resetScoreDialogView.findViewById(R.id.resetText_tv);
                tv.setText(getString(R.string.reset_score_str, player2.getPlayerName()));
            }
            dialog.setView(resetScoreDialogView);

            resetScoreDialogView.findViewById(R.id.cancelReset_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            resetScoreDialogView.findViewById(R.id.yesReset_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    okBtnPress--;
                    if (okBtnPress % 2 == 0) {
                        matchRounds[0]--;
                        legRounds[0]--;
                        player1.setScore(player1.getScore() + player1.getLastScore());
                        score[0].setText(String.valueOf(player1.getScore()));

                        legAvg_help[0] -= player1.getLastScore();
                        matchAvg_help[0] -= player1.getLastScore();
                        player1.setLegAvg(legAvg_help[0] / legRounds[0]);
                        player1.setMatchAvg(matchAvg_help[0] / matchRounds[0]);

                        legAvg[0].setText(String.valueOf(player1.getLegAvg()));
                        matchAvg[0].setText(String.valueOf(player1.getMatchAvg()));

                        enterScore[0].setText("");
                        enterScore[1].requestFocus();
                    } else if (okBtnPress % 2 == 1) {
                        matchRounds[1]--;
                        legRounds[1]--;
                        player2.setScore(player2.getScore() + player2.getLastScore());
                        score[1].setText(String.valueOf(player2.getScore()));

                        legAvg_help[1] -= player2.getLastScore();
                        matchAvg_help[1] -= player2.getLastScore();

                        player2.setLegAvg(legAvg_help[1] / legRounds[1]);
                        player2.setMatchAvg(matchAvg_help[1] / matchRounds[1]);

                        legAvg[1].setText(String.valueOf(player2.getLegAvg()));
                        matchAvg[1].setText(String.valueOf(player2.getMatchAvg()));

                        enterScore[1].setText("");
                        enterScore[0].requestFocus();
                    }
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    };

    /**
     * Mérkőzés végén felugró értesítő dialogós.
     *
     * @return AlertDialog
     */
    private AlertDialog matchWinnerDialog(@NonNull DartsPlayer player) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayGameActivity.this);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onBackPressed();
            }
        });

        builder.setTitle(R.string.match_winner);
        builder.setMessage(getString(R.string.game_winner, player.getPlayerName()));

        return builder.create();
    }

    /**
     * Leg végén felugró értesítő dialógus.
     *
     * @return AlertDialog
     */
    private AlertDialog legWinnerDialog(@NonNull DartsPlayer player) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayGameActivity.this);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        builder.setTitle(R.string.leg_winner);
        builder.setMessage(getString(R.string.leg_win, player.getPlayerName()));

        return builder.create();
    }

    private double numberRound(double val, int places) {
        if (places <= 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(val);
        return bd.setScale(places, RoundingMode.HALF_UP).doubleValue();
    }

    private AlertDialog noInternetPlayer1() {
        return noInternet(new Callable<Void>() {
            @Override
            public Void call() {
                onlinePlayer1Round();
                return null;
            }
        });
    }

    private AlertDialog noInternetPlayer2() {
        return noInternet(new Callable<Void>() {
            @Override
            public Void call() {
                onlinePlayer2Round();
                return null;
            }
        });
    }

    private AlertDialog noInternet(final Callable<Void> playerRound) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayGameActivity.this);

        builder.setIcon(R.mipmap.offline_icon)
                .setTitle(R.string.no_internet)
                .setMessage(R.string.lost_internet_connection)
                .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            playerRound.call();
                        } catch (Exception e) {
                            e.printStackTrace();
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

    private boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        } catch (Exception e) {
            Log.e("isOnlineError", e.getMessage());
            return false;
        }
    }
}