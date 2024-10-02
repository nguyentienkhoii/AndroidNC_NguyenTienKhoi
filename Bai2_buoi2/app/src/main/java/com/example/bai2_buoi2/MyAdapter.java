package com.example.bai2_buoi2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<String> {
    private int layout;
    private ArrayList<String> name;  // Sử dụng ArrayList để có thể thêm và xóa
    private ArrayList<Integer> icon; // Lưu trữ icon tương ứng với mỗi tên

    public MyAdapter(Context c, int l, ArrayList<String> n, ArrayList<Integer> i) {
        super(c, l, n.toArray(new String[0])); // Chuyển ArrayList thành mảng
        layout = l;
        name = n;
        icon = i;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(layout, parent, false);

        TextView t = v.findViewById(R.id.textView1);
        t.setText(name.get(pos));

        ImageView iv = v.findViewById(R.id.image1);
        iv.setImageResource(icon.get(pos));

        // Nút Remove
        Button btnRemove = v.findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(view -> {
            name.remove(pos);
            icon.remove(pos);
            notifyDataSetChanged(); // Cập nhật lại Spinner
        });

        return v;
    }

    @Override
    public View getDropDownView(int p, View convertView, ViewGroup g) {
        return getView(p, convertView, g);
    }
}
