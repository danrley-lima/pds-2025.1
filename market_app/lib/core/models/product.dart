class Product {
  final int id;
  final String name;
  final String brand;
  final double unitPrice;
  final double unitWeight;
  final String unitType;
  final int stockQuantity;
  final bool available;
  final String categoryName;
  final bool? priority;

  Product({
    required this.id,
    required this.name,
    required this.brand,
    required this.unitPrice,
    required this.unitWeight,
    required this.unitType,
    required this.stockQuantity,
    required this.available,
    required this.categoryName,
    this.priority,
  });

  factory Product.fromJson(Map<String, dynamic> json) {
    return Product(
      id: json['id'],
      name: json['name'],
      brand: json['brand'],
      unitPrice: (json['unitPrice'] as num).toDouble(),
      unitWeight: (json['unitWeight'] as num).toDouble(),
      unitType: json['unitType'],
      stockQuantity: json['stockQuantity'],
      available: json['available'],
      categoryName: json['categoryName'],
      priority: json['priority'],
    );
  }

  Map<String, dynamic> toJson() => {
    'id': id,
    'name': name,
    'brand': brand,
    'unitPrice': unitPrice,
    'unitWeight': unitWeight,
    'unitType': unitType,
    'stockQuantity': stockQuantity,
    'available': available,
    'categoryName': categoryName,
    'priority': priority,
  };
}
