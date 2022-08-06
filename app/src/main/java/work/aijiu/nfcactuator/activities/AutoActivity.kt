package work.aijiu.nfcactuator.activities

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcA
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import work.aijiu.nfcactuator.R
import work.aijiu.nfcactuator.ui.theme.NfcActuatorTheme
import work.aijiu.nfcactuator.utils.StatusBarUtils
import work.aijiu.nfcactuator.utils.nfc.NewNfcHelper
import java.util.*
import kotlin.system.exitProcess

/**
 * nfc适配器
 */
private var nfcAdapter: NfcAdapter? = null
private var mContext: Context? = null
private var screenHeight = 0f
private var screenWidth = 0f
private val notSupportedNFCTip = "您的设备不支持NFC"
private val nfcOffTip = "NFC功能未开启,是否进行设置"
private var openDialog: MutableState<Boolean>? = null
private var mTag: Tag? = null
private var nfcA: NfcA? = null
private var help = NewNfcHelper()
private var notSupportedAutoTip: String = "该产品不支持自动识别,确定进入手动选择界面"
private var noSpAutoDialog: MutableState<Boolean>? = null

private var exitTime: Long = 0
private var exitDialog: MutableState<Boolean>? = null


/**
 *
 * @ProjectName:    nfcActuator
 * @Package:        work.aijiu.nfcactuator.activities
 * @ClassName:      AutoActivity
 * @Description:    自动识别设备
 * @Author:         aijiu
 * @CreateDate:     2022/4/28 11:29
 * @UpdateUser:     aijiu
 * @UpdateDate:     2022/4/28 11:29
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class AutoActivity : ComponentActivity(), NfcAdapter.ReaderCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setTextDark(this, true)
        mContext = this
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        setContent {
            NfcActuatorTheme {
                AutoContent()
            }
        }
        val dm: DisplayMetrics? = mContext?.resources?.displayMetrics
        val density = dm?.density
        screenHeight = dm!!.heightPixels / density!!
        screenWidth = dm.widthPixels / density
        if (nfcAdapter != null && nfcAdapter?.isEnabled == true) {
            val options = Bundle()
            options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 1000) // 1s延时
            nfcAdapter!!.enableReaderMode(
                this,
                this,
                NfcAdapter.FLAG_READER_NFC_A or
                        NfcAdapter.FLAG_READER_NFC_B or
                        NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                options
            )
        }
    }

    // 自动识别NFC逻辑
    override fun onTagDiscovered(tag: Tag?) {
        mTag = tag
        nfcA = NfcA.get(tag);
        autoChoose(intent, tag)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event!!.action == KeyEvent.ACTION_DOWN) {
            if(noSpAutoDialog!!.value){
                noSpAutoDialog!!.value = false
            }else if(openDialog!!.value){
                openDialog!!.value = false
            }else{
                if (System.currentTimeMillis() - exitTime > 2000) {
                    Toast.makeText(this, "再次返回退出应用", Toast.LENGTH_LONG).show()
                    exitTime = System.currentTimeMillis()
                }else {
                    exitDialog!!.value = true
                }
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AutoContent() {
    var context = LocalContext.current
    var message by remember { mutableStateOf(checkNFCString()) }
    openDialog = remember { mutableStateOf(checkNFC()) }
    noSpAutoDialog = remember { mutableStateOf(false) }
    exitDialog = remember { mutableStateOf(false) }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data("https://www.aijiu.work/resources/images/auto6.gif")
            .crossfade(true)
            .build(),
        contentScale = ContentScale.Crop,
        imageLoader = ImageLoader.Builder(LocalContext.current)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.height(500.dp),
        )
        Spacer(modifier = Modifier.height(50.dp))
        AutoText()
        LoadingRow()
        Text(text = "手动选择", modifier = Modifier.clickable {
            val intent = Intent(context, ChoiceActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(intent)
        })
    }
    // NFC支持提示
    CheckNFcTip(openDialog!!, message);
    // 不支持自动识别提示
    NotSPAuto(noSpAutoDialog!!)
    // 退出应用提示框
    ExitDialog(exitDialog!!)
}

@Composable
private fun AutoText() {
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
    Text(
        text = stringResource(id = R.string.auto_text),
        fontSize = 12.sp,
        modifier = Modifier.padding(0.dp),
        color = MaterialTheme.colors.onSurface.copy(alpha = alpha)
    )
}

@Composable
private fun Dot(duration: Int) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = duration
                0.7f at 500
            },
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.onSurface.copy(alpha = alpha))
    )
}

@Composable
private fun LoadingRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Dot(300)
        Spacer(modifier = Modifier.width(3.dp))
        Dot(400)
        Spacer(modifier = Modifier.width(3.dp))
        Dot(500)
    }
}

@Composable
fun CheckNFcTip(openDialog: MutableState<Boolean>, message: String) {
    if (openDialog.value) {
        val alphaAnimation = remember { Animatable(0f) }
        LaunchedEffect(alphaAnimation) {
            alphaAnimation.animateTo(
                targetValue = 1f,
                animationSpec = tween(800)
            )
        }
        Surface(
            modifier = Modifier
                .width(300.dp)
                .height(180.dp)
                .absoluteOffset(
                    x = ((screenWidth - 300) / 2).dp,
                    y = ((screenHeight - 200) / 2).dp
                ),
            elevation = 10.dp, shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(colorResource(id = R.color.snow).copy(alpha = alphaAnimation.value)),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(136.dp)
                        .padding(20.dp, 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "提示", fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = message, fontSize = 16.sp, textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                }
                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.whitesmoke))
                )
                Row(
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.snow).copy(alpha = alphaAnimation.value)),
                ) {
                    TextButton(
                        modifier = Modifier
                            .width(148.dp)
                            .fillMaxHeight(),
                        onClick = {
                            // 跳转到开启NFC设置中
                            openDialog.value = false
                            if (message == nfcOffTip) {
                                mContext?.startActivity(Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS))
                            }
                        }
                    ) {
                        Text(
                            text = "确定", fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .background(colorResource(id = R.color.whitesmoke))
                    )
                    TextButton(
                        modifier = Modifier
                            .width(148.dp)
                            .fillMaxHeight(),
                        onClick = {
                            openDialog.value = false
                        }
                    ) {
                        Text(
                            text = "取消", fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(color = colorResource(id = R.color.darkgrey)),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotSPAuto(openDialog: MutableState<Boolean>) {
    if (openDialog.value) {
        val alphaAnimation = remember { Animatable(0f) }
        LaunchedEffect(alphaAnimation) {
            alphaAnimation.animateTo(
                targetValue = 1f,
                animationSpec = tween(800)
            )
        }
        Surface(
            modifier = Modifier
                .width(300.dp)
                .height(180.dp)
                .absoluteOffset(
                    x = ((screenWidth - 300) / 2).dp,
                    y = ((screenHeight - 200) / 2).dp
                ),
            elevation = 10.dp, shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(colorResource(id = R.color.snow).copy(alpha = alphaAnimation.value)),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(136.dp)
                        .padding(20.dp, 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "提示", fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = notSupportedAutoTip, fontSize = 16.sp, textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                }
                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.whitesmoke))
                )
                Row(
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.snow).copy(alpha = alphaAnimation.value)),
                ) {
                    TextButton(
                        modifier = Modifier
                            .width(148.dp)
                            .fillMaxHeight(),
                        onClick = {
                            // 跳转到手动选择产品界面
                            openDialog.value = false
                            mContext?.startActivity(Intent(mContext, ChoiceActivity::class.java))
                        }
                    ) {
                        Text(
                            text = "确定", fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .background(colorResource(id = R.color.whitesmoke))
                    )
                    TextButton(
                        modifier = Modifier
                            .width(148.dp)
                            .fillMaxHeight(),
                        onClick = {
                            openDialog.value = false
                        }
                    ) {
                        Text(
                            text = "取消", fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(color = colorResource(id = R.color.darkgrey)),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExitDialog(openDialog: MutableState<Boolean>) {
    if(openDialog.value){
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "提示") },
            text = {
                Text(
                    text = "是否退出应用程序?"
                )
            }, confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        exitProcess(0)
                    }
                ) {
                    Text(text = "确认")
                }
            }, dismissButton = {
                TextButton(onClick = { openDialog.value = false }) {
                    Text(text = "取消")
                }
            })
    }
}

private fun checkNFC(): Boolean {
    if (nfcAdapter != null) {
        return !nfcAdapter!!.isEnabled
    } else {
        return false
    }
}

private fun checkNFCString(): String {
    if (nfcAdapter != null) {
        if (!nfcAdapter!!.isEnabled) {
            return nfcOffTip
        }
    } else {
        return notSupportedNFCTip
    }
    return "";
}

private fun autoChoose(intent: Intent, tag: Tag?) {
    if (help.Issupport(tag)) {
        // 复旦微
        help.authNew(nfcA);
        val readTag = help.readTag(nfcA, 4, 4)
        val byte: ByteArray = readTag?.get(help.RESULT_BYTE) as ByteArray
        // TODO 根据山区数据进行跳转逻辑
    } else {
        // 利尔达
        // 提示用户芯片不支持自动识别
        noSpAutoDialog?.value = true
    }
}
