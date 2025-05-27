import 'package:flutter/material.dart';
import '../models/chat_message.dart';

class ChatMessageBubble extends StatelessWidget {
  final ChatMessage message;

  const ChatMessageBubble({super.key, required this.message});

  @override
  Widget build(BuildContext context) {
    final isUser = message.sender == ChatSender.user;

    Widget content;
    switch (message.type) {
      case ChatMessageType.text:
        content = Container(
          padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 10),
          decoration: BoxDecoration(
            color: isUser ? Colors.purple.shade700 : Colors.grey.shade200,
            borderRadius: BorderRadius.circular(16),
          ),
          child: Text(
            message.text ?? '',
            style: TextStyle(
              color: isUser ? Colors.white : Colors.black87,
            ),
          ),
        );
        break;

      case ChatMessageType.recipeCard:
        content = Card(
          elevation: 2,
          margin: const EdgeInsets.symmetric(vertical: 4),
          child: SizedBox(
            width: 200,
            height: 100,
            child: Center(
              child: Text(
                'Card de Receita aqui!',
                style: TextStyle(color: Colors.grey.shade700),
              ),
            ),
          ),
        );
        break;
    }

    return Align(
      alignment: isUser ? Alignment.centerRight : Alignment.centerLeft,
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
        child: content,
      ),
    );
  }
}
