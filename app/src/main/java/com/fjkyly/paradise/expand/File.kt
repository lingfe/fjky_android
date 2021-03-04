package com.fjkyly.paradise.expand

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.FileUtils
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.fjkyly.paradise.base.App
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt

/**
 * 进行文件 uri 的适配
 *
 * @param intent Intent
 * @param file File
 * @return Uri
 */
fun compatFileUri(intent: Intent, file: File): Uri =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        FileProvider.getUriForFile(App.appContext, "${AppConfig.getPackageName()}.provider", file)
    } else Uri.fromFile(file)

/**
 * 将 File 对象转化为 MultipartBody.Part 对象
 *
 * @param file File
 * @return MultipartBody.Part
 */
fun fileToMultipartBodyParts(file: File): MultipartBody.Part =
    filesToMultipartBodyParts(listOf(file))[0]

/**
 * 将 List<File> 对象转化为 List<MultipartBody.Part> 对象
 * @param files List<File>
 * @return List<MultipartBody.Part>
 */
fun filesToMultipartBodyParts(files: List<File>): List<MultipartBody.Part> {
    val parts: MutableList<MultipartBody.Part> = ArrayList(files.size)
    for (file in files) {
        // TODO: 2021/3/2 这里为了简单起见，没有判断file的类型
        val requestBody = RequestBody.create(MediaType.parse("image/png"), file)
        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
        parts.add(part)
    }
    return parts
}

/**
 * 将 Uri 对象所对应的文件复制到应用程序的关联目录下
 *
 * @param uri Uri
 * @param fileName String
 * @param block Function1<[@kotlin.ParameterName] File, Unit>
 */
fun copyUriToExternalFilesDir(uri: Uri, fileName: String, block: (file: File) -> Unit) {
    val context: Context = App.appContext
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri)
    val tempDir = context.getExternalFilesDir("temp")
    if (inputStream != null && tempDir != null) {
        val file = File("$tempDir/$fileName")
        val fos = FileOutputStream(file)
        val bis = BufferedInputStream(inputStream)
        val bos = BufferedOutputStream(fos)
        val byteArray = ByteArray(1024)
        var bytes = bis.read(byteArray)
        while (bytes > 0) {
            bos.write(byteArray, 0, bytes)
            bos.flush()
            bytes = bis.read(byteArray)
        }
        bos.close()
        fos.close()
        block(file)
    }
}

/**
 * 通过uri  获取文件路径
 *
 * @param imageUri Uri
 * @return String
 */
@SuppressLint("ObsoleteSdkInt")
fun getFileAbsolutePath(imageUri: Uri): String {
    val context = App.appContext
    val schema = imageUri.scheme
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return getRealFilePath(imageUri)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
        && DocumentsContract.isDocumentUri(context, imageUri)
    ) {
        if (isExternalStorageDocument(imageUri)) {
            val docId = DocumentsContract.getDocumentId(imageUri)
            val split = docId.split(":")
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return "${Environment.getExternalStorageDirectory()}/${split[1]}"
            }
        } else if (isDownloadsDocument(imageUri)) {
            val docId = DocumentsContract.getDocumentId(imageUri)
            val contextUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),
                docId.toLong()
            )
            return getDataColumn(context, contextUri, null, null)
        } else if (isMediaDocument(imageUri)) {
            val docId = DocumentsContract.getDocumentId(imageUri)
            val split = docId.split(":")
            // val contentUri: Uri = if ("image" == type) MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val contentUri: Uri? = when (split[0]) {
                "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                else -> null
            }
            val selection = "${MediaStore.Images.Media._ID}=?"
            val selectionArgs = arrayOf(split[1])
            return getDataColumn(context, contentUri, selection, selectionArgs)
        }
    }
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
            // return uriToFileApiQ(context, imageUri)
            return getFileFromContentUri(context, imageUri)
        }
        "content".equals(schema, ignoreCase = true) -> {
            // Return the remote address
            if (isGooglePhotosUri(imageUri)) {
                return imageUri.lastPathSegment ?: ""
            }
            return getDataColumn(context, imageUri, null, null)
        }
        "file".equals(schema, ignoreCase = true) -> {
            // File
            return imageUri.path ?: ""
        }
        else -> return ""
    }
}

