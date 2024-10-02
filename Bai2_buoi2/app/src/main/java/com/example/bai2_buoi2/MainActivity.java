package com.example.bai2_buoi2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> countryNames;
    private ArrayList<Integer> countryFlags;
    private MyAdapter adapter;
    private Spinner spinnerCountries;
    private int selectedFlagIndex = 0; // Index cho lá cờ đã chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Dữ liệu cho Spinner
        countryNames = new ArrayList<>();
        countryFlags = new ArrayList<>();

        // Thêm các quốc gia mặc định
        addDefaultCountries();

        // Khởi tạo Spinner
        spinnerCountries = findViewById(R.id.spinner_countries);
        adapter = new MyAdapter(this, R.layout.spinner_item, countryNames, countryFlags);
        spinnerCountries.setAdapter(adapter);

        // Thêm nút để mở Dialog
        Button btnAddCountry = findViewById(R.id.btnAddCountry);
        btnAddCountry.setOnClickListener(v -> showAddCountryDialog());
    }

    private void addDefaultCountries() {
        countryNames.add("Vietnam");
        countryNames.add("United States");
        countryNames.add("Japan");
        countryNames.add("Germany");
        countryNames.add("France");

        countryFlags.add(R.drawable.vietnam_flag);
        countryFlags.add(R.drawable.usa_flag);
        countryFlags.add(R.drawable.japan_flag);
        countryFlags.add(R.drawable.germany_flag);
        countryFlags.add(R.drawable.france_flag);
    }

    private void showAddCountryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Country");

        // Layout cho Dialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText countryNameInput = new EditText(this);
        countryNameInput.setHint("Country Name");
        layout.addView(countryNameInput);

        // Hiển thị danh sách 5 lá cờ có sẵn
        String[] flagNames = {"Vietnam", "USA", "Japan", "Germany", "France"};
        int[] flagResources = {
                R.drawable.vietnam_flag,
                R.drawable.usa_flag,
                R.drawable.japan_flag,
                R.drawable.germany_flag,
                R.drawable.france_flag
        };

        ArrayAdapter<String> flagAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, flagNames);
        Spinner flagSpinner = new Spinner(this);
        flagSpinner.setAdapter(flagAdapter);
        layout.addView(flagSpinner);

        // Cập nhật khi chọn cờ
        flagSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                selectedFlagIndex = position;
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Không làm gì cả
            }
        });

        builder.setView(layout);
        builder.setPositiveButton("Add", (dialog, which) -> {
            String countryName = countryNameInput.getText().toString();
            if (!countryName.isEmpty()) {
                countryNames.add(countryName);
                // Sử dụng lá cờ đã chọn
                countryFlags.add(flagResources[selectedFlagIndex]);
                adapter.notifyDataSetChanged(); // Cập nhật Spinner
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}
