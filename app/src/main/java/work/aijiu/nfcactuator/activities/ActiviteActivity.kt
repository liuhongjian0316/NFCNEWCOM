package work.aijiu.nfcactuator.activities

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import work.aijiu.nfcactuator.ui.theme.NfcActuatorTheme
import work.aijiu.nfcactuator.utils.StatusBarUtils
import work.aijiu.nfcactuator.R
import work.aijiu.nfcactuator.ui.theme.Activite_tip_Color
import work.aijiu.nfcactuator.ui.theme.Blue200

/**
 *
 * @ProjectName:    nfcActuator
 * @Package:        work.aijiu.nfcactuator.activities
 * @ClassName:      ActiviteActivity
 * @Description:    java类作用描述
 * @Author:         aijiu
 * @CreateDate:     2022/4/27 15:21
 * @UpdateUser:     aijiu
 * @UpdateDate:     2022/4/27 15:21
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class ActiviteActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setTextDark(this, true);
        setContent {
            NfcActuatorTheme{
                MainContent(this)
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainContent(context: Context) {
    var phone by remember { mutableStateOf("") }
    var cdkey by remember { mutableStateOf("") }
    var isGetSmsFlag  by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "LOGO",
            modifier = Modifier
                .height(220.dp)
                .fillMaxWidth()
                .padding(0.dp, 100.dp, 0.dp, 10.dp),
            contentScale= ContentScale.Inside
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(20.dp, 0.dp, 20.dp, 0.dp),
            value = phone,
            onValueChange = { phone = it },
            textStyle = TextStyle(fontSize = 16.sp,),
            label = { Text(text= stringResource(id = R.string.phone),fontSize = 12.sp)},
        )
        Row(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .padding(0.dp, 0.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment =  Alignment.CenterVertically,
        ) {
            TextField(
                modifier = Modifier
                    .weight(3F)
                    .height(80.dp)
                    .padding(20.dp, 20.dp, 0.dp, 0.dp),
                value = cdkey,
                onValueChange = { cdkey = it },
                textStyle = TextStyle(fontSize = 16.sp),
                label = { Text(text= stringResource(id = R.string.cdkey),fontSize = 12.sp)},
            )
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                enabled = phone.length == 11,
                onClick = {
                    Toast.makeText(context,"已经获取短信验证码",Toast.LENGTH_LONG).show()
                    isGetSmsFlag = true
                          },
                modifier = Modifier
                    .weight(1F)
                    .height(80.dp)
                    .padding(10.dp, 30.dp, 10.dp, 10.dp),
                content = {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment =  Alignment.CenterVertically,
                    ) {
                        Text(text = stringResource(id = R.string.get_sms_code),fontSize=12.sp, color = Color.White)
                    }
                }
            )
        }

        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
            enabled = phone.length == 11 && isGetSmsFlag,
            onClick = { Toast.makeText(context,"执行函数",Toast.LENGTH_LONG).show()},
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(20.dp, 40.dp, 20.dp, 0.dp),
            content = {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment =  Alignment.CenterVertically,
                ) {
                    Text(text = stringResource(id = R.string.activate_now),fontSize=16.sp, color = Color.White)
                }
            }
        )
        Row(modifier = Modifier.height(30.dp).padding(0.dp,0.dp)) {
        }
        Text(
            text = stringResource(id = R.string.activate_help),
            color = Activite_tip_Color,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clickable {
                    //点击回调
                    Toast
                        .makeText(context, "执行函数", Toast.LENGTH_LONG)
                        .show()
                }
                .padding(0.dp, 0.dp)
                .height(20.dp),
        )

    }
}

