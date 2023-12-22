package com.example.todoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.todoapp.databinding.FragmentAddToDoPopUpBinding
import com.example.todoapp.utils.ToDo
import com.google.android.material.textfield.TextInputEditText


class AddToDoPopUpFragment : DialogFragment() {
    private lateinit var binding: FragmentAddToDoPopUpBinding
    private lateinit var listener: Dialogclick
    private var data: ToDo?=null

    fun setListening(listener: Dialogclick){
        this.listener=listener
    }

    companion object{
        const val TAG="AddToDoPopUpFragment"

        @JvmStatic
        fun newInst(taskId: String, task: String) = AddToDoPopUpFragment().apply {
            arguments = Bundle().apply {
                putString("taskId", taskId)
                putString("task", task)
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAddToDoPopUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments!=null){
            data = ToDo(arguments?.getString("taskId").toString(), arguments?.getString("task").toString())
            binding.todoEt.setText(data?.task)
        }
        regEvents()
    }
    private fun regEvents(){
        binding.nextbtn.setOnClickListener {
            val task=binding.todoEt.text.toString().trim()
            if (task.isNotEmpty()){
                if (arguments==null){
                    listener.saveTask(task, binding.todoEt)
                }else{
                    data?.task = task
                    listener.updateTask(data!!, binding.todoEt)
                }

            }else{
                Toast.makeText(context, "Empty field", Toast.LENGTH_SHORT).show()
            }
        }
        binding.closeTask.setOnClickListener {
            dismiss()
        }
    }
    interface Dialogclick{
        fun saveTask(todo:String, todoEd: TextInputEditText)
        fun updateTask(data:ToDo, todoEd: TextInputEditText)
    }
}