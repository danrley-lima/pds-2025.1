import 'package:flutter/material.dart';

import '../../core/models/promotion.dart';

class PromotionCard extends StatelessWidget {
  final Promotion promotion;

  const PromotionCard({super.key, required this.promotion});

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.symmetric(vertical: 8, horizontal: 12),
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(promotion.productName, style: Theme.of(context).textTheme.titleLarge),
            const SizedBox(height: 4),
            Text('Promoção: ${promotion.description}'),
            Text('De R\$ ${promotion.originalPrice.toStringAsFixed(2)} por R\$ ${promotion.promotionalPrice.toStringAsFixed(2)}'),
            Text('Válida de ${promotion.initialDate.toUtc()} até ${promotion.finalDate.toUtc()}'),
          ],
        ),
      ),
    );
  }
}