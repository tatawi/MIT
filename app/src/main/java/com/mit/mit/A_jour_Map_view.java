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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class A_jour_Map_view extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private List<Marker> list_pts;

    private Context context;
    private DAO_Jour daoJour;
    private DAO_Sujet daoSujet;
    private DAO_Options daoOptions;


    private C_Options options;
    private C_Jour jour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_jour__map_view);
        setUpMapIfNeeded();

        list_pts= new ArrayList<Marker>();

        context=this.getApplicationContext();
        daoJour = new DAO_Jour(context);
        daoOptions = new DAO_Options(context);
        daoSujet = new DAO_Sujet(context);

        //récupérations
        this.options=daoOptions.getOptionByUserId();
        this.jour=daoJour.getJourById(options.jourid);
        this.jour.creerLesListes(daoSujet);

        try {
            initialiserMap(jour.ville);
            afficherPositions();
        }
        catch (Exception ex)
        {
            System.out.println("[ERROR] A_jour_Map_view : "+ex.getMessage());
        }


    }


    private void afficherPositions()
    {
        for (C_Sujet s: this.jour.liste_sujets)
        {
            String lati = s.localisation.split(";")[0];
            String longi = s.localisation.split(";")[1];
            LatLng coord = new LatLng(Double.parseDouble(lati), Double.parseDouble(longi));
            Marker point;


            switch (s.type) {
                case "Transport":
                    point=mMap.addMarker(new MarkerOptions()
                            .position(coord)
                            .title("["+s.type+"] "+s.titre)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    break;

                case "Repas":
                    point=mMap.addMarker(new MarkerOptions()
                            .position(coord)
                            .title("["+s.type+"] "+s.titre)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    break;

                case "Visite":
                    point=mMap.addMarker(new MarkerOptions()
                            .position(coord)
                            .title("["+s.type+"] "+s.titre)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    break;

                case "Logement":
                    point=mMap.addMarker(new MarkerOptions()
                            .position(coord)
                            .title("["+s.type+"] "+s.titre)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    break;

                case "Loisir":
                    point=mMap.addMarker(new MarkerOptions()
                            .position(coord)
                            .title("["+s.type+"] "+s.titre)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    break;

                case "Libre":
                    point=mMap.addMarker(new MarkerOptions()
                            .position(coord)
                            .title("["+s.type+"] "+s.titre)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                    break;
            }






        }
    }

    protected void initialiserMap(String addr)
    {
        double lati=0;
        double longi=0;
        LatLng coordMap;

        try
        {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses;
            addresses = geocoder.getFromLocationName(addr, 1);

            if (addresses.size() > 0)
            {
                lati = addresses.get(0).getLatitude();
                longi = addresses.get(0).getLongitude();
            }
            coordMap=new LatLng(lati, longi);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordMap, 15));
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }
        catch (IOException ex)
        {System.out.println("Error : "+ ex.getMessage());}
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
