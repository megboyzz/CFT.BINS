package ru.megboyzz.bin

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.megboyzz.bin.entity.BINInfo
import ru.megboyzz.bin.entity.BINInfoNumber
import ru.megboyzz.bin.ui.theme.main


@Composable
fun MainScreen(){

    val context = LocalContext.current

    val mViewModel: MainViewModel =
        viewModel(factory = MainViewModelFactory(context.applicationContext as App))

    val binInfoList by mViewModel.binInfoNumberList.observeAsState()

    val isInfoLoading by mViewModel.isLoadingInfo.observeAsState()

    val isError = remember { mutableStateOf(false) }

    val bin = remember{ mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(25.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    modifier = Modifier.width(180.dp),
                    value = bin.value,
                    onValueChange = {
                        if(isError.value) isError.value = false
                        if (it.length <= 8) bin.value = it
                    },
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(R.string.title_bin.AsString()) },
                    visualTransformation = MaskTransformation(),
                    trailingIcon = {
                        if(isError.value){
                            Image(R.drawable.error.AsPainter(), "error")
                        }
                        if(isInfoLoading!!){
                            CircularProgressIndicator(
                                modifier = Modifier.size(10.dp),
                                strokeWidth = 1.dp
                            )
                        }
                    },
                    isError = isError.value
                )
                Spacer(Modifier.height(17.dp))
                Button(
                    onClick = {
                                if(bin.value.isEmpty()) isError.value = true
                                else {
                                    isError.value = false
                                    mViewModel.addBinInfo(bin.value.toInt())
                                }
                    },
                    modifier = Modifier
                        .width(170.dp),
                    shape = RoundedCornerShape(4.dp),
                    //colors
                ) {
                    Text(
                        text = R.string.title_get_info.AsString()
                    )
                }
                Spacer(Modifier.height(27.dp))
                Text(
                    modifier = Modifier.width(170.dp),
                    text = R.string.title_last_bins.AsString(),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(10.dp))

                val scrollState = rememberScrollState()

                if(binInfoList!!.isEmpty()){
                    CircularProgressIndicator()
                }else {
                    Column(Modifier.verticalScroll(scrollState)) {
                        for (info in binInfoList!!)
                            BinCard(binInfo = info, viewModel = mViewModel)
                    }
                }

            }
        }

    }

}

class MaskTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return maskFilter(text)
    }
}


fun maskFilter(text: AnnotatedString): TransformedText {

    val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
    var out = ""
    for (i in trimmed.indices) {
        out += trimmed[i]
        if (i==3) out += " "
    }

    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 4) return offset
            if (offset <= 8) return offset +1
            return 9
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 5) return offset
            if (offset <= 9) return offset -1
            return 8
        }
    }

    return TransformedText(AnnotatedString(out), numberOffsetTranslator)
}



@Composable
fun YesNoLabel(isYes: Boolean){
    LeftRightTextLabel(left = "Yes", right = "No", isLeft = isYes)
}

@Composable
fun LeftRightTextLabel(left: String, right: String, isLeft: Boolean){

    val style = TextStyle(fontSize = 12.sp)

    var leftStyle = style.copy()
    var rightStyle = style.copy()
    if(!isLeft) leftStyle = leftStyle.merge(TextStyle(fontWeight = FontWeight.Thin))
    else rightStyle = rightStyle.merge(TextStyle(fontWeight = FontWeight.Thin))

    Row{
        StyledText(text = left, style = leftStyle)
        StyledText(text = " / ", style = style)
        StyledText(text = right, style = rightStyle)
    }
}

@Composable
fun StyledText(text: String, style: TextStyle){
    Text(
        text = text,
        style = style
    )
}

private val defaultLambda = {}

@Composable
fun TitledPosition(
    title: String,
    onPositionClick: () -> Unit = defaultLambda,
    content: @Composable (() -> Unit)
){
    val style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Thin)
    Column(
        Modifier
            //.height(100.dp)
            .clickable(
                enabled = onPositionClick != defaultLambda,
                onClick = onPositionClick
            )) {
        Text(text = title, style = style)
        Spacer(modifier = Modifier.height(14.dp))
        content()
    }

}

@Composable
fun LatitudeAndLongitude(latitude: Int, longitude:Int){
    val styleText = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Thin)
    val styleNumber = TextStyle(fontSize = 12.sp)
    Row{
        Text(text = R.string.title_latitude.AsString(), style = styleText)
        Text(text = latitude.toString(), style = styleNumber)
        Text(text = R.string.title_longitude.AsString(), style = styleText)
        Text(text = longitude.toString(), style = styleNumber)
        Text(text = ")", style = styleText)
    }
}

