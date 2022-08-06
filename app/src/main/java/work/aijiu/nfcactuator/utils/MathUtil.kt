package work.aijiu.nfcactuator.utils

import java.math.BigDecimal
import java.util.*
import kotlin.experimental.and

class MathUtil {
    /**
     * 字节数组 转成 16进制 字符串
     * @param bArr
     * @return
     */
    fun bytesToHexString(bArr: ByteArray): String? {
        val sb = StringBuffer(bArr.size)
        var sTmp: String
        for (i in bArr.indices) {
            sTmp = Integer.toHexString(0xFF and bArr[i].toInt())
            if (sTmp.length < 2) sb.append(0)
            sb.append(sTmp.uppercase(Locale.getDefault()))
        }
        return sb.toString()
    }

    /**
     * 数组 按照固定长度分割
     * @param ary
     * @param subSize
     * @return
     */
    fun splitAry(ary: ByteArray, subSize: Int): Array<Any?>? {
        val count = if (ary.size % subSize == 0) ary.size / subSize else ary.size / subSize + 1
        val subAryList: MutableList<List<Byte>> = ArrayList()
        for (i in 0 until count) {
            var index = i * subSize
            val list: MutableList<Byte> = ArrayList()
            var j = 0
            while (j < subSize && index < ary.size) {
                list.add(ary[index++])
                j++
            }
            subAryList.add(list)
        }
        val subAry = arrayOfNulls<Any>(subAryList.size)
        for (i in subAryList.indices) {
            val subList = subAryList[i]
            val subAryItem = ByteArray(subList.size)
            for (j in subList.indices) {
                subAryItem[j] = subList[j]
            }
            subAry[i] = subAryItem
        }
        return subAry
    }

    /**
     * 字节数组拼接
     * @param arrays
     * @return
     */
    fun addAll(vararg arrays: ByteArray?): ByteArray? {
        return if (arrays.size == 1) {
            arrays[0]
        } else {
            var length = 0
            val var2: Array<ByteArray?> = arrays as Array<ByteArray?>
            val var3 = arrays.size
            var var4: Int
            var4 = 0
            while (var4 < var3) {
                val array = var2[var4]
                if (null != array) {
                    length += array.size
                }
                ++var4
            }
            val result = ByteArray(length)
            length = 0
            val var8: Array<ByteArray?> = arrays
            var4 = arrays.size
            for (var9 in 0 until var4) {
                val array = var8[var9]
                if (null != array) {
                    System.arraycopy(array, 0, result, length, array.size)
                    length += array.size
                }
            }
            result
        }
    }

    /**
     * 数组指定位置插入
     * @param arr
     * @param item
     * @param index
     */
    fun insert(arr: ByteArray, item: ByteArray, index: Int): ByteArray? {
        val newArr = ByteArray(arr.size + item.size)
        if (index == 1) {
            System.arraycopy(item, 0, newArr, 0, item.size)
            System.arraycopy(arr, 0, newArr, item.size, arr.size)
        } else if (index == arr.size + 1) {
            System.arraycopy(arr, 0, newArr, 0, arr.size)
            System.arraycopy(item, 0, newArr, arr.size, item.size)
        } else {
            System.arraycopy(arr, 0, newArr, 0, index - 1)
            System.arraycopy(item, 0, newArr, index - 1, item.size)
            System.arraycopy(arr, index - 1, newArr, item.size + index - 1, arr.size - index + 1)
        }
        return newArr
    }

    fun String.decodeHex(): ByteArray {
        return chunked(2).map { it.toByte(16) }.toByteArray()
    }
    /**
     * Convert hex string to byte[]
     *
     * @param hexString
     * the hex string
     * @return byte[]
     */
    fun hexStringToBytes(hexString: String?): ByteArray? {
        var hexString = hexString
        if (hexString == null || hexString == "") {
            return null
        }
        return hexString.decodeHex();
    }

    /**
     * Convert char to byte
     *
     * @param c
     * char
     * @return byte
     */
    private fun charToByte(c: Char): Byte {
        return "0123456789ABCDEF".indexOf(c).toByte()
    }

    /**
     *
     * 截取字符串后几位
     */
    fun subStr(str: String, n: Int): String? {
        return if (n < str.length) {
            str.substring(str.length - n)
        } else {
            str
        }
    }

    /**
     * 截取字符串
     *
     * @param src
     * @param begin
     * @param count
     * @return
     */
    fun subBytes(src: ByteArray, begin: Int, count: Int): ByteArray? {
        val bs = ByteArray(count)
        for (i in begin until begin + count) bs[i - begin] = src[i]
        return bs
    }

    /**
     * 字节 转 int
     * @param bb
     * @return
     */
    fun ByteToInt(bb: Byte): Int {
        return bb.toInt() and 0xff
    }

    /**
     * 将int转为低字节在前，高字节在后的byte数组
     */
    fun tolh(n: Int): ByteArray? {
        val b = ByteArray(4)
        b[0] = (n and 0xff).toByte()
        b[1] = (n shr 8 and 0xff).toByte()
        b[2] = (n shr 16 and 0xff).toByte()
        b[3] = (n shr 24 and 0xff).toByte()
        return b
    }

    /**
     * 将int转为高字节在前，低字节在后的byte数组
     * @param int_str
     * @return
     */
    fun toHH(int_str: String): ByteArray? {
        val n = int_str.toInt()
        val b = ByteArray(4)
        b[3] = (n and 0xff).toByte()
        b[2] = (n shr 8 and 0xff).toByte()
        b[1] = (n shr 16 and 0xff).toByte()
        b[0] = (n shr 24 and 0xff).toByte()
        return b
    }

    /**
     *
     * @param n
     * @return
     */
    fun get2BytesByIntValue(n: Int): ByteArray? {
        val b = ByteArray(2)
        b[1] = (n and 0xff).toByte()
        b[0] = (n shr 8 and 0xff).toByte()
        return b
    }

    /**
     * 字节转int
     * @param byte_h
     * @param byte_l
     * @return
     */
    fun ByteToInt_1(byte_h: Byte, byte_l: Byte): Int {
        return byte_h.toInt() and 0xff * 256 + byte_l.toInt() and 0xff
    }

    /**
     *
     * 字节转数字
     */
    fun getByteValue(bt: Byte): String? {
        return ByteToInt(bt).toString()
    }

    /**
     * 字节数组转INT
     * @param b
     * @return
     */
    fun byteToInt(b: ByteArray): Int {
        var temp = 0
        var n = 0
        for (i in b.indices) {
            n = n shl 8
            temp = ByteToInt(b[i])
            n = n or temp
        }
        return n
    }

    /**
     * 通过byte数组取到short 带正负号的 高字节在前
     * @param b
     * @return
     */
    fun byteToShort(b: ByteArray): Short {
        return b.toString().toShort();
//        return (b[0] shl 8 or b[1] and 0xff).toShort()
    }


}