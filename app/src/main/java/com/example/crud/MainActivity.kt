package com.example.crud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.content.DialogInterface
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private lateinit var t_id: EditText
    private lateinit var t_name: EditText
    private lateinit var t_details: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    //method for saving records in database
    fun saveRecord(view: View){
        t_id=findViewById(R.id.t_id)
        t_name=findViewById(R.id.t_name)
        t_details=findViewById(R.id.t_details)
        val id = t_id.text.toString()
        val name = t_name.text.toString()
        val details = t_details.text.toString()
        val databaseHandler: DatabaseHandler= DatabaseHandler(this)
        if(id.trim()!="" && name.trim()!="" && details.trim()!=""){
            val status = databaseHandler.addTask(TaskModelClass(Integer.parseInt(id),name, details))
            if(status > -1){
                Toast.makeText(applicationContext,"record saved",Toast.LENGTH_LONG).show()
                t_id.text.clear()
                t_name.text.clear()
                t_details.text.clear()

                viewRecord(view)
            }
        }else{
            Toast.makeText(applicationContext,"id or name or details cannot be blank",Toast.LENGTH_LONG).show()
        }

    }
    //method for read records from database in ListView
    fun viewRecord(view: View){
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler= DatabaseHandler(this)
        //calling the viewTask method of DatabaseHandler class to read the records
        val task: List<TaskModelClass> = databaseHandler.viewTask()
        val taskArrayId = Array<String>(task.size){"0"}
        val taskArrayName = Array<String>(task.size){"null"}
        val taskArrayDetails = Array<String>(task.size){"null"}
        var index = 0
        for(e in task){
            taskArrayId[index] = e.taskId.toString()
            taskArrayName[index] = e.taskName
            taskArrayDetails[index] = e.taskDetails
            index++
        }
        //creating custom ArrayAdapter
        val listView: ListView = findViewById(R.id.listView)

        val myListAdapter = MyListAdapter(this,taskArrayId,taskArrayName,taskArrayDetails)
        listView.adapter = myListAdapter
    }
    //method for updating records based on Task id
    fun updateRecord(view: View){
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogView)

        val edtId = dialogView.findViewById(R.id.updateId) as EditText
        val edtName = dialogView.findViewById(R.id.updateName) as EditText
        val edtDetails = dialogView.findViewById(R.id.updateDetails) as EditText

        dialogBuilder.setTitle("Update Record")
        dialogBuilder.setMessage("Enter data below")
        dialogBuilder.setPositiveButton("Update", DialogInterface.OnClickListener { _, _ ->

            val updateId = edtId.text.toString()
            val updateName = edtName.text.toString()
            val updateDetails = edtDetails.text.toString()
            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler= DatabaseHandler(this)
            if(updateId.trim()!="" && updateName.trim()!="" && updateDetails.trim()!=""){
                //calling the updateTask method of DatabaseHandler class to update record
                val status = databaseHandler.updateTask(TaskModelClass(Integer.parseInt(updateId),updateName, updateDetails))
                if(status > -1){
                    viewRecord(view)
                    Toast.makeText(applicationContext,"record updated",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(applicationContext,"id or name or email cannot be blank",Toast.LENGTH_LONG).show()
            }

        })
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            //pass
        })
        val b = dialogBuilder.create()
        b.show()
    }
    //method for deleting records based on id
    fun deleteRecord(view: View){
        //creating AlertDialog for taking Task id
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_dialog, null)
        dialogBuilder.setView(dialogView)

        val dltId = dialogView.findViewById(R.id.deleteId) as EditText
        dialogBuilder.setTitle("Delete Record")
        dialogBuilder.setMessage("Enter id below")
        dialogBuilder.setPositiveButton("Delete", DialogInterface.OnClickListener { _, _ ->

            val deleteId = dltId.text.toString()
            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler= DatabaseHandler(this)
            if(deleteId.trim()!=""){
                //calling the deleteTask method of DatabaseHandler class to delete record
                val status = databaseHandler.deleteTask(TaskModelClass(Integer.parseInt(deleteId),"",""))
                if(status > -1){
                    viewRecord(view)
                    Toast.makeText(applicationContext,"record deleted",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(applicationContext,"id or name or email cannot be blank",Toast.LENGTH_LONG).show()
            }

        })
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
            //pass
        })
        val b = dialogBuilder.create()
        b.show()

    }
}