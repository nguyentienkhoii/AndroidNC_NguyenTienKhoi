package com.example.bai3_buoi2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TransactionAdapter extends ArrayAdapter<Transaction> {

    private Context context;
    private List<Transaction> transactions;

    public TransactionAdapter(Context context, List<Transaction> transactions) {
        super(context, 0, transactions);
        this.context = context;
        this.transactions = transactions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Transaction transaction = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false);
        }

        TextView tvDate = convertView.findViewById(R.id.tvTransactionDate);
        TextView tvName = convertView.findViewById(R.id.tvTransactionName);
        TextView tvInfo = convertView.findViewById(R.id.tvTransactionInfo);
        TextView tvAmount = convertView.findViewById(R.id.tvTransactionAmount);

        tvDate.setText(transaction.getDate());
        tvName.setText(transaction.getName());
        tvInfo.setText(transaction.getInfo());
        tvAmount.setText(transaction.getAmount());

        if (transaction.getAmount().startsWith("-")) {
            tvAmount.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_dark));
        } else {
            tvAmount.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_dark));
        }


        return convertView;
    }
}

