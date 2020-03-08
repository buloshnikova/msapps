package me.happyclick.movies.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.fragment_qrscanner.*
import me.happyclick.movies.R
import me.happyclick.movies.model.Movie
import me.happyclick.movies.viewmodel.QRScannerViewModel
import org.json.JSONException
import org.json.JSONObject

class QRScannerFragment : Fragment() {

    private lateinit var viewModel: QRScannerViewModel
    private lateinit var qrScanIntegrator: IntentIntegrator

    private var currentMovie: Movie? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(false)
        return inflater.inflate(R.layout.fragment_qrscanner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        qrScanIntegrator = IntentIntegrator.forSupportFragment(this@QRScannerFragment)
        qrScanIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        qrScanIntegrator.setBeepEnabled(true)
        qrScanIntegrator.setOrientationLocked(false)

        startScan()

        viewModel = ViewModelProviders.of(this).get(QRScannerViewModel::class.java)

        observeViewModel()
    }

    private fun startScan() {
        qrScanIntegrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if(result != null) {
            if(result.contents == null) {
                Toast.makeText(activity, R.string.result_not_found, Toast.LENGTH_LONG).show()
            } else {
                try {
                    loading_view.visibility = View.VISIBLE
                    currentMovie = Gson().fromJson(result.contents, Movie::class.java)
                    viewModel.checkMovieInDB(currentMovie!!)

                } catch (e: JSONException) {
                    e.printStackTrace()

                    // Data not in the expected format. So, whole object as toast message.
                    Toast.makeText(activity, result.contents, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun observeViewModel() {
        viewModel.movieExists.observe(viewLifecycleOwner, Observer { exists  ->
            exists?.let {
                if(exists) {
                    Snackbar.make(loading_view, R.string.movie_exists, Snackbar.LENGTH_LONG).show()
                    loading_view.visibility = View.GONE
                    Navigation.findNavController(loading_view).popBackStack()
                } else {
                    currentMovie?.let {
                        viewModel.addMovie(currentMovie!!)
                    }

                }
            }
        })

        viewModel.movieAdded.observe(viewLifecycleOwner, Observer { movieAdded ->
            if(movieAdded) {
                Snackbar.make(loading_view, R.string.movie_added, Snackbar.LENGTH_LONG).show()
            } else {
                Toast.makeText(activity, R.string.movie_added_error, Toast.LENGTH_LONG).show()
            }
            loading_view.visibility = View.GONE
            Navigation.findNavController(loading_view).popBackStack()
        })
    }

}