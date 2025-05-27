class Promotion {
  final String productName;
  final String promotionName;
  final double originalPrice;
  final double discountedPrice;
  final DateTime startDate;
  final DateTime endDate;

  Promotion({
    required this.productName,
    required this.promotionName,
    required this.originalPrice,
    required this.discountedPrice,
    required this.startDate,
    required this.endDate,
  });

  factory Promotion.fromJson(Map<String, dynamic> json) {
    return Promotion(
      productName: json['productName'],
      promotionName: json['promotionName'],
      originalPrice: json['originalPrice'].toDouble(),
      discountedPrice: json['discountedPrice'].toDouble(),
      startDate: DateTime.parse(json['startDate']),
      endDate: DateTime.parse(json['endDate']),
    );
  }

  Map<String, dynamic> toJson() => {
    'productName': productName,
    'promotionName': promotionName,
    'originalPrice': originalPrice,
    'discountedPrice': discountedPrice,
    'startDate': startDate.toIso8601String(),
    'endDate': endDate.toIso8601String(),
  };
}