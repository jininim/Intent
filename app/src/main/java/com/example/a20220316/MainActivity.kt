package com.example.a20220316

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.example.a20220316.databinding.ActivityMainBinding
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        //파일만들기
//        val timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val storageDir : File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        val file = File.createTempFile(
//            "JPEG_${timeStamp}_",
//            ".jpg",
//            storageDir
//        )
//
//        val filePath  = file.absolutePath
//        val photoURI: Uri = FileProvider.getUriForFile(
//            this,
//            "com.example.a20220316.fileprovider" , file
//        )
//        val option = BitmapFactory.Options()
//        option.inSampleSize = 10
//        val bitmap = BitmapFactory.decodeFile(filePath,option)
//        bitmap?.let {
//            binding.imageview1.setImageBitmap(bitmap)
//        }
        //주소록
        binding.btn1.setOnClickListener {
            startActivity(Intent(Intent.ACTION_PICK,ContactsContract.CommonDataKinds.Phone.CONTENT_URI))
        }
        //갤러리
        binding.btn2.setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),30)
            intent.type = "image/*"
        }
        //카메라
        binding.btn3.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent , 10)
        }
        //지도 앱
        binding.btn4.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:37.5662952,126.9779451"))
            startActivity(intent)
        }
        //전화
        binding.btn5.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL,Uri.parse("tel:01-123"))
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //사진 정보 받아오기
        if(requestCode == 10 && resultCode == Activity.RESULT_OK){
//            //사진 데이터 가져오기
             val bitmap = data?.getExtras()?.get("data") as Bitmap
            binding.imageview1.setImageBitmap(bitmap)


        }
        //주소록 정보 받아오기
        if(requestCode == 20 && resultCode == Activity.RESULT_OK){

        }
        //갤러리 이미지 불러오기
        if(requestCode == 30 && resultCode == Activity.RESULT_OK){
            try {
                // insamplesize 비율 지정
                val calRatio = calculateInSampleSize(data!!.data!!,resources.getDimensionPixelSize(
                    com.google.android.material.R.dimen.design_fab_image_size),
                resources.getDimensionPixelSize(com.google.android.material.R.dimen.design_fab_image_size))
                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio
                //이미지 불러오기
                var inputStream = contentResolver.openInputStream(data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream,null,option)
                inputStream!!.close()
                inputStream = null
                bitmap?.let {
                    binding.imageview.setImageBitmap(bitmap)
                }
            }catch (e : Exception){
                e.printStackTrace()
            }

        }
    }

    //이미지 크기 줄이기
    private fun calculateInSampleSize(fileUri : Uri, reqWidth: Int , reqHeight : Int) :Int {

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try {
            var inputStream = contentResolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream,null,options)
            inputStream!!.close()
            inputStream = null
        }catch (e: Exception){
            e.printStackTrace()
        }
        val (height: Int , width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        //insamplesize 비율 계산
        if(height > reqHeight || width > reqWidth){
            val halfHeight : Int = height /2
            val halfWidth : Int = width /2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth){
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}