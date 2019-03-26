package home.lali.darts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;

    private FirebaseAuth firebaseAuth;

    private TextView userWelcomeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

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
        userWelcomeTv = findViewById(R.id.userWelcome_tv);

        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            userWelcomeTv.setText(getString(R.string.welcome_user, user.getDisplayName()));
        }

        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewGameActivity.class);
                intent.putExtra("ONLINE_GAME", false);

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    intent.putExtra("CURRENT_USER", user.getDisplayName());
                }

                startActivity(intent);
            }
        });

        newOnlineGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        Intent intent = new Intent(MainActivity.this, NewGameActivity.class);
                        intent.putExtra("ONLINE_GAME", true);
                        intent.putExtra("CURRENT_USER", user.getDisplayName());

                        startActivity(intent);
                    } else {
                        useSignIn().show();
                    }
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
                    noInternet(LiveMatchActivity.class).show();
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
                    noInternet(GlobalStatActivity.class).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_in_menu:
                if (isOnline()) {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                } else {
                    Toast.makeText(MainActivity.this, R.string.lost_internet_connection, Toast.LENGTH_LONG).show();
                }
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(MainActivity.this);
                Toast.makeText(MainActivity.this, R.string.signed_out, Toast.LENGTH_LONG).show();
                userWelcomeTv.setText("");
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, R.string.signed_in, Toast.LENGTH_LONG).show();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                userWelcomeTv.setText(getString(R.string.welcome_user, user.getDisplayName()));
            } else {
                Toast.makeText(MainActivity.this, R.string.sign_in_cancel, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.launcher)
                .setTitle(R.string.exit_text)
                .setMessage(R.string.exit_message)
                .setPositiveButton(R.string.resetYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.no_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
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
                .setTitle(R.string.no_internet)
                .setMessage(R.string.lost_internet_connection)
                .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isOnline()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                Intent intent = new Intent(MainActivity.this, NewGameActivity.class);
                                intent.putExtra("ONLINE_GAME", true);
                                intent.putExtra("CURRENT_USER", user.getDisplayName());

                                startActivity(intent);
                            } else {
                                useSignIn().show();
                            }
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

    private AlertDialog noInternet(final Class<?> cls) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setIcon(R.mipmap.offline_icon)
                .setTitle(R.string.no_internet)
                .setMessage(R.string.lost_internet_connection)
                .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isOnline()) {
                            Intent intent = new Intent(MainActivity.this, cls);
                            startActivity(intent);
                        } else {
                            noInternet(cls).show();
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

    private AlertDialog useSignIn() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle(R.string.sign_in)
                .setMessage(R.string.use_sign_in)
                .setNeutralButton(R.string.ok_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
}
