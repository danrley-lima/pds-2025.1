import 'package:flutter/material.dart';
import '../models/chat_message.dart';
import 'ai_product_card.dart';

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
            message.data ?? '',
            style: TextStyle(
              color: isUser ? Colors.white : Colors.black87,
            ),
          ),
        );
        break;

      case ChatMessageType.recipeCard:
        content = Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 10),
              decoration: BoxDecoration(
                color: isUser ? Colors.purple.shade700 : Colors.grey.shade200,
                borderRadius: BorderRadius.circular(16),
              ),
              child: Text("Olá, aqui está a resposta para seu resultado:"),
            ),
            ...message.data.products.map((product) => AiProductCard(product: product)).toList(),
            if (message.data.notFoundProducts.isNotEmpty)
              Padding(
                padding: const EdgeInsets.all(12),
                child: Text(
                  'Não encontrados: ${message.data.notFoundProducts.join(', ')}',
                  style: const TextStyle(color: Colors.red),
                ),
              ),
          ],
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
