package work.aijiu.nfcactuator.components.C2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import work.aijiu.nfcactuator.R
import work.aijiu.nfcactuator.activities.AutoActivity
import work.aijiu.nfcactuator.components.ReadWriteAlert
import work.aijiu.nfcactuator.enum.CommonEnum
import work.aijiu.nfcactuator.ui.theme.Blue200
import work.aijiu.nfcactuator.utils.StatusBarUtils

class C2Product {
}



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun C2Product(series:Byte) {
    Log.e("series",series.toString())
    val sysUiController = rememberSystemUiController()
    SideEffect {
        sysUiController.setStatusBarColor(
            color = Blue200
        )
    }
    // 读写模式
    var context = LocalContext.current
    var mode by remember { mutableStateOf("read") }
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
                                    .background(if (mode == "read") Color.Blue else Color.Red)
                                    .clickable {
                                        mode = if (mode == "read") "write" else "read"
                                        openDialog.value = true
                                    },
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (mode == "read") stringResource(id = R.string.read) else stringResource(
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

                    RadioButton(selected = true, onClick = {
                        // TODO
                    })
                    ReadWriteAlert(openDialog, mode)
                }
            )
        }
    )
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
            painter, contentDescription = null, modifier = Modifier
                .padding(10.dp, 0.dp)
                .align(Alignment.CenterVertically)
                .size(16.dp), colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onSurface)
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

