import 'package:flutter/material.dart';

class ProductManagerScreen extends StatefulWidget {
  const ProductManagerScreen({super.key});

  @override
  State<ProductManagerScreen> createState() => _ProductManagerScreenState();
}

class _ProductManagerScreenState extends State<ProductManagerScreen> {
  final _formKey = GlobalKey<FormState>();

  // Controllers
  final TextEditingController nomeController = TextEditingController();
  final TextEditingController precoController = TextEditingController();
  final TextEditingController quantidadeController = TextEditingController();

  String? categoriaSelecionada;
  String? marcaSelecionada;

  final categorias = ['Carnes', 'Laticínios', 'Vegetais', 'Bebidas', 'Frios'];
  final marcas = ['Marca A', 'Marca B', 'Marca C'];

  void _abrirFormularioProduto() {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.vertical(top: Radius.circular(20))),
      builder: (_) => Padding(
        padding: EdgeInsets.only(
          left: 16,
          right: 16,
          bottom: MediaQuery.of(context).viewInsets.bottom + 20,
          top: 20,
        ),
        child: Form(
          key: _formKey,
          child: Wrap(
            runSpacing: 16,
            children: [
              Text('Novo Produto', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),

              TextFormField(
                controller: nomeController,
                decoration: InputDecoration(labelText: 'Nome'),
                validator: (value) => value!.isEmpty ? 'Informe o nome' : null,
              ),

              DropdownButtonFormField<String>(
                value: categoriaSelecionada,
                decoration: InputDecoration(labelText: 'Categoria'),
                items: categorias.map((cat) {
                  return DropdownMenuItem(value: cat, child: Text(cat));
                }).toList(),
                onChanged: (value) => setState(() => categoriaSelecionada = value),
                validator: (value) => value == null ? 'Selecione uma categoria' : null,
              ),

              DropdownButtonFormField<String>(
                value: marcaSelecionada,
                decoration: InputDecoration(labelText: 'Marca'),
                items: marcas.map((marca) {
                  return DropdownMenuItem(value: marca, child: Text(marca));
                }).toList(),
                onChanged: (value) => setState(() => marcaSelecionada = value),
                validator: (value) => value == null ? 'Selecione uma marca' : null,
              ),

              TextFormField(
                controller: precoController,
                decoration: InputDecoration(labelText: 'Preço'),
                keyboardType: TextInputType.number,
                validator: (value) => value!.isEmpty ? 'Informe o preço' : null,
              ),

              TextFormField(
                controller: quantidadeController,
                decoration: InputDecoration(labelText: 'Quantidade'),
                keyboardType: TextInputType.number,
                validator: (value) => value!.isEmpty ? 'Informe a quantidade' : null,
              ),

              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  TextButton(
                    child: Text('Cancelar'),
                    onPressed: () => Navigator.pop(context),
                  ),
                  ElevatedButton(
                    onPressed: () {
                      if (_formKey.currentState!.validate()) {
                        Navigator.pop(context);
                      }
                    },
                    child: Text('Criar Produto'),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final produtos = <Map<String, dynamic>>[];

    return Scaffold(
      body: produtos.isEmpty
          ? Center(child: Text('Nenhum produto cadastrado'))
          : ListView.builder(
              itemCount: produtos.length,
              itemBuilder: (context, index) {
                final produto = produtos[index];
                return ListTile(
                  title: Text(produto['nome']),
                  subtitle: Text('${produto['categoria']} - ${produto['marca']}'),
                  trailing: Text('Qtd: ${produto['quantidade']}'),
                );
              },
            ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: _abrirFormularioProduto,
        label: Text('Novo Produto'),
        icon: Icon(Icons.add),
      ),
    );
  }
}