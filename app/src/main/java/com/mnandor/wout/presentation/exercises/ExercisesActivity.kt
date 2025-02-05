package com.mnandor.wout.presentation.exercises

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mnandor.wout.WoutApplication
import com.mnandor.wout.data.entities.Completion
import com.mnandor.wout.data.entities.Exercise
import com.mnandor.wout.databinding.ActivityConfigBinding
import com.mnandor.wout.databinding.DialogEditExerciseBinding
import com.mnandor.wout.databinding.DialogEditLogBinding

class ExercisesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfigBinding

    private val viewModel: ExercisesViewModel by viewModels {
        ExercisesViewModelFactory((application as WoutApplication).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConfigBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val recyclerView = binding.exerciseTemplatesRecycle
        val adapter = ExercisesRecyclerAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.allTemplates.observe(this, Observer { items ->
            items?.let{adapter.setItems(items)}
            adapter.notifyDataSetChanged()
        })

        adapter.setEditCallback {
            editExerciseName(it)
        }

        setClickListeners()
    }

    public fun editExerciseName(exercise: Exercise){

        val settingsDialog = Dialog(this)

        val dialogBinding = DialogEditExerciseBinding.inflate(layoutInflater)

        with(dialogBinding){
            dialogLogName.text = exercise.name
            dialogLogDateET.setText(exercise.name)


            dialogCancelButton.setOnClickListener { settingsDialog.dismiss() }
            dialogChangeButton.setOnClickListener {

                val oldName = exercise.name
                val newName = dialogLogDateET.text.toString()

                viewModel.rename(oldName, newName)

                settingsDialog.dismiss()

            }
        }



        settingsDialog.setContentView(dialogBinding.root)


        settingsDialog.show()
    }

    private fun setClickListeners(){
        val switchTime = binding.switchTime
        val switchDist = binding.switchDist
        val switchMass = binding.switchMass
        val switchReps = binding.switchReps
        val switchSets = binding.switchSets

        val exNameET = binding.newExTemplateET


        binding.button.setOnClickListener {
            viewModel.insert(
                Exercise(
                exNameET.text.toString(),
                switchTime.isChecked,
                switchDist.isChecked,
                switchMass.isChecked,
                switchSets.isChecked,
                switchReps.isChecked,
                false
            )
            )
        }

        binding.button.setOnLongClickListener {
            return@setOnLongClickListener true // yes, consume event
        }

    }


}