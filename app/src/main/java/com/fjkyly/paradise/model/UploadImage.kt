package com.fjkyly.paradise.model
import com.google.gson.annotations.SerializedName

/**
 * 上传图片文件
 *
 * @property code Int
 * @property count Int
 * @property `data` Data
 * @property msg String
 * @property state Int
 * @constructor
 */
data class UploadImage(
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
        @SerializedName("crt_date")
        val crtDate: String,
        @SerializedName("crt_id")
        val crtId: String,
        @SerializedName("custom_name")
        val customName: String,
        @SerializedName("full_path")
        val fullPath: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("imgUrl")
        val imgUrl: String,
        @SerializedName("list")
        val list: Any,
        @SerializedName("navigator")
        val navigator: Any,
        @SerializedName("old_name")
        val oldName: String,
        @SerializedName("order_id")
        val orderId: Any,
        @SerializedName("original_id")
        val originalId: Any,
        @SerializedName("reamrk")
        val reamrk: Any,
        @SerializedName("remark")
        val remark: Any,
        @SerializedName("searchKey")
        val searchKey: Any,
        @SerializedName("state")
        val state: Int,
        @SerializedName("storage_path")
        val storagePath: String,
        @SerializedName("suffix")
        val suffix: String,
        @SerializedName("user_name")
        val userName: Any,
        @SerializedName("where")
        val `where`: Any,
        @SerializedName("yw_id")
        val ywId: String
    )
}