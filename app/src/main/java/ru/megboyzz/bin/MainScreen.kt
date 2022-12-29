package ru.megboyzz.bin

import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.substring
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import ru.megboyzz.bin.entity.BINInfo
import ru.megboyzz.bin.entity.BINInfoNumber
import ru.megboyzz.bin.ui.theme.main

@Composable
fun MainScreen(){

    Column {
        Spacer(Modifier.height(25.dp))
        val message = remember{mutableStateOf("")}
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column {
                OutlinedTextField(
                    modifier = Modifier.width(170.dp),
                    value = message.value,
                    onValueChange = {
                        if (it.length <= 8) message.value = it
                    },
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                        .merge(TextStyle(fontSize = 15.sp)),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(R.string.title_bin.AsString()) },
                    visualTransformation = MaskTransformation(),
                )
                Spacer(Modifier.height(17.dp))
                Button(
                    onClick = { /*TODO*/ },
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
            Log.i("MainScreen", "originalToTransformed = $offset")
            if (offset <= 4) return offset
            if (offset <= 8) return offset +1
            return 9
        }

        override fun transformedToOriginal(offset: Int): Int {
            Log.i("MainScreen", "transformedToOriginal = $offset")
            if (offset <= 5) return offset
            if (offset <= 9) return offset -1
            return 8
        }
    }

    return TransformedText(AnnotatedString(out), numberOffsetTranslator)
}


@Composable
fun yesNoLabel(isYes: Boolean){
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

@Composable
fun TitledPosition(
    title: String,
    content: @Composable (() -> Unit)
){
    val style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Thin)
    Column {
        Text(text = title, style = style)
        Spacer(modifier = Modifier.height(14.dp))
        content()
    }

}


@Composable
fun BinCard(binInfo: BINInfoNumber){
    var context = LocalContext.current;
    Card(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, main),
        modifier = Modifier.padding(25.dp,14.dp,25.dp, 7.dp)
    ){
        Box {
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(10.dp, 20.dp, 10.dp, 20.dp).fillMaxWidth()
                ) {
                    Image(
                        painter = R.drawable.remove.AsPainter(),
                        contentDescription = "remove",
                        modifier = Modifier.clickable {
                            Toast.makeText(context, "remove", Toast.LENGTH_LONG).show();
                        }
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${binInfo.info.bank.name}, ${binInfo.info.bank.city}")
                        val number = binInfo.number.toString()
                        Text(text = "${number.substring(0,4)} ${number.substring(4,8)}")
                    }
                    Image(
                        painter = R.drawable.arrow_down.AsPainter(),
                        contentDescription = "arrow_down",
                        modifier = Modifier.clickable {
                            Toast.makeText(context, "expand", Toast.LENGTH_LONG).show();
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun cardPrev() {
    val data = "{\"number\":{\"length\":16,\"luhn\":true},\"scheme\":\"visa\",\"type\":\"debit\",\"brand\":\"Visa/Dankort\",\"prepaid\":false,\"country\":{\"numeric\":\"208\",\"alpha2\":\"DK\",\"name\":\"Denmark\",\"emoji\":\"\uD83C\uDDE9\uD83C\uDDF0\",\"currency\":\"DKK\",\"latitude\":56,\"longitude\":10},\"bank\":{\"name\":\"Jyske Bank\",\"url\":\"www.jyskebank.dk\",\"phone\":\"+4589893300\",\"city\":\"Hjørring\"}}"
    val fromJson = Gson().fromJson(data, BINInfo::class.java)
    val info = BINInfoNumber(1, 45717360, fromJson)
    BinCard(binInfo = info)
}




@Preview
@Composable
fun TitleWithContent() {
    Column() {
        TitledPosition(title = "SCHEME/NETWORK") {
            Text(text = "VISA")
        }
        Spacer(modifier = Modifier.height(14.dp))
        TitledPosition(title = "TYPE") {
            LeftRightTextLabel(left = "Debit", right = "Credit", isLeft = true)
        }
        Spacer(modifier = Modifier.height(14.dp))
        TitledPosition(title = "CARD NUMBER") {
            Row{
                TitledPosition(title = "LENGTH") {
                    Text(text = "16")
                }
                Spacer(Modifier.width(20.dp))
                TitledPosition(title = "LUHN") {
                    yesNoLabel(isYes = true)
                }
            }
        }
    }
}

@Preview
@Composable
fun prevYesNo() {
    Column() {
        yesNoLabel(isYes = true)
        LeftRightTextLabel(left = "Debit", right = "Credit", isLeft = true)
    }
}

