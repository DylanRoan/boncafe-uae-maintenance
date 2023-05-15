@file:Suppress("DEPRECATION")

package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_booking__photos.*
import java.net.URI

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Booking_Photos.newInstance] factory method to
 * create an instance of this fragment.
 */
class Booking_Photos : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    // Store URIs of user selected images
    private var storeUriUserGalleryImages: MutableList<Uri> = mutableListOf()

    //private var storeLayoutUploadedImages: MutableList<ViewGroup> = mutableListOf() PLACEHOLDER

    // Request Codes for image selection from gallery
    private val PICK_IMAGES_CODE = 0

    // View References
    private lateinit var uploadedImageLayout: ViewGroup
    private lateinit var uploadedImage: ImageView
    private lateinit var imagesContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_booking__photos, container, false)

        // View References
        imagesContainer = view.findViewById(R.id.images_container)
        val btnUploadPhoto = view.findViewById<Button>(R.id.btn_uploadPhoto)

        // Button to add upload image
        btnUploadPhoto.setOnClickListener {
            selectImagesIntent()
        }

        return view
    }

    // Intent gallery image selection
    @SuppressLint("NewApi")
    private fun selectImagesIntent(){
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image(s)"), PICK_IMAGES_CODE)
    }

    // After selecting gallery images, this function will execute and display images in layout
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        storeUriUserGalleryImages = mutableListOf()

        // Selecting gallery images
        if (requestCode == PICK_IMAGES_CODE && resultCode == Activity.RESULT_OK){
            if (data!!.clipData != null){
                // for picking multiple images
                val count = data.clipData!!.itemCount
                for (i in 0 until count){
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    // store image uri to list
                    storeUriUserGalleryImages.add(imageUri)
                }
            } else if (data.data != null){
                // for picking single image only
                val imageUri = data.data!!
                // store image uri to list
                storeUriUserGalleryImages.add(imageUri)
            }

            // Update and display the selected images to the layout
            for (image in storeUriUserGalleryImages){
                // Referencing layout and its image view
                uploadedImageLayout = layoutInflater.inflate(R.layout.layout_uploaded_image, null, false) as ViewGroup
                uploadedImage = uploadedImageLayout.findViewById(R.id.img_upload)

                // Set selected images to image view
                uploadedImage.setImageURI(image)

                // Referencing and giving a tag to delete button of the parent layout
                // Ensuring that each image has its own delete button
                val btnDeleteImg = uploadedImageLayout.findViewById<ImageView>(R.id.btn_delete)
                btnDeleteImg.tag = uploadedImageLayout

                // Delete button to delete a certain image
                btnDeleteImg.setOnClickListener {
                    val parentLayout = it.tag as? ViewGroup
                    parentLayout?.let{
                        //val parentLayoutIndex = storeLayoutUploadedImages.indexOf(parentLayout) PLACEHOLDER

                        imagesContainer.removeView(parentLayout)
                        //storeLayoutUploadedImages.removeAt(parentLayoutIndex) PLACEHOLDER
                        //storeUriUserGalleryImages.removeAt(parentLayoutIndex) PLACEHOLDER
                    }
                }

                // Add layout of uploaded image to list
                //storeLayoutUploadedImages.add(uploadedImageLayout) PLACEHOLDER

                // Add and display uploaded image to parent layout
                imagesContainer.addView(uploadedImageLayout)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Booking_Photos.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Booking_Photos().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}