package com.mit.mit;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class A_sujet_Map_set extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ImageButton btn_marker;
    private EditText tb_adresse;
    private Button btn_ok;
    private Marker point1;
    private Marker point2;

    private Context context;
    private C_Sujet sujet;
    private C_Participant part;
    private String sujetID;
    private String jourId;
    private String userID;
    private String adresse;
    private String adresse2;
    private boolean isTransport;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_sujet__map_set);
        setUpMapIfNeeded();


        context=this.getApplicationContext();
        tb_adresse = (EditText) findViewById(R.id.sujet_map_set_tb_adress);
        btn_marker = (ImageButton) findViewById(R.id.sujet_map_set_btn_search);
        btn_ok = (Button) findViewById(R.id.sujet_map_set_btn_OK);

        btn_marker.setOnClickListener(onSearchAdress);
        btn_ok.setOnClickListener(onClickbtnOk);

        //récupérations
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            String typeSujet;
            //get data
            this.userID = extras.getString("userID");
            this.sujetID= extras.getString("sujet");
            this.jourId= extras.getString("idEntry");
            typeSujet= extras.getString("type");

            if (typeSujet.equals("Transport"))
            {isTransport=true;}
            else
            {isTransport=false;}

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }



    //bouton ajouter participant
    View.OnClickListener onSearchAdress = new View.OnClickListener() {
        public void onClick(View v)
        {
            double latitude=0;
            double longitude=0;
            try {
                Geocoder geocoder = new Geocoder(context);
                List<Address> addresses;
                addresses = geocoder.getFromLocationName(tb_adresse.getText().toString(), 1);
                if (addresses.size() > 0) {
                    latitude = addresses.get(0).getLatitude();
                    longitude = addresses.get(0).getLongitude();
                }
            }
            catch (IOException ex)
            {
                System.out.println("Error : "+ ex.getMessage());
            }


            point1=mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title("Here")
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            if(isTransport)
            {
                point2=mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title("Here")
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
            }



        }
    };

    //btn valider
    View.OnClickListener onClickbtnOk = new View.OnClickListener() {
        public void onClick(View v)
        {
            LatLng position = point1.getPosition();
            adresse=position.latitude+";"+position.longitude;

            if (isTransport)
            {
                LatLng position2 = point2.getPosition();
                adresse2=position2.latitude+";"+position2.longitude;
            }


            Intent intent = new Intent(A_sujet_Map_set.this, A_jour_Preparation.class);
            intent.putExtra("idEntry", jourId);
            intent.putExtra("userID", userID);
            intent.putExtra("isFromMap", "oui");
            intent.putExtra("sujetID", sujetID);
            intent.putExtra("isTransport", isTransport);
            intent.putExtra("adresse", adresse);
            intent.putExtra("adresse2", adresse2);

            startActivity(intent);


        }
    };
}
