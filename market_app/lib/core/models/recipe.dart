class Recipe {
  final String title;
  final List<String> ingredients;
  final String preparation;

  Recipe({
    required this.title,
    required this.ingredients,
    required this.preparation,
  });

  factory Recipe.fromJson(Map<String, dynamic> json) {
    return Recipe(
      title: json['title'],
      ingredients: List<String>.from(json['ingredients']),
      preparation: json['preparation'],
    );
  }

  Map<String, dynamic> toJson() => {
    'title': title,
    'ingredients': ingredients,
    'preparation': preparation,
  };
}