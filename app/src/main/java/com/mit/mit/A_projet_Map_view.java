package com.mit.mit;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class A_projet_Map_view extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private Context context;
    private DAO_Jour daoJour;
    private DAO_Projet daoProjet;
    private DAO_Participant daoPart;
    private DAO_Options daoOptions;


    private C_Options options;
    private C_Projet projet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_projet__map_view);
        setUpMapIfNeeded();

        context=this.getApplicationContext();
        daoJour = new DAO_Jour(context);
        daoOptions = new DAO_Options(context);
        daoProjet = new DAO_Projet(context);
        daoPart = new DAO_Participant(context);

        //récupérations
        this.options=daoOptions.getOptionByUserId();
        this.projet=daoProjet.getProjetByName(options.projetid);
        this.projet.creerLesListes(daoJour,daoPart);
        //this.jour=daoJour.getJourById(options.jourid);
        //this.jour.creerLesListes(daoSujet);

        try {
            initialiserMap();
        }
        catch (Exception ex)
        {
            System.out.println("[ERROR] A_jour_Map_view : "+ex.getMessage());
        }
    }


    protected void initialiserMap()
    {
        double lati=0;
        double longi=0;
        LatLng coordMap= new LatLng(0, 0);
        LatLng prevCoord= new LatLng(0, 0);
        Marker point;
        SimpleDateFormat sdf= new SimpleDateFormat("EEEE", Locale.FRANCE);
        int parcours=0;

        try
        {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses;

            for (C_Jour j : this.projet.liste_jours)
            {
                try {
                    addresses = geocoder.getFromLocationName(j.ville, 1);
                    if (addresses.size() > 0) {
                        lati = addresses.get(0).getLatitude();
                        longi = addresses.get(0).getLongitude();
                    }
                    coordMap = new LatLng(lati, longi);
                }
                catch(Exception ex)
                {
                    System.out.println("[ERROR]A_projet_Map_view : "+ex.getMessage());
                }


                point=mMap.addMarker(new MarkerOptions()
                        .position(coordMap)
                        .title(sdf.format(j.jour))
                        .draggable(false)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));



                if(parcours!=0)
                {
                    Polyline line = mMap.addPolyline(new PolylineOptions()
                            .add(prevCoord, coordMap)
                            .width(10)
                            .color(Color.CYAN));
                }

                prevCoord=coordMap;
                parcours++;
            }


            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordMap, 5));
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            mMap.animateCamera(CameraUpdateFactory.zoomTo(5), 2000, null);





        }
        catch (Exception exx)
        {System.out.println("Error : "+ exx.getMessage());}
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
}
