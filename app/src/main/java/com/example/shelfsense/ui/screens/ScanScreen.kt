package com.example.shelfsense.ui.screens

import android.Manifest
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.shelfsense.navigation.Routes
import com.example.shelfsense.ui.components.AppScaffold
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.atomic.AtomicBoolean

@Composable
fun ScanScreen(navController: NavController) {
    var hasCamera by remember { mutableStateOf(false) }
    val ask = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCamera = granted }

    LaunchedEffect(Unit) { ask.launch(Manifest.permission.CAMERA) }

    AppScaffold(title = "Scan Part") { pv ->
        Column(Modifier.padding(pv).fillMaxSize()) {
            if (!hasCamera) {
                Text("Camera permission is required to scan parts.", Modifier.padding(16.dp))
                Button(onClick = { ask.launch(Manifest.permission.CAMERA) }, Modifier.padding(16.dp)) {
                    Text("Grant permission")
                }
            } else {
                CameraPreview { raw ->
                    if (!raw.isNullOrBlank()) {
                        navController.navigate(Routes.partDetail(raw)) { launchSingleTop = true }
                    }
                }
            }
        }
    }
}

@Composable
private fun CameraPreview(onBarcode: (String?) -> Unit) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    val providerFuture = remember { ProcessCameraProvider.getInstance(context) }
    val navigated = remember { AtomicBoolean(false) }

    DisposableEffect(Unit) {
        val cameraProvider = providerFuture.get()

        val preview = androidx.camera.core.Preview.Builder()
            .build()
            .also { it.setSurfaceProvider(previewView.surfaceProvider) }

        val analysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .apply {
                setAnalyzer(ContextCompat.getMainExecutor(context)) { proxy ->
                    process(proxy) { value ->
                        if (!value.isNullOrBlank() && navigated.compareAndSet(false, true)) {
                            onBarcode(value)
                        }
                    }
                }
            }

        val selector = CameraSelector.DEFAULT_BACK_CAMERA
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycle, selector, preview, analysis)

        onDispose { cameraProvider.unbindAll() }
    }

    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    )
}

private fun process(proxy: ImageProxy, onBarcode: (String?) -> Unit) {
    val image = proxy.image
    if (image != null) {
        val input = InputImage.fromMediaImage(image, proxy.imageInfo.rotationDegrees)
        val scanner = BarcodeScanning.getClient()
        scanner.process(input)
            .addOnSuccessListener { results ->
                for (b: Barcode in results) {
                    val raw = b.rawValue
                    if (!raw.isNullOrBlank()) {
                        onBarcode(raw)
                        break
                    }
                }
            }
            .addOnCompleteListener { proxy.close() }
    } else {
        proxy.close()
    }
}
