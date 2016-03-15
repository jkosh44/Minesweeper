package hu.ait.android.minesweeper;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import hu.ait.android.minesweeper.model.MinesweeperModel;
import hu.ait.android.minesweeper.view.MinesweeperView;

import static android.support.design.widget.Snackbar.*;

public class MainActivity extends AppCompatActivity {

    private LinearLayout layoutContent;
    private MinesweeperView minesweeperView;
    ToggleButton btnFlags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutContent = (LinearLayout) findViewById(R.id.layoutContent);

        minesweeperView =
                (MinesweeperView) findViewById(R.id.gameView);

        minesweeperView.clearGameArea();

        Button btnRestart = (Button) findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(
                new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        btnFlags.setChecked(false);
                        minesweeperView.clearGameArea();
                        showSnackBarMessage(getString(R.string.restart));
                    }
                }
        );

        btnFlags = (ToggleButton) findViewById(R.id.btnFlags);
        btnFlags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MinesweeperModel.getInstance().toggleFlags();
            }
        });

    }


    public void showSnackBarMessage(String msg) {
        Snackbar.make(layoutContent, msg, Snackbar.LENGTH_LONG).show();
    }

    public void showSnackBarWithDelete(String msg) {
        Snackbar.make(layoutContent, msg, Snackbar.LENGTH_LONG).setAction(
                "Restart", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Restart the game
                        btnFlags.setChecked(false);
                        minesweeperView.clearGameArea();
                    }
                }
        ).show();
    }


}