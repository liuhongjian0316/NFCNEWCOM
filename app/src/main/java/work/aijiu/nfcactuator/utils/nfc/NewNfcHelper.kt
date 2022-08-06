package work.aijiu.nfcactuator.utils.nfc

import android.nfc.Tag
import android.nfc.tech.MifareUltralight
import android.nfc.tech.NfcA
import work.aijiu.nfcactuator.utils.MathUtil
import work.aijiu.nfcactuator.utils.Sha256
import java.io.IOException
import java.util.*

private var sha256:Sha256 = Sha256()
private var mathUtil:MathUtil = MathUtil()

class NewNfcHelper {
    /**
     * 成功
     */
    val SUCCESS = "100"

    /**
     * 失败
     */
    val FIAL = "101"

    /**
     * 标签不是 1443 格式的标签
     */
    val NO_NFCA = "102"

    /**
     * 手机不支持nfc 功能
     */
    val NO_NFC = "103"

    /**
     * 标签tag连接失败,请将手机放到标签上面 或者 没有进行密码验证
     */
    val TAG_CONNECT_FIAL = "104"

    /**
     * 数据不符合规则 4字节长度
     */
    val DATA_ERROR = "105"

    /**
     * 未知错误
     */
    val NO_ERROR = "106"

    /**
     * 版本号
     */
    val VERSION = "1.0.3"

    /**
     * 读标签 返回 字节key
     */
    val RESULT_BYTE = "res:byte"

    /**
     * 读标签 返回 扇区jkey
     */
    val RESULT_SECTOR = "res:sector"


    /**
     * 是否支持MifareUltralight读写
     * @param tag
     * @return
     */
    fun Issupport(tag: Tag?): Boolean {
        val mfu = MifareUltralight.get(tag)
        return mfu == null
    }

    fun String.decodeHex(): ByteArray = chunked(2)
        .map { it.toByte(16) }
        .toByteArray()

