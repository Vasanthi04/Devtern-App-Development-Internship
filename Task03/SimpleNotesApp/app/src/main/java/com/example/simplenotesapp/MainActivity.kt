package com.example.simplenotesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplenotesapp.ui.theme.SimpleNotesAppTheme

data class Note(
    val id: Int,
    val title: String,
    val content: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleNotesAppTheme {
                NotesScreen()
            }
        }
    }
}

@Composable
fun NotesScreen() {
    var notes by remember { mutableStateOf(listOf<Note>()) }
    var selectedNote by remember { mutableStateOf<Note?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                selectedNote = Note(
                    id = notes.size + 1,
                    title = "",
                    content = ""
                )
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Note")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(notes) { note ->
                    ListItem(
                        headlineContent = { Text(note.title) },
                        modifier = Modifier.clickable {
                            selectedNote = note
                        }
                    )
                }
            }

            if (notes.isEmpty()) {
                Text(
                    text = "Notes App",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 32.sp, // Increased font size
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            selectedNote?.let {
                NoteEditor(
                    note = it,
                    onSave = { updatedNote ->
                        notes = notes.map { note ->
                            if (note.id == updatedNote.id) updatedNote else note
                        }
                    },
                    onDismiss = { selectedNote = null }
                )
            }
        }
    }
}

@Composable
fun NoteEditor(note: Note, onSave: (Note) -> Unit, onDismiss: () -> Unit) {
    var title by remember { mutableStateOf(note.title) }
    var content by remember { mutableStateOf(note.content) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Edit Note") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content") },
                    modifier = Modifier.height(150.dp)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(note.copy(title = title, content = content))
                onDismiss()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    SimpleNotesAppTheme {
        NotesScreen()
    }
}
