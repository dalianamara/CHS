package cg.healthyapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewData extends AppCompatActivity {

    // creating variables for our array list,
    // dbhandler, adapter and recycler view.
    private ArrayList<DataModal> courseModalArrayList;
    private DBHandler dbHandler;
    private DataRVAdapter DataRVAdapter;
    private RecyclerView coursesRV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);

        // initializing our all variables.
        courseModalArrayList = new ArrayList<>();
        dbHandler = new DBHandler(ViewData.this);

        // getting our course array
        // list from db handler class.
        courseModalArrayList = dbHandler.readData("oxygen");

        // on below line passing our array lost to our adapter class.
        DataRVAdapter = new DataRVAdapter(courseModalArrayList, ViewData.this);
        coursesRV = findViewById(R.id.idRVCourses);

        // setting layout manager for our recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewData.this, RecyclerView.VERTICAL, false);
        coursesRV.setLayoutManager(linearLayoutManager);

        // setting our adapter to recycler view.
        coursesRV.setAdapter(DataRVAdapter);
    }
}

