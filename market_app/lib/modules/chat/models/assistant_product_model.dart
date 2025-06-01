class AssistantProductModel {
  final String id;
  final String name;
  final String brand;
  final double unitPrice;
  final double stockQuantity;
  final String requiredQuantity;
  final String categoryName;
  final double? promotionalPrice;

  AssistantProductModel({
    required this.id,
    required this.name,
    required this.brand,
    required this.unitPrice,
    required this.stockQuantity,
    required this.requiredQuantity,
    required this.categoryName,
    required this.promotionalPrice,
  });

  factory AssistantProductModel.fromJson(Map<String, dynamic> json) {
    return AssistantProductModel(
      id: json['id'],
      name: json['name'],
      brand: json['brand'],
      unitPrice: double.tryParse(json['unitPrice'].toString()) ?? 0.0,
      stockQuantity: double.tryParse(json['stockQuantity'].toString()) ?? 0.0,
      requiredQuantity: json['requiredQuantity'],
      categoryName: json['categoryName'],
      promotionalPrice: json['promotionalPrice'] != null
          ? double.tryParse(json['promotionalPrice'].toString())
          : null,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'name': name,
      'brand': brand,
      'unitPrice': unitPrice,
      'stockQuantity': stockQuantity,
      'requiredQuantity': requiredQuantity,
      'categoryName': categoryName,
      'promotionalPrice': promotionalPrice,
    };
  }
}