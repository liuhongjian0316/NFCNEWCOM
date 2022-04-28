package work.aijiu.nfcactuator.common.activateaction

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import work.aijiu.nfcactuator.MainActivity
import work.aijiu.nfcactuator.activities.AutoActivity
import work.aijiu.nfcactuator.interfaces.StartBefore
import java.util.*
import kotlin.system.exitProcess


/**
 *
 * @ProjectName:    nfcActuator
 * @Package:        work.aijiu.nfcactuator.common.activateaction
 * @ClassName:      activated
 * @Description:    java类作用描述
 * @Author:         aijiu
 * @CreateDate:     2022/4/25 10:46
 * @UpdateUser:     aijiu
 * @UpdateDate:     2022/4/25 10:46
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class Activated:StartBefore {

    override fun startup(context: Context,state: Boolean) {
       // 直接进入主页
        if(state){
            val intent = Intent(context, AutoActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(intent)
        }
    }
}