@Composable
fun BinCard(binInfo: BINInfoNumber, viewModel: MainViewModel){

    val context = LocalContext.current

    val expanded = remember { mutableStateOf(false) }
    val arrow = remember { mutableStateOf(R.drawable.arrow_down) }

    Card(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, main),
        modifier = Modifier.padding(25.dp,14.dp,25.dp, 7.dp)
    ){
        Column(verticalArrangement = Arrangement.Center) {
            if(expanded.value) arrow.value = R.drawable.arrow_up
            else arrow.value = R.drawable.arrow_down
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(10.dp, 20.dp, 10.dp, 20.dp)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = R.drawable.remove.AsPainter(),
                    contentDescription = "remove",
                    modifier = Modifier.clickable {
                        viewModel.deleteInfo(binInfo)
                    }
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    BankName(info = binInfo.info)
                    val number = binInfo.number.toString()
                    if(number.length == 8)
                        Text(text = "${number.substring(0,4)} ${number.substring(4,8)}")
                    else
                        Text(text = number)
                }
                Image(
                    painter = arrow.value.AsPainter(),
                    contentDescription = "arrow",
                    modifier = Modifier.clickable {
                        expanded.value = !expanded.value
                    }
                )
            }
            if(expanded.value){

                val info = binInfo.info

                Image(
                    painter = R.drawable.incard_line.AsPainter(),
                    contentDescription = "line",
                    alignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ){
                    Column(
                        verticalArrangement = Arrangement.spacedBy(30.dp),
                    ) {
                        val scheme = info.scheme ?: "?"
                        TitledPosition(title = R.string.title_scheme_network.AsString()) {
                            Text(text = scheme.uppercase())
                        }
                        var brand = info.brand ?: "?"
                        TitledPosition(title = R.string.title_brand.AsString()) {
                            if(brand.length > 12)
                                brand = brand.substring(0, 12) + "\n" + brand.substring(12, brand.length)
                            Text(
                                text = brand.uppercase(),
                            )
                        }
                        val number = info.number
                        val length = if(number.length != null){
                            if(number.length > 0) number.length.toString() else "?"
                        }else "?"
                        TitledPosition(title = R.string.title_card_number.AsString()) {
                            Row{
                                TitledPosition(title = R.string.title_card_length.AsString()) {
                                    Text(length)
                                }
                                Spacer(Modifier.width(20.dp))
                                TitledPosition(title = R.string.title_luhn.AsString()) {
                                    if(number.luhn != null)
                                        YesNoLabel(isYes = number.luhn)
                                    else
                                        nullTextLabel()
                                }
                            }
                        }
                        TitledPosition(
                            title = R.string.title_bank.AsString(),
                            onPositionClick = {
                                val bank = getBankName(info)
                                if(bank != "?") {
                                    val intent =
                                        Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$bank"))
                                    context.startActivity(intent)
                                }
                            }
                        ) {
                            BankName(info = info)
                            val uriHandler = LocalUriHandler.current
                            if(info.bank != null) {
                                val url = info.bank.url
                                if (url != null)
                                    ClickableText(
                                        text = getStyledAnnotatedString(url, url),
                                        onClick = {
                                            uriHandler.openUri("https://$url")
                                        }
                                    )
                                else nullTextLabel()
                            }else nullTextLabel()

                            if(info.bank != null) {
                                val phone = info.bank.phone
                                if(phone != null)
                                    ClickableText(text = getStyledAnnotatedString(phone, phone), onClick = {
                                        val intent = Intent(
                                            Intent.ACTION_DIAL,
                                            Uri.parse("tel:$phone")
                                        )
                                        context.startActivity(intent)
                                    })
                                else nullTextLabel()
                            }else nullTextLabel()
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(30.dp),
                    ) {
                        TitledPosition(title = R.string.title_type.AsString()) {
                            val type = info.type
                            if(type != null)
                            LeftRightTextLabel(
                                left = "Debit",
                                right = "Credit",
                                isLeft = type == "debit"
                            )
                            else nullTextLabel()
                                
                        }
                        TitledPosition(title = R.string.title_prepaid.AsString()) {
                            if(info.prepaid != null)
                                YesNoLabel(isYes = info.prepaid)
                            else nullTextLabel()
                        }
                        TitledPosition(
                            title = R.string.title_country.AsString(),
                            onPositionClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:${info.country.latitude},${info.country.longitude}"))
                                context.startActivity(intent)
                            }
                        ) {
                            val country = info.country
                            if(country != null) {

                                val emoji = country.emoji ?: "?"

                                Text(text = "$emoji ${info.country.name}")
                                Spacer(Modifier.height(10.dp))
                                LatitudeAndLongitude(
                                    latitude = info.country.latitude,
                                    longitude = info.country.longitude
                                )
                            }else nullTextLabel()
                        }

                        TitledPosition(title = R.string.title_currency.AsString()) {
                            if(info.country != null)
                                Text(info.country.currency)
                            else
                                nullTextLabel()
                        }

                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 0.dp, 20.dp, 20.dp),
                    horizontalArrangement = Arrangement.Start
                ){
                    TitledPosition(title = R.string.title_numeric.AsString()) {
                        if(info.country != null)
                            Text(info.country.numeric)
                        else
                            nullTextLabel()
                    }
                }

            }
        }
    }
}

fun getStyledAnnotatedString(text: String, url: String) =
    buildAnnotatedString {

        append(text)
        addStyle(
            style = SpanStyle(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            ), start = 0, end = text.length
        )
        addStringAnnotation(
            tag = "URL",
            annotation = url,
            start = 0,
            end = text.length
        )

    }


@Composable
fun nullTextLabel() = Text(text = "?")


@Composable
fun BankName(info: BINInfo){

    var bankName = getBankName(info)
    if(bankName == "?")
        nullTextLabel()
    else {
        if(bankName.length > 12) {
            var prc = bankName.substring(12, bankName.length)
            if(prc.length > 12){
                prc = prc.substring(0, 12) + "\n" + prc.substring(12, prc.length)
            }
            bankName = bankName.substring(0, 12) + "\n" + prc
        }
        Text(bankName)
    }
}


fun getBankName(info: BINInfo): String{
    if(info.bank == null) return "?"
    var name = info.bank.name
    if(name == null) name = "?"

    var city = info.bank.city
    if(city == null) city = "?"

    return if(city == "?" && name == "?")
        "?"
    else
        "$name, $city"
}