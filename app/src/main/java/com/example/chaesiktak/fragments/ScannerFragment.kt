package com.example.chaesiktak.fragments

import LoadingDialog
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
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
import com.example.chaesiktak.activities.LLMResultAcitivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.FileOutputStream

class ScannerFragment : Fragment() {

    private lateinit var previewView: PreviewView
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_scanner, container, false)
        previewView = view.findViewById(R.id.previewView)
        loadingDialog = LoadingDialog(requireContext()) //다이얼로그 초기화

        /*
        네비게이션
         */
        view.findViewById<ImageView>(R.id.homeTap).setOnClickListener {
            view.findNavController().navigate(R.id.action_scannerFragment_to_homeFragment)
        }
        /*
       내정보탭으로 이동
        */
        view.findViewById<ImageView>(R.id.myinfoTap).setOnClickListener {
            view.findNavController().navigate(R.id.action_scannerFragment_to_myInfoFragment)
        }

        /*
       촬영버튼 클릭
        */
        view.findViewById<ImageView>(R.id.captureButton).setOnClickListener {
            takePhoto()
            loadingDialog.show()
            loadingDialog.startAnimation()
        }

        /*
       카메라 권한
        */
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(requireContext(), "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST = 1001
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        // 저장할 파일 생성
        val photoFile = File(
            requireContext().externalMediaDirs.firstOrNull(),
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



//                    uploadImageToServer(photoFile) // 촬영한 이미지를 서버로 전송
                    val compressedFile = compressImage(photoFile)
                    uploadImageToAI(compressedFile)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraX", "사진 촬영 실패", exception)
                }
            }
        )
    }

    //이미지 분석
    private fun uploadImageToAI(imageFile: File) {
        lifecycleScope.launch {
            try {
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
                val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.instance(requireContext()).AIimage(imagePart)
                }

                if (response.isSuccessful ) {
                    Log.d("Upload", "이미지 업로드 >> 200")
                    loadingDialog.stopAnimation()
                    Toast.makeText(requireContext(), "이미지 분석 완료!", Toast.LENGTH_SHORT).show()
                    response.body()?.let { aiResponse -> //응답이 null이 아닌 경우만
                        val intent = Intent(requireContext(), LLMResultAcitivity::class.java).apply {
                            putExtra("ai_response", aiResponse) // Parcelable 데이터 전달
                        }
                        startActivity(intent)
                        Log.d("Upload", "응답: $aiResponse")
                    }
                } else {
                    Log.e("Upload", "서버 오류: ${response.body()}")
                    loadingDialog.stopAnimation()
                    Toast.makeText(requireContext(), "서버 오류 발생", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("Upload", "예외 발생: ${e.message}")
                loadingDialog.stopAnimation()
                Toast.makeText(requireContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun compressImage(imageFile: File): File {
        val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

        val compressedFile = File(
            requireContext().cacheDir,
            "compressed_${imageFile.name}"
        )

        FileOutputStream(compressedFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out) // 70%로 압축
        }

        return compressedFile
    }

}