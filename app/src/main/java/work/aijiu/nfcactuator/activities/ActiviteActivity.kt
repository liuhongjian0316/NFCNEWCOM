package work.aijiu.nfcactuator.activities

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import work.aijiu.nfcactuator.R
import work.aijiu.nfcactuator.ui.theme.Activite_tip_Color
import work.aijiu.nfcactuator.ui.theme.NfcActuatorTheme
import work.aijiu.nfcactuator.utils.StatusBarUtils
import java.util.regex.Pattern

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
                MainContent()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainContent() {
    var context = LocalContext.current
    var phone by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf(false)}
    var cdkey by remember { mutableStateOf("") }
    var isGetSmsFlag by remember { mutableStateOf(false) }
    var getSmsCodeBtnDisable by remember { mutableStateOf(true) }
    val smsTextConstant = stringResource(id = R.string.get_sms_code)
    var countDownText by remember { mutableStateOf(smsTextConstant)}

    // 倒计时
    var countDownHandler = Handler()
    var countDownTime:Long = 10//自己设定的时间
    val countDown = object : Runnable {
        override fun run() {
            if (countDownTime >= 0) {
                countDownHandler.postDelayed(this, 1000)//每秒
                //执行逻辑
                countDownText = "$countDownTime S"
                getSmsCodeBtnDisable = true
            } else {
                //停止的逻辑
                getSmsCodeBtnDisable = false
                countDownText = smsTextConstant
                countDownTime = 10
            }
            countDownTime--
        }
    }

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
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(20.dp, 0.dp, 20.dp, 0.dp),
            value = phone,
            onValueChange = {
                phone = it
                var flag = (!(phone.length == 11 && (Pattern.matches("^1[3|4|5|6|7|8|9][0-9]{9}$", phone))))
                phoneError = flag
                getSmsCodeBtnDisable =flag
                            },
            maxLines = 1,
            singleLine = true,
            isError = phoneError,
            leadingIcon = { Icon(Icons.Filled.PhoneAndroid, contentDescription = "") },
            keyboardOptions= KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(fontSize = 16.sp,color = Color.Gray),
            label = { Text(text= stringResource(id = R.string.phone),fontSize = 12.sp)},
            colors = (TextFieldDefaults.outlinedTextFieldColors())
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
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Gray),
                label = { Text(text= stringResource(id = R.string.cdkey),fontSize = 12.sp)},
                maxLines = 1,
                singleLine = true,
                isError = false,
                leadingIcon = { Icon(Icons.Filled.Keyboard, contentDescription = "") },
                keyboardOptions= KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = (TextFieldDefaults.outlinedTextFieldColors()),
                visualTransformation = PasswordVisualTransformation(),
            )
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                enabled =  !getSmsCodeBtnDisable && (phone.length == 11 && (Pattern.matches("^1[3|4|5|6|7|8|9][0-9]{9}$", phone))),
                onClick = {
                    Toast.makeText(context,"已经获取短信验证码",Toast.LENGTH_LONG).show()
                    countDownHandler.postDelayed(countDown, 0) // 执行倒计
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
                        Text(text = countDownText,fontSize=12.sp, color = Color.White)
                    }
                }
            )
        }

        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
            enabled = phone.length == 11 && (Pattern.matches("^1[3|4|5|6|7|8|9][0-9]{9}$", phone)) && isGetSmsFlag,
            onClick = {
                // 为了保险 提交之前再次校验
                if(phone.length == 11 && (Pattern.matches("^1[3|4|5|6|7|8|9][0-9]{9}$", phone)) && isGetSmsFlag){

                }
            },
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
        Row(modifier = Modifier
            .height(30.dp)
            .padding(0.dp, 0.dp)) {
        }
        Text(
            text = stringResource(id = R.string.activate_help),
            color = Activite_tip_Color,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clickable {
                    val intent = Intent(context, ResetActiviteActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context?.startActivity(intent)
                }
                .padding(0.dp, 0.dp)
                .height(20.dp),
        )
    }
}
