package com.mit.mit;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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

public class A_sujet_Map_view extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private Marker point1;
    private Marker point2;

    private Context context;
    private DAO_Sujet daoSujet;
    private DAO_Options daoOptions;


    private C_Options options;
    private C_Sujet sujet;

    private LatLng coord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_sujet__map_view);
        setUpMapIfNeeded();

        context=this.getApplicationContext();
        daoSujet = new DAO_Sujet(context);
        daoOptions = new DAO_Options(context);

        //récupérations
        this.options=daoOptions.getOptionByUserId();
        this.sujet=daoSujet.getSujetById(options.sujetid);




        //set location marker
        try
        {
            String lati = this.sujet.localisation.split(";")[0];
            String longi = this.sujet.localisation.split(";")[1];
            this.coord = new LatLng(Double.parseDouble(lati), Double.parseDouble(longi));

            switch (sujet.type) {
                case "Repas":
                    point1=mMap.addMarker(new MarkerOptions()
                            .position(coord)
                            .title("["+sujet.type+"] "+sujet.titre)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                    if (sujet.areaSize>0)
                    {
                        CircleOptions circleOptions = new CircleOptions()
                                .center(coord)
                                .radius(sujet.areaSize)
                                .strokeColor(Color.BLUE)
                                .fillColor(0x552196F3);
                        Circle circle = mMap.addCircle(circleOptions);
                    }
                    break;

                case "Visite":
                    point1=mMap.addMarker(new MarkerOptions()
                            .position(coord)
                            .title("["+sujet.type+"] "+sujet.titre)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    if (sujet.areaSize>0)
                    {
                        CircleOptions circleOptions = new CircleOptions()
                                .center(coord)
                                .radius(sujet.areaSize)
                                .strokeColor(Color.GREEN)
                                .fillColor(0x554CAF50);
                        Circle circle = mMap.addCircle(circleOptions);
                    }
                    break;

                case "Logement":
                    point1=mMap.addMarker(new MarkerOptions()
                            .position(coord)
                            .title("["+sujet.type+"] "+sujet.titre)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

                    if (sujet.areaSize>0)
                    {
                        CircleOptions circleOptions = new CircleOptions()
                                .center(coord)
                                .radius(sujet.areaSize)
                                .strokeColor(Color.YELLOW)
                                .fillColor(0x55FFEB3B);
                        Circle circle = mMap.addCircle(circleOptions);
                    }
                    break;

                case "Loisir":
                    point1=mMap.addMarker(new MarkerOptions()
                            .position(coord)
                            .title("["+sujet.type+"] "+sujet.titre)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

                    if (sujet.areaSize>0)
                    {
                        CircleOptions circleOptions = new CircleOptions()
                                .center(coord)
                                .radius(sujet.areaSize)
                                .strokeColor(Color.MAGENTA)
                                .fillColor(0x559C27B0);
                        Circle circle = mMap.addCircle(circleOptions);
                    }
                    break;

                case "Libre":
                    point1=mMap.addMarker(new MarkerOptions()
                            .position(coord)
                            .title("["+sujet.type+"] "+sujet.titre)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

                    if (sujet.areaSize>0)
                    {
                        CircleOptions circleOptions = new CircleOptions()
                                .center(coord)
                                .radius(sujet.areaSize)
                                .strokeColor(Color.BLACK)
                                .fillColor(0x55000000);
                        Circle circle = mMap.addCircle(circleOptions);
                    }
                    break;
                case "Transport":
                    point1=mMap.addMarker(new MarkerOptions()
                            .position(coord)
                            .title("[From] "+sujet.titre)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                    lati = this.sujet.localisation2.split(";")[0];
                    longi = this.sujet.localisation2.split(";")[1];

                    LatLng oriCoord=this.coord;
                    this.coord = new LatLng(Double.parseDouble(lati), Double.parseDouble(longi));

                    point2 = mMap.addMarker(new MarkerOptions()
                            .position(coord)
                            .title("[To] " + this.sujet.titre)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));


                    //specify latitude and longitude of both source and destination
                    Polyline line = mMap.addPolyline(new PolylineOptions()
                            .add(oriCoord, this.coord)
                            .width(5)
                            .color(Color.RED));


                    break;
            }


            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.coord, 15));
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


        }
        catch (Exception ex)
        {
            System.out.println("[ERROR] - A_Sujet_Map_View : "+ex.getMessage());
        }



    }


   /* private void drawPrimaryLinePath( ArrayList<Location> listLocsToDraw )
    {
        if ( mMap == null )
        {
            return;
        }

        if ( listLocsToDraw.size() < 2 )
        {
            return;
        }

        PolylineOptions options = new PolylineOptions();

        options.color( Color.parseColor( "#CC0000FF" ) );
        options.width( 5 );
        options.visible( true );

        for ( Location locRecorded : listLocsToDraw )
        {
            options.add( new LatLng( locRecorded.getLatitude(),
                    locRecorded.getLongitude() ) );
        }

        map.addPolyline( options );

    }*/


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
