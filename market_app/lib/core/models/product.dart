class Product {
  final String name;
  final double unitPrice;
  final double? discountedPrice;
  final double weight;
  final String weightUnit;
  final int stockQuantity;

  Product({
    required this.name,
    required this.unitPrice,
    this.discountedPrice,
    required this.weight,
    required this.weightUnit,
    required this.stockQuantity,
  });

  factory Product.fromJson(Map<String, dynamic> json) {
    return Product(
      name: json['name'],
      unitPrice: json['unitPrice'].toDouble(),
      discountedPrice: json['discountedPrice']?.toDouble(),
      weight: json['weight'].toDouble(),
      weightUnit: json['weightUnit'],
      stockQuantity: json['stockQuantity'],
    );
  }

  Map<String, dynamic> toJson() => {
        'name': name,
        'unitPrice': unitPrice,
        'discountedPrice': discountedPrice,
        'weight': weight,
        'weightUnit': weightUnit,
        'stockQuantity': stockQuantity,
      };
}
