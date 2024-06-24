/**
 * @file Generator.java
 * @author Ivan Burlustkyi
 */
package ija.project.generator;

import lombok.NonNull;

/**
 * Represents a generator interface.
 */
public interface Generator<T> {
    @NonNull
    T nextValue();
}
