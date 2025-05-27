import 'package:flutter/material.dart';

class RecentSearchesCard extends StatelessWidget {
  final List<String> recentSearches;
  final void Function(int) onSearchTap;

  const RecentSearchesCard({super.key, required this.recentSearches, required this.onSearchTap});

  @override
  Widget build(BuildContext context) {
    if (recentSearches.isEmpty) return const SizedBox.shrink();

    return Card(
      elevation: 2,
      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'Pesquisas Recentes',
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 12),
            SizedBox(
              height: 40,
              child: ListView.separated(
                scrollDirection: Axis.horizontal,
                itemCount: recentSearches.length,
                separatorBuilder: (_, __) => const SizedBox(width: 8),
                itemBuilder: (context, index) {
                  final search = recentSearches[index];
                  return GestureDetector(
                    onTap: () => onSearchTap(index),
                    child: Chip(
                      label: Text(search),
                      backgroundColor: Colors.grey.shade100,
                      side: BorderSide(color: Colors.grey.shade300),
                    ),
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}