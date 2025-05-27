import 'package:flutter/material.dart';

import '../../core/services/api/api_service.dart';
import 'models/chat_message.dart';
import 'widgets/chat_message_bubble.dart';

class ChatScreen extends StatefulWidget {
  const ChatScreen({super.key});

  @override
  State<ChatScreen> createState() => _ChatScreenState();
}

class _ChatScreenState extends State<ChatScreen> {
  final ApiService _apiService = ApiService();
  final TextEditingController _controller = TextEditingController();
  final ScrollController _scrollController = ScrollController();

  final List<ChatMessage> _messages = [];

  void _sendMessage() async {
    final text = _controller.text.trim();
    if (text.isEmpty) return;

    setState(() {
      _messages.add(ChatMessage(text: text, type: ChatMessageType.text, sender: ChatSender.user));
      _controller.clear();
    });

    String aiResponse;
    if (text.contains("produto")) {
      aiResponse = await _apiService.getProducts(text);
    } else if (text.contains("receita")) {
      aiResponse = await _apiService.getRecipes(text);
    } else if (text.contains("promoção")) {
      aiResponse = await _apiService.getPromotions(text);
    } else {
      aiResponse = "Desculpe, ainda estou aprendendo a responder isso.";
    }

    setState(() {
      _messages.add(ChatMessage(text: aiResponse, type: ChatMessageType.text, sender: ChatSender.ai));
    });

    Future.delayed(const Duration(milliseconds: 100), () {
      _scrollController.animateTo(
        _scrollController.position.maxScrollExtent,
        duration: const Duration(milliseconds: 300),
        curve: Curves.easeOut,
      );
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Assistente')),
      body: SafeArea(
        child: Column(
          children: [
            Expanded(
              child: ListView.builder(
                controller: _scrollController,
                itemCount: _messages.length,
                itemBuilder: (context, index) {
                  return ChatMessageBubble(message: _messages[index]);
                },
              ),
            ),
            const Divider(height: 1),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 8),
              color: Colors.white,
              child: Row(
                children: [
                  Expanded(
                    child: TextField(
                      controller: _controller,
                      textInputAction: TextInputAction.send,
                      onSubmitted: (_) => _sendMessage(),
                      decoration: const InputDecoration(
                        hintText: 'Digite sua mensagem...',
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.all(Radius.circular(12)),
                        ),
                        contentPadding: EdgeInsets.symmetric(horizontal: 12),
                      ),
                    ),
                  ),
                  const SizedBox(width: 8),
                  IconButton(
                    onPressed: _sendMessage,
                    icon: Icon(Icons.send, color: Colors.purple.shade700),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}