package work.aijiu.nfcactuator.utils

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 *
 * @ProjectName:    nfcActuator
 * @Package:        work.aijiu.nfcactuator.utils
 * @ClassName:      EncryptUtils
 * @Description:    java类作用描述
 * @Author:         aijiu
 * @CreateDate:     2022/4/26 9:41
 * @UpdateUser:     aijiu
 * @UpdateDate:     2022/4/26 9:41
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class EncryptUtils {
    @Throws(Exception::class)
    fun encryptDES(encryptString: String, encryptKey: String): String? {
        //返回实现指定转换的Cipher对象 "算法/模式/填充"
        val cipher: Cipher = Cipher.getInstance("DES/CBC/PKCS5Padding")
        //创建一个DESKeySpec对象，使用8个字节的key作为DES密钥的内容
        val desKeySpec = DESKeySpec(encryptKey.toByteArray(charset("UTF-8")))
        //返回转换指定算法的秘密密钥的SecretKeyFactory对象
        val keyFactory: SecretKeyFactory = SecretKeyFactory.getInstance("DES")
        //根据提供的密钥生成SecretKey对象
        val secretKey: SecretKey = keyFactory.generateSecret(desKeySpec)
        //使用iv中的字节作为iv来构造一个iv ParameterSpec对象。复制该缓冲区的内容来防止后续修改
        val iv = IvParameterSpec(encryptKey.toByteArray())
        //用密钥和一组算法参数初始化此 Cipher；Cipher：加密、解密、密钥包装或密钥解包，具体取决于 opmode 的值。
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv)
        //加密同时解码成字符串返回
        return String(BASE64.encode(cipher.doFinal(encryptString.toByteArray(charset("UTF-8")))))
    }

    @Throws(Exception::class)
    fun decryptDES(decodeString: String, decodeKey: String): String? {
        //使用指定密钥构造IV
        val iv = IvParameterSpec(decodeKey.toByteArray())
        //根据给定的字节数组和指定算法构造一个密钥。
        val skeySpec = SecretKeySpec(decodeKey.toByteArray(), "DES")
        //返回实现指定转换的 Cipher 对象
        val cipher: Cipher = Cipher.getInstance("DES/CBC/PKCS5Padding")
        //解密初始化
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)
        //解码返回
        val byteMi: ByteArray = BASE64.decode(decodeString.toCharArray())
        val decryptedData: ByteArray = cipher.doFinal(byteMi)
        return String(decryptedData)
    }
}