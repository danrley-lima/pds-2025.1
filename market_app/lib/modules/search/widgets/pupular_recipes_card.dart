import 'package:flutter/material.dart';
import '../models/quick_recipe.dart';

class PopularRecipesCard extends StatelessWidget {
  final List<QuickRecipe> recipes;
  final void Function(QuickRecipe) onRecipeTap;

  const PopularRecipesCard({super.key, required this.recipes, required this.onRecipeTap});

  @override
  Widget build(BuildContext context) {
    if (recipes.isEmpty) return const SizedBox.shrink();

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Padding(
          padding: EdgeInsets.symmetric(horizontal: 16, vertical: 8),
          child: Text(
            'Receitas Populares',
            style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
          ),
        ),
        SizedBox(
          height: 180,
          child: ListView.separated(
            scrollDirection: Axis.horizontal,
            padding: const EdgeInsets.symmetric(horizontal: 16),
            itemCount: recipes.length,
            separatorBuilder: (_, __) => const SizedBox(width: 12),
            itemBuilder: (context, index) {
              final recipe = recipes[index];
              return GestureDetector(
                onTap: () => onRecipeTap(recipe),
                child: Container(
                  width: 140,
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(16),
                    boxShadow: [
                      BoxShadow(
                        color: Colors.black.withValues(alpha: 0.05),
                        blurRadius: 5,
                        offset: const Offset(0, 2),
                      ),
                    ],
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      ClipRRect(
                        borderRadius: const BorderRadius.vertical(top: Radius.circular(16)),
                        child: recipe.imageUrl != null ? Image.network(
                          recipe.imageUrl!,
                          height: 100,
                          width: 140,
                          fit: BoxFit.cover,
                        ) : Align(alignment: Alignment.center, child: Icon(Icons.image_not_supported_sharp, size: 30)),
                      ),
                      Padding(
                        padding: const EdgeInsets.all(8),
                        child: Text(
                          recipe.title,
                          style: const TextStyle(fontWeight: FontWeight.bold),
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 8),
                        child: Text(
                          recipe.subtitle,
                          style: const TextStyle(fontSize: 12, color: Colors.black54),
                          maxLines: 2,
                          overflow: TextOverflow.ellipsis,
                        ),
                      ),
                    ],
                  ),
                ),
              );
            },
          ),
        ),
      ],
    );
  }
}