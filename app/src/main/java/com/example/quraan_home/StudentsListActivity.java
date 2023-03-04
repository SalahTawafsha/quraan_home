package com.example.quraan_home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import entities.Student;

public class StudentsListActivity extends AppCompatActivity {
    private ListView list;
    private final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);

        list = findViewById(R.id.students);
        sharedPref = getSharedPreferences(
                getString(R.string.login)
                , Context.MODE_PRIVATE);
        loadStudents();

        list.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(this, StudentPageActivity.class);
            startActivity(intent);
        });
    }

    private void loadStudents() {
        database.child("student").orderByChild("teacherName").equalTo(sharedPref.getString("logInID", ""))
                .get().addOnCompleteListener(task -> {
                    List<String> students = new ArrayList<>();
                    for (DataSnapshot ds : task.getResult().getChildren())
                        students.add(Objects.requireNonNull(ds.getValue(Student.class)).getName());

                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, students);
                    list.setAdapter(adapter);

                });
    }
}