package home.lali.darts;

import android.content.Context;
import android.content.DialogInterface;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import home.lali.darts.database.DatabaseAccess;
import home.lali.darts.model.DartsPlayer;

public class PlayGameActivity extends AppCompatActivity {

    private TextView playerName1, playerName2;
    private TextView score1, score2;
    private TextView legsWon1, legsWon2;
    private TextView legAvg1, legAvg2;
    private TextView matchAvg1, matchAvg2;
    private TextView checkout1, checkout2;

    private EditText enterScore1, enterScore2;

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
    private int matchRounds1 = 0;
    private int legRounds1 = 0;
    private int matchRounds2 = 0;
    private int legRounds2 = 0;
    private static int okBtnPress = 0;

    private double legAvg_help1 = 0;
    private double matchAvg_help1 = 0;
    private double legAvg_help2 = 0;
    private double matchAvg_help2 = 0;

    private DatabaseAccess databaseAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        initFields();

        databaseAccess = DatabaseAccess.getInstance(PlayGameActivity.this);

        getInformation();
        updateEnterScore();

        enterScore1.requestFocus();

        ok_btn.setOnClickListener(okBtnFunction);
        reset.setOnClickListener(resetScore);
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

        playerName1 = scoreboardLayout.findViewById(R.id.playerName_tv);
        score1 = scoreboardLayout.findViewById(R.id.score_tv);
        enterScore1 = scoreboardLayout.findViewById(R.id.enterScore_et);
        legsWon1 = scoreboardLayout.findViewById(R.id.legsWon_tv);
        legAvg1 = scoreboardLayout.findViewById(R.id.avgLeg_tv);
        matchAvg1 = scoreboardLayout.findViewById(R.id.avgMatch_tv);
        checkout1 = scoreboardLayout.findViewById(R.id.checkout_tv);

