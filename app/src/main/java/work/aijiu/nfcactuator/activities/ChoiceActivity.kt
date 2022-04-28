package work.aijiu.nfcactuator.activities

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import work.aijiu.nfcactuator.R
import work.aijiu.nfcactuator.ui.theme.NfcActuatorTheme
import work.aijiu.nfcactuator.utils.StatusBarUtils

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
class ChoiceActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setTextDark(this, true);
        setContent {
            NfcActuatorTheme{
                ChoiceContent()
            }
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ChoiceContent() {
    val lazyListState = rememberLazyListState()

    Row(modifier = Modifier
        .height(60.dp)
        .fillMaxWidth()
        .padding(0.dp)
        .background(
            color = MaterialTheme.colors.background
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Row(modifier = Modifier
            .height(60.dp)
            .width(60.dp)
            .padding(18.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment =  Alignment.CenterVertically){
            Icon(
                Icons.Filled.Build, contentDescription = null,
                tint = MaterialTheme.colors.onSurface,
                modifier=Modifier.size(18.dp)
            )
        }
        if (!lazyListState.isScrollingUp()){
            Text(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 60.dp, 0.dp),textAlign=TextAlign.Center,
                text = "手动选择产品",color = MaterialTheme.colors.onSurface,fontSize = 16.sp,)
        }
    }
    LazyColumn (
        modifier = Modifier.padding(0.dp,60.dp,0.dp,0.dp),
        state = lazyListState){
        item{
            Text(
                text = "手动选择产品",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp, 12.dp, 12.dp, 0.dp),
                fontSize = 22.sp,
                color = MaterialTheme.colors.onSurface
            )
        }
        item{
            Text(
                text = "单元阀",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp, 6.dp, 12.dp, 0.dp),
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        // Add a single item
        item {
            Row(modifier = Modifier,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment =  Alignment.CenterVertically) {
                Card()
                Card()
            }
        }
        // Add 5 items
        items(5) { index ->
            Text(text = "Item: $index",modifier = Modifier
                .height(300.dp)
                .fillMaxWidth())
        }
        // Add another single item
        item {
            Text(text = "Last item",modifier = Modifier
                .height(300.dp)
                .fillMaxWidth())
        }
    }
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
private fun ProductItem() {
    Row(
        modifier = Modifier
            .height(60.dp)
            .width(60.dp)
            .padding(18.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment =  Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = R.drawable.nfc_actuator), contentDescription = null)
        Text("U1")
    }
}

@Composable
private fun Card() {
    BoxWithConstraints {
        if (maxWidth < 400.dp) {
            Column {
                Image(painter = painterResource(id = R.drawable.nfc_actuator), contentDescription = null)
                Text("U1")
            }
        } else {
            Row {
                Column {
                    Text("U1")
                    Text("U1")
                }
                Image(painter = painterResource(id = R.drawable.nfc_actuator), contentDescription = null)
            }
        }
    }
}

