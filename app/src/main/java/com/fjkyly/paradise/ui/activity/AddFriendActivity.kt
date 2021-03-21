package com.fjkyly.paradise.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityAddFriendBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.network.request.Repository
import com.fjkyly.paradise.ui.views.ConfirmDialog
import com.vondear.rxtool.RxKeyboardTool
import com.vondear.rxtool.RxRegTool

/**
 * 添加亲友界面
 */
class AddFriendActivity : MyActivity() {

    private var _binding: ActivityAddFriendBinding? = null
    private val mBinding get() = _binding!!
    private var isAddState = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddFriendBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initView() {
        val friendName = intent.getStringExtra("friendName")
        val friendPhoneNum = intent.getStringExtra("friendPhoneNum")
        val friendRelation = intent.getStringExtra("friendRelation")
        val isAdd = intent.getBooleanExtra("isAdd", true)
        mBinding.run {
            addFriendTitleTv.text = if (isAdd) "添加亲友" else "编辑亲友信息"
            addFriendNameEt.setText(friendName)
            addFriendPhoneNumEt.setText(friendPhoneNum)
            friendRelationTv.text = friendRelation
            deleteBtn.visibility = if (isAdd) View.GONE else View.VISIBLE
        }
    }

    override fun initEvent() {
        mBinding.run {
            addFriendBackIv.setOnClickListener {
                finish()
            }
            friendRelationContainer.setOnClickListener {
                RxKeyboardTool.hideSoftInput(this@AddFriendActivity)
                // 亲友关系选择
                val friendRelationList = mutableListOf<String>().apply {
                    add("父母")
                    add("子女")
                    add("配偶")
                    add("兄弟")
                    add("姐妹")
                    add("朋友")
                    add("其他")
                }
                OptionsPickerBuilder(this@AddFriendActivity) { options1: Int, _: Int, _: Int, _: View? ->
                    val friendRelation = friendRelationList[options1]
                    friendRelationTv.text = friendRelation
                }
                    .isAlphaGradient(true)
                    .build<String>()
                    .apply {
                        setOnDismissListener {
                            friendRelationList.clear()
                        }
                        setPicker(friendRelationList)
                        show()
                    }
            }
            deleteBtn.setOnClickListener {
                // 根据亲友 ID 删除亲友
                val deleteFriendDialog = ConfirmDialog(this@AddFriendActivity)
                deleteFriendDialog.run {
                    setContentView(R.layout.dialog_confirm)
                    post {
                        setDialogMessage("确定要删除该亲友吗？")
                        setConfirmTextColor(Color.parseColor("#666666"))
                        setGivUpTextColor(Color.parseColor("#FF5050"))
                    }
                    show()
                }
                deleteFriendDialog.setOnDialogActionClickListener(object :
                    ConfirmDialog.OnDialogActionSimpleListener() {
                    override fun onGiveUpClick() {
                        val friendId = intent.getStringExtra("friendId")
                        if (friendId == null) {
                            simpleToast("亲友删除失败")
                            return
                        }
                        simpleToast("正在删除该亲友")
                        Log.d(TAG, "onGiveUpClick: ===>friendId：$friendId")
                        // 根据亲友 ID 删除对应的亲友
                        Repository.deleteFriendById(
                            lifecycle = lifecycle,
                            friendId = friendId
                        ) {
                            simpleToast(it.msg)
                            finish()
                        }
                    }
                })
            }
            saveBtn.setOnClickListener {
                val friendId = intent.getStringExtra("friendId")
                val friendName = addFriendNameEt.text.toString()
                val friendPhoneNum = addFriendPhoneNumEt.text.toString()
                val friendRelation = friendRelationTv.text.toString()
                if (friendName.isEmpty()) {
                    simpleToast("请输入亲友姓名")
                    return@setOnClickListener
                }
                // 限制中英文和数字
                if ((friendName matches Regex("^[\\u4e00-\\u9fa5_a-zA-Z0-9]+$")).not()) {
                    simpleToast("姓名不得包含特殊字符")
                    return@setOnClickListener
                }
                if (friendPhoneNum.isEmpty()) {
                    simpleToast("请输入友亲手机号")
                    return@setOnClickListener
                }
                if (RxRegTool.isMobile(friendPhoneNum).not()) {
                    simpleToast("请输入正确的手机号")
                    return@setOnClickListener
                }
                if (friendRelation.isEmpty()) {
                    simpleToast("请选择亲友与你的关系")
                    return@setOnClickListener
                }
                // 如果是添加状态，则调用添加亲友的接口，否则调用修改亲友的接口
                if (intent.getBooleanExtra("isAdd", true)) {
                    Repository.addFriend(
                        lifecycle = lifecycle,
                        friendName = friendName,
                        friendPhoneNum = friendPhoneNum,
                        friendRelation = friendRelation
                    ) {
                        simpleToast(it.msg)
                        finish()
                    }
                } else {
                    if (friendId == null) {
                        simpleToast("亲友信息修改失败")
                        return@setOnClickListener
                    }
                    Repository.modifyFriendInfo(
                        lifecycle = lifecycle,
                        friendId = friendId,
                        friendName = friendName,
                        friendPhoneNum = friendPhoneNum,
                        friendRelation = friendRelation
                    ) {
                        simpleToast(it.msg)
                        finish()
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "AddFriendActivity"
        fun startActivity(
            context: Context,
            friendName: String? = null,
            friendPhoneNum: String? = null,
            friendRelation: String? = null,
            friendId: String? = null,
            isAdd: Boolean = true
        ) {
            val intent = Intent(context, AddFriendActivity::class.java)
            intent.putExtra("friendName", friendName)
            intent.putExtra("friendPhoneNum", friendPhoneNum)
            intent.putExtra("friendRelation", friendRelation)
            intent.putExtra("friendId", friendId)
            intent.putExtra("isAdd", isAdd)
            context.startActivity(intent)
        }
    }
}