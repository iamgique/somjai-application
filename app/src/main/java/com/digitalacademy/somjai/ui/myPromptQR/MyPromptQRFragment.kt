package com.digitalacademy.somjai.ui.myPromptQR

import android.content.Intent
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.digitalacademy.somjai.R
import com.digitalacademy.somjai.controller.MyPromptQRActivity
import com.digitalacademy.somjai.service.MyPromptQRDataService
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class MyPromptQRFragment : Fragment(), ZXingScannerView.ResultHandler {

    private lateinit var myPromptQRViewModel: MyPromptQRViewModel
    private lateinit var zXingScannerView: ZXingScannerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myPromptQRViewModel = ViewModelProviders.of(this).get(MyPromptQRViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_my_prompt_qr, container, false)
        val textView: TextView = root.findViewById(R.id.textMyPromptQR)
        myPromptQRViewModel.text.observe(viewLifecycleOwner, Observer { textView.text = it })

        zXingScannerView = ZXingScannerView(activity);
        return zXingScannerView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var cameraId = -1
        val numberOfCameras: Int = Camera.getNumberOfCameras()
        for (i in 0 until numberOfCameras) {
            val info = Camera.CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.d("Camera", "Camera found")
                cameraId = i
                break
            }
        }
        zXingScannerView.startCamera(cameraId)
    }

    override fun onResume() {
        super.onResume()
        zXingScannerView.setResultHandler(this)
        zXingScannerView.startCamera(1)
    }

    override fun onPause() {
        super.onPause()
        zXingScannerView.stopCamera()
    }

    override fun handleResult(result: Result?) {
        val resultString: String = result?.getText().toString().trim()
        Log.d("Raw Text", resultString)
        Toast.makeText(activity, "Raw Text = $resultString", Toast.LENGTH_SHORT).show();
        zXingScannerView.stopCamera()

        MyPromptQRDataService.rawMyPromptQRText = resultString
        val myPromptQRIntent = Intent(activity, MyPromptQRActivity::class.java)
        startActivity(myPromptQRIntent)
    }

}