    /**
     * 新nfc 芯片读方法
     * @param nfcA
     * @param startPage
     * @param endPage
     * @return
     */
    fun readTag(nfcA: NfcA?, startPage: Int, endPage: Int): Map<String, Any>? {
        try {
            if (nfcA != null) {
                if (!nfcA.isConnected) {
                    nfcA.connect()
                }
            }
            val resMap: MutableMap<String, Any> = HashMap()
            //扇区数据
            var sector = ""
            //返回数据
            val str = StringBuilder()
            for (i in startPage..endPage) {
                val transceive = nfcA!!.transceive(byteArrayOf(0x30, i.toByte()))
                val newStr: StringBuilder =
                    StringBuilder(mathUtil.bytesToHexString(transceive)!!.substring(0, 8))
                str.append(newStr)
                var j = newStr.length - 2
                while (j > 0) {
                    newStr.insert(j, "-")
                    j = j - 2
                }
                sector += "扇区$i:$newStr\r\n"
            }
            val res = str.toString().decodeHex()
            resMap[RESULT_BYTE] = res
            resMap[RESULT_SECTOR] = sector
            return resMap
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (nfcA != null) {
                try {
                    nfcA.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    /**
     * 新nfc 芯片写方法
     * @param nfcA
     * @param writeByte
     * @param block
     */
    fun writeTag(nfcA: NfcA?, writeByte: ByteArray?, block: Int) {
        try {
            if (nfcA != null) {
                if (!nfcA.isConnected) {
                    nfcA.connect()
                }
            }
            val temp = ByteArray(2)
            temp[0] = 0xA2.toByte()
            temp[1] = block.toByte()
            val cmd: ByteArray? = mathUtil.addAll(temp, writeByte)
            nfcA!!.transceive(cmd)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (nfcA != null) {
                try {
                    nfcA.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }






    /**
     * nfc 设置密码
     * @param pwd
     * @return
     */
    fun settingAuthPwd(tag: Tag?, pwd: ByteArray?): String? {
        return if (pwd != null && pwd.size == 4) {
            val e3 = ByteArray(16)
            var result = readData(tag, (-29.toByte()).toByte(), e3)
            if (result != SUCCESS) {
                result
            } else {
                val e4 = ByteArray(16)
                result = readData(tag, (-28.toByte()).toByte(), e4)
                if (result != SUCCESS) {
                    result
                } else {
                    e4[0] = -121
                    result = writeData(
                        tag,
                        (-28.toByte()).toByte(), byteArrayOf(e4[0], e4[1], e4[2], e4[3])
                    )
                    if (result != SUCCESS) {
                        result
                    } else {
                        result = writeData(tag, (-27.toByte()).toByte(), pwd)
                        if (result != SUCCESS) {
                            result
                        } else {
                            result =
                                writeData(tag, (-26.toByte()).toByte(), byteArrayOf(-112, 0, 0, 0))
                            if (result != SUCCESS) {
                                result
                            } else {
                                e3[3] = 4
                                result = writeData(
                                    tag,
                                    (-29.toByte()).toByte(), byteArrayOf(e3[0], e3[1], e3[2], e3[3])
                                )
                                if (result != SUCCESS) result else SUCCESS
                            }
                        }
                    }
                }
            }
        } else {
            DATA_ERROR
        }
    }




    /**
     * 验证密码
     * @param tag
     * @param pwd
     * @return
     */
    fun auth(tag: Tag?, pwd: ByteArray): String? {
        val nfcA = NfcA.get(tag)
        val var4: String
        try {
            if (nfcA != null) {
                if (!nfcA.isConnected) {
                    nfcA.connect()
                }
                val data = ByteArray(pwd.size + 1)
                data[0] = 27
                System.arraycopy(pwd, 0, data, 1, pwd.size)
                val transceive = nfcA.transceive(data)
                val s = byte2hex(transceive)
                if (s == "9000") {
                    val transceive1 = nfcA.transceive(byteArrayOf(48, -29))
                    val transceive2 = nfcA.transceive(byteArrayOf(48, -28))
                    transceive1[3] = -1
                    transceive2[0] = 0
                    val e3 = byteArrayOf(
                        -94,
                        -29,
                        transceive1[0],
                        transceive1[1],
                        transceive1[2],
                        transceive1[3]
                    )
                    val e4 = byteArrayOf(
                        -94,
                        -28,
                        transceive2[0],
                        transceive2[1],
                        transceive2[2],
                        transceive2[3]
                    )
                    val e5 = byteArrayOf(-94, -27, 0, 0, 0, 0)
                    nfcA.transceive(e3)
                    nfcA.transceive(e4)
                    nfcA.transceive(e5)
                    return SUCCESS
                }
                if (s == "04") {
                    return FIAL
                }
            }
            return NO_NFCA
        } catch (var25: IOException) {
            var25.printStackTrace()
            var4 = FIAL
            return var4
        } catch (var26: Exception) {
            var26.printStackTrace()
            var4 = NO_ERROR
        } finally {
            try {
                nfcA?.close()
            } catch (var24: Exception) {
                var24.printStackTrace()
            }
        }
        return var4
    }

    /**
     * 读取数据 返回标志
     * @param tag
     * @param block
     * @param receiveData
     * @return
     */
    fun readData(tag: Tag?, block: Byte, receiveData: ByteArray?): String {
        val nfcA = NfcA.get(tag)
        var var5: String
        try {
            if (nfcA == null) {
                return NO_NFCA
            }
            if (!nfcA.isConnected) {
                nfcA.connect()
            }
            val transceive = nfcA.transceive(byteArrayOf(48, block))
            System.arraycopy(transceive, 0, receiveData, 0, transceive.size)
            var5 = SUCCESS
        } catch (var18: IOException) {
            var18.printStackTrace()
            var5 = TAG_CONNECT_FIAL
            return var5
        } catch (var19: Exception) {
            var19.printStackTrace()
            var5 = NO_ERROR
            return var5
        } finally {
            try {
                nfcA?.close()
            } catch (var17: Exception) {
                var17.printStackTrace()
            }
        }
        return var5
    }

    /**
     * 写数据 返回标志
     * @param tag
     * @param block
     * @param data
     * @return
     */
    fun writeData(tag: Tag?, block: Byte, data: ByteArray?): String {
        val nfcA = NfcA.get(tag)
        val var4: String
        try {
            val var5: String
            try {
                if (nfcA != null) {
                    if (!nfcA.isConnected) {
                        nfcA.connect()
                    }
                    if (data != null && data.size == 4) {
                        nfcA.transceive(byteArrayOf(-94, block, data[0], data[1], data[2], data[3]))
                        var4 = SUCCESS
                        return var4
                    }
                    var4 = DATA_ERROR
                    return var4
                }
                var4 = NO_NFCA
            } catch (var19: IOException) {
                var19.printStackTrace()
                var5 = TAG_CONNECT_FIAL
                return var5
            } catch (var20: Exception) {
                var20.printStackTrace()
                var5 = NO_ERROR
                return var5
            }
        } finally {
            try {
                nfcA?.close()
            } catch (var18: Exception) {
                var18.printStackTrace()
            }
        }
        return var4
    }

    /**
     * 16进制字节 转 字符串
     * @param b
     * @return
     */
    fun byte2hex(b: ByteArray?): String {
        val hs = StringBuilder()
        var n = 0
        while (b != null && n < b.size) {
            val stmp = Integer.toHexString(b[n].toInt() and 255)
            if (stmp.length == 1) {
                hs.append('0')
            }
            hs.append(stmp)
            ++n
        }
        return hs.toString().uppercase(Locale.getDefault())
    }

    /**
     * 新nfc芯片验证密码
     * @param nfcA
     * @return
     */
    fun authNew(nfcA: NfcA?): String? {
        return try {
            if (nfcA != null) {
                if (!nfcA.isConnected) {
                    nfcA.connect()
                }
            }
            val pwd = handlePwd(nfcA)
            if (pwd!!.size == 4) {
                val command = ByteArray(5)
                command[0] = 0x1B.toByte()
                command[1] = pwd[0]
                command[2] = pwd[1]
                command[3] = pwd[2]
                command[4] = pwd[3]
                nfcA!!.transceive(command)
                return SUCCESS
            }
            DATA_ERROR
        } catch (e: IOException) {
            e.printStackTrace()
            FIAL
        }
    }

    /**
     * 处理密码
     * @param nfcA
     * @return
     */
    fun handlePwd(nfcA: NfcA?): ByteArray? {
        return try {
            val uid = readUid(nfcA)
            val uid_byte = byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
            val secret =
                byteArrayOf(0x79, 0x46, 0x32, 0x44, 0x78, 0x57, 0x38, 0x4a)
            for (i in uid_byte.indices) {
                uid_byte[i] = uid!![i]
            }
            val byte_unique: ByteArray? = sha256.byteMerger(uid_byte, secret)
            sha256.getAuthPwdByHexArray(byte_unique)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 读取uid （无需密码）
     * @param nfcA
     * @return
     */
    fun readUid(nfcA: NfcA?): ByteArray? {
        try {
            if (nfcA != null) {
                if (!nfcA.isConnected) {
                    nfcA.connect()
                }
            }
            val command = ByteArray(2)
            command[0] = 0x30.toByte()
            command[1] = 0x00.toByte()
            val uid = nfcA!!.transceive(command)

            //返回8长度字节数组
            return byteArrayOf(
                uid[0],
                uid[1], uid[2], uid[3], uid[4], uid[5], uid[6], uid[7]
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取当前版本
     * @return
     */
    fun getCurrentVersion(): String? {
        return "1.0.3"
    }

    @Throws(Exception::class)
    fun close() {
    }


    /**
     * 写入温控id
     * @param nfcA
     */
    fun writeTempId(input: String?, nfcA: NfcA?) {
        val bytes1: ByteArray? = mathUtil.hexStringToBytes(input)
        var help = NewNfcHelper()
        help.authNew(nfcA)
        help.writeTag(nfcA, byteArrayOf(bytes1!![7], bytes1[6], bytes1[5], bytes1[4]), 0x2C)
        help.authNew(nfcA)
        help.writeTag(nfcA, byteArrayOf(bytes1[3], bytes1[2], bytes1[1], bytes1[0]), 0x2D)
    }


}