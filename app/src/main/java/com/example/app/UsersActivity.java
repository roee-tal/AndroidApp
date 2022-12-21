
package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

public class UsersActivity extends AppCompatActivity
{

    public static ArrayList<User> shapeList = new ArrayList<User>();

    private ListView listView;
    private Button nameAscButton, messages;
    private ArrayList<String> selectedFilters = new ArrayList<String>();
    private String currentSearchText = "";
    private SearchView searchView;
    Location[] allLocations = {Location.Center, Location.North, Location.South};

    private int white, darkGray, red;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

//        initSearchWidgets();
        initWidgets();
        setupData();
        setUpList();
        setUpOnclickListener();
//        hideFilter();
//        hideSort();
        initColors();
//        lookSelected(idAscButton);
//        lookSelected(allButton);
        selectedFilters.add("all");
    }

    private void initColors()
    {
        white = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        red = ContextCompat.getColor(getApplicationContext(), R.color.red);
        darkGray = ContextCompat.getColor(getApplicationContext(), R.color.darkerGray);
    }

    private void lookSelected(Button parsedButton)
    {
//        parsedButton.setTextColor(white);
//        parsedButton.setBackgroundColor(red);
    }

    private void lookUnSelected(Button parsedButton)
    {
        parsedButton.setTextColor(red);
        parsedButton.setBackgroundColor(darkGray);
    }

    private void initWidgets()
    {
////        sortButton = (Button) findViewById(R.id.sortButton);
//        filterButton = (Button) findViewById(R.id.filterButton);
//        filterView1 = (LinearLayout) findViewById(R.id.filterTabsLayout);
//        filterView2 = (LinearLayout) findViewById(R.id.filterTabsLayout2);
//        sortView = (LinearLayout) findViewById(R.id.sortTabsLayout2);
        nameAscButton  = (Button) findViewById(R.id.nameAsc);
        messages = (Button) findViewById(R.id.Messag);


    }
//
//    private void initSearchWidgets()
//    {
//        searchView = (SearchView) findViewById(R.id.shapeListSearchView);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s)
//            {
//                currentSearchText = s;
//                ArrayList<User> filteredShapes = new ArrayList<User>();
//
//                for(User site : shapeList)
//                {
//                    if(site.getEmail().toLowerCase().contains(s.toLowerCase()))
//                    {
//                        if(selectedFilters.contains("all"))
//                        {
//                            filteredShapes.add(site);
//                        }
//                        else
//                        {
//                            for(String filter: selectedFilters)
//                            {
//                                if (site.getEmail().toLowerCase().contains(filter))
//                                {
//                                    filteredShapes.add(site);
//                                }
//                            }
//                        }
//                    }
//                }
//                setAdapter(filteredShapes);
//
//                return false;
//            }
//        });
//    }

    private void setupData()
    {
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        FirebaseFirestore fstore;
        fstore = FirebaseFirestore.getInstance();
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        fstore.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                String email = document.getString("Email");
                                String id = document.getString("ID");
                                String user = document.getString("isUser");
                                String mes = document.getString("LettersNum");
                                User u = new User(email,id,user,mes);
                                shapeList.add(u);
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void setUpList()
    {
        listView = (ListView) findViewById(R.id.shapesListView);

        setAdapter(shapeList);
    }

    private void setUpOnclickListener()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                User selectShape = (User) (listView.getItemAtPosition(position));
                Intent showDetail = new Intent(getApplicationContext(), DetailUserActivity.class);
                showDetail.putExtra("id",selectShape.getId());
                startActivity(showDetail);
            }
        });
    }

    private void filterList(String status)
    {
        String location;
        if(status != null && selectedFilters.contains(status)) { // in case of second click on location
            selectedFilters.remove(status); //in this case remove location from the filter list
        }
        else if (status != null)
            selectedFilters.add(status);

        ArrayList<User> filteredShapes = new ArrayList<User>();

        for(User site : shapeList)
        {
            for(String filter: selectedFilters)
            {
                // filter for location name:
                location = isLocation(filter);
                if(!location.equals("NOTHING"))
                {
                    if(currentSearchText == "")
                    {
                        filteredShapes.add(site); //Todo: add here wheris.. query and delete 'for(Site site : shapeList)'
                    }
                    else
                    {
                        if(site.getEmail().toLowerCase().contains(currentSearchText.toLowerCase()))
                        {
                            filteredShapes.add(site); //Todo: add here wheris.. query
                        }
                    }
                }

//                // filter for other free text:            //Todo: verify that not need this 'else if':
//                else if(site.getName().toLowerCase().contains(filter))
//                {
//                    if(currentSearchText == "")
//                    {
//                        filteredShapes.add(site); //Todo: add here wheris.. query and delete 'for(Site site : shapeList)'
//                    }
//                    else
//                    {
//                        if(site.getName().toLowerCase().contains(currentSearchText.toLowerCase()))
//                        {
//                            filteredShapes.add(site); //Todo: add here wheris.. query
//                        }
//                    }
//                }
            }
        }

        setAdapter(filteredShapes);
    }

    private String isLocation(String filter) {
        for (Location l:allLocations) {
            if (filter.equals(l.name())) {
                Log.println(Log.DEBUG, "check1236", "Find Location - " + l.name().toLowerCase());
                return l.name();
            }
        }
        return "NOTHING";
    }

    public void nameASCTapped(View view)
    {
        Collections.sort(shapeList, User.nameAscending);
        checkForFilter();
//        unSelectAllSortButtons();
        lookSelected(nameAscButton);
    }

    public void messageTapped(View view)
    {
        Collections.sort(shapeList, User.mesAscending);
        checkForFilter();
//        unSelectAllSortButtons();
        lookSelected(messages);
    }

    private void checkForFilter()
    {
        if(selectedFilters.contains("all"))
        {
            if(currentSearchText.equals(""))
            {
                setAdapter(shapeList);
            }
            else
            {
                ArrayList<User> filteredShapes = new ArrayList<User>();
                for(User site : shapeList)
                {
                    if(site.getEmail().toLowerCase().contains(currentSearchText))
                    {
                        filteredShapes.add(site);
                    }
                }
                setAdapter(filteredShapes);
            }
        }
//        else
//        {
//            filterList(null);
//        }
    }

    private void setAdapter(ArrayList<User> shapeList)
    {
        UserAdapter adapter = new UserAdapter(getApplicationContext(), 0, shapeList);
        listView.setAdapter(adapter);
    }
}