        playerName2 = scoreboardLayout2.findViewById(R.id.playerName_tv);
        score2 = scoreboardLayout2.findViewById(R.id.score_tv);
        enterScore2 = scoreboardLayout2.findViewById(R.id.enterScore_et);
        legsWon2 = scoreboardLayout2.findViewById(R.id.legsWon_tv);
        legAvg2 = scoreboardLayout2.findViewById(R.id.avgLeg_tv);
        matchAvg2 = scoreboardLayout2.findViewById(R.id.avgMatch_tv);
        checkout2 = scoreboardLayout2.findViewById(R.id.checkout_tv);

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
    }

    /**
     * Adatok lekérdezése az előző Activity-ről.
     * Játékosok inicializálása
     */
    private void getInformation() {
        Bundle bundle = getIntent().getExtras();
        ArrayList<String> players;

        if (bundle != null) {
            gameMode = Integer.parseInt(bundle.getString("GAME_MODE"));
            legNumber = bundle.getInt("LEG_NUMBER");
            players = bundle.getStringArrayList("PLAYERS_LIST");

            Log.i("Játékmód: ", Integer.toString(gameMode));
            Log.i("Legek száma: ", Integer.toString(legNumber));

            player1.setPlayerName(players.get(0));
            player1.setScore(gameMode);

            player2.setPlayerName(players.get(1));
            player2.setScore(gameMode);

            playerName1.setText(player1.getPlayerName());
            score1.setText(String.valueOf(player1.getScore()));
            playerName2.setText(player2.getPlayerName());
            score2.setText(String.valueOf(player2.getScore()));
        }
    }

    /**
     * Gombok lenyomása után beírjuk a számot a ScoreET-be
     */
    private void updateEnterScore() {
        num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((okBtnPress % 2) == 0) {
                    enterScore1.append("1");
                } else if ((okBtnPress % 2) == 1) {
                    enterScore2.append("1");
                }
            }
        });

        num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((okBtnPress % 2) == 0) {
                    enterScore1.append("2");
                } else if ((okBtnPress % 2) == 1) {
                    enterScore2.append("2");
                }
            }
        });

        num3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((okBtnPress % 2) == 0) {
                    enterScore1.append("3");
                } else if ((okBtnPress % 2) == 1) {
                    enterScore2.append("3");
                }
            }
        });

        num4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((okBtnPress % 2) == 0) {
                    enterScore1.append("4");
                } else if ((okBtnPress % 2) == 1) {
                    enterScore2.append("4");
                }
            }
        });

        num5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((okBtnPress % 2) == 0) {
                    enterScore1.append("5");
                } else if ((okBtnPress % 2) == 1) {
                    enterScore2.append("5");
                }
            }
        });

        num6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((okBtnPress % 2) == 0) {
                    enterScore1.append("6");
                } else if ((okBtnPress % 2) == 1) {
                    enterScore2.append("6");
                }
            }
        });

        num7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((okBtnPress % 2) == 0) {
                    enterScore1.append("7");
                } else if ((okBtnPress % 2) == 1) {
                    enterScore2.append("7");
                }
            }
        });

        num8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((okBtnPress % 2) == 0) {
                    enterScore1.append("8");
                } else if ((okBtnPress % 2) == 1) {
                    enterScore2.append("8");
                }
            }
        });

        num9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((okBtnPress % 2) == 0) {
                    enterScore1.append("9");
                } else if ((okBtnPress % 2) == 1) {
                    enterScore2.append("9");
                }
            }
        });

        num0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((okBtnPress % 2) == 0) {
                    enterScore1.append("0");
                } else if ((okBtnPress % 2) == 1) {
                    enterScore2.append("0");
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((okBtnPress % 2) == 0) {
                    enterScore1.setText("");
                } else if ((okBtnPress % 2) == 1) {
                    enterScore2.setText("");
                }
            }
        });
    }

    /**
     * Elvégezzük a scoreboard frissítését.
     */
    View.OnClickListener okBtnFunction = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if ((okBtnPress % 2) == 0) {
                player1Round();
            } else if ((okBtnPress % 2) == 1) {
                player2Round();
            }
        }
    };

    private void getPlayerCheckout(DartsPlayer player, TextView score, TextView checkout) {
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
            score_helper = Integer.parseInt(String.valueOf(enterScore1.getText()));

            if (score_helper > 180) {
                Toast.makeText(PlayGameActivity.this, "This is impossible with 3 darts!", Toast.LENGTH_LONG).show();
            } else {

                if (player1.getScore() - score_helper < 0) {
                    Toast.makeText(PlayGameActivity.this, "Not possible!", Toast.LENGTH_LONG).show();
                } else if (player1.getScore() - score_helper == 0 && player1.getScore() == 180) {
                    Toast.makeText(PlayGameActivity.this, "180 is not checkout!", Toast.LENGTH_LONG).show();
                } else {
                    matchRounds1++;
                    legRounds1++;
                    okBtnPress++;

                    player1.setScore(player1.getScore() - score_helper);
                    score1.setText(String.valueOf(player1.getScore()));
                    player1.setLastScore(score_helper);

                    ///Kiszállózás
                    getPlayerCheckout(player1, score1, checkout1);

                    legAvg_help1 += score_helper;
                    matchAvg_help1 += score_helper;

                    player1.setLegAvg(legAvg_help1 / legRounds1);
                    player1.setMatchAvg(matchAvg_help1 / matchRounds1);

                    legAvg1.setText(String.valueOf(player1.getLegAvg()));
                    matchAvg1.setText(String.valueOf(player1.getMatchAvg()));

                    enterScore1.setText("");
                    enterScore2.requestFocus();
                }
            }

            //Legek végét figyelő kódrész.
            if (Integer.parseInt(String.valueOf(score1.getText())) == 0) {
                legWinnerDialog(player1).show();
                legRounds1 = 0;
                legAvg_help1 = 0;
                player1.setLegW(player1.getLegW() + 1);
                legsWon1.setText(String.valueOf(player1.getLegW()));
                score1.setText(String.valueOf(gameMode));
                player1.setScore(gameMode);
                score2.setText(String.valueOf(gameMode));
                player2.setScore(gameMode);
                checkout2.setText("");

                setLegStartPlayer(player1, player2);
            }


            if (player1.getLegW() == (legNumber / 2 + 1)) {
                matchWinnerDialog(player1).show();

                double p1Avg = round(player1.getMatchAvg(), 2);
                double p2Avg = round(player2.getMatchAvg(), 2);

                databaseAccess.open();
                databaseAccess.saveLocalGameStat(player1.getPlayerName(), player1.getLegW(), p1Avg,
                        player2.getPlayerName(), player2.getLegW(), p2Avg);
                databaseAccess.close();
            }

        } catch (Exception ex) {
            Toast.makeText(PlayGameActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("DARTS", "Exception:" + Log.getStackTraceString(ex));
        }
    }

    private void player2Round() {
        try {
            int score_helper;
            score_helper = Integer.parseInt(String.valueOf(enterScore2.getText()));

            if (score_helper > 180) {
                Toast.makeText(PlayGameActivity.this, "This is impossible with 3 darts!", Toast.LENGTH_LONG).show();
            } else {
                if (player2.getScore() - score_helper < 0) {
                    Toast.makeText(PlayGameActivity.this, "Not possible!", Toast.LENGTH_LONG).show();
                } else if (player2.getScore() - score_helper == 0 && player2.getScore() == 180) {
                    Toast.makeText(PlayGameActivity.this, "180 is not checkout!", Toast.LENGTH_LONG).show();
                } else {
                    matchRounds2++;
                    legRounds2++;
                    okBtnPress++;

                    player2.setScore(player2.getScore() - score_helper);
                    score2.setText(String.valueOf(player2.getScore()));
                    player2.setLastScore(score_helper);

                    ///Kiszállók
                    getPlayerCheckout(player2, score2, checkout2);

                    legAvg_help2 += score_helper;
                    matchAvg_help2 += score_helper;

                    player2.setLegAvg(legAvg_help2 / legRounds2);
                    player2.setMatchAvg(matchAvg_help2 / matchRounds2);

                    legAvg2.setText(String.valueOf(player2.getLegAvg()));
                    matchAvg2.setText(String.valueOf(player2.getMatchAvg()));

                    enterScore2.setText("");
                    enterScore1.requestFocus();
                }
            }

            //Legek végét figyelő kódrész.
            if (Integer.parseInt(String.valueOf(score2.getText())) == 0) {
                legWinnerDialog(player2).show();
                legRounds2 = 0;
                legAvg_help2 = 0;
                player2.setLegW(player2.getLegW() + 1);
                legsWon2.setText(String.valueOf(player2.getLegW()));
                score2.setText(String.valueOf(gameMode));
                player2.setScore(gameMode);

                score1.setText(String.valueOf(gameMode));
                player1.setScore(gameMode);
                checkout1.setText("");

                setLegStartPlayer(player1, player2);
            }

            if (player2.getLegW() == (legNumber / 2 + 1)) {
                matchWinnerDialog(player2).show();

                double p1Avg = round(player1.getMatchAvg(), 2);
                double p2Avg = round(player2.getMatchAvg(), 2);

                databaseAccess.open();
                databaseAccess.saveLocalGameStat(player1.getPlayerName(), player1.getLegW(), p1Avg,
                        player2.getPlayerName(), player2.getLegW(), p2Avg);
                databaseAccess.close();

            }
        } catch (Exception ex) {
            Toast.makeText(PlayGameActivity.this, "Score field is empty!", Toast.LENGTH_LONG).show();
        }
    }

    private void setLegStartPlayer(DartsPlayer p1, DartsPlayer p2) {
        if (p1.getLegStart()) {
            p1.setLegStart(false);
            p2.setLegStart(true);
            if ((okBtnPress % 2) == 0) {
                okBtnPress--;
            }
        } else if (p2.getLegStart()) {
            p2.setLegStart(false);
            p1.setLegStart(true);
            if ((okBtnPress % 2) == 1) {
                okBtnPress--;
            }
        }
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
                        matchRounds1--;
                        legRounds1--;
                        player1.setScore(player1.getScore() + player1.getLastScore());
                        score1.setText(String.valueOf(player1.getScore()));

                        legAvg_help1 -= player1.getLastScore();
                        matchAvg_help1 -= player1.getLastScore();
                        player1.setLegAvg(legAvg_help1 / legRounds1);
                        player1.setMatchAvg(matchAvg_help1 / matchRounds1);

                        legAvg1.setText(String.valueOf(player1.getLegAvg()));
                        matchAvg1.setText(String.valueOf(player1.getMatchAvg()));

                        enterScore1.setText("");
                        enterScore2.requestFocus();
                    } else if (okBtnPress % 2 == 1) {
                        matchRounds2--;
                        legRounds2--;
                        player2.setScore(player2.getScore() + player2.getLastScore());
                        score2.setText(String.valueOf(player2.getScore()));

                        legAvg_help2 -= player2.getLastScore();
                        matchAvg_help2 -= player2.getLastScore();

                        player2.setLegAvg(legAvg_help2 / legRounds2);
                        player2.setMatchAvg(matchAvg_help2 / matchRounds2);

                        legAvg2.setText(String.valueOf(player2.getLegAvg()));
                        matchAvg2.setText(String.valueOf(player2.getMatchAvg()));

                        enterScore2.setText("");
                        enterScore1.requestFocus();
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
    private AlertDialog matchWinnerDialog(DartsPlayer player) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayGameActivity.this);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onBackPressed();
            }
        });

        builder.setTitle("Match Winner");
        builder.setMessage(player.getPlayerName() + " is won the game!");

        return builder.create();
    }

    /**
     * Leg végén felugró értesítő dialógus.
     *
     * @return AlertDialog
     */
    private AlertDialog legWinnerDialog(DartsPlayer player) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayGameActivity.this);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        builder.setTitle("Leg winner");
        builder.setMessage(player.getPlayerName() + " is won the leg. Confirm?");

        return builder.create();
    }

    private double round(double val, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(val);
        return bd.setScale(places, RoundingMode.HALF_UP).doubleValue();

    }
}