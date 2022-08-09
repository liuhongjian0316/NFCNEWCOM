package work.aijiu.nfcactuator.activities.auth

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import work.aijiu.nfcactuator.R
import work.aijiu.nfcactuator.ui.theme.NfcActuatorTheme
import work.aijiu.nfcactuator.utils.StatusBarUtils

class AuthNewActivity : ComponentActivity() {

    private var mContext: Context? = null
    private var thisMode: MutableState<String?>? = null
    private var receiveMode: String? = null
    private var screenHeight = 0f
    private var screenWidth = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setTextDark(this, true)
        mContext = this
        val mode = intent.getStringExtra("mode")
        receiveMode = mode
        setContent {
            NfcActuatorTheme {
                MainContent()
            }
        }
        // 计算出屏幕宽高
        val dm: DisplayMetrics? = mContext?.resources?.displayMetrics
        val density = dm?.density
        screenHeight = dm!!.heightPixels / density!!
        screenWidth = dm.widthPixels / density

    }

    @Composable
    private fun MainContent() {
        val PI: Float = 3.14f
        val frequency = 10
        val r1: Float = 10.0f
        val r2: Float = 9.0f
        var l1 = 2 * PI * r1
        var item1 = l1 / 10 // 6.28


        // 1s 之内旋转将周长旋转完


        val infiniteTransition = rememberInfiniteTransition()
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 1000
                    0.7f at 500
                },
                repeatMode = RepeatMode.Reverse
            )
        )
        // 默认读模式
        thisMode = remember { mutableStateOf("read") }
        thisMode!!.value = receiveMode

        // 同心圆
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .shadow(10.dp, shape = CircleShape)
                .absoluteOffset(
                    x = ((screenWidth - 300) / 2).dp,
                    y = ((screenHeight - 200) / 2).dp
                )
                .background(
                    color = colorResource(
                        id = R.color.chocolate
                    )
                ),
        )
        Box(
            modifier = Modifier
                .width(90.dp)
                .height(90.dp)
                .shadow(10.dp, shape = CircleShape)
                .absoluteOffset(
                    x = ((screenWidth - 300) / 2).dp,
                    y = ((screenHeight - 200) / 2).dp
                )
                .background(
                    color = colorResource(
                        id = R.color.white
                    )
                ),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.operation),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(70.dp, 60.dp),
                contentScale = ContentScale.FillBounds,
            )
            if (thisMode!!.value == "read") {
                Text(
                    text = stringResource(id = R.string.read_mode),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(0.dp),
                    color = MaterialTheme.colors.onSurface.copy(alpha = alpha)
                )

            }
            if (thisMode!!.value == "write") {
                Text(
                    text = stringResource(id = R.string.write_mode),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(0.dp),
                    color = MaterialTheme.colors.onSurface.copy(alpha = alpha)
                )
            }
        }
    }
}