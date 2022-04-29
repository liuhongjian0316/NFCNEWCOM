package work.aijiu.nfcactuator.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import work.aijiu.nfcactuator.R

/**
 * 产品
 */
object ProductContent {

}


@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Product() {
    U1Product()
}


@Composable
fun U1Product() {
    BoxWithConstraints() {
        val width = maxWidth
        LazyColumn(){
            item{
                Top(width)
            }
            item {
                Bottom()
            }
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
    var valueList by remember { mutableStateOf(
        mutableListOf(
            "100%",
            "100%",
            "100℃",
            "100℃",
            "100Kpa",
            "100Kpa",
            "1024",
        )
    ) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Spacer(modifier = Modifier
            .height(2.dp)
            .width(width))
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment =  Alignment.CenterVertically,
            modifier = Modifier
                .clickable(enabled = true) { }
                .width(width - Dp(8f))
                .height(300.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.6f),
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
                for ((index, item) in spanList.withIndex()){
                    TopListItem(iconList[index],item,valueList[index],width - Dp(8f)-(width/3))
                }
            }
        }
    }
}
@Composable
private fun TopListItem(painter: Painter, label:String, value:String,width:Dp) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment =  Alignment.CenterVertically,
        modifier = Modifier
            .padding(6.dp)
            .height(30.dp)
            .width(width)
    ) {
        Image(painter, contentDescription = null,modifier = Modifier.padding(10.dp,0.dp).align(Alignment.CenterVertically).size(16.dp),colorFilter = ColorFilter.tint(color=MaterialTheme.colors.onSurface))
        Text(text=label,maxLines = 1,fontSize = 14.sp,color = MaterialTheme.colors.onSurface,modifier = Modifier.weight(4f).align(Alignment.CenterVertically))
        Text(text=value,maxLines = 1,fontSize = 14.sp,color = MaterialTheme.colors.onSurface,modifier = Modifier.weight(2f).align(Alignment.CenterVertically))
    }
}
@Composable
private fun Bottom(){
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
    var valueList by remember { mutableStateOf(
        mutableListOf(
            "ENTER",
            "RS485",
            "1",
            "无校验",
            "19200",
            "上电自检",
        )
    ) }
    Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        for ((index, item) in spanList.withIndex()){
            BottomListItem(iconList[index],item,valueList[index])
        }

    }
}
@Composable
private fun BottomListItem(painter: Painter, label:String, value:String){
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment =  Alignment.CenterVertically,
        modifier = Modifier
            .padding(6.dp)
            .height(30.dp)
            .fillMaxWidth()
    ) {
        Image(painter, contentDescription = null,modifier = Modifier.padding(10.dp,0.dp).size(16.dp).align(Alignment.CenterVertically),colorFilter = ColorFilter.tint(color=MaterialTheme.colors.onSurface))
        Text(text=label,maxLines = 1,fontSize = 14.sp,color = MaterialTheme.colors.onSurface,modifier = Modifier.weight(10f).align(Alignment.CenterVertically))
        Text(text=value,maxLines = 1,fontSize = 14.sp,color = MaterialTheme.colors.onSurface,modifier = Modifier.weight(2f).align(Alignment.CenterVertically))
        Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "",modifier = Modifier.align(Alignment.CenterVertically))
    }
}




