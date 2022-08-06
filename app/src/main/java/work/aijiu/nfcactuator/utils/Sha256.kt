package work.aijiu.nfcactuator.utils

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

public class Sha256 {

    fun getSHA256ByHexArray(hex_array: ByteArray?): String? {
        val messageDigest: MessageDigest
        var encodestr: String? = ""
        try {
            messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.update(hex_array)
            encodestr = byte2Hex(messageDigest.digest())
            return encodestr
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getAuthPwdByHexArray(hex_array: ByteArray?): ByteArray? {
        val messageDigest: MessageDigest
        val encodestr = byteArrayOf(0x00, 0x00, 0x00, 0x00)
        try {
            messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.update(hex_array)
            val all_bytes: ByteArray = messageDigest.digest()
            for (i in encodestr.indices) {
                encodestr[i] = all_bytes[i]
            }
            return encodestr
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getSHA256ByHexStr(hex_str: String?): String? {
        val messageDigest: MessageDigest
        var encodestr: String? = ""
        try {
            messageDigest = MessageDigest.getInstance("SHA-256")
            if (hex_str != null) {
                hexToByteArray(hex_str)?.let { messageDigest.update(it) }
            }
            encodestr = byte2Hex(messageDigest.digest())
            return encodestr
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getSHA256(str: String): String? {
        val messageDigest: MessageDigest
        var encodestr: String? = ""
        try {
            messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.update(str.toByteArray(StandardCharsets.UTF_8))
            encodestr = byte2Hex(messageDigest.digest())
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return encodestr
    }

    fun byte2Hex(bytes: ByteArray): String? {
        val stringBuffer = StringBuffer()
        var temp: String? = null
        for (i in bytes.indices) {
            temp = Integer.toHexString(bytes[i].toInt() and 0xFF)
            if (temp.length == 1) {
                // 1�õ�һλ�Ľ��в�0����
                stringBuffer.append("0")
            }
            stringBuffer.append(temp)
        }
        return stringBuffer.toString()
    }

    private fun hexToByte(inHex: String): Byte {
        return inHex.toInt(16).toByte()
    }

    private fun hexToByteArray(inHex: String): ByteArray? {
        var inHex = inHex
        return try {
            var hexlen = inHex.length
            val result: ByteArray
            if (hexlen % 2 == 1) {
                // ����
                hexlen++
                result = ByteArray(hexlen / 2)
                inHex = "0$inHex"
            } else {
                // ż��
                result = ByteArray(hexlen / 2)
            }
            var j = 0
            var i = 0
            while (i < hexlen) {
                result[j] = hexToByte(inHex.substring(i, i + 2))
                j++
                i += 2
            }
            result
        } catch (e: java.lang.Exception) {
            // TODO: handle exception
            null
        }
    }

    fun byteMerger(bt1: ByteArray, bt2: ByteArray): ByteArray? {
        val bt3 = ByteArray(bt1.size + bt2.size)
        System.arraycopy(bt1, 0, bt3, 0, bt1.size)
        System.arraycopy(bt2, 0, bt3, bt1.size, bt2.size)
        return bt3
    }

}