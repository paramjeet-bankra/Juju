package com.example.videorecordingapplication.presentation.view.signup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.videorecordingapplication.R
import com.example.videorecordingapplication.data.entity.SchoolEntity
import com.example.videorecordingapplication.data.entity.request.StudentSignUp
import com.example.videorecordingapplication.data.localdatasource.DataSource
import com.example.videorecordingapplication.presentation.view.login.UserLogInActivity
import com.example.videorecordingapplication.presentation.view.recommendation.RecommendationViewModel
import io.reactivex.Observable
import kotlin.collections.ArrayList

class StudentSignUpFragment : Fragment() {
    private lateinit var viewModel : StudentSignUpViewModel

    var schoolList : ArrayList<SchoolEntity>? = null

    lateinit var etName : AppCompatEditText
    lateinit var etAge : AppCompatEditText
    lateinit var etMentorName : AppCompatEditText
    lateinit var etPassword : AppCompatEditText
    lateinit var btnSignUp : AppCompatButton
    lateinit var tvLogIn : AppCompatTextView
    lateinit var spinner: Spinner
    lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_sign_up, container, false)
    }

    private fun observeList(){
        viewModel.observeSchoolList().observe(requireActivity(), Observer {
            schoolList?.clear()
            schoolList?.addAll(it)
        })
    }

    private fun observeSignUpResponse(){
        viewModel.observeSignUpResponse().observe(requireActivity(), Observer {
            requireActivity().finish()
            val intent = Intent(requireContext(), UserLogInActivity::class.java)
            intent.putExtra("type", getString(R.string.text_btn_signup_student))
            startActivity(intent)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progress = view.findViewById(R.id.progress)
        etAge = view.findViewById(R.id.et_signup_age)
        etName = view.findViewById(R.id.et_signup_name)
        etMentorName = view.findViewById(R.id.et_signup_mentor_name)
        etPassword = view.findViewById(R.id.et_signup_password)
        btnSignUp = view.findViewById(R.id.btn_signup)
        tvLogIn = view.findViewById(R.id.tv_signup_login)

        spinner = view.findViewById(R.id.spinner_school_name)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.school_array,
            R.layout.layout_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        initializeListener()
        observeList()
        observeSignUpResponse()
        observeErrorState()
    }

    private fun initializeListener(){
        btnSignUp.setOnClickListener{
            if(etName.text.isNullOrBlank() ||
                etPassword.text.isNullOrBlank() ||
                etAge.text.isNullOrBlank()){
                Toast.makeText(requireContext(), "Please fill all mandatory feilds", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            progress.visibility = View.VISIBLE
            viewModel.updateSignUpData(StudentSignUp(
                etAge.text.toString().toInt(), 4, etName.text.toString(),
                1, etPassword.text.toString()))
        }

        tvLogIn.setOnClickListener{
            requireActivity().finish()
            val intent = Intent(requireContext(), UserLogInActivity::class.java)
            intent.putExtra("type", getString(R.string.text_btn_signup_student))
            startActivity(intent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            StudentSignUpFragment().apply {
                arguments = Bundle()
            }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(StudentSignUpViewModel::class.java)
    }

    private fun observeErrorState() {
        viewModel.observeErrorState().observe(requireActivity(), Observer {
            if (!it.status && !it.message.isNullOrBlank()) {
                if (it.message.equals(getString(R.string.error_string))){
                    Toast.makeText(requireContext(), getString(R.string.alternate_error_string), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext()
                        , it.message, Toast.LENGTH_SHORT).show()
                }
            }
            progress.visibility = View.GONE
        })
    }
}