/**
 * 此方法 只能用于4.4以下的版本
 *
 * @param uri Uri
 * @return String
 */
fun getRealFilePath(uri: Uri): String {
    val context = App.appContext
    val scheme = uri.scheme
    var data = ""
    if (scheme == null || ContentResolver.SCHEME_FILE == scheme) data = uri.path ?: ""
    else if (ContentResolver.SCHEME_CONTENT == scheme) {
        val projection = arrayOf(MediaStore.Images.ImageColumns.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.let {
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                if (index > -1) {
                    data = cursor.getString(index)
                }
            }
            cursor.close()
        }
    }
    return data
}

/**
 * Whether the Uri authority is ExternalStorageProvider.
 *
 * @param uri Uri
 * @return Boolean
 */
fun isExternalStorageDocument(uri: Uri): Boolean =
    "com.android.externalstorage.documents" == uri.authority

/**
 * Whether the Uri authority is DownloadsProvider.
 *
 * @param uri Uri
 * @return Boolean
 */
fun isDownloadsDocument(uri: Uri): Boolean =
    "com.android.providers.downloads.documents" == uri.authority

fun getDataColumn(
    context: Context,
    uri: Uri?,
    selection: String?,
    selectionArgs: Array<String>?
): String {
    var cursor: Cursor? = null
    val column = MediaStore.Images.Media.DATA
    val projection = arrayOf(column)
    try {
        if (uri == null) return ""
        cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
        cursor?.let {
            if (it.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        }
    } finally {
        cursor?.close()
    }
    return ""
}

fun isMediaDocument(uri: Uri): Boolean = "com.android.providers.media.documents" == uri.authority

/**
 * Android 10 以上适配 另一种写法
 *
 * @param context Context
 * @param uri Uri
 * @return String
 */
fun getFileFromContentUri(context: Context, uri: Uri): String {
    val filePathColumn = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME)
    val contentResolver = context.contentResolver
    val cursor = contentResolver.query(
        uri, filePathColumn, null,
        null, null
    )
    cursor?.let {
        it.moveToFirst()
        try {
            return cursor.getString(cursor.getColumnIndex(filePathColumn[0]))
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }
    cursor?.close()
    return ""
}

/**
 * Android 10 以上适配
 *
 * @param context Context
 * @param uri Uri
 * @return String
 */
@RequiresApi(api = Build.VERSION_CODES.Q)
fun uriToFileApiQ(context: Context, uri: Uri): String {
    val scheme = uri.scheme
    var file: File? = null
    if (ContentResolver.SCHEME_FILE == scheme) {
        uri.path?.let {
            file = File(it)
        }
    } else if (ContentResolver.SCHEME_CONTENT == scheme) {
        // 把文件复制到沙盒目录
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.let {
            if (it.moveToFirst()) {
                val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                runCatching {
                    val ism = contentResolver.openInputStream(uri)
                    val fileAbsolutePath = context.externalCacheDir?.absolutePath
                    if (fileAbsolutePath != null) {
                        val cache = File("${((Math.random() + 1) * 1000).roundToInt()}$displayName")
                        var fos: FileOutputStream? = null
                        try {
                            fos = FileOutputStream(cache)
                            ism?.let {
                                FileUtils.copy(ism, fos)
                                file = cache
                                ism.close()
                            }
                        } catch (t: Throwable) {
                            t.printStackTrace()
                        } finally {
                            fos?.close()
                        }
                    }
                }
            }
        }
        cursor?.close()
    }
    return file?.absolutePath ?: ""
}

/**
 * Uri 权限是否为谷歌 Photos
 *
 * @param uri Uri
 * @return Boolean
 */
fun isGooglePhotosUri(uri: Uri): Boolean = "com.google.android.apps.photos.content" == uri.authority