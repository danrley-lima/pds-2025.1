import 'product.dart';

class Category {
  final String name;
  final List<Product> products;

  Category({
    required this.name,
    required this.products,
  });

  factory Category.fromJson(Map<String, dynamic> json) {
    return Category(
      name: json['name'],
      products: (json['products'] as List)
          .map((p) => Product.fromJson(p))
          .toList(),
    );
  }

  Map<String, dynamic> toJson() => {
    'name': name,
    'products': products.map((p) => p.toJson()).toList(),
  };
}