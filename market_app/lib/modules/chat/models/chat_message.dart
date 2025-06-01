enum ChatSender { user, ai }

enum ChatMessageType {
  text,
  recipeCard,
}

class ChatMessage {
  final ChatSender sender;
  final ChatMessageType type;
  final dynamic data;

  ChatMessage({
    required this.sender,
    required this.type,
    required this.data,
  });
}
