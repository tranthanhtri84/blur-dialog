package com.tripleT;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tripleT.blur.dialog.BlurConfirmDialog;
import com.tripleT.blur.dialog.BlurMessageDialog;
import com.tripleT.blur.dialog.IConfirmBuilder;
import com.tripleT.blur.dialog.WheelLoadingDialog;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirm();
            }
        });
    }

    private void showConfirm() {
        BlurConfirmDialog.getInstance(this)
                .message("Are you sure that you want to delete?")
                .leftButton("No")
                .leftButtonColor(R.color.colorPrimary)
                .leftButton(new IConfirmBuilder.IButton() {
                    @Override
                    public void onClick() {

                    }
                })
                .rightButton("Yes")
                .rightButtonColor(R.color.colorAccent)
                .rightButton(new IConfirmBuilder.IButton() {
                    @Override
                    public void onClick() {
                        delete();
                    }
                })
                .show();
    }


    private void delete() {
        WheelLoadingDialog.getInstance(this)
                .message("Deleting...")
                .show();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingFinished();
                        }
                    });

                }
            }
        }).start();

    }

    private void loadingFinished() {
        WheelLoadingDialog.getInstance(MainActivity.this).dismiss();
        if ((TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) % 2) == 0) {
            BlurMessageDialog.getInstance(this)
                    .message("Deleted")
                    .messageColor(R.color.colorPrimary)
                    .show();
        }else{
            BlurMessageDialog.getInstance(this)
                    .message("Something went wrong!")
                    .error()
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
