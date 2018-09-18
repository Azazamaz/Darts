package home.lali.darts;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button newgameBtn;
    private Button exitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newgameBtn = findViewById(R.id.newGame_btn);
        exitBtn = findViewById(R.id.exit_btn);

        newgameBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, NewGameActivity.class);
                Bundle extras = new Bundle();
                extras.putString("EXTRA_MODE1", "501");
                extras.putString("EXTRA_MODE2", "301");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = exitDialog();
                dialog.setIcon(R.drawable.launcher);
                dialog.show();
            }
        });
    }

    private AlertDialog exitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });

        builder.setTitle("Exit");
        builder.setMessage("You will exit from the application. Are you sure?");

        AlertDialog dialog = builder.create();
        return  dialog;
    }
}
