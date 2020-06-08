package com.example.videorecordingapplication.presentation.view.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.*
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videorecordingapplication.R
import com.example.videorecordingapplication.data.entity.SchoolEntity
import com.example.videorecordingapplication.data.entity.request.ModeratorSignUp
import com.example.videorecordingapplication.data.localdatasource.DataSource
import com.example.videorecordingapplication.presentation.view.login.UserLogInActivity
import com.google.android.material.textfield.TextInputLayout

class TeacherSignUpFragment : Fragment(), SchoolListAdapter.OnItemClickListener{
    private lateinit var viewModel : TeacherSignUpViewModel

    var schoolList : ArrayList<SchoolEntity>? = null

    lateinit var etName : AppCompatEditText
    lateinit var etEmail : AppCompatEditText
    lateinit var etPassword : AppCompatEditText
    lateinit var btnSignUp : AppCompatButton
    lateinit var tvLogIn : AppCompatTextView
    private var rvPopUp: RecyclerView? = null
    private var popupWindow: PopupWindow? = null
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
        return inflater.inflate(R.layout.fragment_teacher_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progress = view.findViewById(R.id.progress)
        etEmail = view.findViewById(R.id.et_signup_email)
        etName = view.findViewById(R.id.et_signup_name)
        etPassword = view.findViewById(R.id.et_signup_name)
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

        //setSchoolAdapter()
        initializeListener()
        observeList()
        observeSignUpResponse()
        observeErrorState()
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
            intent.putExtra("type", getString(R.string.text_btn_signup_teacher))
            startActivity(intent)
        })
    }

    private fun initializeListener(){
        btnSignUp.setOnClickListener{
            if(etEmail.text.isNullOrBlank() ||
                    etName.text.isNullOrBlank() ||
                    etPassword.text.isNullOrBlank() ||
                (spinner.selectedItem.toString().isNullOrBlank()&&spinner.selectedItem.toString().equals("Select School"))){
                Toast.makeText(requireContext(), "Please fill all mandatory feilds", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progress.visibility = View.VISIBLE
            viewModel.updateSignUpData(
                ModeratorSignUp(
                   etEmail.text.toString(),
                etName.text.toString(),
                1,
                etPassword.text.toString()))
        }

        tvLogIn.setOnClickListener{
            requireActivity().finish()
            val intent = Intent(requireContext(), UserLogInActivity::class.java)
            intent.putExtra("type", getString(R.string.text_btn_signup_teacher))
            startActivity(intent)
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(TeacherSignUpViewModel::class.java)
    }

    private fun observeErrorState() {
        viewModel.observeErrorState().observe(requireActivity(), Observer {
            if (!it.status && !it.message.isNullOrBlank()) {
                if (it.message.equals(getString(R.string.error_string))){
                    Toast.makeText(requireContext(), getString(R.string.alternate_error_string), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
            progress.visibility = View.GONE
        })
    }

    override fun onItemClick(school: SchoolEntity) {
     //   etSchoolName.setText(school.name)
    }
}
