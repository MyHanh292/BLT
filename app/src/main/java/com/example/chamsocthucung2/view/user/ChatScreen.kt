@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chamsocthucung2.view.user

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chamsocthucung2.R
import com.example.chamsocthucung2.data.model.user.Chat.ChatMessage
import com.example.chamsocthucung2.viewmodel.user.ChatViewModel
import java.io.File

@Composable
fun ChatScreen(navController: NavController) {
    val viewModel: ChatViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    var messageText by remember { mutableStateOf("") }
    val scrollState = rememberLazyListState()
    val doctorInfo = remember { VetDoctor("Yến Oanh", "...", R.drawable.doctor2) }
    val context = LocalContext.current
    var showEmojiPicker by remember { mutableStateOf(false) } // State để hiển thị/ẩn bảng emoji

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.addMessage("[Hình ảnh]", true, uri.toString()) }
    }

    val tempImageUri = remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && tempImageUri.value != null) {
            viewModel.addMessage("[Đã chụp ảnh]", true, tempImageUri.value!!.toString())
            viewModel.clearTempImageUri()
        } else {
            viewModel.clearTempImageUri()
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            scrollState.animateScrollToItem(messages.lastIndex)
        }
    }

    Scaffold(
        topBar = { ChatAppBar(navController, doctorInfo) },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Column {
                UserInput(
                    onMessageChange = { messageText = it },
                    onSendClick = {
                        if (messageText.isNotBlank()) {
                            viewModel.addMessage(messageText, true)
                            messageText = ""
                            viewModel.simulateReply()
                        }
                    },
                    messageText = messageText,
                    onImageClick = { pickImageLauncher.launch("image/*") },
                    onCameraClick = {
                        val uri = context.createTempImageFile().toUri()
                        tempImageUri.value = uri
                        cameraLauncher.launch(uri)
                    },
                    onEmojiClick = { showEmojiPicker = !showEmojiPicker } // Toggle hiển thị emoji picker
                )
                if (showEmojiPicker) {
                    EmojiPicker(onEmojiClick = { emoji ->
                        messageText += emoji
                        showEmojiPicker = false
                    })
                }
            }
        }
    ) { padding ->
        MessageList(messages = messages, scrollState = scrollState, contentPadding = padding)
    }
}

@Composable
fun EmojiPicker(onEmojiClick: (String) -> Unit) {
    val emojis = listOf(
        "😊", "😂", "❤️", "👍", "🎉", "🙏", "🤔", "🔥", "💯", "✨",
        "🐶", "🐱", "🐰", "🐠", "🐦", "⚽", "🏀", "🎮", "🎬", "🎵",
        "🍔", "🍕", "🍦", "☕", "🍺", "🚗", "✈️", "🏠", "🌳", "☀️"
        // Bạn có thể thêm nhiều emoji hơn vào đây
    )
    LazyVerticalGrid(GridCells.Fixed(8), contentPadding = PaddingValues(8.dp)) {
        items(emojis) { emoji ->
            Button(
                onClick = { onEmojiClick(emoji) },
                modifier = Modifier.padding(2.dp),
                contentPadding = PaddingValues(4.dp)
            ) {
                Text(emoji, style = TextStyle(fontSize = 20.sp))
            }
        }
    }
}

