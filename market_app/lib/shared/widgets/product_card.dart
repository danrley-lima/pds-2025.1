import 'package:flutter/material.dart';
import '../../core/models/product.dart';

class ProductCard extends StatelessWidget {
  final Product product;

  const ProductCard({super.key, required this.product});

  @override
  Widget build(BuildContext context) {
    final priceText = product.unitPrice.toStringAsFixed(2);

    return Card(
      margin: const EdgeInsets.symmetric(vertical: 8, horizontal: 12),
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(product.name, style: Theme.of(context).textTheme.titleLarge),
            const SizedBox(height: 4),
            Text('Preço: R\$ $priceText'),
            Text('Peso: ${product.unitWeight} ${product.unitType}'),
            Text('Estoque: ${product.stockQuantity} unidades'),
          ],
        ),
      ),
    );
  }
}