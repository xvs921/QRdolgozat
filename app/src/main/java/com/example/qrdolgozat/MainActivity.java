package com.example.qrdolgozat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button btnScan, btnKiir;
    private TextView textEredmeny;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("QR Code Scaning by app");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }

        });
        btnKiir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String allapot;
                File file;
                String szovegesAdat;
                Date date = Calendar.getInstance().getTime();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");

                String formatedDate = dateFormat.format(date);

                szovegesAdat = textEredmeny.getText().toString() + "," + formatedDate + "," + "\r\n";

                allapot = Environment.getExternalStorageState();
                if (allapot.equals(Environment.MEDIA_MOUNTED)) {
                    file = new File(Environment.getExternalStorageDirectory(), "scannedCodes.csv");
                    try {
                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true), 1024);
                        bufferedWriter.append(szovegesAdat);
                        bufferedWriter.close();
                    } catch (Exception e) {
                        System.out.println("Hiba: " + e);
                    }
                }
            }

        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Kiléptél a scannelésből!", Toast.LENGTH_SHORT).show();
            } else {
                textEredmeny.setText(" " + result.getContents());
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
        btnScan = findViewById(R.id.btnScan);
        btnKiir = findViewById(R.id.btnKiir);
        textEredmeny = findViewById(R.id.textEredmeny);
    }
}
