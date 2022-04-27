package work.aijiu.nfcactuator.common.activateaction

import android.content.Context
import android.content.Intent
import work.aijiu.nfcactuator.activities.ActiviteActivity
import work.aijiu.nfcactuator.interfaces.StartBefore

/**
 *
 * @ProjectName:    nfcActuator
 * @Package:        work.aijiu.nfcactuator.common.activateaction
 * @ClassName:      NotActivated
 * @Description:    java类作用描述
 * @Author:         aijiu
 * @CreateDate:     2022/4/25 10:47
 * @UpdateUser:     aijiu
 * @UpdateDate:     2022/4/25 10:47
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class NotActivated: StartBefore {
    override fun startup(context: Context, state: Boolean) {
        if(!state){
            val intent = Intent(context, ActiviteActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(intent)
        }
    }
}