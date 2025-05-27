class Promotion {
  final int id;
  final String description;
  final String productName;
  final double originalPrice;
  final double promotionalPrice;
  final DateTime initialDate;
  final DateTime finalDate;

  Promotion({
    required this.id,
    required this.description,
    required this.productName,
    required this.originalPrice,
    required this.promotionalPrice,
    required this.initialDate,
    required this.finalDate,
  });

  factory Promotion.fromJson(Map<String, dynamic> json) {
    return Promotion(
      id: json['id'],
      description: json['description'],
      productName: json['productName'],
      originalPrice: (json['originalPrice'] as num).toDouble(),
      promotionalPrice: (json['promotionalPrice'] as num).toDouble(),
      initialDate: DateTime.parse(json['initialDate']),
      finalDate: DateTime.parse(json['finalDate']),
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'description': description,
        'productName': productName,
        'originalPrice': originalPrice,
        'promotionalPrice': promotionalPrice,
        'initialDate': initialDate.toIso8601String(),
        'finalDate': finalDate.toIso8601String(),
      };
}