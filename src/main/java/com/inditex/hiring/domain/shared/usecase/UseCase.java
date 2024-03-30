package com.inditex.hiring.domain.shared.usecase;

public interface UseCase<I, R> {

  R execute(I input);
}
