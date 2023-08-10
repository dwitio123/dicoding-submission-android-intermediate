package tgs.app.storyapp.ui.maps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import tgs.app.storyapp.R
import tgs.app.storyapp.viewModelFactory.ViewModelFactory
import tgs.app.storyapp.databinding.ActivityMapsBinding
import tgs.app.storyapp.model.ListStoryItem
import tgs.app.storyapp.model.LoginResult
import tgs.app.storyapp.preference.UserPreference

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mUserPreference: UserPreference
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var loginResult: LoginResult
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mUserPreference = UserPreference(this)
        loginResult = mUserPreference.getUser()

        mapsViewModel = obtainViewModel(this@MapsActivity)
        mapsViewModel.mapsUser(loginResult.token.toString())

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mapsViewModel.listStoryItem.observe(this) { listStoryItem ->
            setItemsData(listStoryItem)
        }
    }

    private fun setItemsData(listStoryItem: List<ListStoryItem>) {

        listStoryItem.forEach {
            val latLng = LatLng(it.lat, it.lon)
            mMap.addMarker(MarkerOptions().position(latLng).title(it.name).snippet(it.description))
            boundsBuilder.include(latLng)
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }

    private fun obtainViewModel(activity: AppCompatActivity): MapsViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MapsViewModel::class.java]
    }
}