package work.aijiu.nfcactuator.common.activateaction

import android.content.Context
import android.util.Log
import work.aijiu.nfcactuator.interfaces.StartBefore

/**
 *
 * @ProjectName:    nfcActuator
 * @Package:        work.aijiu.nfcactuator.common.activateaction
 * @ClassName:      ActivateContext
 * @Description:    java类作用描述
 * @Author:         aijiu
 * @CreateDate:     2022/4/25 10:54
 * @UpdateUser:     aijiu
 * @UpdateDate:     2022/4/25 10:54
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class ActivateContext() {
    private var startBefore: StartBefore? = null

    constructor(startBefore: StartBefore) : this() {
        this.startBefore = startBefore
    }

    fun startBefore(context: Context,state:Boolean){
        startBefore?.startup(context,state)
    }
}