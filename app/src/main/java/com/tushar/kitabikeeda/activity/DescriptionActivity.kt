package com.tushar.kitabikeeda.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import com.tushar.kitabikeeda.R
import com.tushar.kitabikeeda.util.ConnectionManager
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {
    lateinit var txtBookName:TextView
    lateinit var txtBookAuthor:TextView
    lateinit var txtBookPrice:TextView
    lateinit var txtBookRating:TextView
    lateinit var imgBookImage:ImageView
    lateinit var txtBookdes:TextView
    lateinit var btnaddtofav:Button
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var toolbar: Toolbar

    var bookId:String="100"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        txtBookName=findViewById(R.id.txtBookName)
        txtBookAuthor=findViewById(R.id.txtBookAuthor)
        txtBookPrice=findViewById(R.id.txtBookPrice)
        txtBookRating=findViewById(R.id.txtBookRating)
        imgBookImage=findViewById(R.id.imgBookImage)
        txtBookdes=findViewById(R.id.txtbookdes)
        btnaddtofav=findViewById(R.id.btnAddtofav)
        progressBar=findViewById(R.id.progressBar)
        progressBar.visibility= View.VISIBLE
        progressLayout=findViewById(R.id.progLayout)
        progressLayout.visibility=View.VISIBLE
        toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Book Details"


        if(intent!=null){
            bookId= intent.getStringExtra("book_id").toString()
        }else{
            finish()
            Toast.makeText(this@DescriptionActivity,"Some unexpected error occured",Toast.LENGTH_SHORT).show()
        }
        if(bookId=="100"){
            finish()
            Toast.makeText(this@DescriptionActivity,"Some unexpected error occured",Toast.LENGTH_SHORT).show()
        }
        val queue=Volley.newRequestQueue(this@DescriptionActivity)
        val url="http://13.235.250.119/v1/book/get_book/"
        val jsonParams=JSONObject()
                jsonParams.put("book_id",bookId,)
        if(ConnectionManager().checkConnectivity(this@DescriptionActivity)){
            val jsonRequest=object:JsonObjectRequest(Method.POST,url,jsonParams,Response.Listener{
                try {
                    val success=it.getBoolean("success")
                    if(success) {
                        val bookJsonObject=it.getJSONObject("book_data")
                        progressLayout.visibility=View.GONE
                        Picasso.get().load(bookJsonObject.getString("image")).error(R.drawable.default_book_cover).into(imgBookImage)
                        txtBookName.text=bookJsonObject.getString("name")
                        txtBookAuthor.text=bookJsonObject.getString("author")
                        txtBookPrice.text=bookJsonObject.getString("price")
                        txtBookRating.text=bookJsonObject.getString("rating")
                        txtBookdes.text=bookJsonObject.getString("description")

                    }else{
                        Toast.makeText(
                            this@DescriptionActivity, "Some Error Occurred!!!!", Toast.LENGTH_SHORT).show()
                    }
                }catch (e:Exception){
                    Toast.makeText(this@DescriptionActivity,"Some error occurred!!",Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(this@DescriptionActivity,"Volley Error $it",Toast.LENGTH_SHORT).show()

            }){

                override fun getHeaders(): MutableMap<String, String> {
                    val headers=HashMap<String,String>()
                    headers["Content-type"]="application/json"
                    headers["token"]="c786423b26d082"
                    return headers
                }
            }
            queue.add(jsonRequest)

        }else{
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
                //do nothing
            }
            dialog.setNegativeButton("Exit") { text, listner ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
                //do nothing
            }
            dialog.create()
            dialog.show()
        }
        }

    }