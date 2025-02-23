package com.example.chaesiktak.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.chaesiktak.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.camera.view.PreviewView
import androidx.lifecycle.lifecycleScope
import com.example.chaesiktak.ImageAnalyzeRequest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class ScannerFragment : Fragment() {

    private lateinit var previewView: PreviewView
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(requireContext(), "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_scanner, container, false)

        previewView = view.findViewById(R.id.previewView)

        // 네비게이션 버튼 동작 추가
        view.findViewById<ImageView>(R.id.homeTap).setOnClickListener {
            view.findNavController().navigate(R.id.action_scannerFragment_to_homeFragment)
        }
        view.findViewById<ImageView>(R.id.myinfoTap).setOnClickListener {
            view.findNavController().navigate(R.id.action_scannerFragment_to_myInfoFragment)
        }

        // 촬영 버튼 클릭 리스너
        view.findViewById<ImageView>(R.id.captureButton).setOnClickListener {
            takePhoto()
        }

        // 카메라 권한 체크 및 요청
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        return view
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Log.e("CameraX", "카메라 바인딩 실패", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            requireContext().getExternalFilesDir(null),
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    Log.d("CameraX", "사진 저장됨: $savedUri")
                    Toast.makeText(requireContext(), "사진 저장됨: $savedUri", Toast.LENGTH_SHORT).show()

                    lifecycleScope.launch {
                        uploadImage(photoFile)
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraX", "사진 촬영 실패", exception)
                }
            }
        )
    }

    private suspend fun uploadImage(imageFile: File) {
        val requestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)

        try {
            val response = RetrofitClient.instance(requireContext()).uploadImage(imagePart)
            if (response.success) {
                Log.d("ImageUpload", "업로드 성공: ${response.message}")
                Toast.makeText(requireContext(), "이미지 업로드 성공", Toast.LENGTH_SHORT).show()

                // 업로드 성공 후 분석 API 호출
                ImageAnalyze(response.data)
                Log.d("응답 데이터", "${response.data}")
            } else {
                Log.e("ImageUpload", "업로드 실패: ${response.message}")
                Toast.makeText(requireContext(), "이미지 업로드 실패: ${response.message}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("ImageUpload", "네트워크 오류", e)
            Toast.makeText(requireContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun ImageAnalyze(imageUrl: String) {
        try {
            val request = ImageAnalyzeRequest(imageUrl)
            val response = RetrofitClient.instance(requireContext()).ImageAnalyze(request)
            if (response.success) {
                Log.d("SecondAPI", "전송 성공: ${response.message}")
                Toast.makeText(requireContext(), "이미지 분석 성공", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("SecondAPI", "전송 실패: ${response.message}")
                Toast.makeText(requireContext(), "이미지 분석 실패: ${response.message}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("SecondAPI", "네트워크 오류", e)
            Toast.makeText(requireContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
        }
    }
}
