package yun.todo.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import yun.todo.domain.Todo;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    TodoRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAllInBatch();
    }

    @Test
    void findAll() {
        // given
        int quantity = 10;
        for (int i = 0; i < quantity; i++) {
            repository.save(createEntity());
        }

        // when
        List<Todo> todoList = repository.findAll();

        // then
        assertThat(todoList.size()).isEqualTo(quantity);
    }

    @Test
    void save() {
        // given
        Todo entity = createEntity();

        // when
        Todo savedEntity = repository.save(entity);

        // then
        assertThat(savedEntity.getId()).isNotNull().isPositive();
        assertThat(savedEntity.getId()).isEqualTo(entity.getId());

        assertThat(savedEntity.getDescription()).isEqualTo(entity.getDescription());

        assertThat(savedEntity.getDeadline()).isEqualTo(entity.getDeadline());
    }

    @Test
    void delete() {
        // given
        Todo entity = createEntity();
        Todo savedEntity = repository.save(entity);

        // when
        repository.delete(savedEntity);
        Long entityId = savedEntity.getId();

        // then
        assertThat(repository.findById(entityId)).isEmpty();
    }

    private Todo createEntity() {
        String description = "It's Test Todo.";
        int year = 2025;
        int month = 12;
        int dayOfMonth = 25;
        int hour = 12;
        int minute = 30;
        int second = 51;
        LocalDateTime deadline = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);

        Todo entity = Todo.builder().description(description).deadline(deadline).build();

        return entity;
    }
}