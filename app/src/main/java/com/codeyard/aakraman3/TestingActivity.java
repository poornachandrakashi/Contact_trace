package com.codeyard.aakraman3;

import android.os.Bundle;
import android.widget.TextView;

import com.codeyard.aakraman3.models.UserIDModel;
import com.codeyard.aakraman3.utils.BluetoothUtils;
import com.codeyard.aakraman3.utils.FileUtil;

import androidx.appcompat.app.AppCompatActivity;

public class TestingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

//        Read from db
//        Show userID
        TextView textView = findViewById(R.id.user_id);
        UserIDModel userIDModel = new UserIDModel(this);
        textView.setText(userIDModel.getUserId());
        TextView blName = findViewById(R.id.blname);
        blName.setText(BluetoothUtils.getBluetoothAdapter().getName());

        TextView logs = findViewById(R.id.logs);
        logs.setText(FileUtil.readFromFile(this));
    }
}
