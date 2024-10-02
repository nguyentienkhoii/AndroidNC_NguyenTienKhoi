package com.example.bai3_buoi2;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lvTransactions;
    ArrayList<Transaction> transactions;
    TransactionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvTransactions = findViewById(R.id.lvTransactions);

        transactions = new ArrayList<>();
        transactions.add(new Transaction("15-Jul", "Salary", "Income", "+$1,463.63"));
        transactions.add(new Transaction("18-Jul", "Market basket", "Party stuff", "-$36.46"));
        transactions.add(new Transaction("20-Jul", "Starbucks", "Coffee", "-$8.24"));
        transactions.add(new Transaction("20-Jul", "McDonald's", "Breakfast", "-$12.63"));
        transactions.add(new Transaction("28-Jul", "PG&E", "Utilities", "-$45.93"));

        adapter = new TransactionAdapter(this, transactions);
        lvTransactions.setAdapter(adapter);
    }
}
