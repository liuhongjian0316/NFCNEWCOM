package work.aijiu.nfcactuator.activities

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import work.aijiu.nfcactuator.R
import work.aijiu.nfcactuator.components.Product
import work.aijiu.nfcactuator.ui.theme.NfcActuatorTheme
import work.aijiu.nfcactuator.utils.StatusBarUtils
import java.util.*

/**
 *
 * @ProjectName:    nfcActuator
 * @Package:        work.aijiu.nfcactuator.activities
 * @ClassName:      ActiviteActivity
 * @Description:    产品
 * @Author:         aijiu
 * @CreateDate:     2022/4/27 15:21
 * @UpdateUser:     aijiu
 * @UpdateDate:     2022/4/27 15:21
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class ProductActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setTextDark(this, true);
        setContent {
            NfcActuatorTheme{
                ProductMain()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProductMain() {
    var mode by remember { mutableStateOf("read") }
    var openDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    var drawerState = rememberDrawerState(DrawerValue.Closed)

    Scaffold(
        topBar = {
            TopAppBar { 
                Text(text = "标题栏")
            }
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .padding(0.dp)
                    .background(MaterialTheme.colors.background),
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment =  Alignment.CenterVertically,
                ) {
                    Row(modifier = Modifier
                        .clip(CircleShape)
                        .size(60.dp)
                        .background(if (mode == "read") Color.Blue else Color.Red)
                        .clickable {
//                            mode = if (mode == "read") "write" else "read"
//                            openDialog.value = true
                            drawerState = DrawerState(DrawerValue.Open)

                        },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment =  Alignment.CenterVertically){
                        Text(
                            text = if (mode=="read") stringResource(id = R.string.read) else stringResource(id = R.string.write),
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) {
        Product()
        addAlterDialog(openDialog,mode)
        ScrollBoxes(drawerState)
    }
}





@Composable
fun addAlterDialog(openDialog: MutableState<Boolean>,mode:String) {
    if (openDialog.value) {
        AlertDialog(
            modifier=Modifier.width(100.dp).height(100.dp),
            onDismissRequest = {  },
            title = {
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxSize().padding(0.dp,10.dp,0.dp,0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Filled.Info, contentDescription = null,
                        tint = MaterialTheme.colors.onSurface,
                        modifier=Modifier.size(44.dp)
                    )
                    Text(
                        text = if(mode == "read") "读模式" else "写模式",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

            }, buttons={}
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScrollBoxes(drawerState:DrawerState) {

    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    
    BottomSheetScaffold(
        sheetContent = {
            Column(
                modifier = Modifier
                    .background(Color.LightGray)
                    .size(100.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                repeat(10) {
                    Text("Item $it", modifier = Modifier.padding(2.dp))
                }
            }
        },
        // Defaults to BottomSheetScaffoldDefaults.SheetPeekHeight
        sheetPeekHeight = 128.dp,
        // Defaults to true
        sheetGesturesEnabled = false
    ) {
        // Screen content

    }

}
