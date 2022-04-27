package work.aijiu.nfcactuator.interfaces

import android.content.Context

/**
 *
 * @ProjectName:    nfcActuator
 * @Package:        work.aijiu.nfcactuator.interfaces
 * @ClassName:      StartBefore
 * @Description:    java类作用描述
 * @Author:         aijiu
 * @CreateDate:     2022/4/27 15:17
 * @UpdateUser:     aijiu
 * @UpdateDate:     2022/4/27 15:17
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
interface StartBefore {
    fun startup(context: Context, state: Boolean);
}