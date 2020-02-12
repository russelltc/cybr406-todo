package com.cybr406.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController public class TodoRestController {
    @Autowired()
    InMemoryTodoRepository inMemoryTodoRepository;


    @PostMapping("/todos")
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody Todo todoObject) {
        Todo createdToDo = inMemoryTodoRepository.create(todoObject);
        if (todoObject.getAuthor().isEmpty() || todoObject.getDetails().isEmpty()) {
            return new ResponseEntity<>(createdToDo, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdToDo, HttpStatus.CREATED);
    }
    @GetMapping ("/todos/{todo}")
    public ResponseEntity<Todo> findTodo(@PathVariable long todo){

        Optional<Todo> details = inMemoryTodoRepository.find(todo);

        if (details.isPresent()){
            Todo information = details.get();

            return new ResponseEntity<>(information, HttpStatus.OK);
        }

        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }
    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> findall(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
    List<Todo> list = inMemoryTodoRepository.findAll(page, size);
    return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @PostMapping("/todos/{id}/tasks")
    public ResponseEntity<Todo> addTask(@PathVariable long id, @RequestBody Task Task){

        Todo todo = inMemoryTodoRepository.addTask(id, Task);

        return new ResponseEntity<>(todo, HttpStatus.CREATED);
    }
    @DeleteMapping("/todos/{id}")
    public ResponseEntity Delete(@PathVariable long id){
        try {
            inMemoryTodoRepository.delete(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        catch (NoSuchElementException err){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity DeleteTask(@PathVariable long id){
        try {
            inMemoryTodoRepository.deleteTask(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        catch (NoSuchElementException err){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }


}
