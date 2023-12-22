package com.example.todoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentHomeBinding
import com.example.todoapp.databinding.FragmentSigninBinding
import com.example.todoapp.utils.ToDo
import com.example.todoapp.utils.ToDoAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment(), AddToDoPopUpFragment.Dialogclick, ToDoAdapter.todoAdapter {
    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var databaseRef: DatabaseReference
    private lateinit var binding: FragmentHomeBinding
    private var popUpFragment: AddToDoPopUpFragment?=null
    private lateinit var adapter: ToDoAdapter
    private lateinit var list:MutableList<ToDo>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        regClick()
        getDataFromFb()
    }
    private fun init(view: View){
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef=FirebaseDatabase.getInstance().reference
            .child("Tasks").child(auth.currentUser?.uid.toString()) //database creation and insert values
        binding.recview.setHasFixedSize(true)
        binding.recview.layoutManager=LinearLayoutManager(context)  //rec view initializing
        list= mutableListOf()
        adapter= ToDoAdapter(list)
        adapter.setListener(this)
        binding.recview.adapter=adapter

    }
    private fun regClick(){
        binding.addbtn.setOnClickListener {
            if (popUpFragment!=null){
                childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
            }
            popUpFragment = AddToDoPopUpFragment()
            popUpFragment!!.setListening(this)
            popUpFragment!!.show(
                childFragmentManager,
                AddToDoPopUpFragment.TAG
            )
        }


    }
    private fun getDataFromFb(){
        databaseRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (taskShot in snapshot.children){
                    val todoTask =taskShot.key?.let {
                        ToDo(it, taskShot.value.toString())
                    }
                    if (todoTask != null){
                        list.add(todoTask)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun saveTask(todo: String, todoEd: TextInputEditText) {
        databaseRef.push().setValue(todo).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context, "Task saved successfully", Toast.LENGTH_LONG).show()
                todoEd.text=null
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_LONG).show()
            }
            popUpFragment!!.dismiss()
        }
    }

    override fun updateTask(data: ToDo, todoEd: TextInputEditText) {
        val map=HashMap<String, Any>()
        map[data.taskId]=data.task
        databaseRef.updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context, "Updated successfully", Toast.LENGTH_LONG).show()

            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_LONG).show()
            }
            todoEd.text=null
            popUpFragment?.dismiss()
        }
    }


    override fun onDelete(todoData: ToDo) {
        databaseRef.child(todoData.taskId).removeValue().addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onEdit(todoData: ToDo) {
        if (popUpFragment != null){
            childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
        }
        popUpFragment = AddToDoPopUpFragment.newInst(todoData.taskId, todoData.task)
        popUpFragment!!.setListening(this)
        popUpFragment!!.show(childFragmentManager, AddToDoPopUpFragment.TAG)
    }

}