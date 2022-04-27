package work.aijiu.nfcactuator.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.telephony.TelephonyManager
import java.io.*
import java.util.*


/**
 *
 * @ProjectName:    nfcActuator
 * @Package:        work.aijiu.nfcactuator.utils
 * @ClassName:      DeviceUuidFactory
 * @Description:    java类作用描述
 * @Author:         aijiu
 * @CreateDate:     2022/4/26 9:40
 * @UpdateUser:     aijiu
 * @UpdateDate:     2022/4/26 9:40
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class DeviceUuidFactory {
    protected val PREFS_FILE = "dev_id.xml"
    protected val DEVICE_UUID_FILE_NAME = ".dev_id.txt"
    protected val PREFS_DEVICE_ID = "dev_id"
    protected val KEY = "cyril'98"
    protected var uuid: UUID? = null


    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    constructor(context: Context) {
        if (uuid == null) {
            synchronized(DeviceUuidFactory::class.java) {
                if (uuid == null) {
                    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILE, 0)
                    val id = prefs.getString(PREFS_DEVICE_ID, null)
                    if (id != null) {
                        uuid = UUID.fromString(id)
                    } else {
                        if (recoverDeviceUuidFromSD() != null) {
                            uuid = UUID.fromString(recoverDeviceUuidFromSD())
                        } else {
                            val androidId: String = Settings.Secure.getString(
                                context.getContentResolver(),
                                Settings.Secure.ANDROID_ID
                            )
                            try {
                                if ("9774d56d682e549c" != androidId) {
                                    uuid =
                                        UUID.nameUUIDFromBytes(androidId.toByteArray(charset("utf8")))
                                    try {
                                        EncryptUtils().encryptDES(
                                            uuid.toString(),
                                            KEY
                                        )?.let {
                                            saveDeviceUuidToSD(
                                                it
                                            )
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                } else {
                                    @SuppressLint("MissingPermission") val deviceId =
                                        (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
                                    uuid = if (deviceId != null) UUID.nameUUIDFromBytes(
                                        deviceId.toByteArray(charset("utf8"))
                                    ) else UUID.randomUUID()
                                    try {
                                        EncryptUtils().encryptDES(
                                            uuid.toString(),
                                            KEY
                                        )?.let {
                                            saveDeviceUuidToSD(
                                                it
                                            )
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            } catch (e: UnsupportedEncodingException) {
                                throw RuntimeException(e)
                            }
                        }
                        prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).commit()
                    }
                }
            }
        }
    }

    private fun recoverDeviceUuidFromSD(): String? {
        return try {
            val dirPath: String = Environment.getExternalStorageDirectory().getAbsolutePath()
            val dir = File(dirPath)
            val uuidFile = File(dir, DEVICE_UUID_FILE_NAME)
            if (!dir.exists() || !uuidFile.exists()) {
                return null
            }
            val fileReader = FileReader(uuidFile)
            val sb = StringBuilder()
            val buffer = CharArray(100)
            var readCount: Int
            while (fileReader.read(buffer).also { readCount = it } > 0) {
                sb.append(buffer, 0, readCount)
            }
            //通过UUID.fromString来检查uuid的格式正确性
            val uuid = UUID.fromString(EncryptUtils().decryptDES(sb.toString(), KEY))
            uuid.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun saveDeviceUuidToSD(uuid: String) {
        val dirPath: String = Environment.getExternalStorageDirectory().getAbsolutePath()
        val targetFile = File(dirPath, DEVICE_UUID_FILE_NAME)
        if (targetFile != null) {
            if (targetFile.exists()) {
            } else {
                val osw: OutputStreamWriter
                try {
                    osw = OutputStreamWriter(FileOutputStream(targetFile), "utf-8")
                    try {
                        osw.write(uuid)
                        osw.flush()
                        osw.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }

    @JvmName("getUuid1")
    fun getUuid(): UUID? {
        return uuid
    }

}