package work.aijiu.nfcactuator.activities

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.tech.NfcA
import android.os.Bundle
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import work.aijiu.nfcactuator.R
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
class ResetActiviteActivity : ComponentActivity(){
    private val nfcA: NfcA? = null
    private val mAdapter: NfcAdapter? = null
    private val context_main: Context? = null
    private var mPendingIntent: PendingIntent? = null
    private val alertDialog: AlertDialog? = null
    private val intent_tag: Intent? = null
    private var mFilters: Array<IntentFilter> = TODO()
    private var mTechLists: Array<Array<String>> = TODO()
    private val mVibrator: Vibrator? = null
    private val exitTime: Long = 0 // 退出时间


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setTextDark(this, true);
        setContent {
            NfcActuatorTheme{
                ResetContent(left={
                    finish()
                })
            }
        }

        setNfcForeground()
    }


    fun setNfcForeground() {
        mPendingIntent = PendingIntent.getActivity(this, 0, Intent(applicationContext, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        mFilters = arrayOf(IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED))
        mTechLists = arrayOf(arrayOf(NfcA::class.java.name))
    }

}

@Composable
fun ResetContent(left: () -> Unit) {
    var context = LocalContext.current
    var phone by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf(false)}
    var cdkey by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        // 标题
        Row(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Row(modifier = Modifier
                .height(60.dp)
                .width(60.dp)
                .clickable(onClick = left),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment =  Alignment.CenterVertically){
                Icon(Icons.Filled.ArrowBack, contentDescription = null,
                    tint = MaterialTheme.colors.onSurface
                )
            }
            Text(text= stringResource(id = R.string.reset_code), modifier = Modifier.padding(14.dp,0.dp,0.dp,0.dp), fontWeight = FontWeight.Bold,color = MaterialTheme.colors.onSurface, fontSize = 20.sp)
        }
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
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(20.dp, 20.dp, 20.dp, 0.dp),
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
            enabled = phone.length == 11 && (Pattern.matches("^1[3|4|5|6|7|8|9][0-9]{9}$", phone)),
            onClick = {
                // 为了保险 提交之前再次校验
                if(phone.length == 11 && (Pattern.matches("^1[3|4|5|6|7|8|9][0-9]{9}$", phone))){

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
                    Text(text = stringResource(id = R.string.reset_code),fontSize=16.sp, color = Color.White)
                }
            }
        )
    }
}
