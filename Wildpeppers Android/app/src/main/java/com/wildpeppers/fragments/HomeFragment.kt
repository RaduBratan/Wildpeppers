package com.wildpeppers.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.wildpeppers.R
import com.wildpeppers.adapters.WallpapersAdapter
import com.wildpeppers.models.WallpapersModel
import java.util.*

class HomeFragment : Fragment(), (WallpapersModel) -> Unit {

    //var wallpaperList: MutableList<WallpapersModel>? = null
    var wallpaperList: List<WallpapersModel> = ArrayList()
    private var favList: List<WallpapersModel>? = null

    private var recyclerView: RecyclerView? = null

    var adapter: WallpapersAdapter? = null
    private var dbAllWallpapers: DatabaseReference? = null

    var progressBar: ProgressBar? = null
    private var navController: NavController? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        favList = ArrayList()
        wallpaperList = ArrayList()

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = GridLayoutManager(activity, 2)

        //adapter = WallpapersAdapter(activity, wallpaperList)
        adapter = WallpapersAdapter(wallpaperList, this)
        recyclerView?.adapter = adapter

        //navController = Navigation.findNavController(view)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        //navController = Navigation.findNavController(findViewById(R.id.nav_host_fragment))

        progressBar = view.findViewById(R.id.progressbar)
        dbAllWallpapers = FirebaseDatabase.getInstance().getReference("all_images_list") //was: .child(category)
        progressBar?.visibility = View.VISIBLE

        dbAllWallpapers!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                progressBar?.visibility = View.GONE
                if (dataSnapshot.exists()) {
                    for (ws in dataSnapshot.children) { //ws = wallpaper snapshot
                        val id = ws.key
                        val title = ws.child("title").getValue(String::class.java)
                        val desc = ws.child("desc").getValue(String::class.java)
                        val url = ws.child("url").getValue(String::class.java)
                        val w = WallpapersModel(id, title, desc, url)
                        (wallpaperList as ArrayList<WallpapersModel>).add(w)
                        //wallpaperList.add(w)
                    }
                    adapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun invoke(wallpaper: WallpapersModel) {
        val action = HomeFragmentDirections.actionHomeFragmentToSetWallpaperFragment(wallpaper.url)
        navController!!.navigate(action)
    }
}