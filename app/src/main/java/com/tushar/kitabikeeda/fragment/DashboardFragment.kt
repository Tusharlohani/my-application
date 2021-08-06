package com.tushar.kitabikeeda.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.tushar.kitabikeeda.R
import com.tushar.kitabikeeda.adapter.DashboardRecyclerAdapter
import com.tushar.kitabikeeda.model.Book
import com.tushar.kitabikeeda.util.ConnectionManager
import org.json.JSONException


/*
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
*/


class DashboardFragment : Fragment() {
    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    //lateinit var btnCheckInternet: Button
    lateinit var progressBar:ProgressBar
    lateinit var progresslayout:RelativeLayout



    //STATIC LIST OF BOOKS

    val bookInfoList = arrayListOf<Book>()

    /*Book("P.S. I love You", "Cecelia Ahern", "Rs. 299", "4.5", R.drawable.ps_ily),
        Book("The Great Gatsby", "F. Scott Fitzgerald", "Rs. 399", "4.1", R.drawable.great_gatsby),
        Book("Anna Karenina", "Leo Tolstoy", "Rs. 199", "4.3", R.drawable.anna_kare),
        Book("Madame Bovary", "Gustave Flaubert", "Rs. 500", "4.0", R.drawable.madame),
        Book("War and Peace", "Leo Tolstoy", "Rs. 249", "4.8", R.drawable.war_and_peace),
        Book("Lolita", "Vladimir Nabokov", "Rs. 349", "3.9", R.drawable.lolita),
        Book("Middlemarch", "George Eliot", "Rs. 599", "4.2", R.drawable.middlemarch),
        Book("The Adventures of Huckleberry Finn", "Mark Twain", "Rs. 699", "4.5", R.drawable.adventures_finn),
        Book("Moby-Dick", "Herman Melville", "Rs. 499", "4.5", R.drawable.moby_dick),
        Book("The Lord of the Rings", "J.R.R Tolkien", "Rs. 749", "5.0", R.drawable.lord_of_rings)
    )*/

/*
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        recyclerDashboard = view.findViewById(R.id.recyclerview)
        //btnCheckInternet = view.findViewById(R.id.btnCheckInternet)
        progressBar=view.findViewById(R.id.Progressbar)
        progresslayout=view.findViewById(R.id.Progresslayout)
        progresslayout.visibility=View.VISIBLE

//BUTTON FOR INTERNET CONNECTIVITY
       /* btnCheckInternet.setOnClickListener() {
            if (ConnectionManager().checkConnectivity(activity as Context)) {
                //internet is available
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Success")
                dialog.setMessage("Internet Connection Found")
                dialog.setPositiveButton("Ok") { text, listener ->
                    //do nothing
                }
                dialog.setNegativeButton("Cancel") { text, listner ->
                    //do nothing
                }
                dialog.create()
                dialog.show()
            } else {
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection Not Found")
                dialog.setPositiveButton("Ok") { text, listener ->
                    //do nothing
                }
                dialog.setNegativeButton("Cancel") { text, listner ->
                    //do nothing
                }
                dialog.create()
                dialog.show()
                //Internet is not available
            }
        }*/
        layoutManager = LinearLayoutManager(activity)
        val queue=Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if (ConnectionManager().checkConnectivity(activity as Context)){
            val jsonObjectRequest = object :
                JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

                    try{
                        progresslayout.visibility=View.GONE
                        val success = it.getBoolean("success")
                        //handle responses
                        if (success) {
                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val bookJsonObject = data.getJSONObject(i)
                                val bookObject = Book(
                                    bookJsonObject.getString("book_id"),
                                    bookJsonObject.getString("name"),
                                    bookJsonObject.getString("author"),
                                    bookJsonObject.getString("rating"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("image")
                                )
                                bookInfoList.add(bookObject)
                                recyclerAdapter = DashboardRecyclerAdapter(activity as Context, bookInfoList)
                                recyclerDashboard.adapter = recyclerAdapter
                                recyclerDashboard.layoutManager = layoutManager
                                /*recyclerDashboard.addItemDecoration(
                                    DividerItemDecoration(
                                        recyclerDashboard.context,
                                        (layoutManager as LinearLayoutManager).orientation
                                    )
                                )*/
                            }
                        } else {
                            Toast.makeText(
                                activity as Context, "Some Error Occurred!!!!", Toast.LENGTH_SHORT).show()
                        }
                    }catch (e: JSONException){
                        Toast.makeText(activity as Context,"Some unexppected error occured!!!",Toast.LENGTH_SHORT).show()
                    }


                }, Response.ErrorListener { //handle errors
                    Toast.makeText(activity as Context,"Volley error occured!!!",Toast.LENGTH_SHORT).show()
                })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "c786423b26d082"
                    return headers
                }
                /*fun onErrorResponse(error: VolleyError) {
                    Log.d("Volly Error", error.toString())
                    val networkResponse = error.networkResponse
                    if (networkResponse != null) {
                        Log.d("Status code", networkResponse.statusCode.toString())
                    }
                }*/
            }
            queue.add(jsonObjectRequest)

        }else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
                //do nothing
            }
            dialog.setNegativeButton("Exit") { text, listner ->
                ActivityCompat.finishAffinity(activity as Activity)
                //do nothing
            }
            dialog.create()
            dialog.show()
        }

        //        if(ConnectionManager().checkConnectivity(activity as Context)){
        return view
    }
//        }
}



























    /*companion object {
        *//**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         *//*
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/
