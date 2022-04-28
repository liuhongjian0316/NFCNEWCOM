package work.aijiu.nfcactuator

import android.content.res.Configuration
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import work.aijiu.nfcactuator.common.activateaction.ActivateContext
import work.aijiu.nfcactuator.common.activateaction.Activated
import work.aijiu.nfcactuator.common.activateaction.NotActivated
import work.aijiu.nfcactuator.ui.theme.NfcActuatorTheme
import work.aijiu.nfcactuator.utils.DeviceUuidFactory
import work.aijiu.nfcactuator.utils.StatusBarUtils
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setTextDark(this, true);
        setContent {
            NfcActuatorTheme{
                WeclomeContent()
            }
        }
        // 逻辑 检测app 手机否激活
        checkIsActivate()
    }

    fun checkIsActivate() {
        val activateContext1: ActivateContext =  ActivateContext(Activated())
        val activateContext2: ActivateContext =  ActivateContext(NotActivated())
        val delayTask: TimerTask = object : TimerTask() {
            override fun run() {
                activateContext1.startBefore(applicationContext,getActivateState())
                activateContext2.startBefore(applicationContext,getActivateState())
                finish()
//                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            }
        }
        val timer = Timer()
        timer.schedule(delayTask, 3000)
    }
    fun getActivateState(): Boolean {
//        val deviceId = DeviceUtil().getDeviceId(this)
//        return DeviceUuidFactory(this).getUuid().toString() == "e067a948-fbbe-37a7-b9b4-9db59155d2ec"
        return true
    }
}


@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WeclomeContent() {
    Surface(color = MaterialTheme.colors.background,elevation= 10.dp) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,//设置水平居中对齐
            verticalAlignment =  Alignment.CenterVertically,//设置垂直居中对齐
        ) {
            val painter = rememberAsyncImagePainter(
                model =  ImageRequest.Builder(LocalContext.current)
                    .data("https://www.aijiu.work/resources/images/nfcgif.gif")
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                imageLoader = ImageLoader.Builder(LocalContext.current)
                    .components {
                        if (SDK_INT >= 28) {
                            add(ImageDecoderDecoder.Factory())
                        } else {
                            add(GifDecoder.Factory())
                        }
                    }
                    .build()
            )
            Surface(color = colorResource(id = R.color.logo_bg),elevation= 10.dp, shape = RoundedCornerShape(16.dp)) {
                Row(
                    modifier = Modifier.size(168.dp,100.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment =  Alignment.CenterVertically,
                ){
                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.padding(0.dp)
                    )
                }
            }
        }
    }
}





