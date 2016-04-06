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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class A_jour_Map_view extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.


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
        LatLng prevCoord= new LatLng(0,0);
        int parcours=0;


        //trier la liste selon les dates
        Collections.sort(this.jour.liste_sujets, new Comparator<C_Sujet>() {
            @Override
            public int compare(C_Sujet s1, C_Sujet s2) {

                return s1.heure.compareTo(s2.heure);
            }
        });



        for (C_Sujet s: this.jour.liste_sujets)
        {
            String lati = s.localisation.split(";")[0];
            String longi = s.localisation.split(";")[1];
            LatLng coord = new LatLng(Double.parseDouble(lati), Double.parseDouble(longi));
            LatLng destiCoord= new LatLng(0,0);
            Marker point;
            Marker point2;

            switch (s.type) {
                case "Repas":
                    point=mMap.addMarker(new MarkerOptions()
                            .position(coord)
                            .title(parcours+1+"-["+s.type+"] "+s.titre)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                    if (s.areaSize>0)
                    {
                        CircleOptions circleOptions = new CircleOptions()
                                .center(coord)
                                .radius(s.areaSize)
                                .strokeColor(Color.BLUE)
                                .fillColor(0x552196F3);
                        Circle circle = mMap.addCircle(circleOptions);
                    }
                    break;

                case "Visite":
                    point=mMap.addMarker(new MarkerOptions()
                            .position(coord)
                            .title(parcours+1+"-["+s.type+"] "+s.titre)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    if (s.areaSize>0)
                    {
                        CircleOptions circleOptions = new CircleOptions()
                                .center(coord)
                                .radius(s.areaSize)
                                .strokeColor(Color.GREEN)
                                .fillColor(0x554CAF50);
                        Circle circle = mMap.addCircle(circleOptions);
                    }
                    break;

                case "Logement":
                    point=mMap.addMarker(new MarkerOptions()
                            .position(coord)
                            .title(parcours+1+"-["+s.type+"] "+s.titre)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

                    if (s.areaSize>0)
                    {
                        CircleOptions circleOptions = new CircleOptions()
                                .center(coord)
                                .radius(s.areaSize)
                                .strokeColor(Color.YELLOW)
                                .fillColor(0x55FFEB3B);
                        Circle circle = mMap.addCircle(circleOptions);
                    }
                    break;

                case "Loisir":
                    point=mMap.addMarker(new MarkerOptions()
                            .position(coord)
                            .title(parcours+1+"-["+s.type+"] "+s.titre)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

                    if (s.areaSize>0)
                    {
                        CircleOptions circleOptions = new CircleOptions()
                                .center(coord)
                                .radius(s.areaSize)
                                .strokeColor(Color.MAGENTA)
                                .fillColor(0x559C27B0);
                        Circle circle = mMap.addCircle(circleOptions);
                    }
                    break;

                case "Libre":
                    point=mMap.addMarker(new MarkerOptions()
                            .position(coord)
                            .title(parcours+1+"-["+s.type+"] "+s.titre)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

                    if (s.areaSize>0)
                    {
                        CircleOptions circleOptions = new CircleOptions()
                                .center(coord)
                                .radius(s.areaSize)
                                .strokeColor(Color.BLACK)
                                .fillColor(0x55000000);
                        Circle circle = mMap.addCircle(circleOptions);
                    }
                    break;

                case "Transport":
                    point=mMap.addMarker(new MarkerOptions()
                            .position(coord)
                            .title(parcours+1+"-["+s.type+"]From: "+s.titre)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));


                    lati = s.localisation2.split(";")[0];
                    longi = s.localisation2.split(";")[1];
                    destiCoord = new LatLng(Double.parseDouble(lati), Double.parseDouble(longi));

                    point2=mMap.addMarker(new MarkerOptions()
                            .position(destiCoord)
                            .title(parcours+1+"-["+s.type+"]To: "+s.titre)
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                    Polyline line = mMap.addPolyline(new PolylineOptions()
                            .add(coord, destiCoord)
                            .width(5)
                            .color(Color.RED));

                    break;
            }


            if(parcours!=0)
            {
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(prevCoord, coord)
                        .width(2)
                        .color(Color.BLACK));
            }

            if(s.type.equals("Transport"))
            {prevCoord=destiCoord;}
            else
            {prevCoord=coord;}


            parcours++;
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
