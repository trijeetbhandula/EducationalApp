package com.example.educationalapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.educationalapp.database.AppDatabase
import com.example.educationalapp.database.User
import com.example.educationalapp.database.UserDao
import com.example.educationalapp.databinding.FragmentGameBinding
import kotlinx.coroutines.launch

class GameFragment : Fragment() {


    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var username: String

    private var selectedValue: Int = 5

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val sharedPrefs = context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
        selectedValue = sharedPrefs.getInt("selected_value", 5)
    }

    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private var score = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {


        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
            inflater, R.layout.fragment_game, container, false
        )

        db = AppDatabase.getDatabase(requireContext())
        userDao = db.userDao()

        username = arguments?.getString(ARG_USERNAME) ?: ""

        // Shuffles the questions and sets the question index to the first question.
        randomizeQuestions()

        // Bind this fragment class to the layout
        binding.game = this

        // Set the onClickListener for the submitButton
        binding.submitButton.setOnClickListener {
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            // Do nothing if nothing is checked (id == -1)
            if (checkedId != -1) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                    R.id.thirdAnswerRadioButton -> answerIndex = 2
                    R.id.fourthAnswerRadioButton -> answerIndex = 3
                }
                // The first answer in the original question is always the correct one, so if our
                // answer matches, we have the correct answer.
                if (answers[answerIndex] == currentQuestion.answers[0]) {
                    score++
                } else {
                    Toast.makeText(activity, "Incorrect answer", Toast.LENGTH_SHORT)
                        .show()
                }
                updateQuestion(binding)
            }
            saveUserScore(username, score)
        }
        return binding.root
    }

    private fun saveUserScore(username: String, score: Int) {
        lifecycleScope.launch {
            val user = User(username = username, score = score)
            userDao.insert(user)
        }
    }

    private fun updateQuestion(binding: FragmentGameBinding) {
        questionIndex++
        if (questionIndex < selectedValue) {
            currentQuestion = questions[questionIndex]
            setQuestion()
            binding.invalidateAll()
        } else {
            // Log the score and message
            Log.d("GameFragment", "Quiz Completed! Score: $score")
            Toast.makeText(
                activity, "Quiz Completed! Score: $score", Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(activity, UserStatsActivity::class.java)
            startActivity(intent)
        }
    }

    // randomize the questions and set the first question
    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    // Sets the question and randomizes the answers.  This only changes the data, not the UI.
    // Calling invalidateAll on the FragmentGameBinding updates the data.
    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        // randomize the answers into a copy of the array
        answers = currentQuestion.answers.toMutableList()
        // and shuffle them
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.title_educational_app_question, questionIndex + 1, selectedValue)
    }

    companion object {
        const val ARG_USERNAME = "username"
    }
}