fun Context.createTempImageFile(): File {
    val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile("temp_image", ".jpg", storageDir)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatAppBar(navController: NavController, doctor: VetDoctor) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = doctor.imageRes,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(doctor.name, style = MaterialTheme.typography.titleMedium)
                    Text("Đang hoạt động", style = MaterialTheme.typography.bodySmall, color = Color.Green)
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun MessageList(messages: List<ChatMessage>, scrollState: LazyListState, contentPadding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .padding(contentPadding),
        state = scrollState,
        reverseLayout = false,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }
        items(messages) { message ->
            MessageBubble(message = message)
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    val isUser = message.isFromUser
    val backgroundColor = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val textColor = Color.White
    val alignment = if (isUser) Arrangement.End else Arrangement.Start
    val bubbleShape = RoundedCornerShape(8.dp)
    val avatarAlignment = if (!isUser) Alignment.Top else Alignment.CenterVertically

    var showReactionPopup by remember { mutableStateOf(false) }
    var reactionAnchor by remember { mutableStateOf(IntOffset(0, 0)) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = alignment,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isUser) {
            Row(verticalAlignment = Alignment.Bottom) {
                AsyncImage(
                    model = R.drawable.doctor2,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .align(avatarAlignment),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box {
                    Surface(
                        modifier = Modifier
                            .wrapContentWidth(Alignment.Start)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = { offset ->
                                        reactionAnchor = IntOffset(offset.x.toInt(), offset.y.toInt())
                                        showReactionPopup = true
                                    }
                                )
                            },
                        shape = bubbleShape,
                        color = backgroundColor,
                        tonalElevation = 4.dp
                    ) {
                        if (message.imageUri != null) {
                            AsyncImage(
                                model = message.imageUri,
                                contentDescription = "Hình ảnh đã gửi",
                                modifier = Modifier
                                    .widthIn(max = 200.dp)
                                    .heightIn(max = 200.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                text = message.text,
                                color = textColor,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }

                    if (showReactionPopup && !message.isFromUser) {
                        DropdownMenu(
                            expanded = showReactionPopup,
                            onDismissRequest = { showReactionPopup = false },
                            offset = reactionAnchor
                        ) {
                            DropdownMenuItem(text = { Text("👍") }, onClick = { /* TODO: Handle like */ showReactionPopup = false })
                            DropdownMenuItem(text = { Text("❤️") }, onClick = { /* TODO: Handle love */ showReactionPopup = false })
                            DropdownMenuItem(text = { Text("😂") }, onClick = { /* TODO: Handle laugh */ showReactionPopup = false })
                            DropdownMenuItem(text = { Text("😮") }, onClick = { /* TODO: Handle surprised */ showReactionPopup = false })
                            DropdownMenuItem(text = { Text("😥") }, onClick = { /* TODO: Handle sad */ showReactionPopup = false })
                            DropdownMenuItem(text = { Text("😠") }, onClick = { /* TODO: Handle angry */ showReactionPopup = false })
                            // Thêm các icon reaction khác
                        }
                    }
                }
            }
        } else {
            Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom) {
                Spacer(modifier = Modifier.weight(1f))
                Surface(
                    modifier = Modifier
                        .wrapContentWidth(Alignment.End),
                    shape = bubbleShape,
                    color = backgroundColor,
                    tonalElevation = 4.dp
                ) {
                    if (message.imageUri != null) {
                        AsyncImage(
                            model = message.imageUri,
                            contentDescription = "Hình ảnh đã gửi",
                            modifier = Modifier
                                .widthIn(max = 200.dp)
                                .heightIn(max = 200.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = message.text,
                            color = textColor,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

fun DropdownMenu(expanded: Boolean, onDismissRequest: () -> Unit, offset: IntOffset, content: @Composable ColumnScope.() -> Unit) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInput(
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit,
    messageText: String,
    onImageClick: () -> Unit,
    onCameraClick: () -> Unit,
    onEmojiClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onImageClick) {
            Icon(Icons.Outlined.Image, contentDescription = "Hình ảnh", tint = MaterialTheme.colorScheme.onSurface)
        }
        IconButton(onClick = onCameraClick) {
            Icon(Icons.Outlined.CameraAlt, contentDescription = "Chụp ảnh", tint = MaterialTheme.colorScheme.onSurface)
        }
        IconButton(onClick = onEmojiClick) { // Nút emoji
            Icon(Icons.Outlined.EmojiEmotions, contentDescription = "Emoji", tint = MaterialTheme.colorScheme.onSurface)
        }
        Spacer(modifier = Modifier.width(8.dp))
        OutlinedTextField(
            value = messageText,
            onValueChange = onMessageChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Nhập tin nhắn...") },
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            ),
            singleLine = false,
            maxLines = 4
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onSendClick,
            enabled = messageText.isNotBlank()
        ) {
            Icon(Icons.Filled.Send, contentDescription = "Gửi", tint = MaterialTheme.colorScheme.primary)
        }
    }
}
