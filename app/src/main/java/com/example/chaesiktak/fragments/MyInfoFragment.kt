package com.example.chaesiktak.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaesiktak.BookmarkActivity
import com.example.chaesiktak.NoticeBoard
import com.example.chaesiktak.ProfileEditActivity
import com.example.chaesiktak.R
import com.example.chaesiktak.RecentAdapter
import com.example.chaesiktak.RecentRecipeData
import com.example.chaesiktak.ResetPassword
import com.example.chaesiktak.account_deactivation

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyInfoFragment : Fragment() {

    private lateinit var nicknameTextView: TextView

    // 최근 본 항목 받아오기
    private lateinit var recentRecyclerView: RecyclerView
    private lateinit var recentAdapter: RecentAdapter
    private val recentItems = listOf(
        RecentRecipeData(R.drawable.food1)
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_info, container, false)

        // Nickname 텍스트뷰
        nicknameTextView = view.findViewById(R.id.Nickname)

        // SharedPreferences에서 현재 설정된 이름과 닉네임 가져오기
        val sharedPref = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val currentName = sharedPref.getString("name", "이름") ?: "이름"
        val currentNickname = sharedPref.getString("nickname", "닉네임") ?: "닉네임"

        nicknameTextView.text = currentNickname  // Nickname 설정

        // 프로필 수정 결과 처리
        val editProfileLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val newNickname = data?.getStringExtra("newNickname")

                if (!newNickname.isNullOrBlank()) {
                    // 변경된 이름과 닉네임을 SharedPreferences에 저장
                    with(sharedPref.edit()) {
                        putString("nickname", newNickname)
                        apply()
                    }

                    nicknameTextView.text = newNickname  // Nickname 설정
                }
            }
        }

        recentRecyclerView = view.findViewById(R.id.recentRecyclerView)
        recentRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recentAdapter = RecentAdapter(recentItems)
        recentRecyclerView.adapter = recentAdapter

        // 프로필 버튼 클릭 이벤트 설정
        val profileButton = view.findViewById<Button>(R.id.profileButton)
        profileButton.setOnClickListener {
            val intent = Intent(activity, ProfileEditActivity::class.java)
            startActivity(intent)
        }


        // 공지사항 버튼 클릭 이벤트
        val noticeButton = view.findViewById<Button>(R.id.noticeButton)
        noticeButton.setOnClickListener {
            val intent = Intent(activity, NoticeBoard::class.java)
            startActivity(intent)
        }
        // 비밀번호 재설정 버튼 클릭 이벤트 설정
        val passwordChangeBtn = view.findViewById<Button>(R.id.passwordChangeBtn)
        passwordChangeBtn.setOnClickListener {
            val intent = Intent(activity, ResetPassword::class.java)
            startActivity(intent)
        }
        // 저장된 항목 버튼 클릭 이벤트 설정
        val likeListButton = view.findViewById<Button>(R.id.likeListBtn)
        likeListButton.setOnClickListener {
            val intent = Intent(activity, BookmarkActivity::class.java)
            startActivity(intent)
        }
        // 탈퇴하기 클릭 이벤트 설정
        val deactivationButton = view.findViewById<Button>(R.id.deactivationBtn)
        deactivationButton.setOnClickListener {
            val intent = Intent(activity, account_deactivation::class.java)
            startActivity(intent)
        }
        // 로그아웃 클릭 이벤트 설정
        val logoutButton = view.findViewById<Button>(R.id.logoutBtn)
        logoutButton.setOnClickListener {
            // 로그아웃 확인 대화상자 표시
            AlertDialog.Builder(activity)
                //.setTitle("로그아웃")
                .setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("확인") { dialog, which ->
                    // 확인 버튼 클릭 시 로그아웃 처리
                    Toast.makeText(activity, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
                    // 첫 화면으로 이동
                    val intent = Intent(activity, HomeFragment::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    activity?.finish()
                }
                .setNegativeButton("취소", null)
                .show()
        }


        view.findViewById<ImageView>(R.id.homeTap).setOnClickListener {
            view.findNavController().navigate(R.id.action_myInfoFragment_to_homeFragment)
        }
        view.findViewById<ImageView>(R.id.scannerTap).setOnClickListener {
            view.findNavController().navigate(R.id.action_myInfoFragment_to_scannerFragment)
        }
        // Inflate the layout for this fragment
        return view
    }

    override fun onResume() {
        super.onResume()
        // Fragment가 다시 보일 때마다 닉네임을 업데이트
        val sharedPref = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val currentNickname = sharedPref.getString("nickname", "닉네임") ?: "닉네임"
        nicknameTextView.text = currentNickname
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}