package work.aijiu.nfcactuator.activities

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcA
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import work.aijiu.nfcactuator.components.C2.C2Product
import work.aijiu.nfcactuator.components.U1Product
import work.aijiu.nfcactuator.ui.theme.NfcActuatorTheme
import work.aijiu.nfcactuator.utils.StatusBarUtils
import java.util.*


/**
 *
 * @ProjectName:    nfcActuator
 * @Package:        work.aijiu.nfcactuator.activities
 * @ClassName:      ActiviteActivity
 * @Description:    产品
 * @Author:         aijiu
 * @CreateDate:     2022/4/27 15:21
 * @UpdateUser:     aijiu
 * @UpdateDate:     2022/4/27 15:21
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class ProductActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setTransparent(this)
        StatusBarUtils.setTextDark(this, true);
//        val uiMode = this.resources.configuration.uiMode
//        StatusBarUtils.setColor(this,
//            if(Configuration.UI_MODE_NIGHT_YES == uiMode) 0xFFFFFF else 0x000000
//        )
        // 获取当前的产品头
        val product = intent.getStringExtra("product")
        val series = intent.getByteExtra("series",0x00)
        if("U1" == product){
            setContent {
                NfcActuatorTheme{
                    C2Product(series)
                }
            }
        }
        if("U2" == product){
            setContent {
                NfcActuatorTheme{
                    U1Product()
                }
            }
        }
        if("R1" == product){
            setContent {
                NfcActuatorTheme{
                    U1Product()
                }
            }
        }
        if("R2" == product){
            setContent {
                NfcActuatorTheme{
                    U1Product()
                }
            }
        }
        if("E1" == product){
            setContent {
                NfcActuatorTheme{
                    U1Product()
                }
            }
        }
        if("E2" == product){
            setContent {
                NfcActuatorTheme{
                    U1Product()
                }
            }
        }
        if("Q" == product){
            setContent {
                NfcActuatorTheme{
                    U1Product()
                }
            }
        }
        if("Z" == product){
            setContent {
                NfcActuatorTheme{
                    U1Product()
                }
            }
        }
        if("NewQ" == product){
            setContent {
                NfcActuatorTheme{
                    U1Product()
                }
            }
        }
        if("NewZ" == product){
            setContent {
                NfcActuatorTheme{
                    U1Product()
                }
            }
        }
        if("NewU2" == product){
            setContent {
                NfcActuatorTheme{
                    U1Product()
                }
            }
        }
    }
}
