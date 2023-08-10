package tgs.app.storyapp.ui.addStory

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import tgs.app.storyapp.R
import tgs.app.storyapp.viewModelFactory.ViewModelFactory
import tgs.app.storyapp.databinding.ActivityAddStoryBinding
import tgs.app.storyapp.model.LoginResult
import tgs.app.storyapp.preference.UserPreference
import tgs.app.storyapp.utils.reduceFileImage
import tgs.app.storyapp.utils.rotateFile
import tgs.app.storyapp.ui.CameraActivity
import tgs.app.storyapp.utils.uriToFile
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var addStoryViewModel: AddStoryViewModel
    private lateinit var loginResult: LoginResult
    private lateinit var mUserPreference: UserPreference
    private var getFile: File? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (!allPermissionGranted()) {
                Toast.makeText(this, getString(R.string.failed_to_get_permission), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSION,
                REQUEST_CODE_PERMISSION
            )
        }

        mUserPreference = UserPreference(this)
        loginResult = mUserPreference.getUser()

        addStoryViewModel = obtainViewModel(this@AddStoryActivity)
        addStoryViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener {
            val description = binding.edAddDescription.text.toString()
            if (description.isEmpty()) {
                Toast.makeText(applicationContext, getString(R.string.please_fill_the_description), Toast.LENGTH_SHORT).show()
            } else {
                uploadImage(description)
            }
        }

        supportActionBar?.title = "Add Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun obtainViewModel(activity: AppCompatActivity): AddStoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[AddStoryViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launchIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launchIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding.ivPreviewPhoto.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddStoryActivity)
                getFile = myFile
                binding.ivPreviewPhoto.setImageURI(uri)
            }
        }
    }

    private fun uploadImage(description: String) {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val descriptionText = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            addStoryViewModel.addStory(loginResult.token.toString(), imageMultipart, descriptionText)
            addStoryViewModel.addStoryResponse.observe(this) {
                if (it.message == "Story created successfully") {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        } else {
            Toast.makeText(this@AddStoryActivity, getString(R.string.please_insert_image), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSION = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 10
    }
}