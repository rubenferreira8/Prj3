
package estg.ipvc.prj3;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class mapa extends AppCompatActivity implements OnMapReadyCallback {
    private TextView mTextViewResult;
    private RequestQueue mQueue;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean GPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        if(!GPSEnabled){
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }else{
            mLocationPermissionsGranted=true;
        }

        mMap = googleMap;

       /* LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean GPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!GPSEnabled){
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }else{*/
            if (mLocationPermissionsGranted) {
                getDeviceLocation();

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);

            //}
        }

    }

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 18f;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);


        mTextViewResult = findViewById(R.id.taxis);
        final ImageButton buttonParse = findViewById(R.id.imgtaxis);
        final ImageButton buttonParse1 = findViewById(R.id.imglocais);
        final ImageButton buttonParse2 = findViewById(R.id.estacionamento);

        mQueue = Volley.newRequestQueue(this);

        buttonParse.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
              buttonParse1.clearAnimation();
                buttonParse2.clearAnimation();
                Animation buttonParse = new AlphaAnimation(0.2f, 1.0f);
                //buttonParse.setDuration(2000);
                buttonParse.setDuration(999999999);
                //buttonParse.setRepeatCount(1000000000);
                v.startAnimation(buttonParse);
                jsonParse();
            }



        });

        buttonParse1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonParse.clearAnimation();
                buttonParse2.clearAnimation();
                //Animation buttonParse1 = new AlphaAnimation(1.0f, 0.0f);
                Animation buttonParse1 = new AlphaAnimation(0.2f, 1.0f);
                buttonParse1.setDuration(999999999);
                //buttonParse1.setRepeatCount(1000000000);
                v.startAnimation(buttonParse1);

                jsonParse1();
            }
        });

        buttonParse2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonParse1.clearAnimation();
                buttonParse.clearAnimation();
                //Animation buttonParse2 = new AlphaAnimation(1.0f, 0.0f);
                Animation buttonParse2 = new AlphaAnimation(0.2f, 1.0f);
                //buttonParse2.setDuration(2000);
                buttonParse2.setDuration(999999999);
                //buttonParse2.setRepeatCount(1000000000);
                v.startAnimation(buttonParse2);


                jsonParse2();
            }
        });

       /*btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                buttonParse.clearAnimation();
                buttonParse1.clearAnimation();
                buttonParse2.clearAnimation();
                Intent it = new Intent(mapa.this, escolhatipocategoria.class);
                startActivity(it);
            }
        });
        */

        getLocationPermission();
    }


    private void jsonParse(){
        mMap.clear();
        /*Toast toast1 = Toast.makeText(mapa.this, "ola", Toast.LENGTH_SHORT);
        toast1.show();*/


        String url = "https://geo.cm-viana-castelo.pt/arcgis/rest/services/Viana_acessivel/MapServer/1/query?where=1%3D1&text=&objectIds=&time=&geometry=&geometryType=esriGeometryPoint&inSR=&spatialRel=esriSpatialRelContains&relationParam=&outFields=*&returnGeometry=true&returnTrueCurves=false&maxAllowableOffset=&geometryPrecision=&outSR=&returnIdsOnly=false&returnCountOnly=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVersion=&returnDistinctValues=false&resultOffset=&resultRecordCount=&queryByDistance=&returnExtentsOnly=false&datumTransformation=&parameterValues=&rangeValues=&f=pjson";
        //String url ="https://api.myjson.com/bins/kp9wz";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,/*(String)*/null,
                new Response.Listener<JSONObject>() {

                    @Override

                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("features");
                /*if(jsonArray.length()==0){
                Toast toast2 = Toast.makeText(mapa.this, "Sem ligação à Internet, não foi possível carregar os dados", Toast.LENGTH_SHORT);
                    toast2.show();
                }*/
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject employee = jsonArray.getJSONObject(i);
                                JSONObject geometry = employee.getJSONObject("geometry");
                                JSONObject attributes = employee.getJSONObject("attributes");
                                String lugares = attributes.getString("LUGARES");
                                Double x = geometry.getDouble("x");
                                Double y = geometry.getDouble("y");
                               /* Toast toast1 = Toast.makeText(mapa.this,lugares, Toast.LENGTH_SHORT);
                                toast1.show();*/

                                LatLng ponto = new LatLng(y,x);
                                mMap.addMarker(new MarkerOptions().position(ponto).title("Número de lugares: " + lugares) );


/*
                                Toast toast2 = Toast.makeText(mapa.this, x + "," + y, Toast.LENGTH_SHORT);
                                toast2.show();*/
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast toast2 = Toast.makeText(mapa.this, "Sem ligação à Internet, não foi possível carregar os dados", Toast.LENGTH_SHORT);
                            toast2.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast2 = Toast.makeText(mapa.this, "Sem ligação à Internet, não foi possível carregar os dados", Toast.LENGTH_SHORT);
                toast2.show();
            }
        });

        mQueue.add(request);
    }


    private void jsonParse1(){
        mMap.clear();
        /*Toast toast1 = Toast.makeText(mapa.this, "ola", Toast.LENGTH_SHORT);
        toast1.show();*/


        String url = "https://geo.cm-viana-castelo.pt/arcgis/rest/services/Viana_acessivel/MapServer/0/query?where=1%3D1&text=&objectIds=&time=&geometry=&geometryType=esriGeometryPoint&inSR=&spatialRel=esriSpatialRelIntersects&relationParam=&outFields=*&returnGeometry=true&returnTrueCurves=false&maxAllowableOffset=&geometryPrecision=&outSR=&returnIdsOnly=false&returnCountOnly=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVersion=&returnDistinctValues=false&resultOffset=&resultRecordCount=&queryByDistance=&returnExtentsOnly=false&datumTransformation=&parameterValues=&rangeValues=&f=pjson";
        //String url ="https://api.myjson.com/bins/kp9wz";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,/*(String)*/null,
                new Response.Listener<JSONObject>() {

                    @Override

                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("features");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject employee = jsonArray.getJSONObject(i);
                                JSONObject geometry = employee.getJSONObject("geometry");
                                JSONObject attributes = employee.getJSONObject("attributes");
                                String designacao = attributes.getString("DESIGNACAO");
                                Double x = geometry.getDouble("x");
                                Double y = geometry.getDouble("y");

                                LatLng ponto = new LatLng(y,x);
                                mMap.addMarker(new MarkerOptions().position(ponto).title(designacao) );

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast toast2 = Toast.makeText(mapa.this, "Sem ligação à Internet, não foi possível carregar os dados", Toast.LENGTH_SHORT);
                            toast2.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast2 = Toast.makeText(mapa.this, "Sem ligação à Internet, não foi possível carregar os dados", Toast.LENGTH_SHORT);
                toast2.show();
            }
        });

        mQueue.add(request);
    }



    private void jsonParse2(){
        mMap.clear();


        String url = "https://geo.cm-viana-castelo.pt/arcgis/rest/services/Viana_acessivel/MapServer/2/query?where=1%3D1&text=&objectIds=&time=&geometry=&geometryType=esriGeometryEnvelope&inSR=&spatialRel=esriSpatialRelIntersects&relationParam=&outFields=*&returnGeometry=true&returnTrueCurves=false&maxAllowableOffset=&geometryPrecision=&outSR=&returnIdsOnly=false&returnCountOnly=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVersion=&returnDistinctValues=false&resultOffset=&resultRecordCount=&queryByDistance=&returnExtentsOnly=false&datumTransformation=&parameterValues=&rangeValues=&f=pjson";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,/*(String)*/null,
                new Response.Listener<JSONObject>() {

                    @Override

                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("features");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject employee = jsonArray.getJSONObject(i);
                                JSONObject geometry = employee.getJSONObject("geometry");
                                JSONObject attributes = employee.getJSONObject("attributes");
                                String lugares = attributes.getString("LUGARES");
                                Double x = geometry.getDouble("x");
                                Double y = geometry.getDouble("y");

                                LatLng ponto = new LatLng(y,x);
                                mMap.addMarker(new MarkerOptions().position(ponto).title("Número de lugares: " + lugares) );

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast toast2 = Toast.makeText(mapa.this, "Sem ligação à Internet, não foi possível carregar os dados", Toast.LENGTH_SHORT);
                            toast2.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast2 = Toast.makeText(mapa.this, "Sem ligação à Internet, não foi possível carregar os dados", Toast.LENGTH_SHORT);
                toast2.show();
            }
        });

        mQueue.add(request);
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);
                            mMap.setMapType(mMap.MAP_TYPE_SATELLITE);

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(mapa.this, "Sem ligação à Internet, não foi possivel aceder ao mapa", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void initMap(){
        //mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(mapa.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = true;
        initMap();
        /*switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map

                    initMap();


                }
            }
        }*/
    }

    public void onClick (View v){
        Intent i = new Intent(mapa.this, tipogrupo.class);
        startActivity(i);

    }


}
