package work.aijiu.nfcactuator.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.R
import java.lang.reflect.Field
import java.lang.reflect.Method


/**
 *
 * @ProjectName:    nfcActuator
 * @Package:        work.aijiu.nfcactuator.utils
 * @ClassName:      StatusBarUtils
 * @Description:    java类作用描述
 * @Author:         aijiu
 * @CreateDate:     2022/4/27 14:26
 * @UpdateUser:     aijiu
 * @UpdateDate:     2022/4/27 14:26
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
object StatusBarUtils {
    private val FAKE_STATUS_BAR_VIEW_ID: Int = work.aijiu.nfcactuator.R.id.fake_status_bar_view

    /**
     * 获得状态栏的高度
     *
     * @param context 上下文
     * @return 状态栏高度
     */
    fun getHeight(context: Context): Int {
        var statusBarHeight = 0
        try {
            val resourceId: Int = context.resources.getIdentifier(
                "status_bar_height", "dimen",
                "android"
            )
            if (resourceId > 0) {
                statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return statusBarHeight
    }

    /**
     * 设置状态栏颜色
     *
     * @param context 上下文，尽量使用Activity
     * @param color   状态栏颜色
     */
    fun setColor(context: Context, color: Int) {
        if (context is Activity) {
            setColor((context as Activity).window, color)
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @param window 窗口，可用于Activity和Dialog等
     * @param color  状态栏颜色
     */
    fun setColor(window: Window, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = color
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setColor(window, color, false)
        }
    }

    /**
     * Android 5.0 以下版本设置状态栏颜色
     *
     * @param window        窗口
     * @param color         状态栏颜色值
     * @param isTransparent 是否透明
     */
    fun setColor(
        window: Window, color: Int,
        isTransparent: Boolean
    ) {
        val context: Context = window.getContext()
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        val decorView = window.decorView
        val contentView: View = decorView.findViewById(R.id.content)
        if (contentView != null) {
            contentView.setPadding(0, if (isTransparent) 0 else getHeight(context), 0, 0)
        }
        val fakeStatusBarView: View = decorView.findViewById(FAKE_STATUS_BAR_VIEW_ID)
        if (fakeStatusBarView != null) {
            fakeStatusBarView.setBackgroundColor(color)
            if (fakeStatusBarView.visibility === View.GONE) {
                fakeStatusBarView.visibility = View.VISIBLE
            }
        } else {
            // 绘制一个和状态栏一样高的矩形
            val statusBarView = View(context)
            val layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                getHeight(context)
            )
            statusBarView.layoutParams = layoutParams
            statusBarView.setBackgroundColor(color)
            statusBarView.id = FAKE_STATUS_BAR_VIEW_ID
//            decorView.addView(statusBarView)
        }
    }

    /**
     * 设置状态栏透明
     *
     * @param context 上下文，尽量使用Activity
     */
    fun setTransparent(context: Context) {
        if (context is Activity) {
            setTransparent((context as Activity).window)
        }
    }

    /**
     * 设置状态栏透明
     *
     * @param window 窗口，可用于Activity和Dialog等
     */
    fun setTransparent(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.decorView
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setColor(window, -0x80000000, true)
        }
    }

    /**
     * 设置状态栏是否为黑色文字
     *
     * @param context 上下文，尽量使用Activity
     * @param isDark  是否为黑色文字
     */
    fun setTextDark(context: Context, isDark: Boolean) {
        if (context is Activity) {
            setTextDark((context as Activity).window, isDark)
        }
    }

    /**
     * 设置状态栏是否为黑色文字
     *
     * @param window 窗口，可用于Activity和全屏Dialog
     * @param isDark 是否为黑色文字
     */
    private fun setTextDark(window: Window, isDark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView: View = window.decorView
            val systemUiVisibility: Int = decorView.getSystemUiVisibility()
            if (isDark) {
                decorView.setSystemUiVisibility(systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            } else {
                decorView.setSystemUiVisibility(systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            when (OSUtils.getRomType()) {
                OSUtils.ROM.MIUI -> setMIUIDark(window, isDark)
                OSUtils.ROM.Flyme -> setFlymeDark(window, isDark)
                else -> {}
            }
        }
    }

    /**
     * 设置MIUI系统状态栏是否为黑色文字
     *
     * @param window 窗口，仅可用于Activity
     * @param isDark 是否为黑色文字
     */
    private fun setMIUIDark(window: Window, isDark: Boolean) {
        try {
            val clazz: Class<out Window?> = window.javaClass
            val darkModeFlag: Int
            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field: Field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            darkModeFlag = field.getInt(layoutParams)
            val extraFlagField: Method = clazz.getMethod(
                "setExtraFlags",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
            extraFlagField.invoke(window, if (isDark) darkModeFlag else 0, darkModeFlag)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 设置Flyme系统状态栏是否为黑色文字
     *
     * @param window 窗口
     * @param isDark 是否为黑色文字
     */
    private fun setFlymeDark(window: Window?, isDark: Boolean) {
        if (window != null) {
            try {
                val lp: WindowManager.LayoutParams = window.attributes
                val darkFlag: Field = WindowManager.LayoutParams::class.java
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags: Field = WindowManager.LayoutParams::class.java
                    .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit: Int = darkFlag.getInt(null)
                var value: Int = meizuFlags.getInt(lp)
                value = if (isDark) {
                    value or bit
                } else {
                    value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                window.setAttributes(lp)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}