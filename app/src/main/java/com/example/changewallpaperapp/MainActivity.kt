package com.example.changewallpaperapp

import android.Manifest
import android.app.WallpaperManager
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import java.io.File
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var listaImagini = mutableListOf<Uri>()
    var ok = false
    var indexDeAratat : Int = 0
    var sharedPreferences : SharedPreferences? = null
    var timpDefault : Long = 5
    var timp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        when {
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {

            }
            else -> {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
            }
        }

        sharedPreferences = applicationContext.getSharedPreferences("preferences", MODE_PRIVATE)

        if (sharedPreferences!!.contains("uriImagini") ){

            if(!sharedPreferences!!.getString("uriImagini","").equals("")){

                var rawData : String? = sharedPreferences!!.getString("uriImagini","")
             //   var processedData : String? = rawData!!.subSequence(0,rawData.length).toString()

                var arrayImg : List<String> = rawData!!.split(",")

                Toast.makeText(applicationContext,arrayImg.size.toString(),Toast.LENGTH_SHORT).show()
                for (i in 0..(arrayImg.size-1)){
                    listaImagini.add(Uri.parse(arrayImg[i]))
                }

                try{
                    var imgView = findViewById<ImageView>(R.id.imageView)
                    imgView.setImageURI(listaImagini[0])
                }catch (e : Exception){
                    Toast.makeText(applicationContext,"Exceptie",Toast.LENGTH_LONG).show()
                }
            }
        }


    }

    fun setFun(view : View){
        ok=true
        timp = findViewById<EditText>(R.id.editNumber).text.toString()
        var textEdit = findViewById<EditText>(R.id.editNumber).setText("")
    }

    fun pickImages(view : View){
        var intent : Intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        startActivityForResult(intent,1)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1){

            var clipedData = data?.clipData
            if(clipedData != null){

                var count : Int = clipedData.itemCount
                listaImagini.clear()
                for(i in 0..count-1){

                    var img = clipedData.getItemAt(i).uri
                    listaImagini.add(img)

                }
                ok=false

                saveLista()
                var imgView = findViewById<ImageView>(R.id.imageView)
                imgView.setImageURI(listaImagini[0])


            }

        }else{
            Toast.makeText(applicationContext,"Imaginile nu au fost selctate",Toast.LENGTH_LONG).show()
        }
    }

    fun stop(view : View){
        ok=false
    }

    fun arataImagini(view : View){

        if(listaImagini.size != 0){
            if(indexDeAratat + 1 == listaImagini.size){
                indexDeAratat = 0
            }else{
                indexDeAratat++
            }
            var imgView = findViewById<ImageView>(R.id.imageView)
            imgView.setImageURI(listaImagini[indexDeAratat])
            Toast.makeText(applicationContext,"Setat : "+ listaImagini[indexDeAratat].toString(),Toast.LENGTH_LONG).show()
        }
    }

    fun saveLista(){

        var deSalvat = ""
        for(i in 0..(listaImagini.size-1)){
            if(i == listaImagini.size-1) {
                deSalvat += listaImagini[i].toString()
            }else{
                deSalvat += listaImagini[i].toString() + ","
            }

        }
        sharedPreferences!!.edit().clear().apply()
        sharedPreferences!!.edit().putString("uriImagini",deSalvat).apply()
        Toast.makeText(applicationContext,"SAlvat : "+ deSalvat,Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        super.onStop()

        while (ok){
            val randNr = (Math.random() * (listaImagini.size)).toInt()
            val source = ImageDecoder.createSource(this.contentResolver, listaImagini[randNr])
            val bitmap = ImageDecoder.decodeBitmap(source)
            val wallpaperManger : WallpaperManager = WallpaperManager.getInstance(applicationContext)
            wallpaperManger.setBitmap(bitmap)

            if(timp.equals("")){
                Thread.sleep(timpDefault * 1000)
            }else{
                Thread.sleep(timp.toLong()* 1000)
            }
        }
    }

    override fun onPause() {
        super.onPause()

        while (ok){
            val randNr = (Math.random() * (listaImagini.size)).toInt()
            val source = ImageDecoder.createSource(this.contentResolver, listaImagini[randNr])
            val bitmap = ImageDecoder.decodeBitmap(source)
            val wallpaperManger : WallpaperManager = WallpaperManager.getInstance(applicationContext)
            wallpaperManger.setBitmap(bitmap)

            if(timp.equals("")){
                Thread.sleep(timpDefault * 1000)
            }else{
                Thread.sleep(timp.toLong()* 1000)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        val randNr = (Math.random() * (listaImagini.size)).toInt()
        val source = ImageDecoder.createSource(this.contentResolver, listaImagini[randNr])
        val bitmap = ImageDecoder.decodeBitmap(source)
        val wallpaperManger : WallpaperManager = WallpaperManager.getInstance(applicationContext)
        wallpaperManger.setBitmap(bitmap)
    }
}