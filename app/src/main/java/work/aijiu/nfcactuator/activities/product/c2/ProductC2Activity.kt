package work.aijiu.nfcactuator.activities.product.c2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcA
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import work.aijiu.nfcactuator.R
import work.aijiu.nfcactuator.activities.AutoActivity
import work.aijiu.nfcactuator.activities.auth.AuthNewActivity
import work.aijiu.nfcactuator.enum.CommonEnum
import work.aijiu.nfcactuator.ui.theme.Blue200
import work.aijiu.nfcactuator.ui.theme.NfcActuatorTheme
import work.aijiu.nfcactuator.utils.StatusBarUtils
import work.aijiu.nfcactuator.utils.nfc.NewNfcHelper
import java.util.*
import kotlin.system.exitProcess


class ProductC2Activity : ComponentActivity(), NfcAdapter.ReaderCallback {

    // 全局变量
    private var newNfcHelper:NewNfcHelper = NewNfcHelper()
    private var mContext: Context? = null
    private var nfcAdapter: NfcAdapter? = null
    private var mTag: Tag? = null
    private var nfcA: NfcA? = null
    private var nfcEnable:Boolean = true
    private var mode: MutableState<String>? = null
    private var screenHeight = 0f
    private var screenWidth = 0f
    private var isSupportNfc: MutableState<Boolean>? = null
    private var exitDialog: MutableState<Boolean>? = null
    private var exitTime:Long = 0
    // tips
    private val notSupportedNFCTip = "您的设备不支持NFC"
    private val nfcOffTip = "NFC功能未开启,是否进行设置"

