package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity
{
    private StorageReference myStorage;

    public static ArrayList<Site> shapeList = new ArrayList<Site>();

    private ListView listView;
    private Button sortButton;
    private Button filterButton;
    private LinearLayout filterView1;
    private LinearLayout filterView2;
    private LinearLayout sortView;

    boolean sortHidden = true;
    boolean filterHidden = true;
    Location[] allLocations = {Location.Center, Location.North, Location.South}; //Location.All,
    boolean centerSelected = false;
    boolean southSelected = false;
    boolean northSelected = false;

    private Button southButton, centetButton, northButton, allButton,contact;
    private Button down2upRateButton, up2downRateButton, nameAscButton, nameDescButton;

    private ArrayList<String> selectedFilters = new ArrayList<String>();
    private String currentSearchText = "";
    private SearchView searchView;

    private int white, darkGray, red;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myStorage = FirebaseStorage.getInstance().getReference();
        try {
            final File localTempFile = File.createTempFile("shvil", "jpg");
            myStorage.child("picture/shvil.jpg").getFile(localTempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Log.d("firebaseFailed", "in!");
//                            Log.d("firebaseFailed", localTempFile.getName());

                            Toast.makeText(MainActivity.this, "Picture Retrieved",Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(localTempFile.getAbsolutePath());
                            ((ImageView) findViewById(R.id.mainImage)).setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error During Picture Retrieved",Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (IOException e){
            e.printStackTrace();
        }

        contact = findViewById(R.id.contact);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ContactActivity.class));
                finish();
            }
        });

        initSearchWidgets();
        initWidgets();
        setupData();
        setUpList();
        setUpOnclickListener();
        hideFilter();
        hideSort();
        initColors();
        unSelectAllSortButtons();
        lookSelected(up2downRateButton);
        lookSelected(allButton);
        unSelectAllFilterButtons();
        selectedFilters.add("all");
    }

    private void initColors()
    {
        white = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        red = ContextCompat.getColor(getApplicationContext(), R.color.red);
        darkGray = ContextCompat.getColor(getApplicationContext(), R.color.darkerGray);
    }

    private void unSelectAllSortButtons()
    {
        lookUnSelected(down2upRateButton);
        lookUnSelected(up2downRateButton);
        lookUnSelected(nameAscButton);
        lookUnSelected(nameDescButton);
    }

    private void unSelectAllFilterButtons()
    {
        lookUnSelected(allButton);
        lookUnSelected(northButton);
        lookUnSelected(centetButton);
        lookUnSelected(southButton);
    }

    private void lookSelected(Button parsedButton)
    {
        parsedButton.setTextColor(white);
        parsedButton.setBackgroundColor(red);
    }

    private void lookUnSelected(Button parsedButton)
    {
        parsedButton.setTextColor(red);
        parsedButton.setBackgroundColor(darkGray);
    }

    private void initWidgets()
    {
        sortButton = (Button) findViewById(R.id.sortButton);
        filterButton = (Button) findViewById(R.id.filterButton);
        filterView1 = (LinearLayout) findViewById(R.id.filterTabsLayout);
        filterView2 = (LinearLayout) findViewById(R.id.filterTabsLayout2);
        sortView = (LinearLayout) findViewById(R.id.sortTabsLayout2);

        southButton = (Button) findViewById(R.id.southFilter);
        centetButton = (Button) findViewById(R.id.centerFilter);
        northButton = (Button) findViewById(R.id.northFilter);
        allButton  = (Button) findViewById(R.id.allFilter);

        down2upRateButton = (Button) findViewById(R.id.down2upRate);
        up2downRateButton = (Button) findViewById(R.id.up2downRate);
        nameAscButton  = (Button) findViewById(R.id.nameAsc);
        nameDescButton  = (Button) findViewById(R.id.nameDesc);
    }

    private void initSearchWidgets()
    {
        searchView = (SearchView) findViewById(R.id.shapeListSearchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
                currentSearchText = s;
                ArrayList<Site> filteredShapes = new ArrayList<Site>();

                for(Site site : shapeList)
                {
                    if(site.getName().toLowerCase().contains(s.toLowerCase()))
                    {
                        if(selectedFilters.contains("all"))
                        {
                            filteredShapes.add(site);
                        }
                        else
                        {
                            for(String filter: selectedFilters)
                            {
                                if (site.getName().toLowerCase().contains(filter))
                                {
                                    filteredShapes.add(site);
                                }
                            }
                        }
                    }
                }
                setAdapter(filteredShapes);

                return false;
            }
        });
    }

    private void setupData()
    {
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Site circle = new Site("11", "Jerusalem Forest", "picture/shvil.jpg", 9, "bla lba", Location.Center);
        shapeList.add(circle);

//        final String[] ans = new String[1];
//        mDatabase.child("site").child("1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("firebase", "Error getting data", task.getException());
//                }
//                else {
//                    ans[0] = String.valueOf(task.getResult().getValue());
//                    Toast.makeText(MainActivity.this, ans[0], Toast.LENGTH_LONG).show();
//
//                    Log.d("firebase", "ans[0]");
//                    Log.d("firebase", ans[0]);
//                }
//            }
//        });

        Site triangle = new Site("1","Tel Aviv beach", "picture/shvil.jpg", 0, "bla lba", Location.Center);
        shapeList.add(triangle);

        Site square = new Site("2","Herzliya beach", "picture/shvil.jpg", 3, "bla lba", Location.South);
        shapeList.add(square);

        Site rectangle = new Site("3","Rectangle", "picture/shvil.jpg", 1, "bla lba", Location.South);
        shapeList.add(rectangle);

        Site octagon = new Site("4","Octagon", "picture/shvil.jpg", 7.6, "bla lba", Location.North);
        shapeList.add(octagon);

        Site circle2 = new Site("5", "Circle 2", "picture/shvil.jpg", 1.2, "bla lba", Location.North);
        shapeList.add(circle2);

        Site triangle2 = new Site("6","Triangle 2", "picture/shvil.jpg", 9, "bla lba", Location.Center);
        shapeList.add(triangle2);

        Site square2 = new Site("7","Square 2", "picture/shvil.jpg", 9, "bla lba", Location.Center);
        shapeList.add(square2);

        Site rectangle2 = new Site("8","Rectangle 2", "picture/shvil.jpg", 9, "bla lba", Location.Center);
        shapeList.add(rectangle2);

        Site octagon2 = new Site("9","Octagon 2", "picture/shvil.jpg", 9, "bla lba", Location.Center);
        shapeList.add(octagon2);
//        //--------------------------------------------
//        // push all the objects to firebase:
//        for (Site site: shapeList) {
//            mDatabase.child("site").push().setValue(site);
//        }
//        //--------------------------------------------
        // this will hold our collection of all Site's.
        final ArrayList<Site> siteList = new ArrayList<Site>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        databaseReference.child("site").addValueEventListener(new ValueEventListener() {
            /**
             * This method will be invoked any time the data on the database changes.
             * Additionally, it will be invoked as soon as we connect the listener, so that we can get an initial snapshot of the data on the database.
             * @param dataSnapshot
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get all of the children at this level.
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                // shake hands with each of them.'
                for (DataSnapshot child : children) {
                    Site s = child.getValue(Site.class);
                    siteList.add(s);
                    assert s != null;
                }
                //        shapeList.clear(); //only for self check of data loading
                for (Site site:siteList) {
//            shapeList.add(site);
                    Log.d("firebase0129", site.getRate() +"---"+ site.getName());
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }});
        //--------------------------------------------
////        shapeList.clear(); //only for self check of data loading
//        for (Site site:siteList) {
////            shapeList.add(site);
//            Log.d("firebase0129", site.getRate() +"---"+ site.getName());
//        }

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
                Site selectShape = (Site) (listView.getItemAtPosition(position));
                Intent showDetail = new Intent(getApplicationContext(), DetailActivity.class);
                showDetail.putExtra("id",selectShape.getId());
                showDetail.putExtra("name",selectShape.getName());
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

        ArrayList<Site> filteredShapes = new ArrayList<Site>();

        for(Site site : shapeList)
        {
            for(String filter: selectedFilters)
            {
                // filter for location name:
                location = isLocation(filter);
                if(!location.equals("NOTHING") && site.getLocation().name().equals(location))
                {
                    if(currentSearchText == "")
                    {
                        filteredShapes.add(site); //Todo: add here wheris.. query and delete 'for(Site site : shapeList)'
                    }
                    else
                    {
                        if(site.getName().toLowerCase().contains(currentSearchText.toLowerCase()))
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

    public void allFilterTapped(View view)
    {
        selectedFilters.clear();
        selectedFilters.add("all");

        unSelectAllFilterButtons();
        lookSelected(allButton);

        setAdapter(shapeList);
    }

    public void centerFilterTapped(View view)
    {
        filterList("Center");
        if (!centerSelected) {
            lookSelected(centetButton);
            lookUnSelected(allButton);
        }
        else {
            lookUnSelected(centetButton);
        }
        centerSelected = !centerSelected;
    }

    public void southFilterTapped(View view)
    {
        filterList("South");
        if (!southSelected) {
            lookSelected(southButton);
            lookUnSelected(allButton);
        }
        else {
            lookUnSelected(southButton);
        }
        southSelected = !southSelected;
    }

    public void northFilterTapped(View view)
    {
        filterList("North");
        if (!northSelected) {
            lookSelected(northButton);
            lookUnSelected(allButton);
        }
        else {
            lookUnSelected(northButton);
        }
        northSelected = !northSelected;
    }

    public void showFilterTapped(View view)
    {
        if(filterHidden == true)
        {
            filterHidden = false;
            showFilter();
        }
        else
        {
            filterHidden = true;
            hideFilter();
        }
    }

    public void showSortTapped(View view)
    {
        if(sortHidden == true)
        {
            sortHidden = false;
            showSort();
        }
        else
        {
            sortHidden = true;
            hideSort();
        }
    }

    private void hideFilter()
    {
        searchView.setVisibility(View.GONE);
        filterView1.setVisibility(View.GONE);
        filterView2.setVisibility(View.GONE);
        filterButton.setText("FILTER");
    }

    private void showFilter()
    {
        searchView.setVisibility(View.VISIBLE);
        filterView1.setVisibility(View.VISIBLE);
        filterView2.setVisibility(View.VISIBLE);
        filterButton.setText("HIDE");
    }

    private void hideSort()
    {
        sortView.setVisibility(View.GONE);
        sortButton.setText("SORT");
    }

    private void showSort()
    {
        sortView.setVisibility(View.VISIBLE);
        sortButton.setText("HIDE");
    }

    public void down2upRateApped(View view)
    {
        Collections.sort(shapeList, Site.rateSort);
        checkForFilter();
        unSelectAllSortButtons();
        lookSelected(down2upRateButton);
    }

    public void up2downRateApped(View view)
    {
        Collections.sort(shapeList, Site.rateSort);
        Collections.reverse(shapeList);
        checkForFilter();
        unSelectAllSortButtons();
        lookSelected(up2downRateButton);
    }

    public void nameASCTapped(View view)
    {
        Collections.sort(shapeList, Site.nameAscending);
        checkForFilter();
        unSelectAllSortButtons();
        lookSelected(nameAscButton);
    }

    public void nameDESCTapped(View view)
    {
        Collections.sort(shapeList, Site.nameAscending);
        Collections.reverse(shapeList);
        checkForFilter();
        unSelectAllSortButtons();
        lookSelected(nameDescButton);
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
                ArrayList<Site> filteredShapes = new ArrayList<Site>();
                for(Site site : shapeList)
                {
                    if(site.getName().toLowerCase().contains(currentSearchText))
                    {
                        filteredShapes.add(site);
                    }
                }
                setAdapter(filteredShapes);
            }
        }
        else
        {
            filterList(null);
        }
    }

    private void setAdapter(ArrayList<Site> shapeList)
    {
        ShapeAdapter adapter = new ShapeAdapter(getApplicationContext(), 0, shapeList);
        listView.setAdapter(adapter);
    }
}