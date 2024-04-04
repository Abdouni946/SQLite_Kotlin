package com.example.db_manipulation

import DatabaseHelper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val databaseHelper = DatabaseHelper(this)
            DB01Screen(databaseHelper)
        }
    }
}

@Composable
fun DB01Screen(databaseHelper: DatabaseHelper) {
    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var person by remember {
        mutableStateOf(databaseHelper.getAllItems())
    }
    var selectedPersonIds by remember { mutableStateOf(setOf<Int>()) }
    var personnes by remember {
        mutableStateOf(databaseHelper.cursorToPersonnes(databaseHelper.getAllItems()))
    }

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Database Manipulation",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp))

        OutlinedTextField(
            value = id,
            onValueChange = { id = it },
            label = { Text("ID") }
        )
        Spacer(modifier = Modifier.size(6.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        Spacer(modifier = Modifier.size(16.dp))
        Row {
            Button(onClick = {
                coroutineScope.launch {
                    databaseHelper.addItem(id, name)
                    personnes = databaseHelper.cursorToPersonnes(databaseHelper.getAllItems())
                }
            }, colors = ButtonDefaults.buttonColors(containerColor = Color.Green)) {
                Text("ENREGISTRER", color = Color.Black)
            }
            Button(onClick = {
                coroutineScope.launch {
                    databaseHelper.deleteItem(id)
                    personnes = databaseHelper.cursorToPersonnes(databaseHelper.getAllItems())
                }
            }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                Text("SUPPRIMER")
            }
            Button(onClick = {
                coroutineScope.launch {
                    databaseHelper.updateItem(id, name)
                    personnes = databaseHelper.cursorToPersonnes(databaseHelper.getAllItems())
                }
            }, colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)) {
                Text("UPDATE")
            }
        }
        Spacer(modifier = Modifier.size(16.dp))

        // display data
       /* LazyColumn {
            items(person.count) { index ->
                person.moveToPosition(index)
                Text(text = "${person.getString(0)} - ${person.getString(1)}")
            }
        }*/

        LazyColumn {
            items(personnes) { personne ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Checkbox(
                        checked = selectedPersonIds.contains(personne.id),
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                selectedPersonIds = selectedPersonIds + personne.id
                                id = personne.id.toString()
                                name = personne.name
                            } else {
                                selectedPersonIds = selectedPersonIds - personne.id
                            }
                        },
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = "ID: ${personne.id}",
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = "Name: ${personne.name}",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }



    }
}

@Preview(showBackground = true)
@Composable
fun DB01ScreenPreview() {
    // Preview does not handle database interaction, provide a fake DatabaseHelper if needed
    DB01Screen(DatabaseHelper(LocalContext.current))
}
