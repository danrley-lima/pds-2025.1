import 'package:flutter/material.dart';
import 'package:market_app/core/services/api/api_service.dart';
import '../chat/chat_screen.dart';
import 'models/quick_recipe.dart';
import 'widgets/list_screen.dart';
import 'widgets/pupular_recipes_card.dart';
import 'widgets/recent_search_card.dart';
import 'widgets/search_ai_card.dart';

class SearchScreen extends StatefulWidget {
  const SearchScreen({super.key});

  @override
  State<SearchScreen> createState() => _SearchScreenState();
}

class _SearchScreenState extends State<SearchScreen> {
  final TextEditingController _controller = TextEditingController();

  ApiService service = ApiService();

  final List<String> recentSearches = [
    'Produtos',
    'Promoções',
    'Categorias',
  ];

  final List<QuickRecipe> popularRecipes = [
    QuickRecipe(
      title: 'Panqueca de frango',
      subtitle: 'Saudável e rápida',
      imageUrl: 'https://www.saboresajinomoto.com.br/uploads/images/recipes/panqueca-de-frango.jpg',
    ),
    QuickRecipe(
      title: 'Salada Tropical',
      subtitle: 'Leve e refrescante',
    ),
    QuickRecipe(
      title: 'Torta de Frango',
      subtitle: 'Perfeita para o jantar',
    ),
  ];

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: Column(
        children: [
          SearchAICard(
            textFieldController: _controller,
            onFindIngredients: () {
              final query = _controller.text;
              debugPrint('Buscar ingredientes para: $query');
              Navigator.of(context).push(MaterialPageRoute(builder: (context) => const ChatScreen()));
            },
          ),
          RecentSearchesCard(
            recentSearches: recentSearches,
            onSearchTap: (index) async {
              if(index == 0) {
                final list = await service.getAllProducts();
                if(context.mounted) {
                  Navigator.of(context).push(MaterialPageRoute(builder: (context) => ListScreen(itemList: list, title: "Produtos")));
                }
              }

              if(index == 1) {
                final list = await service.getActivePromotions();
                if(context.mounted) {
                  Navigator.of(context).push(MaterialPageRoute(builder: (context) => ListScreen(itemList: list, title: "Promoções")));
                }
              } 
              
              if(index == 2) {
                final list = await service.getAllCategories();
                if(context.mounted) {
                  Navigator.of(context).push(MaterialPageRoute(builder: (context) => ListScreen(itemList: list, title: "Categorias")));
                }
              }
            },
          ),
          PopularRecipesCard(
            recipes: popularRecipes,
            onRecipeTap: (recipe) {
              debugPrint('Ver receita: ${recipe.title}');
            },
          ),
          const SizedBox(height: 8),
        ],
      ),
    );
  }
}