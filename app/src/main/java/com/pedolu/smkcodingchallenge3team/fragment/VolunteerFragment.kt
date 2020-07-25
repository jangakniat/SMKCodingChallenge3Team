package com.pedolu.smkcodingchallenge3team.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pedolu.smkcodingchallenge3team.R
import com.pedolu.smkcodingchallenge3team.adapter.VolunteerAdapter
import com.pedolu.smkcodingchallenge3team.data.model.room.VolunteerModel
import com.pedolu.smkcodingchallenge3team.util.dismissLoading
import com.pedolu.smkcodingchallenge3team.util.showLoading
import com.pedolu.smkcodingchallenge3team.util.tampilToast
import com.pedolu.smkcodingchallenge3team.viewmodel.VolunteerViewModel
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_volunteer.*
import java.io.File


class VolunteerFragment : Fragment() {
    private val volunteerViewModel by viewModels<VolunteerViewModel>()
    private var volunteerList: MutableList<VolunteerModel> = ArrayList()
    private lateinit var auth: FirebaseAuth
    lateinit var ref: DatabaseReference
    private lateinit var uid: String
    private lateinit var storageRef: StorageReference
    private lateinit var imagesRef: StorageReference
    private lateinit var localImage: File
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_volunteer, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("Users")
        uid = auth.currentUser!!.uid
        retriveRoomVolunteer()
        swipeRefreshLayout.post { callVolunteer() }
        swipeRefreshLayout.setOnRefreshListener {
            callVolunteer()
        }
    }

    private fun retriveRoomVolunteer() {
        volunteerViewModel.init(requireContext(), uid)
        volunteerViewModel.volunteers.observe(
            viewLifecycleOwner,
            Observer { volunteer ->
                if (volunteer.isEmpty()) {
                    callVolunteer()
                } else {
                    showVolunteer(volunteer)
                }
            })
    }


    private fun callVolunteer() {
        setInvisible()
        showLoading(requireContext(), swipeRefreshLayout)
        storageRef = FirebaseStorage.getInstance().reference

        ref.addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                setVisible()
                dismissLoading(swipeRefreshLayout)
                tampilToast(requireContext(), "Database Error")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val volunteer = snapshot.child("Data").getValue(VolunteerModel::class.java)
                    val key = snapshot.key.toString()
                    volunteer?.key = key
                    callVolunteerImage(snapshot.child("Data"))
                    volunteerList.add(volunteer!!)

                }
                volunteerViewModel.addAllData(volunteerList)
                showVolunteer(volunteerList)
                setVisible()
                dismissLoading(swipeRefreshLayout)
            }
        })
    }

    private fun callVolunteerImage(dataSnapshot: DataSnapshot) {
        localImage = File.createTempFile(dataSnapshot.key.toString(), ".png")
        imagesRef = storageRef.child("images/${dataSnapshot.key.toString()}")
        imagesRef.getFile(localImage).addOnSuccessListener {
            val Volunteer =
                VolunteerModel(
                    dataSnapshot.child("name").value.toString(),
                    dataSnapshot.child("telp").value.toString(),
                    dataSnapshot.child("address").value.toString(),
                    localImage.toString(),
                    dataSnapshot.key.toString()
                )
            volunteerViewModel.init(requireContext(), dataSnapshot.key.toString())
            volunteerViewModel.updateData(Volunteer)
            Log.i("img", "firebase local tem file created  created $localImage")
        }.addOnFailureListener { exception ->
            Log.e(
                "firebase ",
                ";local tem file not created  created $exception"
            )
        }
    }

    private fun showVolunteer(volunteerList: List<VolunteerModel>) {
        listVolunteer.layoutManager = LinearLayoutManager(context)
        listVolunteer.adapter =
            VolunteerAdapter(
                requireContext(),
                volunteerList
            ) {
                val volunteer = it
                tampilToast(requireContext(), volunteer.name)
            }
    }

    private fun setVisible() {
        txtVolunteer.visibility = View.VISIBLE
        listVolunteer.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun setInvisible() {
        txtVolunteer.visibility = View.GONE
        listVolunteer.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }
}
