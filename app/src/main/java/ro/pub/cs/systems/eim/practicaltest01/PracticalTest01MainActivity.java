package ro.pub.cs.systems.eim.practicaltest01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PracticalTest01MainActivity extends AppCompatActivity {


    EditText edit1Text;
    EditText edit2Text;
    Button button1;
    Button button2;
    Button button3;
    Button navigateToSecondaryActivityButton;
    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private int serviceStatus = Constants.SERVICE_STOPPED;
    private IntentFilter intentFilter = new IntentFilter();

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(Constants.BROADCAST_RECEIVER_TAG, intent.getStringExtra(Constants.BROADCAST_RECEIVER_EXTRA));
        }
    }

    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int leftNumberOfClicks1 = Integer.parseInt(edit2Text.getText().toString());
            int rightNumberOfClicks1 = Integer.parseInt(edit1Text.getText().toString());
            switch(view.getId()) {
                case R.id.number_1_button:
                    int leftNumberOfClicks = Integer.parseInt(edit2Text.getText().toString());
                    leftNumberOfClicks++;
                    edit2Text.setText(String.valueOf(leftNumberOfClicks));
                    break;
                case R.id.number_2_button:
                    int rightNumberOfClicks = Integer.parseInt(edit1Text.getText().toString());
                    rightNumberOfClicks++;
                    edit1Text.setText(String.valueOf(rightNumberOfClicks));
                    break;
                case R.id.navigate_to_secondary_activity_button:
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01SecondActivity.class);
                    int numberOfClicks = Integer.parseInt(edit2Text.getText().toString()) +
                            Integer.parseInt(edit1Text.getText().toString());
                    intent.putExtra("numberOfClicks", numberOfClicks);
                    startActivityForResult(intent, 1);
                    break;
            }

            if (leftNumberOfClicks1 + rightNumberOfClicks1 > Constants.NUMBER_OF_CLICKS_THRESHOLD
                    && serviceStatus == Constants.SERVICE_STOPPED) {
                Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                intent.putExtra("firstNumber", leftNumberOfClicks1);
                intent.putExtra("secondNumber", rightNumberOfClicks1);
                getApplicationContext().startService(intent);
                serviceStatus = Constants.SERVICE_STARTED;
            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);

        edit1Text = (EditText)findViewById(R.id.edit1_text);
        edit2Text = (EditText)findViewById(R.id.edit2_text);
        button1 = (Button)findViewById(R.id.navigate_to_secondary_activity_button);
        button2 = (Button)findViewById(R.id.number_1_button);
        button2.setOnClickListener(buttonClickListener);
        button3 = (Button)findViewById(R.id.number_2_button);
        button3.setOnClickListener(buttonClickListener);

        navigateToSecondaryActivityButton = (Button)findViewById(R.id.navigate_to_secondary_activity_button);
        navigateToSecondaryActivityButton.setOnClickListener(buttonClickListener);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("edit1Text")) {
                edit1Text.setText(savedInstanceState.getString("edit1Text"));
            }
            if (savedInstanceState.containsKey("edit2test")) {
                edit1Text.setText(savedInstanceState.getString("edit1Text"));
            }

        }

        for (int index = 0; index < Constants.actionTypes.length; index++) {
            intentFilter.addAction(Constants.actionTypes[index]);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("edit2Text", edit2Text.getText().toString());
        savedInstanceState.putString("edit1Text", edit1Text.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("edit1Text")) {
            edit1Text.setText(savedInstanceState.getString("edit1Text"));
        } else {
            edit1Text.setText(String.valueOf(0));
        }
        if (savedInstanceState.containsKey("edit2Text")) {
            edit2Text.setText(savedInstanceState.getString("edit2Text"));
        } else {
            edit2Text.setText(String.valueOf(0));
        }
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PracticalTest01Service.class);
        stopService(intent);
        super.onDestroy();
    }

}