    // 注意事项 一定要在onCreate之前定义 新版本
    var requestDataLauncher =  registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
    ) {
        val data = it.data
        val resultCode = it.resultCode
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setTransparent(this)
        StatusBarUtils.setTextDark(this, true)
        mContext = this
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        setContent {
            NfcActuatorTheme {
                MainContent()
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

    /**
     * NFC逻辑
     */
    override fun onTagDiscovered(tag: Tag?) {
        mTag = tag
        nfcA = NfcA.get(tag)
        handlerC2(intent);
    }

    /**
     * 劫持按键事件
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event!!.action == KeyEvent.ACTION_DOWN) {
            if(isSupportNfc!!.value){
                isSupportNfc!!.value = false
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

    private fun handlerC2(intent: Intent) {
        if(!nfcEnable){
            return
        }
        if(newNfcHelper.Issupport(mTag)){
            if(mode!!.value == "read"){

            }
            if(mode!!.value == "write"){

            }
            var cusIntent = Intent(mContext, AuthNewActivity::class.java)
            cusIntent.putExtra("mode",mode!!.value)
            requestDataLauncher.launch(cusIntent)
        }
    }


    @Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun MainContent() {
        // 是否支持NF
        isSupportNfc = remember { mutableStateOf(checkNFC()) }
        var isSupportNfcTip by remember { mutableStateOf(checkNFCTip()) }
        exitDialog = remember { mutableStateOf(false) }


        val sysUiController = rememberSystemUiController()
        SideEffect {
            sysUiController.setStatusBarColor(
                color = Blue200
            )
        }

        // 读写模式
        var context = LocalContext.current
        mode = remember { mutableStateOf("read") }
        var openDialog: MutableState<Boolean> = remember {
            mutableStateOf(false)
        }
        // 底部弹出框
        val (gesturesEnabled) = remember { mutableStateOf(false) } // 关闭手势
        var bottomShow = rememberBottomDrawerState(BottomDrawerValue.Closed)
        var bottomType by remember { mutableStateOf(CommonEnum.BottomPop.DEFAULT) }
        val scope = rememberCoroutineScope()

        val com_list = stringArrayResource(id = R.array.com_list)
        val parity_list = stringArrayResource(id = R.array.parity_list)
        val baud_rate_list = stringArrayResource(id = R.array.baud_rate_list)
        val self_test_list = stringArrayResource(id = R.array.self_test_list)

        var ctl_open by remember { mutableStateOf(1000) }
        var return_open by remember { mutableStateOf(1000) }
        var supply_temp by remember { mutableStateOf(1000) }
        var return_temp by remember { mutableStateOf(1000) }
        var supply_pressure by remember { mutableStateOf(1000) }
        var return_pressure by remember { mutableStateOf(1000) }
        var software_version by remember { mutableStateOf("") }

        var com_mode by remember { mutableStateOf(0) }
        var rs by remember { mutableStateOf(1) }

        BottomDrawer(
            modifier = Modifier,
            gesturesEnabled = gesturesEnabled,
            drawerShape = MaterialTheme.shapes.medium,
            drawerContentColor = Color.Black.copy(alpha = 1f),
            drawerState = bottomShow,
            drawerContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(MaterialTheme.colors.background)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(10.dp, 0.dp)
                            .background(Color(0xF2F2F2)),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.clickable {
                                scope.launch {
                                    bottomShow.close()
                                    bottomType = CommonEnum.BottomPop.DEFAULT
                                }
                            },
                            text = stringResource(id = R.string.cancel),
                            color = MaterialTheme.colors.primary
                        )
                        val chooseTxt = stringResource(id = R.string.choice_txt)
                        Text(
                            text = when (bottomType) {
                                CommonEnum.BottomPop.DEFAULT -> stringResource(id = R.string.default_txt)
                                CommonEnum.BottomPop.COM_MODE -> stringResource(id = R.string.com_mode) + chooseTxt
                                CommonEnum.BottomPop.RS485 -> stringResource(id = R.string.rs_address) + chooseTxt
                                CommonEnum.BottomPop.PARITY -> stringResource(id = R.string.parity) + chooseTxt
                                CommonEnum.BottomPop.BAUD_RATE -> stringResource(id = R.string.baud_rate) + chooseTxt
                                CommonEnum.BottomPop.SELF_TEST_MODE -> stringResource(id = R.string.self_test) + chooseTxt
                                else -> stringResource(id = R.string.default_txt)
                            }, fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(id = R.string.sure),
                            color = MaterialTheme.colors.primary
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.LightGray)
                                .height(200.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            repeat(10) {
                                Text("Item $it", modifier = Modifier.padding(2.dp))
                            }
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.LightGray)
                                .height(200.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            repeat(10) {
                                Text("Item $it", modifier = Modifier.padding(2.dp))
                            }
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.LightGray)
                                .height(200.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            repeat(10) {
                                Text("Item $it", modifier = Modifier.padding(2.dp))
                            }
                        }
                    }
                }
            },
            content = {
                Scaffold(
                    topBar = {
                        Row(
                            modifier = Modifier
                                .height(StatusBarUtils.getHeight(context).dp)
                                .background(MaterialTheme.colors.primarySurface),
                            verticalAlignment = Alignment.Bottom,
                        ) {
                            TopAppBar(
                                modifier = Modifier.padding(0.dp),
                                elevation = Dp(0f),
                                backgroundColor = MaterialTheme.colors.primarySurface,
                                title = { Text(text = stringResource(id = R.string.c2)) },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        context.startActivity(
                                            Intent(
                                                context,
                                                AutoActivity::class.java
                                            )
                                        )
                                    }) {
                                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                                    }
                                },
                                actions = {
                                    // RowScope here, so these icons will be placed horizontally
                                    IconButton(onClick = {
                                        // 弹出设置菜单

                                    }) {
                                        Icon(
                                            Icons.Filled.Menu,
                                            contentDescription = "Localized description"
                                        )
                                    }
                                }
                            )
                        }

                    },
                    bottomBar = {
                        BottomAppBar(
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth()
                                .padding(0.dp)
                                .background(MaterialTheme.colors.background),
                            backgroundColor = MaterialTheme.colors.background
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colors.background),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(60.dp)
                                        .background(if (mode!!.value == "read") Color.Blue else Color.Red)
                                        .clickable {
                                            mode!!.value = if (mode!!.value == "read") "write" else "read"
                                            openDialog.value = true
                                        },
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (mode!!.value == "read") stringResource(id = R.string.read) else stringResource(
                                            id = R.string.write
                                        ),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 22.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    },
                    content = {
                        BoxWithConstraints() {
                            val width = maxWidth
                            LazyColumn() {
                                item {
                                    Top(width)
                                    Divider(
                                        modifier = Modifier.padding(16.dp, 0.dp)
                                    )
                                }
                                item {
                                    var spanList: MutableList<String> = mutableListOf(
                                        stringResource(id = R.string.local_ctr),
                                        stringResource(id = R.string.com_mode),
                                        stringResource(id = R.string.rs_address),
                                        stringResource(id = R.string.parity),
                                        stringResource(id = R.string.baud_rate),
                                        stringResource(id = R.string.self_test),
                                    )
                                    var iconList: MutableList<Painter> = mutableListOf(
                                        painterResource(id = R.drawable.ic_localcontrol),
                                        painterResource(id = R.drawable.ic_com_mode),
                                        painterResource(id = R.drawable.ic_rs485),
                                        painterResource(id = R.drawable.ic_parity),
                                        painterResource(id = R.drawable.ic_baud_rate),
                                        painterResource(id = R.drawable.ic_self_test),
                                    )
                                    var valueList by remember {
                                        mutableStateOf(
                                            mutableListOf(
                                                "ENTER",
                                                "RS485",
                                                "1",
                                                "无校验",
                                                "19200",
                                                "上电自检",
                                            )
                                        )
                                    }
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        for ((index, item) in spanList.withIndex()) {
                                            val painter = iconList[index]
                                            val value = valueList[index]
                                            Row(
                                                horizontalArrangement = Arrangement.Center,
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier
                                                    .padding(6.dp, 0.dp)
                                                    .height(40.dp)
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        scope.launch {
                                                            bottomType = when (index) {
                                                                0 -> CommonEnum.BottomPop.LOCAL_CTL
                                                                1 -> CommonEnum.BottomPop.COM_MODE
                                                                2 -> CommonEnum.BottomPop.RS485
                                                                3 -> CommonEnum.BottomPop.PARITY
                                                                4 -> CommonEnum.BottomPop.BAUD_RATE
                                                                5 -> CommonEnum.BottomPop.SELF_TEST_MODE
                                                                else -> {
                                                                    CommonEnum.BottomPop.DEFAULT
                                                                }
                                                            }
                                                            if (bottomType != CommonEnum.BottomPop.LOCAL_CTL) {
                                                                bottomShow.open()
                                                                clickEvent(bottomType, context)
                                                            }
                                                        }
                                                    }
                                            ) {
                                                Image(
                                                    painter,
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .padding(10.dp, 0.dp)
                                                        .size(16.dp)
                                                        .align(Alignment.CenterVertically),
                                                    colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onSurface)
                                                )
                                                Text(
                                                    text = item,
                                                    maxLines = 1,
                                                    fontSize = 14.sp,
                                                    color = MaterialTheme.colors.onSurface,
                                                    modifier = Modifier
                                                        .weight(10f)
                                                        .align(Alignment.CenterVertically)
                                                )
                                                Text(
                                                    text = value,
                                                    maxLines = 1,
                                                    fontSize = 14.sp,
                                                    color = MaterialTheme.colors.onSurface,
                                                    modifier = Modifier
                                                        .weight(2f)
                                                        .align(Alignment.CenterVertically)
                                                )
                                                Icon(
                                                    Icons.Filled.KeyboardArrowRight,
                                                    contentDescription = "",
                                                    modifier = Modifier.align(Alignment.CenterVertically)
                                                )
                                            }
                                            Divider(
                                                modifier = Modifier.padding(16.dp, 0.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // 读写切换
                        ReadWriteAlert(openDialog, mode!!.value)
                        // NFC支持提示
                        CheckNFcTip(isSupportNfc!!, isSupportNfcTip)
                        // 退出应用提示框
                        ExitDialog(exitDialog!!)
                    }
                )
            }
        )

    }


    // 读写模式切换弹框
    @Composable
    fun ReadWriteAlert(openDialog: MutableState<Boolean>, mode: String) {
        if (openDialog.value) {
            AlertDialog(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp),
                onDismissRequest = { },
                title = {
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(0.dp, 10.dp, 0.dp, 0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Filled.Info, contentDescription = null,
                            tint = MaterialTheme.colors.onSurface,
                            modifier = Modifier.size(44.dp)
                        )
                        Text(
                            text = if (mode == "read") "读模式" else "写模式",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }, buttons = {}
            )

            // 延时2秒关闭
            val delayTask: TimerTask = object : TimerTask() {
                override fun run() {
                    openDialog.value = false
                }
            }
            val timer = Timer()
            timer.schedule(delayTask, 1500)
        }
    }

    /**
     * 点击事件
     */
    private fun clickEvent(type: CommonEnum.BottomPop, context: Context) {
        when (type) {
            CommonEnum.BottomPop.DEFAULT -> {

            }
            CommonEnum.BottomPop.COM_MODE -> {

            }
            CommonEnum.BottomPop.RS485 -> {

            }
            CommonEnum.BottomPop.PARITY -> {

            }
            CommonEnum.BottomPop.BAUD_RATE -> {

            }
            CommonEnum.BottomPop.SELF_TEST_MODE -> {

            }
        }
    }

    @Composable
    private fun Top(width: Dp) {
        var spanList: MutableList<String> = mutableListOf(
            stringResource(id = R.string.ctr_open),
            stringResource(id = R.string.feed_open),
            stringResource(id = R.string.supply_temp),
            stringResource(id = R.string.return_temp),
            stringResource(id = R.string.supply_pressure),
            stringResource(id = R.string.return_pressure),
            stringResource(id = R.string.software_version),
        )
        var iconList: MutableList<Painter> = mutableListOf(
            painterResource(id = R.drawable.ic_open),
            painterResource(id = R.drawable.ic_open),
            painterResource(id = R.drawable.ic_temp),
            painterResource(id = R.drawable.ic_temp),
            painterResource(id = R.drawable.ic_pressure),
            painterResource(id = R.drawable.ic_pressure),
            painterResource(id = R.drawable.ic_version),
        )
        var valueList by remember {
            mutableStateOf(
                mutableListOf(
                    "100%",
                    "100%",
                    "100℃",
                    "100℃",
                    "100Kpa",
                    "100Kpa",
                    "1024",
                )
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(
                modifier = Modifier
                    .height(2.dp)
                    .width(width)
            )
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable(enabled = true) { }
                    .width(width - Dp(8f))
                    .height(300.dp)
                    .background(
                        color = MaterialTheme.colors.background,
                        shape = RoundedCornerShape(4.dp)
                    )
            ) {
                Image(
                    modifier = Modifier
                        .width(width = width / 3)
                        .height(300.dp)
                        .padding(0.dp, 0.dp),
                    painter = painterResource(id = R.drawable.u11),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    for ((index, item) in spanList.withIndex()) {
                        TopListItem(
                            iconList[index],
                            item,
                            valueList[index],
                            width - Dp(8f) - (width / 3)
                        )
                        Divider(
                            modifier = Modifier.padding(16.dp, 0.dp)
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun TopListItem(painter: Painter, label: String, value: String, width: Dp) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(0.dp, 6.dp)
                .height(30.dp)
                .width(width)
        ) {
            Image(
                painter,
                contentDescription = null,
                modifier = Modifier
                    .padding(10.dp, 0.dp)
                    .align(Alignment.CenterVertically)
                    .size(16.dp),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onSurface)
            )
            Text(
                text = label,
                maxLines = 1,
                fontSize = 14.sp,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .weight(4f)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = value,
                maxLines = 1,
                fontSize = 14.sp,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .weight(2f)
                    .align(Alignment.CenterVertically)
            )
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun BottomListItem(
        painter: Painter,
        label: String,
        value: String,
        type: CommonEnum.BottomPop,
        show: BottomDrawerState
    ) {

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

    /**
     * 是否支持NFC
     */
    private fun checkNFC(): Boolean {
        if (nfcAdapter != null) {
            return !nfcAdapter!!.isEnabled
        } else {
            return false
        }
    }

    private fun checkNFCTip(): String {
        if (nfcAdapter != null) {
            if (!nfcAdapter!!.isEnabled) {
                return nfcOffTip
            }
        } else {
            return notSupportedNFCTip
        }
        return "";
    }
}





