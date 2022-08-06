package work.aijiu.nfcactuator.utils

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.view.WindowManager
import androidx.annotation.NonNull
import androidx.annotation.Nullable


class ScreenUtils {
    /***
     * 获取屏幕的高度，全面屏和非全面屏
     * @param context
     * @return
     */
//    fun getFullActivityHeight(@Nullable context: Context?): Int {
//        return if (!isAllScreenDevice()) {
//            getScreenHeight(context)
//        } else getScreenRealHeight(context)
//    }

    private val PORTRAIT = 0
    private val LANDSCAPE = 1

    @Volatile
    private var mHasCheckAllScreen = false

    @Volatile
    private var mIsAllScreenDevice = false

    @NonNull
    private val mRealSizes: Array<Point?> = arrayOfNulls<Point>(2)

//    fun getScreenRealHeight(@Nullable context: Context?): Int {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            return getScreenHeight(context)
//        }
//        var orientation = context?.resources?.configuration?.orientation
//        orientation = if (orientation == Configuration.ORIENTATION_PORTRAIT) PORTRAIT else LANDSCAPE
//        if (mRealSizes[orientation] == null) {
//            val windowManager =
//                (if (context != null) context.getSystemService(Context.WINDOW_SERVICE) else context?.getSystemService(
//                    Context.WINDOW_SERVICE
//                ))
//                    ?: return getScreenHeight(context)
//            val display = windowManager.defaultDisplay
//            val point = Point()
//            display.getRealSize(point)
//            mRealSizes[orientation] = point
//        }
//        return mRealSizes[orientation]!!.y
//    }
//
//    fun getScreenHeight(@Nullable context: Context?): Int {
//        return if (context != null) {
//            context.getResources().getDisplayMetrics().heightPixels
//        } else 0
//    }
//
//    /***
//     * 获取当前手机是否是全面屏
//     * @return
//     */
//    fun isAllScreenDevice(): Boolean {
//        if (mHasCheckAllScreen) {
//            return mIsAllScreenDevice
//        }
//        mHasCheckAllScreen = true
//        mIsAllScreenDevice = false
//        // 低于 API 21的，都不会是全面屏。。。
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            return false
//        }
//        val windowManager = AppManager.getAppManager().getNowContext()
//            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        if (windowManager != null) {
//            val display = windowManager.defaultDisplay
//            val point = Point()
//            display.getRealSize(point)
//            val width: Float
//            val height: Float
//            if (point.x < point.y) {
//                width = point.x
//                height = point.y
//            } else {
//                width = point.y
//                height = point.x
//            }
//            if (height / width >= 1.97f) {
//                mIsAllScreenDevice = true
//            }
//        }
//        return mIsAllScreenDevice
//    }


}