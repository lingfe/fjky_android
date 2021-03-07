package com.fjkyly.paradise.ui.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.fjkyly.paradise.R
import com.fjkyly.paradise.adapter.ItemKinsfolkNumAdapter
import com.fjkyly.paradise.base.BaseActivity
import com.fjkyly.paradise.databinding.ActivityFamilyNumberBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.expand.startActivity
import com.fjkyly.paradise.model.Kinsfolk
import com.fjkyly.paradise.ui.views.ConfirmDialog
import kotlin.random.Random

/**
 * 亲情号码列表展示界面
 *
 * @property mBinding ActivityAddFamilyNumberBinding
 * @property itemKinsfolkNumAdapter ItemKinsfolkNumAdapter
 * @property mKinsfolkList MutableList<Kinsfolk>
 */
class FamilyNumberActivity : BaseActivity() {

    private lateinit var mBinding: ActivityFamilyNumberBinding
    private val itemKinsfolkNumAdapter by lazy {
        ItemKinsfolkNumAdapter()
    }
    private val mKinsfolkList = arrayListOf<Kinsfolk>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFamilyNumberBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initView() {
        mBinding.run {
            familyNumberRv.run {
                adapter = itemKinsfolkNumAdapter
                layoutManager = LinearLayoutManager(this@FamilyNumberActivity)
            }
        }
    }

    override fun initData() {
        mKinsfolkList.run {
            repeat(40) {
                add(
                    Kinsfolk(
                        "NJNUFBUfHKSDH${Random.nextInt(10000)}",
                        "张三",
                        "155${(Random.nextDouble(1.0) * 100000000).toInt()}"
                    )
                )
            }
            itemKinsfolkNumAdapter.setData(this)
        }
    }

    override fun initEvent() {
        mBinding.run {
            addFamilyNumberBackIv.setOnClickListener {
                finish()
            }
            addKinfolkTv.setOnClickListener {
                startActivity<AddKinsfolkActivity>()
                simpleToast("新增亲友号码开发中...")
            }
        }
        itemKinsfolkNumAdapter.run {
            setOnItemLongClickListener { kinsfolk, _ ->
                ConfirmDialog(this@FamilyNumberActivity).run {
                    setContentView(R.layout.dialog_confirm)
                    setCancelable(false)
                    post {
                        setDialogMessage("确定要删除此号吗？")
                    }
                    setOnDialogActionClickListener(object :
                        ConfirmDialog.OnDialogActionSimpleListener() {
                        /**
                         * 因为 “确定” 的文字是在右边的，所以回调的接口相反了
                         */
                        override fun onGiveUpClick() {
                            deleteByKinsfolkId(kinsfolk)
                        }
                    })
                    show()
                }
            }
        }
    }
}