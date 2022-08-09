package work.aijiu.nfcactuator.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import work.aijiu.nfcactuator.R
import work.aijiu.nfcactuator.activities.product.c2.ProductC2Activity
import work.aijiu.nfcactuator.enum.CommonEnum
import work.aijiu.nfcactuator.ui.theme.NfcActuatorTheme
import work.aijiu.nfcactuator.utils.StatusBarUtils

/**
 *
 * @ProjectName:    nfcActuator
 * @Package:        work.aijiu.nfcactuator.activities
 * @ClassName:      AutoActivity
 * @Description:    手动选择产品界面
 * @Author:         aijiu
 * @CreateDate:     2022/4/28 11:29
 * @UpdateUser:     aijiu
 * @UpdateDate:     2022/4/28 11:29
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class ChoiceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setTextDark(this, true);
        setContent {
            NfcActuatorTheme {
                ChoiceContent()
            }
        }
    }
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ChoiceContent() {
    val lazyListState = rememberLazyListState()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            UserCenter()
        },
    ) {
        BoxWithConstraints {
            val width = maxWidth;
            val alpha = when {
                lazyListState.ScrollTop() in 0..0 -> {
                    1.0f
                }
                lazyListState.ScrollTop() in 1..10 -> {
                    1.0f
                }
                lazyListState.ScrollTop() in 10..20 -> {
                    0.9f
                }
                lazyListState.ScrollTop() in 20..30 -> {
                    0.8f
                }
                lazyListState.ScrollTop() in 30..40 -> {
                    0.7f
                }
                lazyListState.ScrollTop() in 40..50 -> {
                    0.6f
                }
                lazyListState.ScrollTop() in 50..60 -> {
                    0.5f
                }
                lazyListState.ScrollTop() in 60..70 -> {
                    0.4f
                }
                lazyListState.ScrollTop() in 70..80 -> {
                    0.3f
                }
                lazyListState.ScrollTop() in 80..90 -> {
                    0.2f
                }
                else -> {
                    0.1f
                }
            }
            val context = LocalContext.current
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colors.background
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Row(
                        modifier = Modifier
                            .height(60.dp)
                            .width(60.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = {
                            context.startActivity(Intent(context, AutoActivity::class.java))
                        }) {
                            Icon(
                                Icons.Filled.ArrowBack, contentDescription = null,
                                tint = MaterialTheme.colors.onSurface,
                            )
                        }
                    }
                    if (!lazyListState.isScrollingUp()) {
                        Text(
                            modifier = Modifier
                                .width(width - Dp(120f))
                                .padding(0.dp, 0.dp, 0.dp, 0.dp),
                            textAlign = TextAlign.Center,
                            text = stringResource(id = R.string.choice_product),
                            color = MaterialTheme.colors.onSurface,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Spacer(
                            modifier = Modifier
                                .height(60.dp)
                                .width(width - Dp(120f))
                        )
                    }
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .height(36.dp)
                            .width(36.dp)
                            .background(Color(0xFFC5C5C5))
                            .clickable {
                                scope.launch {
                                    scaffoldState.drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Person, contentDescription = null,
                            tint = MaterialTheme.colors.onSurface,
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp),
                    state = lazyListState
                ) {
                    item {
                        ProductChoiceText(alpha)
                    }
                    item {
                        ProductTypeText(R.string.unit_valve)
                    }
                    item {
                        ProductCard(CommonEnum.Product.U)
                    }
                    item {
                        ProductTypeText(R.string.rom_valve)
                    }
                    item {
                        ProductCard(CommonEnum.Product.R)
                    }
                    item {
                        ProductTypeText(R.string.zxc)
                    }
                    item {
                        ProductCard(CommonEnum.Product.ZXC)
                    }
                    item {
                        ProductTypeText(R.string.hrz_valve)
                    }
                    item {
                        ProductCard(CommonEnum.Product.E)
                    }
                }
            }
        }
    }

}


@Composable
private fun UserCenter() {
    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.lightgoldenrodyellow))
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .height(60.dp)
                    .width(60.dp)
                    .background(Color(0xFFC5C5C5))
                    .padding(10.dp)
                    .clickable {

                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Person, contentDescription = null,
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
            }
            Spacer(
                modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth()
            )
            Text(text = "管理员", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        }
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(colorResource(id = R.color.bisque))
        )
        Text(
            modifier = Modifier.padding(10.dp, 10.dp, 0.dp, 0.dp),
            text = "基本信息", fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(color = colorResource(id = R.color.gray))
        )
        Spacer(
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth()
        )
        UserCenterItem("用户名称", "管理员")
        UserCenterItem("手机号", "131*****96")
        UserCenterItem("APPID", "****")
        UserCenterItem("到期时间", "2001-07-08 12:12:12")

        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(colorResource(id = R.color.bisque))
        )
        Text(
            modifier = Modifier.padding(10.dp, 10.dp, 0.dp, 0.dp),
            text = "基本权限", fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(color = colorResource(id = R.color.gray))
        )
        Spacer(
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth()
        )
        UserCenterItem("U1", "高级功能")
        UserCenterItem("U2", "高级功能")
        UserCenterItem("R1", "高级功能")
        UserCenterItem("R2", "高级功能")
        UserCenterItem("E1", "高级功能")
        UserCenterItem("E2", "高级功能")
        UserCenterItem("Q", "高级功能")


        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(colorResource(id = R.color.bisque))
        )
        Text(
            modifier = Modifier.padding(10.dp, 10.dp, 0.dp, 0.dp),
            text = "高级操作", fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(color = colorResource(id = R.color.gray))
        )
        Spacer(
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth()
        )
        Row(modifier = Modifier) {
            Spacer(
                modifier = Modifier
                    .width(10.dp)
            )
            Button(
                modifier = Modifier,
                onClick = {

                }
            ) {
                Text(
                    text = "刷新开启模块", fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(color = colorResource(id = R.color.white)),
                )
            }
            Spacer(
                modifier = Modifier
                    .width(10.dp)
            )
            Button(
                modifier = Modifier,
                onClick = {

                }
            ) {
                Text(
                    text = "重置激活状态", fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(color = colorResource(id = R.color.white)),
                )
            }
        }
    }
}


@Composable
private fun UserCenterItem(label: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(0.dp, 6.dp)
            .height(24.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            maxLines = 1,
            fontSize = 16.sp,
            color = MaterialTheme.colors.onSurface,
            style = TextStyle(textAlign = TextAlign.Start),
            modifier = Modifier
                .weight(2f)
                .align(Alignment.CenterVertically)
                .padding(10.dp, 0.dp, 0.dp, 0.dp)
        )
        Text(
            text = value,
            maxLines = 1,
            fontSize = 16.sp,
            color = MaterialTheme.colors.onSurface,
            fontWeight = FontWeight.Bold,
            style = TextStyle(textAlign = TextAlign.Right),
            modifier = Modifier
                .weight(4f)
                .padding(0.dp, 0.dp, 10.dp, 0.dp)
        )
    }
}

@Composable
private fun ProductChoiceText(alpha: Float) {
    Text(
        stringResource(id = R.string.choice_product),
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 12.dp, 12.dp, 0.dp)
            .alpha(alpha),
        fontSize = 22.sp,
        color = MaterialTheme.colors.onSurface,
    )
}

@Composable
private fun ProductTypeText(stringId: Int) {
    Text(
        stringResource(id = stringId),
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 12.dp, 12.dp, 12.dp),
        fontSize = 14.sp,
        color = Color.Gray
    )
}

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@Composable
private fun LazyListState.ScrollTop(): Int {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    val value = remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
    return previousScrollOffset
}

@Composable
private fun ProductCard(type: CommonEnum.Product) {
    BoxWithConstraints {
        val width = maxWidth
        if (maxWidth < 400.dp) {
            val spaceWidth = Dp(15f)
            val itemWidth = (width - spaceWidth) / 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (type == CommonEnum.Product.U) {
                    ProductCardItem(itemWidth, R.string.u1, R.drawable.u1, "U1")
                    Spacer(modifier = Modifier.width(5.dp))
                    ProductCardItem(itemWidth, R.string.u2, R.drawable.u2, "U2")
                }
                if (type == CommonEnum.Product.R) {
                    ProductCardItem(itemWidth, R.string.r1, R.drawable.r1, "R1")
                    Spacer(modifier = Modifier.width(5.dp))
                    ProductCardItem(itemWidth, R.string.r2, R.drawable.r2, "R2")
                }
                if (type == CommonEnum.Product.ZXC) {
                    ProductCardItem(itemWidth, R.string.zxc, R.drawable.dsetv, "Z")
                    Spacer(modifier = Modifier.width(5.dp))
                    ProductCardItem(itemWidth, R.string.qf, R.drawable.ball, "!")
                }
                if (type == CommonEnum.Product.E) {
                    ProductCardItem(itemWidth, R.string.e1, R.drawable.e1, "E1")
                    Spacer(modifier = Modifier.width(5.dp))
                    ProductCardExpect(itemWidth)
                }
            }
        } else {
//            val spaceWidth = Dp(20f)
//            val itemWidth = (width-spaceWidth)/3
//            Row(modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment =  Alignment.CenterVertically
//            ) {
//                ProductCardItem(itemWidth,R.string.u1,R.drawable.u1)
//                Spacer(modifier = Modifier.width(5.dp))
//                ProductCardItem(itemWidth,R.string.u2,R.drawable.u2)
//                Spacer(modifier = Modifier.width(5.dp))
//            }
        }
    }
}

@Composable
private fun ProductCardExpect(width: Dp) {
    Button(
        modifier = Modifier
            .width(width)
            .height(width),
        colors = buttonColors(
            backgroundColor = Color.Black.copy(0.6F),
            contentColor = Color.Black.copy(0.6F)
        ),
        onClick = { /* ... */ },
        contentPadding = PaddingValues(
            start = 0.dp,
            top = 0.dp,
            end = 0.dp,
            bottom = 0.dp
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.expect),
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 0.dp)
                    .width(width)
                    .background(MaterialTheme.colors.primary),
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Image(
                painter = painterResource(id = R.drawable.expect),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(width)
                    .padding(70.dp, 60.dp),
                contentScale = ContentScale.FillBounds,
            )
        }
    }
}

@Composable
private fun ProductCardItem(width: Dp, stringId: Int, drawId: Int, product: String) {
    val context = LocalContext.current
    Button(
        modifier = Modifier
            .width(width)
            .height(width),
        onClick = { enterProductEvent(context, product) },
        contentPadding = PaddingValues(
            start = 0.dp,
            top = 0.dp,
            end = 0.dp,
            bottom = 0.dp
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = stringId),
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp),
                color = Color.White
            )
            Image(
                painter = painterResource(id = drawId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
        }
    }
}

private fun enterProductEvent(context: Context, product: String) {
    val intent = Intent(context, ProductC2Activity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    intent.putExtra("product", product)
    context?.startActivity(intent)
}