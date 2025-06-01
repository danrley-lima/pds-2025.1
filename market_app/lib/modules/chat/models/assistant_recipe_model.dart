import 'assistant_product_model.dart';

class AssistantRecipeModel {
  final List<AssistantProductModel> products;
  final List<AssistantProductModel> notFoundProducts;

  AssistantRecipeModel({
    required this.products,
    required this.notFoundProducts,
  });

  factory AssistantRecipeModel.fromJson(Map<String, dynamic> json) {
    return AssistantRecipeModel(
      products: (json['products'] as List)
          .map((item) => AssistantProductModel.fromJson(item))
          .toList(),
      notFoundProducts: (json['notFoundProducts'] as List)
          .map((item) => AssistantProductModel.fromJson(item))
          .toList(),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'products': products.map((e) => e.toJson()).toList(),
      'notFoundProducts': notFoundProducts.map((e) => e.toJson()).toList(),
    };
  }
}
