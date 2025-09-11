package com.example.controller;

import java.net.URI;
import java.util.List;

import com.example.alunos.model.Aluno;
import com.example.alunos.repository.AlunoRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoRepository repo;

    public AlunoController(AlunoRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Aluno> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> obter(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Aluno> criar(@Valid @RequestBody Aluno aluno) {
        Aluno salvo = repo.save(aluno);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(salvo.getId()).toUri(); // certifique-se de que o método correto é getId()
        return ResponseEntity.created(uri).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizar(@PathVariable Long id, @Valid @RequestBody Aluno aluno ) {
        return repo.findById(id).map(existing -> {
            existing.setNome(aluno.getNome());
            existing.setEmail(aluno.getEmail());
            existing.setMatricula(aluno.getMatricula());
            existing.setDataNascimento(aluno.getDataNascimento());
            repo.save(existing);
            return ResponseEntity.ok(existing);

        }).orElse(ResponseEntity.notFound().build());
    }

    
}