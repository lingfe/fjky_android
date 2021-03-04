package com.fjkyly.paradise.model
import com.google.gson.annotations.SerializedName

/**
 * 用户登录时返回的信息
 *
 * @property code Int
 * @property count Int
 * @property `data` Data
 * @property msg String
 * @property state Int
 * @constructor
 */
data class Login(
    @SerializedName("code")
    val code: Int,
    @SerializedName("count")
    val count: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("state")
    val state: Int
) {
    data class Data(
        @SerializedName("account_info")
        val accountInfo: AccountInfo,
        @SerializedName("ass_info")
        val assInfo: AssInfo,
        @SerializedName("token")
        val token: String,
        @SerializedName("user_info")
        val userInfo: UserInfo
    ) {
        data class AccountInfo(
            @SerializedName("account_name")
            val accountName: Any,
            @SerializedName("account_pwd")
            val accountPwd: String,
            @SerializedName("crt_date")
            val crtDate: String,
            @SerializedName("crt_id")
            val crtId: Any,
            @SerializedName("id")
            val id: String,
            @SerializedName("list")
            val list: Any,
            @SerializedName("order_id")
            val orderId: Any,
            @SerializedName("original_id")
            val originalId: Any,
            @SerializedName("reamrk")
            val reamrk: Any,
            @SerializedName("searchKey")
            val searchKey: Any,
            @SerializedName("state")
            val state: Int,
            @SerializedName("user_name")
            val userName: String,
            @SerializedName("where")
            val `where`: Any,
            @SerializedName("yw_id")
            val ywId: Any
        )

        data class AssInfo(
            @SerializedName("crt_date")
            val crtDate: Long,
            @SerializedName("id")
            val id: String,
            @SerializedName("id_card")
            val idCard: String,
            @SerializedName("phone")
            val phone: String,
            @SerializedName("state")
            val state: Int,
            @SerializedName("yw_id")
            val ywId: String
        )

        data class UserInfo(
            @SerializedName("crt_date")
            val crtDate: Long,
            @SerializedName("id")
            val id: String,
            @SerializedName("pwd")
            val pwd: String,
            @SerializedName("state")
            val state: Int,
            @SerializedName("user_img")
            val userImg: String,
            @SerializedName("username")
            var username: String
        )
    }
}