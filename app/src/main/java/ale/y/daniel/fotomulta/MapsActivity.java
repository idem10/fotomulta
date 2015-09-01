package ale.y.daniel.fotomulta;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class MapsActivity extends FragmentActivity {
    TextView mTvSpeed;
    private final static String TAG = "Velocimetro";
    private GoogleMap mMap;
    private int vista = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        mTvSpeed = (TextView) findViewById(R.id.tvSpeed);
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                mTvSpeed.setText(String.valueOf(location.getSpeed()));
                Log.i(TAG, "Locacion");
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


    }
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }
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
    private void setUpMap() {

            mMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location myLocation = locationManager.getLastKnownLocation(provider);
            double latitud = myLocation.getLatitude();
            double longitud = myLocation.getLongitude();
            LatLng latLng = new LatLng(latitud, longitud);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            mMap.addMarker(new MarkerOptions().position(new LatLng(latitud, longitud)).title("Estas aqui!").snippet("Latiud" + latitud + "\n longitud" + longitud));

    //Camara de fotomultas 1
            PolygonOptions opcionesPoligono = new PolygonOptions()
                .add(new LatLng(25.402809, -100.985183))
                .add(new LatLng(25.402373, -100.984840))
                .add(new LatLng(25.404960, -100.980967))
                .add(new LatLng(25.405493, -100.981343));
            Polygon poligono = mMap.addPolygon(opcionesPoligono);
            poligono.setFillColor(Color.BLUE);
            poligono.setStrokeColor(Color.RED);

    //Camara de fotomultas 2
            opcionesPoligono = new PolygonOptions()
                .add(new LatLng(25.369989, -101.003047))
                .add(new LatLng(25.369310, -101.002403))
                .add(new LatLng(25.375417, -100.997168))
                .add(new LatLng(25.375708, -100.997683));
            poligono = mMap.addPolygon(opcionesPoligono);
            poligono.setStrokeColor(Color.RED);
        mMap.addMarker(new MarkerOptions().position(new LatLng(25.404018, -100.983005)).title("Camara Foto Multa").snippet("Camara fotoMulta 1"));

        //Camara de fotomultas Prueba
        opcionesPoligono = new PolygonOptions()
                .add(new LatLng(25.424328, -100.990116))
                .add(new LatLng(25.424256, -100.990025))
                .add(new LatLng(25.425651, -100.989140))
                .add(new LatLng(25.425694, -100.989258));
        poligono = mMap.addPolygon(opcionesPoligono);
        poligono.setStrokeColor(Color.RED);

        mMap.addMarker(new MarkerOptions().position(new LatLng(25.372652, -100.999807)).title("Camara Foto Multa").snippet("Camara fotoMulta 2"));
        if(new LatLng(25.424328, -100.990116) == new LatLng(latitud, longitud)) {
            Toast.makeText(this, "camara de fotoMultas", Toast.LENGTH_SHORT).show();

        }
    }
    private void CambiarVista(){
        vista = (vista + 1) % 4;
        switch(vista)
        {
            case 0:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case 1:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case 2:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case 3:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.brujla:
                Intent i = new Intent(this, Velocimetro.class);
                startActivity(i);
                break;
            case R.id.velocimetro:
                CambiarVista();
                break;
            case R.id.optionAyuda:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/idem.daniel")));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}