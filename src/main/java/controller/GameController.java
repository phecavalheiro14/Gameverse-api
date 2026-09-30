package controller;

import model.Game;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/games")
public class GameController {

    private final List<Game> games = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);


    @PostMapping
    public ResponseEntity<Game> cadastrar(@RequestBody Game game) {
        game.setId(idCounter.getAndIncrement());
        games.add(game);
        return ResponseEntity.status(HttpStatus.CREATED).body(game);
    }

    @GetMapping
    public ResponseEntity<List<Game>> listarTodos(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String plataforma) {

        List<Game> resultado = games.stream().filter(g -> {
            boolean matchTitulo = (titulo == null || (g.getTitulo() != null && g.getTitulo().toLowerCase().contains(titulo.toLowerCase())));
            boolean matchGenero = (genero == null || (g.getGenero() != null && g.getGenero().equalsIgnoreCase(genero)));
            boolean matchPlataforma = (plataforma == null || (g.getPlataforma() != null && g.getPlataforma().equalsIgnoreCase(plataforma)));
            return matchTitulo && matchGenero && matchPlataforma;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> buscarPorId(@PathVariable Long id) {
        Optional<Game> game = games.stream()
                .filter(g -> g.getId().equals(id))
                .findFirst();

        return game.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Game> atualizar(@PathVariable Long id, @RequestBody Game gameAtualizado) {
        Optional<Game> gameExistente = games.stream()
                .filter(g -> g.getId().equals(id))
                .findFirst();

        if (gameExistente.isPresent()) {
            Game game = gameExistente.get();
            game.setTitulo(gameAtualizado.getTitulo());
            game.setGenero(gameAtualizado.getGenero());
            game.setPlataforma(gameAtualizado.getPlataforma());
            game.setPreco(gameAtualizado.getPreco());
            return ResponseEntity.ok(game);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        boolean removido = games.removeIf(g -> g.getId().equals(id));
        if (removido) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}