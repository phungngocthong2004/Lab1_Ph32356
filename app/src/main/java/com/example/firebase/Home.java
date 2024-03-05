package com.example.firebase;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
//
//import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.checkerframework.checker.units.qual.s;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {
    List<SinhVien> List;
    City_Adapter svAdapter;

    FirebaseFirestore db;
    FloatingActionButton floatadd;
    RecyclerView rcCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        floatadd = findViewById(R.id.floatadd);
        rcCity = findViewById(R.id.rccity);
        db = FirebaseFirestore.getInstance();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
             getdanhsach();



        floatadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
themCongViec();
            }
        });
    }

    void themCongViec() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.item_add, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        EditText edtname = view.findViewById(R.id.name);
//        EditText edconten = view.findViewById(R.id.ed_nd);
        EditText edmasv = view.findViewById(R.id.masv);
        EditText eddiem = view.findViewById(R.id.diem);
        ImageView imgchonanh=view.findViewById(R.id.imgchonanh);
        Button btnthemm = view.findViewById(R.id.btnthem);
        CollectionReference cities = db.collection("cities");
        btnthemm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setTitle("Thêm");
                builder.setMessage("Bạn có muốn thêm Sinh Viên không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String ten = edtname.getText().toString().trim();
                        String masv = edmasv.getText().toString().trim();
                       Double diem = Double.valueOf(eddiem.getText().toString().trim());

                        // Kiểm tra các trường thông tin có được nhập đầy đủ hay không
                        if (ten.isEmpty() || masv.isEmpty()) {
                            Toast.makeText(Home.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Tạo một đối tượng SinhVien mới
                        SinhVien sinhVien = new SinhVien(ten, masv, diem);

                        // Gửi đối tượng SinhVien lên server
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(APIservice.DOMAIN)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        APIservice apiService = retrofit.create(APIservice.class);
                        Call<List<SinhVien>> call = apiService.addSinhVien(sinhVien);
                        call.enqueue(new Callback<List<SinhVien>>() {
                            @Override
                            public void onResponse(Call<List<SinhVien>> call, Response<List<SinhVien>> response) {
                                if (response.isSuccessful()) {
                                    getdanhsach();
                                   Log.e("thanhcong","jdsbfdsgfdg");

                                } else {
                                    Log.e("Thatbai","aaaaaaaaaaaaaa");
                                    Log.e("sinhvien",sinhVien.toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<java.util.List<SinhVien>> call, Throwable t) {
                                        getdanhsach();

                                dialog.dismiss();
                            }


                        });
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }
    private  void getdanhsach(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIservice.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIservice apiService = retrofit.create(APIservice.class);

        Call<List<SinhVien>> call = apiService.getSinhVien();

        call.enqueue(new Callback<List<SinhVien>>() {
            @Override
            public void onResponse(Call<List<SinhVien>> call, Response<List<SinhVien>> response) {
                if (response.isSuccessful()) {
                    List = response.body();
                    svAdapter=new City_Adapter(Home.this,List);

                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(Home.this,LinearLayoutManager.VERTICAL,false);
                    rcCity.setLayoutManager(linearLayoutManager);
                    rcCity.setAdapter(svAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<SinhVien>> call, Throwable t) {
                Log.e("Main", t.getMessage());
            }
        });

    }
//
//    private void ghiDulieu() {
//        CollectionReference cities = db.collection("cities");
//
//        Map<String, Object> data1 = new HashMap<>();
//        data1.put("name", "San Francisco");
//        data1.put("state", "CA");
//        data1.put("country", "USA");
//        data1.put("capital", false);
//        data1.put("population", 860000);
//        data1.put("regions", Arrays.asList("west_coast", "norcal"));
//        cities.document("SF").set(data1);
//
//        Map<String, Object> data2 = new HashMap<>();
//        data2.put("name", "Los Angeles");
//        data2.put("state", "CA");
//        data2.put("country", "USA");
//        data2.put("capital", false);
//        data2.put("population", 3900000);
//        data2.put("regions", Arrays.asList("west_coast", "socal"));
//        cities.document("LA").set(data2);
//
//        Map<String, Object> data3 = new HashMap<>();
//        data3.put("name", "Washington D.C.");
//        data3.put("state", null);
//        data3.put("country", "USA");
//        data3.put("capital", true);
//        data3.put("population", 680000);
//        data3.put("regions", Arrays.asList("east_coast"));
//        cities.document("DC").set(data3);
//
//        Map<String, Object> data4 = new HashMap<>();
//        data4.put("name", "Tokyo");
//        data4.put("state", null);
//        data4.put("country", "Japan");
//        data4.put("capital", true);
//        data4.put("population", 9000000);
//        data4.put("regions", Arrays.asList("kanto", "honshu"));
//        cities.document("TOK").set(data4);
//
//        Map<String, Object> data5 = new HashMap<>();
//        data5.put("name", "Beijing");
//        data5.put("state", null);
//        data5.put("country", "China");
//        data5.put("capital", true);
//        data5.put("population", 21500000);
//        data5.put("regions", Arrays.asList("jingjinji", "hebei"));
//        cities.document("BJ").set(data5);
//
//        //cities.add();
//    }
//
//    String TAG = "HomeActivity";
//
//
//    private void docDulieu() {
//        CollectionReference cities = db.collection("cities");
//        cities.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
//                    List = new ArrayList<>();
//
//                    for (DocumentSnapshot document : documents) {
//                        String name = document.getString("name");
//
//                        String country = document.getString("country");
//
//                        int population = document.getLong("population").intValue();
//
////
////                        SinhVien city = new SinhVien(name, country, population);
////                        List.add(city);
//                    }
//
//
//                    cityAdapter = new City_Adapter(Home.this, List);
//                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Home.this, LinearLayoutManager.VERTICAL, false);
//                    rcCity.setLayoutManager(linearLayoutManager);
//                    rcCity.setAdapter(cityAdapter);
//                } else {
//                    Log.d(TAG, "Error getting documents: ", task.getException());
//                }
//            }
//        });
//    }
}