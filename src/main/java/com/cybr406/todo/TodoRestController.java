package com.cybr406.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController public class TodoRestController {
    @Autowired()
    InMemoryTodoRepository inMemoryTodoRepository;
    @Autowired()
    TodoJpaRepository todorep;
    @Autowired()
    TaskJpaRepository taskrep;


    @PostMapping("/todos")
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody Todo todoObject) {
       Todo created = todorep.save(todoObject);
       return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    @GetMapping ("/todos/{id}")
    public ResponseEntity<Todo> findTodo(@PathVariable long id){

        Optional<Todo> details = todorep.findById(id);

        if (details.isPresent()){
            Todo information = details.get();

            return new ResponseEntity<>(information, HttpStatus.OK);
        }

        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }
    @GetMapping("/todos")
    public Page<Todo> findAll(Pageable page)

    {
    return todorep.findAll(page);
    }

    @PostMapping("/todos/{id}/tasks")
    public ResponseEntity<Todo> addTask(@PathVariable long id, @RequestBody Task Task){

       Optional<Todo> addingTask = todorep.findById(id);

       if (addingTask.isPresent()){
           Todo todo = addingTask.get();
           todo.getTasks().add(Task);
           Task.setTodo(todo);
           taskrep.save(Task);
           return new ResponseEntity<>(todo, HttpStatus.CREATED);
       }

    else{
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
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
