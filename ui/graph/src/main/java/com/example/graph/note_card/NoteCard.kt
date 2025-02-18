package com.example.graph.note_card

import android.net.Uri
import android.text.format.DateFormat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.common_ui.Cons.AUDIOS
import com.example.common_ui.Cons.EDIT_ROUTE
import com.example.common_ui.Cons.IMAGES
import com.example.common_ui.Cons.JPEG
import com.example.common_ui.Cons.KEY_CLICK
import com.example.common_ui.Cons.MP3
import com.example.common_ui.Cons.NON
import com.example.common_ui.DataStoreVM
import com.example.common_ui.Icons.ANGLE_DOWN_ICON
import com.example.common_ui.Icons.ANGLE_UP_ICON
import com.example.common_ui.Icons.CIRCLE_ICON_18
import com.example.common_ui.Icons.CLOCK_ICON
import com.example.common_ui.Icons.RESET_ICON
import com.example.common_ui.codeUrl
import com.example.graph.ImageDisplayed
import com.example.graph.navigation_drawer.Screens
import com.example.graph.navigation_drawer.Screens.*
import com.example.graph.sound
import com.example.links.model.NoteAndLink
import com.example.links.ui.LinkPart
import com.example.links.ui.LinkVM
import com.example.links.ui.NoteAndLinkVM
import com.example.note.DataViewModel
import com.example.note.model.Data
import com.example.note.model.Note
import com.example.tasks.NoteAndTaskViewModel
import com.example.tasks.TaskViewModel
import com.example.tasks.model.NoteAndTask
import com.example.tasks.model.Task
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import me.saket.swipe.rememberSwipeableActionsState
import java.io.File
import java.util.*

