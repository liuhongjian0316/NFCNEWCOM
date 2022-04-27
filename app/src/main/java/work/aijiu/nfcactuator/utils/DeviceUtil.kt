package work.aijiu.nfcactuator.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*


/**
 *
 * @ProjectName:    nfcActuator
 * @Package:        work.aijiu.nfcactuator.utils
 * @ClassName:      DeviceUtil
 * @Description:    设备工具箱
 * @Author:         aijiu
 * @CreateDate:     2022/4/26 9:05
 * @UpdateUser:     aijiu
 * @UpdateDate:     2022/4/26 9:05
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class DeviceUtil {


    /**
     * 获取设备唯一ID
     */
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context):String {
        // 判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // 获取不到替代方案
            return customDeviceId(context)
        }else {
            // 可以获取到
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (telephonyManager == null){
                // 替代方案
                return customDeviceId(context)
            } else {
                return telephonyManager.deviceId;
            }
        }
        return customDeviceId(context)
    }

    fun customDeviceId(context: Context): String {
        val sbDeviceId = StringBuilder()
        val imei = getIMEI(context)
        val androidID = getAndroidId(context)
        val serial = getSerial()
        val id: String = getDeviceUUID().replace("-", "")
        if (imei != null && imei.length > 0) {
            sbDeviceId.append(imei)
            sbDeviceId.append("|")
        }
        if (androidID != null && androidID.length > 0) {
            sbDeviceId.append(androidID)
            sbDeviceId.append("|")
        }
        if (serial != null && serial.length > 0) {
            sbDeviceId.append(serial)
            sbDeviceId.append("|")
        }
        if (id != null && id.length > 0) {
            sbDeviceId.append(id)
        }
        if (sbDeviceId.length > 0) {
            try {
                val hash: ByteArray = getHashByString(sbDeviceId.toString())
                val sha1: String = bytesToHex(hash)
                if (sha1 != null && sha1.length > 0) {
                    //返回最终的DeviceId
                    return sha1
                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }
        return ""
    }


    private fun getDeviceUUID(): String {
        val dev = "100001" + Build.BOARD +
                Build.BRAND +
                Build.DEVICE +
                Build.HARDWARE +
                Build.ID +
                Build.MODEL +
                Build.PRODUCT +
                Build.SERIAL
        return UUID(dev.hashCode().toLong(), Build.SERIAL.hashCode().toLong()).toString()
    }

    private fun getSerial(): String? {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Build.getSerial()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    private fun getAndroidId(context: Context): String? {
        try {
            return Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        return ""
    }

    private fun getIMEI(context: Context): String? {
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return tm.deviceId
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        return ""
    }

    private fun bytesToHex(data: ByteArray): String {
        val sb = java.lang.StringBuilder()
        for (b in data) {
            val st = String.format("%02X", b)
            sb.append(st)
        }
        return sb.toString().uppercase(Locale.CHINA)
    }
    private fun getHashByString(data: String): ByteArray {
        return try {
            val messageDigest: MessageDigest = MessageDigest.getInstance("SHA1")
            messageDigest.reset()
            messageDigest.update(data.toByteArray(StandardCharsets.UTF_8))
            messageDigest.digest()
        } catch (e: java.lang.Exception) {
            "".toByteArray()
        }
    }
}