package com.mit.mit;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class A_sujet_Map_set extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ImageButton btn_marker;
    private EditText tb_adresse;
    private Button btn_ok;
    private LinearLayout ll_adresses;
    EditText tb_addr;

    private Marker point1;
    private Marker point2;

    private Context context;
    private DAO_Sujet daoSujet;
    private DAO_Options daoOptions;
    private DAO_Jour daoJour;

    private C_Options options;
    private C_Sujet sujet;
    private C_Jour jour;

    //private String adresse;
    private String destination;

    private LatLng coord;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_sujet__map_set);
        setUpMapIfNeeded();

        context=this.getApplicationContext();
        daoSujet = new DAO_Sujet(context);
        daoOptions = new DAO_Options(context);
        daoJour = new DAO_Jour(context);

        tb_adresse = (EditText) findViewById(R.id.sujet_map_set_tb_adress);
        btn_marker = (ImageButton) findViewById(R.id.sujet_map_set_btn_search);
        btn_ok = (Button) findViewById(R.id.sujet_map_set_btn_OK);
        ll_adresses= (LinearLayout) findViewById(R.id.sujet_map_ll_adreses);

        btn_marker.setOnClickListener(onSearchAdress);
        btn_ok.setOnClickListener(onClickbtnOk);

        //récupérations
        this.options=daoOptions.getOptionByUserId();
        this.sujet=daoSujet.getSujetById(options.sujetid);
        this.jour=daoJour.getJourById(options.jourid);
        this.jour.creerLesListes(daoSujet);

        System.out.println("****"+sujet.idSujet);


        //ajout champs if transport
        if(sujet.type.equals("Transport"))
        {
            LinearLayout llAdd= new LinearLayout(this);
            llAdd.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            llAdd.setLayoutParams(LLParams);

            TextView lb_titre = new TextView(this);
            tb_addr = new EditText(this);
            lb_titre.setText("Destination : ");

            llAdd.addView(lb_titre);
            llAdd.addView(tb_addr);

            ll_adresses.addView(llAdd);
        }

        initialiserMap();



    }

    private void initialiserMap()
    {
        double lati=0;
        double longi=0;

        try
        {
            //if internet access
            if(options.online)
            {
                Geocoder geocoder = new Geocoder(context);
                List<Address> addresses;
                addresses = geocoder.getFromLocationName(this.jour.ville, 1);
                if (addresses.size() > 0) {
                    lati = addresses.get(0).getLatitude();
                    longi = addresses.get(0).getLongitude();
                }
                this.coord=new LatLng(lati, longi);
            }

            //offline
            else
            {
                //get approximate coord
                this.coord=getApproximateCoord();
            }




            moveToCurrentLocation(coord);

        }
        catch (IOException ex)
        {
            System.out.println("Error : "+ ex.getMessage());
        }
    }


    //position marker on the map from an adress
    private void setMarkerToAdress(String adresse)
    {
        double lati=0;
        double longi=0;

        try
        {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses;
            addresses = geocoder.getFromLocationName(adresse, 1);
            if (addresses.size() > 0) {
                lati = addresses.get(0).getLatitude();
                longi = addresses.get(0).getLongitude();
            }
            this.coord=new LatLng(lati, longi);

            point1=mMap.addMarker(new MarkerOptions()
                    .position(coord)
                    .title(adresse)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            moveToCurrentLocation(coord);

            if(sujet.type.equals("Transport"))
            {
                addresses = geocoder.getFromLocationName(this.destination, 1);
                if (addresses.size() > 0) {
                    lati = addresses.get(0).getLatitude();
                    longi = addresses.get(0).getLongitude();
                }
                this.coord=new LatLng(lati, longi);

                point2=mMap.addMarker(new MarkerOptions()
                        .position(coord)
                        .title(adresse)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }

        }
        catch (IOException ex)
        {
            System.out.println("Error : "+ ex.getMessage());
        }
    }


    //move camera to position
    private void moveToCurrentLocation(LatLng currentLocation)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }


    //calculate an approximative position from all other subjets in the current day
    private LatLng getApproximateCoord()
    {
        LatLng theCoord;
        double longi=0;
        double lati=0;

        //TODO get aproxi coord for offline map
        /*for(C_Sujet s: jour.liste_sujets)
        {

        }*/



        return new LatLng(lati, longi);
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



    //bouton ajouter adresse
    View.OnClickListener onSearchAdress = new View.OnClickListener() {
        public void onClick(View v)
        {

        if(!tb_adresse.getText().toString().equals(""))
        {
            try {
                point1.remove();
                point2.remove();
            }
            catch (Exception e)
            {

            }

            if(sujet.type.equals("Transport"))
            {
                destination=tb_addr.getText().toString();
            }
            setMarkerToAdress(tb_adresse.getText().toString());
        }


        }
    };

    //btn valider
    View.OnClickListener onClickbtnOk = new View.OnClickListener() {
        public void onClick(View v)
        {
            if(sujet.type.equals("Transport"))
            {
                LatLng position2 = point2.getPosition();
                sujet.localisation2=position2.latitude+";"+position2.longitude;
            }

            LatLng position = point1.getPosition();
            sujet.localisation=position.latitude+";"+position.longitude;
            daoSujet.modifier(sujet, options.online);

            Intent intent = new Intent(A_sujet_Map_set.this, A_jour_Preparation.class);
            startActivity(intent);


        }
    };
}



