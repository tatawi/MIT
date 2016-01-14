package com.mit.mit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.Projection;
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

public class A_sujet_Map_set extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ViewSwitcher viewSwitcher;
    private View mySecondView;
    private View myFirstView;

    private ImageButton btn_marker;
    private ImageButton btn_change;
    private EditText tb_adresse;
    private Button btn_ok;
    private LinearLayout ll_adresses;
    private LinearLayout ll_global;
    private EditText tb_addr;

    private Marker point1;
    private Marker point2;

    private Context context;
    private DAO_Sujet daoSujet;
    private DAO_Options daoOptions;
    private DAO_Jour daoJour;

    private C_Options options;
    private C_Sujet sujet;
    private C_Jour jour;

    private String adresse;
    private String destination;

    private LatLng coord;
    private LatLng coordMap;


//sujet_map_ll_viewSujetss


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
        btn_change=(ImageButton) findViewById(R.id.sujet_map_set_btn_change);
        btn_ok = (Button) findViewById(R.id.sujet_map_set_btn_OK);
        ll_adresses= (LinearLayout) findViewById(R.id.sujet_map_ll_adreses);
        ll_global=(LinearLayout) findViewById(R.id.newsujet_linearlayout_global);


        btn_marker.setOnClickListener(onSearchAdress);
        btn_ok.setOnClickListener(onClickbtnOk);
        // Setting a click event handler for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        markerOptions.title("Selected point");
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                point1=mMap.addMarker(markerOptions);
            }
        });

        viewSwitcher =   (ViewSwitcher)findViewById(R.id.sujet_map_viewSwitcher1);
        myFirstView= findViewById(R.id.sujet_map_ll_viewMap);
        mySecondView = findViewById(R.id.sujet_map_ll_viewSujets);
        btn_change.setOnClickListener(onBtnChange);


        //récupérations
        this.options=daoOptions.getOptionByUserId();
        this.sujet=daoSujet.getSujetById(options.sujetid);
        this.jour=daoJour.getJourById(options.jourid);
        this.jour.creerLesListes(daoSujet);

        System.out.println("****" + sujet.idSujet);
        System.out.println("timeMap : "+sujet.heure.toString());

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


        //ajout layout autres sujets
        for(C_Sujet s: this.jour.liste_sujets)
        {
            if(s.localisation.length()>2) {
                //panel button
                LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                //layout global du sujet
                LinearLayout LLsujet = new LinearLayout(this);
                LLsujet.setOrientation(LinearLayout.HORIZONTAL);
                LLsujet.setLayoutParams(LLParams);
                LLsujet.setId(s.id);
                LLsujet.setOnClickListener(onClickLayout);

                //layout sujet du haut
                LinearLayout LLsujetUP = new LinearLayout(this);
                LLsujetUP.setOrientation(LinearLayout.VERTICAL);
                LLsujetUP.setLayoutParams(LLParams);
                LLsujetUP.setId(s.id);
                LLsujetUP.setOnClickListener(onClickLayout);


                //type (haut)
                TextView titreDescription = new TextView(this);
                titreDescription.setText("[" + s.type + "]");

                //titre sujet (bas)
                TextView titreSujet = new TextView(this);
                titreSujet.setText(s.titre);

                //image
                ImageButton img = new ImageButton(this);
                img.setId(s.id);
                img.setOnClickListener(onClickSubjectImg);
                img.setBackgroundColor(Color.TRANSPARENT);


                switch (s.type) {
                    case "Transport":
                        if (s.valide)
                            img.setImageResource(R.drawable.ic_jour_transports_fill);
                        else
                            img.setImageResource(R.drawable.ic_jour_transports);
                        titreSujet.setTextColor(Color.parseColor("#e74c3c"));
                        titreDescription.setTextColor(Color.parseColor("#e74c3c"));
                        break;

                    case "Repas":
                        if (s.valide)
                            img.setImageResource(R.drawable.ic_jour_repas_fill);
                        else
                            img.setImageResource(R.drawable.ic_jour_repas);
                        titreSujet.setTextColor(Color.parseColor("#2980b9"));
                        titreDescription.setTextColor(Color.parseColor("#2980b9"));
                        break;

                    case "Visite":
                        if (s.valide)
                            img.setImageResource(R.drawable.ic_jour_visite_fill);
                        else
                            img.setImageResource(R.drawable.ic_jour_visite);
                        titreSujet.setTextColor(Color.parseColor("#16a085"));
                        titreDescription.setTextColor(Color.parseColor("#16a085"));
                        break;

                    case "Logement":
                        if (s.valide)
                            img.setImageResource(R.drawable.ic_jour_logement_fill);
                        else
                            img.setImageResource(R.drawable.ic_jour_logement);
                        titreSujet.setTextColor(Color.parseColor("#f39c12"));
                        titreDescription.setTextColor(Color.parseColor("#f39c12"));
                        break;

                    case "Loisir":
                        if (s.valide)
                            img.setImageResource(R.drawable.ic_jour_loisir_fill);
                        else
                            img.setImageResource(R.drawable.ic_jour_loisir);
                        titreSujet.setTextColor(Color.parseColor("#9b59b6"));
                        titreDescription.setTextColor(Color.parseColor("#9b59b6"));
                        break;

                    case "Libre":
                        if (s.valide)
                            img.setImageResource(R.drawable.ic_jour_libre_fill);
                        else
                            img.setImageResource(R.drawable.ic_jour_libre);
                        titreSujet.setTextColor(Color.parseColor("#5b5b5b"));
                        titreDescription.setTextColor(Color.parseColor("#5b5b5b"));
                        break;
                }

                LLsujetUP.addView(titreDescription);
                LLsujetUP.addView(titreSujet);

                LLsujet.addView(img);
                LLsujet.addView(LLsujetUP);

                ll_global.addView(LLsujet);
            }
        }


        if(options.online)
        {
            if(this.jour.ville.length()>2)
            {
                initialiserMap(this.jour.ville);
            }
            else
            {initialiserMap();}
        }
        else
        {initialiserMap();}




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



