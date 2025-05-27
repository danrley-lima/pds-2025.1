import 'package:flutter/material.dart';

class ChecklistScreen extends StatefulWidget {
  const ChecklistScreen({super.key});

  @override
  State<ChecklistScreen> createState() => _ChecklistScreenState();
}

class _ChecklistScreenState extends State<ChecklistScreen> {
  final int totalItens = 10;
  final int itensConcluidos = 0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            Align(
              alignment: Alignment.centerLeft,
              child: Text(
                'Meu Checklist de Compras',
                style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
            ),
            const SizedBox(height: 16),
            Card(
              elevation: 2,
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  children: [
                    // Cabeçalho do card
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Row(
                          children: [
                            Icon(Icons.check_box_outlined, color: Colors.deepPurple),
                            const SizedBox(width: 8),
                            Text(
                              'Checklist de Compras',
                              style: TextStyle(fontWeight: FontWeight.bold),
                            ),
                          ],
                        ),
                        Text('$itensConcluidos/$totalItens concluídos'),
                      ],
                    ),

                    const SizedBox(height: 32),

                    // Conteúdo: checklist vazio
                    Column(
                      children: [
                        Text('Seu checklist está vazio'),
                        const SizedBox(height: 12),
                        ElevatedButton.icon(
                          icon: Icon(Icons.search, color: Colors.white),
                          label: const Text('Buscar Produtos', style: TextStyle(color: Colors.white),),
                          style: ElevatedButton.styleFrom(
                            backgroundColor: Colors.deepPurple,
                            padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 12),
                            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
                          ),
                          onPressed: () {
                            // Redirecionar para buscar produtos
                          },
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}