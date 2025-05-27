enum ChatSender { user, ai }

enum ChatMessageType {
  text,
  recipeCard,
}

class ChatMessage {
  final ChatSender sender;
  final ChatMessageType type;
  final String? text;

  ChatMessage({
    required this.sender,
    required this.type,
    this.text,
  });
}