//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------

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
                setMarkersToAdress(tb_adresse.getText().toString(), destination);
            }
            else
            {
                setMarkerToAdress(tb_adresse.getText().toString());
            }

        }
        }
    };


    //On change view
    View.OnClickListener onBtnChange = new View.OnClickListener() {
        public void onClick(View v)
        {

            if (viewSwitcher.getCurrentView() != myFirstView) {

                viewSwitcher.showPrevious();
            } else if (viewSwitcher.getCurrentView() != mySecondView) {

                viewSwitcher.showNext();
            }
        }
    };




    //layout sujet - layout
    View.OnClickListener onClickLayout = new View.OnClickListener() {
        public void onClick(View v) {
            LinearLayout selectedLL = (LinearLayout) v;

            for (C_Sujet s:jour.liste_sujets)
            {
                if(s.id==selectedLL.getId())
                {
                    setpointFromSubject(s);
                }
            }
        }
    };

    //layout sujet - img
    View.OnClickListener onClickSubjectImg = new View.OnClickListener() {
        public void onClick(View v) {
            ImageButton selectedLL = (ImageButton) v;

            for (C_Sujet s:jour.liste_sujets)
            {
                if(s.id==selectedLL.getId())
                {
                    setpointFromSubject(s);
                }
            }
        }
    };


    //btn valider
    View.OnClickListener onClickbtnOk = new View.OnClickListener() {
        public void onClick(View v)
        {
            LatLng position;
            if(sujet.type.equals("Transport"))
            {
                LatLng position2 = point2.getPosition();
                sujet.localisation2=position2.latitude+";"+position2.longitude;
            }

            try
            {
                position = point1.getPosition();
            }
            catch (Exception ex)
            {
                position=coordMap;
            }

            sujet.localisation=position.latitude+";"+position.longitude;
            daoSujet.modifier(sujet, options.online);

            System.out.println("timeMapEnd : " + sujet.heure.toString());
            Intent intent = new Intent(A_sujet_Map_set.this, A_jour_Preparation.class);
            startActivity(intent);

        }
    };





//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------



    protected void initialiserMap()
    {
        try
        {
            this.coordMap=getApproximateCoord();
            moveToCurrentLocation(coordMap);
        }
        catch (Exception ex)
        {
            System.out.println("Error : "+ ex.getMessage());
        }
    }


    protected void initialiserMap(String addr)
    {
        double lati=0;
        double longi=0;

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
            this.coordMap=new LatLng(lati, longi);
            moveToCurrentLocation(coordMap);
        }
        catch (IOException ex)
        {System.out.println("Error : "+ ex.getMessage());}
    }


    //déplace la caméra sur la position en param
    protected void moveToCurrentLocation(LatLng currentLocation)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }


    //calculate an approximative position from all other subjets in the current day
    protected LatLng getApproximateCoord()
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

    //positionne le marqueur de l'adresse sur la carte
    protected void setMarkerToAdress(String adresse )
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


        }
        catch (IOException ex)
        {
            System.out.println("Error : "+ ex.getMessage());
        }
    }


    //positionne les marqueurs des adresses sur la carte
    protected  void setMarkersToAdress(String adresse, String destination)
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
                    .title("From : "+adresse)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            moveToCurrentLocation(coord);

            if(sujet.type.equals("Transport"))
            {
                addresses = geocoder.getFromLocationName(destination, 1);
                if (addresses.size() > 0) {
                    lati = addresses.get(0).getLatitude();
                    longi = addresses.get(0).getLongitude();
                }
                LatLng oriCoord=this.coord;
                this.coord=new LatLng(lati, longi);

                point2=mMap.addMarker(new MarkerOptions()
                        .position(coord)
                        .title("To : "+adresse)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(oriCoord, this.coord)
                        .width(5)
                        .color(Color.BLUE));
            }

        }
        catch (IOException ex)
        {
            System.out.println("Error : "+ ex.getMessage());
        }
    }

    //met le marker du sujet sur la carte
    private void setpointFromSubject(C_Sujet s)
    {
        mMap.clear();
        String lati = s.localisation.split(";")[0];
        String longi = s.localisation.split(";")[1];
        coord = new LatLng(Double.parseDouble(lati), Double.parseDouble(longi));

        mMap.animateCamera(CameraUpdateFactory.newLatLng(coord));
        point1=mMap.addMarker(new MarkerOptions()
                .position(coord)
                .title(s.titre)
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        moveToCurrentLocation(coord);

        if (s.type.equals("Transport"))
        {
            point1=mMap.addMarker(new MarkerOptions()
                    .position(coord)
                    .title(s.titre)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
    }



}



