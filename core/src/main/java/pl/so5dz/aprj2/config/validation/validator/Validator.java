package pl.so5dz.aprj2.config.validation.validator;

import pl.so5dz.aprj2.config.validation.result.ValidationResult;

public interface Validator<T> {
    ValidationResult validate(T object);
}
