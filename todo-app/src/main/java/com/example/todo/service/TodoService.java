package com.example.todo.service;

import com.example.todo.entity.Todo;
import com.example.todo.entity.User;

import java.util.List;

/**
 * Todo に関するビジネスロジックのインターフェース
 */
public interface TodoService {
    /**
     * 全てのTodoを取得する
     */
    List<Todo> findAll();

    /**
     * IDに対応するTodoを取得する
     * @param id TodoのID
     */
    Todo findById(Long id);

    /**
     * Todoを保存または更新する
     * @param todo 保存するTodo
     */
    Todo save(Todo todo);

    /**
     * 指定されたIDのTodoを削除する
     * @param id 削除するTodoのID
     */
    void delete(Long id);

    /**
     * 指定ユーザーのTodo一覧を取得する
     * @param user ユーザー
     */
    List<Todo> findByUser(User user);
}
