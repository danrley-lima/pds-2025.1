import 'package:flutter/material.dart';

import '../../../core/models/product.dart';
import '../../../core/models/promotion.dart';
import '../../../shared/widgets/product_card.dart';
import '../../../shared/widgets/promotion_card.dart';

class ListScreen extends StatefulWidget {
  final String title;
  final List<dynamic> itemList;

  const ListScreen({super.key, required this.title, required this.itemList});

  @override
  State<ListScreen> createState() => _ListScreenState();
}

class _ListScreenState extends State<ListScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: ListView.builder(
        itemCount: widget.itemList.length,
        itemBuilder: (context, index) {
          if (widget.itemList is List<Product>) {
            return ProductCard(product: widget.itemList[index]);
          }else if (widget.itemList is List<Promotion>) {
            return PromotionCard(promotion: widget.itemList[index]);
          }
          return Center(child: Text("Nenhum item disponível"));
        },
      ),
    );
  }
}