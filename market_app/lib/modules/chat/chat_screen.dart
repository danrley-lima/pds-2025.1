import 'package:flutter/material.dart';
import 'package:market_app/modules/chat/models/assistant_product_model.dart';
import '../../core/services/api/api_service.dart';
import 'models/assistant_recipe_model.dart';
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
      _messages.add(ChatMessage(data: text, type: ChatMessageType.text, sender: ChatSender.user));
      _controller.clear();
      _messages.add(ChatMessage(data: '...', type: ChatMessageType.text, sender: ChatSender.ai));
    });

    AssistantRecipeModel aiResponse;
    //aiResponse = await _apiService.getSuggestionRecipe(text);

    aiResponse = AssistantRecipeModel(
      products: [
        AssistantProductModel(
          id: "2",
          name: "Carne Moída",
          brand: "Friboi",
          unitPrice: 22.0,
          stockQuantity: 100.0,
          requiredQuantity: "500g",
          categoryName: "Carnes",
          promotionalPrice: 15.99,
        ),
        AssistantProductModel(
          id: "61",
          name: "Macarrão Espaguete",
          brand: "Renata",
          unitPrice: 4.0,
          stockQuantity: 120.0,
          requiredQuantity: "250g",
          categoryName: "Massas",
          promotionalPrice: null,
        ),
        AssistantProductModel(
          id: "31",
          name: "Queijo Mussarela",
          brand: "Vigor",
          unitPrice: 15.0,
          stockQuantity: 120.0,
          requiredQuantity: "200g",
          categoryName: "Laticínios",
          promotionalPrice: null,
        ),
        AssistantProductModel(
          id: "24",
          name: "Extrato de Tomate",
          brand: "Elefante",
          unitPrice: 3.8,
          stockQuantity: 100.0,
          requiredQuantity: "130g",
          categoryName: "Enlatados",
          promotionalPrice: null,
        ),
        AssistantProductModel(
          id: "7",
          name: "Cebola",
          brand: "Natural",
          unitPrice: 3.5,
          stockQuantity: 200.0,
          requiredQuantity: "100g",
          categoryName: "Hortifruti",
          promotionalPrice: 2.99,
        ),
        AssistantProductModel(
          id: "16",
          name: "Alho",
          brand: "Natural",
          unitPrice: 2.0,
          stockQuantity: 100.0,
          requiredQuantity: "10g",
          categoryName: "Temperos",
          promotionalPrice: null,
        ),
        AssistantProductModel(
          id: "58",
          name: "Óleo de Soja",
          brand: "Soya",
          unitPrice: 7.0,
          stockQuantity: 150.0,
          requiredQuantity: "50ml",
          categoryName: "Óleos e Gorduras",
          promotionalPrice: null,
        ),
        AssistantProductModel(
          id: "17",
          name: "Sal",
          brand: "Refinado",
          unitPrice: 1.5,
          stockQuantity: 500.0,
          requiredQuantity: "5g",
          categoryName: "Temperos",
          promotionalPrice: null,
        ),
        AssistantProductModel(
          id: "18",
          name: "Pimenta do Reino",
          brand: "Sadia",
          unitPrice: 5.0,
          stockQuantity: 80.0,
          requiredQuantity: "2g",
          categoryName: "Temperos",
          promotionalPrice: null,
        ),
      ],
      notFoundProducts: [],
    );

    Future.delayed(const Duration(seconds: 7), () {
      setState(() {
        _messages.removeLast();
        _messages.add(ChatMessage(data: aiResponse, type: ChatMessageType.recipeCard, sender: ChatSender.ai));
      });
    });

    Future.delayed(const Duration(milliseconds: 100), () {
      _scrollController.animateTo(
        _scrollController.position.maxScrollExtent,
        duration: const Duration(milliseconds: 300),
        curve: Curves.easeOut,
      );
    });
  }

  // Widget _buildMessage(AIMessage message) {
  //   switch (message.type) {
  //     case AIMessageType.recipe:
  //       final AiRecipeResponse data = message.data;
  //       return Column(
  //         crossAxisAlignment: CrossAxisAlignment.start,
  //         children: [
  //           const Padding(
  //             padding: EdgeInsets.all(12),
  //             child: Text("Receita gerada pela IA:"),
  //           ),
  //           ...data.products.map((product) => ProductCard(product: product)).toList(),
  //           if (data.notFoundProducts.isNotEmpty)
  //             Padding(
  //               padding: const EdgeInsets.all(12),
  //               child: Text(
  //                 'Não encontrados: ${data.notFoundProducts.join(', ')}',
  //                 style: const TextStyle(color: Colors.red),
  //               ),
  //             ),
  //         ],
  //       );
  //     case AIMessageType.text:
  //     default:
  //       return Padding(
  //         padding: const EdgeInsets.all(12),
  //         child: Text(message.data),
  //       );
  //   }
  // }

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