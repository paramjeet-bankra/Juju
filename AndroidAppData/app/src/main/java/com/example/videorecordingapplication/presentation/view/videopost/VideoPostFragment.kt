package com.example.videorecordingapplication.presentation.view.videopost

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.FileProvider.getUriForFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videorecordingapplication.BuildConfig
import com.example.videorecordingapplication.R
import com.example.videorecordingapplication.data.entity.SchoolEntity
import com.example.videorecordingapplication.data.entity.VideoDataUpdateRequest
import com.example.videorecordingapplication.data.entity.VideoEntity
import com.example.videorecordingapplication.data.localdatasource.DataSource
import com.example.videorecordingapplication.data.localdatasource.UserData
import com.example.videorecordingapplication.presentation.view.GlideApp
import com.example.videorecordingapplication.presentation.view.profile.ProfileFragment
import com.example.videorecordingapplication.presentation.view.signup.SchoolListAdapter
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import kotlin.random.Random

class VideoPostFragment : Fragment(), SchoolListAdapter.OnItemClickListener {
    lateinit var ivProfile: AppCompatImageView
    lateinit var etCaption: AppCompatEditText
    lateinit var btnSendToMentor: AppCompatButton
    lateinit var ivRight: AppCompatImageView
    lateinit var tvTagFriends: AppCompatTextView
    private lateinit var viewModel: VideoPostViewModel
    private var rvPopUp: RecyclerView? = null
    private var popupWindow: PopupWindow? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(VideoPostViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().actionBar?.title = getString(R.string.title_post)

        ivProfile = view.findViewById(R.id.iv_profile)
        etCaption = view.findViewById(R.id.et_caption)
        btnSendToMentor = view.findViewById(R.id.btn_mentor)
        ivRight = view.findViewById(R.id.iv_right)
        tvTagFriends = view.findViewById(R.id.tv_tag_friends)
        initListeners()

        if (!arguments?.getString("url").isNullOrBlank()) {
            viewModel.extractFrames(arguments?.getString("url"))

            try {
                GlideApp.with(requireActivity())
                    .load(viewModel.localImageUrl)
                    .into(ivProfile)
            } catch (e: Exception) {
                ivProfile.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_profile_default))
                e.printStackTrace()
            }
        }
    }

    fun extractFrames(uri: String?): Bitmap {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(uri, HashMap<String, String>())
        var bitmap: Bitmap? = null
        var index = 0

        /*
        * 1000 is multiplied with video duration to convert duration to microseconds as
        * MediaMetaDataRetriever.getFrameAtTime accepts time in microseconds
        */
        var videoDuration: Long = (mediaMetadataRetriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_DURATION
        )).toLong() * 1000

        val start = ((10 * videoDuration) / 100)
        bitmap = mediaMetadataRetriever.getFrameAtTime(start)

        mediaMetadataRetriever.release()
        return bitmap
    }

    private fun initListeners() {
        btnSendToMentor.setOnClickListener {
            Toast.makeText(requireContext(), "Video Uploading started", Toast.LENGTH_SHORT).show()
           // getMultipartData("Video", arguments?.getString("url"))
            //getMultipartData("Image", viewModel.localImageUrl)

            UserData.videoList.add(
                VideoEntity(UserData.age,
                    etCaption.text.toString(),
                    arguments?.getString("url")!!,
                    UserData.mentorId,
                    UserData.mentorName,
            "pending",
                    UserData.studentId,
                    UserData.name,
                    viewModel.localImageUrl!!,
                    etCaption.text.toString(),
                    UserData.counter+1))

            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment())
                .commit()
        }
        ivRight.setOnClickListener {
            showPopUpMenu()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(filePath: String?) =
            VideoPostFragment()
                .apply {
                    val bundle = Bundle()
                    bundle.putString("url", filePath)
                    arguments = bundle
                }
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
            popupWindow?.setHeight(
                Math.round(150 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).toInt()
            )
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
            rvPopUp?.adapter = SchoolListAdapter(requireContext(), this, DataSource.getFriendList())
            popupWindow?.setContentView(view)
        }
        return popupWindow
    }

    fun showPopUpMenu() {
        if (isAdded) {
            Handler().postDelayed({
                if (isAdded) {
                    getPopupWindow()!!.showAsDropDown(tvTagFriends, 0, 0)
                }
            }, 300)
        }
    }

    override fun onItemClick(friend: SchoolEntity) {
        tvTagFriends.setText(friend.name)
        popupWindow?.dismiss()
    }

    private fun getMultipartData(type : String, url : String?){
        if (!url.isNullOrBlank()) {
            val newFile = File(url)
            val contentUri : Uri = getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.provider", newFile)

            val requestFile: RequestBody = RequestBody.create(
                MediaType.parse(requireActivity().getContentResolver().getType(contentUri)!!), newFile
            )

            val body : MultipartBody.Part
            val name_prefix : String
            if (type == "Video"){
                name_prefix = "JujuPlusVideo_"
                body = MultipartBody.Part.createFormData("video", newFile.getName(), requestFile)
            } else {
                name_prefix = "JujuPlusImage_"
                body = MultipartBody.Part.createFormData("image", newFile.getName(), requestFile)
            }

            val description: RequestBody = RequestBody.create(
                MultipartBody.FORM, "desc")

            val map = HashMap<String, RequestBody>();
            map.put("key", toRequestBody("${name_prefix}${System.currentTimeMillis()}"))
            viewModel.uploadData("${name_prefix}${System.currentTimeMillis()}",body, description)
        }
    }

    fun toRequestBody(value:String) : RequestBody {
        return RequestBody.create(
            MediaType.parse("text/plain"),
            value
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PostFragmentInterationListener)
            (context as PostFragmentInterationListener).onPostFragmentVisible()
    }

    interface PostFragmentInterationListener{
        fun onPostFragmentVisible()
    }
}
