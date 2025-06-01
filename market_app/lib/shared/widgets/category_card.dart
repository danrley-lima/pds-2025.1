import 'package:flutter/material.dart';
import '../../core/models/category.dart';

class CategoryCard extends StatelessWidget {
  final ProductCategory category;

  const CategoryCard({super.key, required this.category});

  @override
  Widget build(BuildContext context) {

    return Card(
      margin: const EdgeInsets.symmetric(vertical: 8, horizontal: 12),
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Text(category.name, style: Theme.of(context).textTheme.titleLarge),
      ),
    );
  }
}