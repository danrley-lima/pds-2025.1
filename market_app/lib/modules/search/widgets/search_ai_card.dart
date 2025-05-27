import 'package:flutter/material.dart';

import 'suggestion_chip.dart';

class SearchAICard extends StatelessWidget {
  final TextEditingController textFieldController;
  final VoidCallback onFindIngredients;

  const SearchAICard({super.key, required this.textFieldController, required this.onFindIngredients});

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 2,
      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            Row(
              children: const [
                Icon(Icons.auto_awesome, color: Colors.deepPurple),
                SizedBox(width: 8),
                Text(
                  'Assistente de Compras com IA',
                  style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                ),
              ],
            ),
            const SizedBox(height: 12),
            const Text(
              'Peça receitas, ideias de refeições ou ingredientes específicos. Por exemplo:',
              style: TextStyle(fontSize: 14, color: Colors.black54),
            ),
            const SizedBox(height: 12),
            SuggestionChip(text: 'Receita de lasanha para 10 pessoas'),
            SuggestionChip(text: 'Ingredientes para um café da manhã saudável para 4 pessoas'),
            SuggestionChip(text: 'Ideias de jantar sem glúten'),
            const SizedBox(height: 12),
            TextField(
              controller: textFieldController,
              decoration: const InputDecoration(
                hintText: 'O que você gostaria de cozinhar?',
                prefixIcon: Icon(Icons.search),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.all(Radius.circular(12)),
                ),
              ),
            ),
            const SizedBox(height: 8),
            ElevatedButton.icon(
              onPressed: onFindIngredients,
              icon: const Icon(Icons.shopping_cart, color: Colors.white),
              label: const Text('Encontrar Ingredientes', style: TextStyle(color: Colors.white),),
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.deepPurple,
                padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 14),
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
              ),
            ),
            // Row(
            //   children: [
            //     Expanded(
            //       child: TextField(
            //         controller: textFieldController,
            //         decoration: const InputDecoration(
            //           hintText: 'O que você gostaria de cozinhar?',
            //           prefixIcon: Icon(Icons.search),
            //           border: OutlineInputBorder(
            //             borderRadius: BorderRadius.all(Radius.circular(12)),
            //           ),
            //         ),
            //       ),
            //     ),
            //     const SizedBox(width: 8),
            //     ElevatedButton.icon(
            //       onPressed: onFindIngredients,
            //       icon: const Icon(Icons.shopping_cart),
            //       label: const Text('Encontrar Ingredientes'),
            //       style: ElevatedButton.styleFrom(
            //         backgroundColor: Colors.deepPurple,
            //         padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 14),
            //         shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
            //       ),
            //     ),
            //   ],
            // ),
          ],
        ),
      ),
    );
  }
}