@Composable
fun NoteCard(
    dataStoreVM: DataStoreVM = hiltViewModel(),
    screen: Screens,
    noteEntity: Note,
    navController: NavController,
    homeSelectionState: MutableState<Boolean>?,
    trashSelectionState: MutableState<Boolean>?,
    selectedNotes: SnapshotStateList<Data>?,
    onSwipeNote: (Note) -> Unit
) {
    val swipeState = rememberSwipeableActionsState()
    val currentLayout = remember(dataStoreVM, dataStoreVM::getLayout).collectAsState()

    val action = SwipeAction(
        onSwipe = {
            onSwipeNote.invoke(noteEntity)
        },
        icon = {},
        background = Color.Transparent
    )

    if (currentLayout.value == "LIST") {
        SwipeableActionsBox(
            modifier = Modifier,
            backgroundUntilSwipeThreshold = Color.Transparent,
            endActions = listOf(action),
            swipeThreshold = 100.dp,
            state = swipeState
        ) {
            Card(
                noteEntity = noteEntity,
                navController = navController,
                screen = screen,
                homeSelectionState = homeSelectionState,
                trashSelectionState = trashSelectionState,
                selectedNotes = selectedNotes
            )
        }
    } else {
        Card(
            noteEntity = noteEntity,
            navController = navController,
            screen = screen,
            homeSelectionState = homeSelectionState,
            trashSelectionState = trashSelectionState,
            selectedNotes = selectedNotes
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun Card(
    taskViewModel: TaskViewModel = hiltViewModel(),
    noteAndTodoVM: NoteAndTaskViewModel = hiltViewModel(),
    dataViewModel: DataViewModel = hiltViewModel(),
    dataStoreVM: DataStoreVM = hiltViewModel(),
    linkVM: LinkVM = hiltViewModel(),
    noteAndLinkVM: NoteAndLinkVM = hiltViewModel(),
    noteEntity: Note,
    navController: NavController,
    screen: Screens,
    homeSelectionState: MutableState<Boolean>?,
    trashSelectionState: MutableState<Boolean>?,
    selectedNotes: SnapshotStateList<Data>?
) {
    val ctx = LocalContext.current
    val thereIsSoundEffect = remember(dataStoreVM, dataStoreVM::getSound).collectAsState()

    val note = noteEntity.dataEntity
    val labels = noteEntity.tagEntities
    val internalPath = ctx.filesDir.path

    val observeTodoList = remember(taskViewModel, taskViewModel::getAllTaskList).collectAsState()
    val observeNoteAndTodo =
        remember(noteAndTodoVM, noteAndTodoVM::getAllNotesAndTask).collectAsState()

    val observerLinks = remember(linkVM, linkVM::getAllLinks).collectAsState()
    val observerNoteAndLink =
        remember(noteAndLinkVM, noteAndLinkVM::getAllNotesAndLinks).collectAsState()

    val mediaPath = ctx.filesDir.path + "/$AUDIOS/" + note.uid + "." + MP3
    val imagePath = "$internalPath/$IMAGES/${note.uid}.$JPEG"

    var todoListState by remember { mutableStateOf(false) }
    val media = remember { mutableStateOf<Uri?>(File(imagePath).toUri()) }

    val haptic = LocalHapticFeedback.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .combinedClickable(
                onLongClick = {
                    // To make vibration.
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                    when (screen) {
                        HOME_SCREEN -> {
                            homeSelectionState?.value = true
                        }
                        TRASH_SCREEN -> {
                            trashSelectionState?.value = true
                        }
                        else -> {}
                    }
                    selectedNotes?.add(note)
                }
            ) {
                sound.makeSound.invoke(ctx, KEY_CLICK, thereIsSoundEffect.value)

                if (screen == HOME_SCREEN && !homeSelectionState?.value!!) {
                    navController.navigate(
                        route = EDIT_ROUTE + "/" +
                                note.uid + "/" +
                                codeUrl.invoke(note.title) + "/" +
                                codeUrl.invoke(note.description) + "/" +
                                note.color + "/" +
                                note.textColor + "/" +
                                note.priority + "/" +
                                note.audioDuration + "/" +
                                note.reminding
                    )

                } else if (screen == TRASH_SCREEN && !trashSelectionState?.value!!) {
                    /*do nothing.*/
                } else {
                    when {
                        !selectedNotes?.contains(note)!! -> selectedNotes.add(note)
                        else -> selectedNotes.remove(note)
                    }
                }
                selectedNotes?.ifEmpty {
                    homeSelectionState?.value = false
                    trashSelectionState?.value = false
                }
            }
            .drawBehind {
                if (note.priority.equals(NON, true)) {
                    normalNotePath(note)
                } else {
                    clipNotePath(note)
                }
            },
        shape = AbsoluteRoundedCornerShape(15.dp),
        border =
            if(selectedNotes?.contains(note) == true) {
                when(screen) {
                    HOME_SCREEN -> BorderStroke(3.dp, Color.Cyan)
                    TRASH_SCREEN -> BorderStroke(3.dp, Color.Red)
                    else -> { throw Exception("") }
                }
            } else {
                BorderStroke(0.dp, Color.Transparent)
            } ,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)

    ) {

        // display the image.
        when (screen) {
            HOME_SCREEN, TRASH_SCREEN -> {
                ImageDisplayed(media = dataViewModel::imageDecoder.invoke(ctx, note.uid))
            }
            else -> { // Timber.tag(TAG).d("")
            }
        }

        Text(
            text = note.title ?: "",
            fontSize = 19.sp,
            color = Color(note.textColor),
            modifier = Modifier.padding(3.dp)
        )
        Text(
            text = note.description ?: "",
            fontSize = 15.sp,
            color = Color(note.textColor),
            modifier = Modifier.padding(start = 3.dp, end = 3.dp, bottom = 7.dp)
        )

        //media display.
                if (
                    File(mediaPath).exists()
                ) {
                    com.example.media_player.NoteMediaPlayer(localMediaUid = note.uid)
                }

        // tagEntities.
        LazyRow {
            items(items = labels) { label ->
                AssistChip(
                    modifier = Modifier.alpha(.7f),
                    border = AssistChipDefaults.assistChipBorder(borderColor = Color.Transparent),
                    onClick = { },
                    label = {
                        label.label?.let { Text(it, fontSize = 11.sp) }
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = CIRCLE_ICON_18),
                            contentDescription = null,
                            tint = Color(label.color),
                            modifier = Modifier.size(10.dp)
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        labelColor = Color(note.textColor)
                    ),
                    shape = CircleShape
                )
                Spacer(modifier = Modifier.width(3.dp))
            }
        }

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (screen == TRASH_SCREEN) {
                IconButton(onClick = {
                    dataViewModel.editData(
                        Data(
                            title = note.title,
                            description = note.description,
                            priority = note.priority,
                            uid = note.uid,
                            trashed = 0,
                            color = note.color,
                            textColor = note.textColor
                        )
                    )
                }) {
                    Icon(
                        painterResource(id = RESET_ICON), null,
                        tint = Color(note.textColor)
                    )
                }
            }

            //
            if (screen==HOME_SCREEN && note.reminding != 0L) {
                note.reminding.let {
                    kotlin.runCatching {
                        ElevatedAssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    DateFormat.format("yyyy-MM-dd HH:mm", Date(it)).toString(),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textDecoration = if (it < Calendar.getInstance().time.time) {
                                        TextDecoration.LineThrough
                                    } else {
                                        TextDecoration.None
                                    }
                                )
                            },
                            leadingIcon = {
                                if (it >= Calendar.getInstance().time.time) {
                                    Icon(painterResource(CLOCK_ICON), null)
                                }
                            }
                        )
                    }
                }
            }
        }

        // display link card.
        observerLinks.value.filter {
            observerNoteAndLink.value.contains(
                NoteAndLink(note.uid, it.id)
            )
        }.forEach { _link ->
            LinkPart(
                linkVM = linkVM,
                noteAndLinkVM = noteAndLinkVM,
                noteUid = note.uid,
                swipeable = false,
                link = _link,
            )
        }

        // display tasks list.
        if (
            observeTodoList.value.any {
                observeNoteAndTodo.value.contains(
                    NoteAndTask(note.uid, it.id)
                )
            }
        ) {
            Icon(
                painterResource(
                    if (todoListState) ANGLE_UP_ICON else ANGLE_DOWN_ICON
                ),
                null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        todoListState = !todoListState
                    },
                tint = Color(note.textColor)
            )
        }

        AnimatedVisibility(visible = todoListState, modifier = Modifier.height(100.dp)) {
            LazyColumn {
                item {
                    observeTodoList.value.filter {
                        observeNoteAndTodo.value.contains(
                            NoteAndTask(note.uid, it.id)
                        )
                    }.forEach { todo ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = todo.isDone, onClick = {
                                taskViewModel.updateTotoItem(
                                    Task(
                                        id = todo.id,
                                        item = todo.item,
                                        isDone = !todo.isDone
                                    )
                                )
                            },
                                colors = RadioButtonDefaults.colors(
                                selectedColor = Color.Gray,
                                unselectedColor = Color(note.textColor)
                            ),
                                modifier = Modifier
                                    .padding(5.dp)
                                    .size(14.dp)
                            )
                            todo.item?.let { item ->
                                Text(
                                    text = item,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 14.sp,
                                    style = TextStyle(
                                        textDecoration = if (todo.isDone) {
                                            TextDecoration.LineThrough
                                        } else {
                                            TextDecoration.None
                                        },
                                        color = if (todo.isDone) Color.Gray else Color(note.textColor)
                                    ),
                                    modifier = Modifier.padding(3.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

