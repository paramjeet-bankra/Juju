package com.example.videorecordingapplication.presentation.view.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.*
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.Toast
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

   // lateinit var tilSchoolName : TextInputLayout
    //lateinit var etSchoolName : AppCompatEditText
    lateinit var etName : AppCompatEditText
    lateinit var etEmail : AppCompatEditText
    lateinit var etPassword : AppCompatEditText
    lateinit var btnSignUp : AppCompatButton
    lateinit var tvLogIn : AppCompatTextView
    private var rvPopUp: RecyclerView? = null
    private var popupWindow: PopupWindow? = null
    lateinit var spinner: Spinner

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
        //tilSchoolName = view.findViewById(R.id.til_school_name)
        //etSchoolName = view.findViewById(R.id.et_signup_school_name)
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
    }

    private fun observeList(){
        viewModel.observeSchoolList().observe(requireActivity(), Observer {
            schoolList?.clear()
            schoolList?.addAll(it)
            //setSchoolAdapter()
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
    
    private fun setSchoolAdapter(){
        schoolList?.apply {
            val names : List<String> = schoolList!!.map { it -> it.name}
            val adapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_spinner_item,
                names)
            //etSchoolName.setAdapter(adapter)
        }
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

    private fun getPopupWindow(): PopupWindow? {
        if (popupWindow == null) {
            val inflater =
                context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view: View =
                inflater.inflate(R.layout.pop_up_menu, null)

            val displayMetrics = context!!.resources.displayMetrics
            popupWindow = PopupWindow(activity)
            popupWindow?.setWidth(WindowManager.LayoutParams.WRAP_CONTENT)
            popupWindow?.setHeight(Math.round(150 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).toInt())
            popupWindow?.setFocusable(false)
            popupWindow?.setOutsideTouchable(true)

            val layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            rvPopUp = view.findViewById(R.id.rv_suggestion)
            rvPopUp?.setLayoutManager(layoutManager)
            val dividerItemDecoration = DividerItemDecoration(
                rvPopUp?.getContext(),
                layoutManager.orientation
            )
            rvPopUp?.addItemDecoration(dividerItemDecoration)
            rvPopUp?.adapter = SchoolListAdapter(requireContext(), this,DataSource.getSchoolList())
            popupWindow?.setContentView(view)
        }
        return popupWindow
    }

    /*fun showPopUpMenu() {
        if (isAdded) {
            if (etSchoolName.hasFocus()) {
                Handler().postDelayed({
                    if (isAdded) {
                        etSchoolName.requestFocus()
                        getPopupWindow()!!.showAsDropDown(etSchoolName, 0, 0)
                    }
                }, 300)
            }
        }
    }*/

    override fun onItemClick(school: SchoolEntity) {
     //   etSchoolName.setText(school.name)
    }
}
