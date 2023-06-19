package am.solidy.flowsummator

import am.solidy.flowsummator.ui.theme.FlowSimulatorTheme
import am.solidy.flowsummator.ui.theme.Typography
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlowSimulatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
) {

    val inputText = remember { viewModel.summatorInputValue }.value
    val summatorResult = viewModel.summatorResult.collectAsState()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(top = 64.dp),
            text = "Flow summator",
            style = Typography.titleLarge
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            TextField(
                value = inputText,
                label = { Text(text = "Write count of flows") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { it ->
                    viewModel.setInputValue(it)
                }
            )
            Button(
                modifier = Modifier.padding(top = 8.dp),
                onClick = {
                    viewModel.startSummator()
                },
                enabled = inputText.text.isNotBlank()
            ) {
                Text(text = "Start Summator")
            }

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = summatorResult.value.joinToString(" "),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlowSimulatorTheme {
        MainScreen(
            viewModel = viewModel()
        )
    }
}