package com.example.spaceconcordia.spacecadets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/// Activity allows user to access launch and emergency stop buttons directly, and access to screenselectdialog
public class MainActivity extends AppCompatActivity {

    private Button launchButton;
    private Button emergencyStopButton;
    private MenuItem screenSelectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();

        launchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO what happens when the launch button is clicked
            }
        });

        emergencyStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO what happens when the emergency stop button is clicked
            }
        });

    }

    /// OnClickListener for the "Display Mode" button in the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        screenSelectButton = menu.findItem(R.id.screenSelectActionButton);

        screenSelectButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                Intent intent = new Intent(MainActivity.this, ScreenSelectDialog.class);
//                startActivity(intent);

                ScreenSelectDialog dialog = new ScreenSelectDialog();
                dialog.show(getSupportFragmentManager(), "Select Display Mode");
                return false;
            }
        });

        return true;
    }

    protected void setupUI(){
        this.launchButton = findViewById(R.id.launchButton);
        this.emergencyStopButton = findViewById(R.id.emergencyStopButton);
    }

}
