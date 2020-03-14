package ch.epfl.sdp.ui.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import ch.epfl.sdp.R;

public class MapFragment extends Fragment{
    private MapView mapView;
    private GoogleMapProvider googleMapProvider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.map_fragment, container, false);

        mapView= view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        googleMapProvider = new GoogleMapProvider(mapView, this.getContext());
        googleMapProvider.setMyLocationButtonEnabled(true);
        googleMapProvider.setMyLocationEnabled(true);
        addMarker("Vidy", new LatLng(46.518615, 6.591796), googleMapProvider);
        addMarker("Satellite", new LatLng(46.520564, 6.567827), googleMapProvider);
        addMarker("Football", new LatLng(46.523345, 6.569809), googleMapProvider);
        return view;
    }


    public void addMarker(String eventName, LatLng coordinates, GoogleMapProvider googleMapProvider) {
        googleMapProvider.addMarker(new MarkerOptions().position(coordinates).title(eventName));
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}