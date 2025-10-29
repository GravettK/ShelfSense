package com.example.shelfsense.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.shelfsense.data.AppDatabase
import com.example.shelfsense.data.entities.StockItem
import com.example.shelfsense.ui.components.AppScaffold
import com.example.shelfsense.ui.components.BottomNavBar
import com.example.shelfsense.ui.components.ScreenColumn
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun ScanScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val dao = remember { db.stockItemDao() }
    val scope = rememberCoroutineScope()

    var scannedSku by remember { mutableStateOf("") }
    var matchedPart by remember { mutableStateOf<StockItem?>(null) }
    var isScanning by remember { mutableStateOf(true) }

    var showAdd by remember { mutableStateOf(false) }
    var showSub by remember { mutableStateOf(false) }
    var deltaText by remember { mutableStateOf("") }

    fun toast(msg: String) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

    suspend fun findPartBySku(sku: String): StockItem? = withContext(Dispatchers.IO) {
        dao.observeAll().first().firstOrNull { it.sku.equals(sku, ignoreCase = true) }
    }
    suspend fun updateQty(item: StockItem, qty: Int) = withContext(Dispatchers.IO) {
        dao.insert(item.copy(qty = qty.coerceAtLeast(0)))
    }

    // Camera permission
    val hasCamPerm = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }
    val requestPerm = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCamPerm.value = granted }

    LaunchedEffect(Unit) {
        if (!hasCamPerm.value) requestPerm.launch(Manifest.permission.CAMERA)
    }

    // Match whenever text/scan changes
    LaunchedEffect(scannedSku) {
        matchedPart = if (scannedSku.isBlank()) null else findPartBySku(scannedSku)
    }

    AppScaffold(
        title = "Scan",
        bottomBar = { BottomNavBar(navController) }
    ) { pv ->
        ScreenColumn(padding = pv) {

            if (hasCamPerm.value) {
                // Bounded preview at top — no overlap
                ScannerPreview(
                    enabled = isScanning,
                    onBarcode = { value ->
                        if (value.isNotBlank()) {
                            scannedSku = value.trim()
                            isScanning = false // pause after first hit
                            toast("Scanned: $scannedSku")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f)
                        .clip(MaterialTheme.shapes.medium)
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(onClick = {
                        isScanning = true
                        scannedSku = ""
                        matchedPart = null
                    }) { Text("Resume Scanner") }
                }
            } else {
                Text(
                    "Camera permission denied. You may still type a SKU below.",
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedTextField(
                value = scannedSku,
                onValueChange = { scannedSku = it.trim() },
                label = { Text("Scanned / Entered SKU") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Divider()

            val item = matchedPart
            when {
                scannedSku.isBlank() -> {
                    Text("Scan or type a SKU to begin.", style = MaterialTheme.typography.bodyMedium)
                }
                item == null -> {
                    Text(
                        "No part found matching \"$scannedSku\".",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                else -> {
                    Text("Part: ${item.name}", style = MaterialTheme.typography.titleMedium)
                    Text("SKU: ${item.sku}")
                    Text("Bin: ${item.bin}")
                    Text("Current Qty: ${item.qty}")

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(modifier = Modifier.weight(1f), onClick = { showAdd = true }) {
                            Text("Add")
                        }
                        Button(modifier = Modifier.weight(1f), onClick = { showSub = true }) {
                            Text("Subtract")
                        }
                    }

                    if (showAdd) {
                        NumberPromptDialog(
                            title = "Add to stock",
                            confirmLabel = "Add",
                            value = deltaText,
                            onValueChange = { if (it.all(Char::isDigit) || it.isEmpty()) deltaText = it },
                            onCancel = { showAdd = false; deltaText = "" },
                            onConfirm = {
                                val d = deltaText.toIntOrNull() ?: 0
                                val newQty = item.qty + d
                                scope.launch {
                                    updateQty(item, newQty)
                                    matchedPart = item.copy(qty = newQty)
                                    toast("Added $d • New qty: $newQty")
                                }
                                showAdd = false; deltaText = ""
                            }
                        )
                    }
                    if (showSub) {
                        NumberPromptDialog(
                            title = "Subtract from stock",
                            confirmLabel = "Subtract",
                            value = deltaText,
                            onValueChange = { if (it.all(Char::isDigit) || it.isEmpty()) deltaText = it },
                            onCancel = { showSub = false; deltaText = "" },
                            onConfirm = {
                                val d = deltaText.toIntOrNull() ?: 0
                                val newQty = (item.qty - d).coerceAtLeast(0)
                                scope.launch {
                                    updateQty(item, newQty)
                                    matchedPart = item.copy(qty = newQty)
                                    toast("Removed $d • New qty: $newQty")
                                }
                                showSub = false; deltaText = ""
                            }
                        )
                    }
                }
            }
        }
    }
}

/* -------- CameraX + ML Kit preview/analyzer -------- */

@Composable
private fun ScannerPreview(
    enabled: Boolean,
    onBarcode: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    AndroidView(
        factory = {
            PreviewView(it).apply {
                // Avoid z-order overlap with Compose
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }
        },
        modifier = modifier,
        update = { previewView ->
            val provider = cameraProviderFuture.get()
            provider.unbindAll()

            if (!enabled) return@AndroidView

            val preview = Preview.Builder()
                .setTargetResolution(Size(1280, 720))
                .build()
                .also { it.setSurfaceProvider(previewView.surfaceProvider) }

            val analysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { ia ->
                    ia.setAnalyzer(
                        ContextCompat.getMainExecutor(context),
                        BarcodeAnalyzer(onBarcode)
                    )
                }

            provider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                analysis
            )
        }
    )
}

private class BarcodeAnalyzer(
    private val onBarcode: (String) -> Unit
) : ImageAnalysis.Analyzer {
    private val scanner = BarcodeScanning.getClient()

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: run { imageProxy.close(); return }
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        scanner.process(image)
            .addOnSuccessListener { list ->
                val value = list.firstOrNull()?.rawValue ?: list.firstOrNull()?.displayValue
                if (!value.isNullOrBlank()) onBarcode(value)
            }
            .addOnCompleteListener { imageProxy.close() }
    }
}

/* -------------------- Numeric Prompt Dialog -------------------- */

@Composable
private fun NumberPromptDialog(
    title: String,
    confirmLabel: String,
    value: String,
    onValueChange: (String) -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text("Amount") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = { TextButton(onClick = onConfirm) { Text(confirmLabel) } },
        dismissButton = { TextButton(onClick = onCancel) { Text("Cancel") } }
    )
}
