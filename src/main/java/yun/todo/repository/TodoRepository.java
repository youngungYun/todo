package yun.todo.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import yun.todo.domain.Todo;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Override
    @NonNull
    List<Todo> findAll();

    // 등록 및 수정
    @Override
    @NonNull
    <S extends Todo> S save(@NonNull S entity);

    @Override
    void deleteById(@NonNull Long id) throws EmptyResultDataAccessException;
}
