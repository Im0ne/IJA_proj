/**
 * @file LongGeneratorSingleton.java
 * @author Ivan Burlustkyi
 */
package ija.project.generator;

import lombok.NonNull;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a singleton generator of Long values.
 * This class provides a thread-safe mechanism to generate a sequence of unique Long values
 * starting from a given initial value or zero if not specified. The sequence is incremented
 * by one for each call to {@code nextValue()}.
 */
public class LongGeneratorSingleton implements Generator<Long> {
    
    /**
     * The singleton instance of the LongGeneratorSingleton class.
     * Volatile to ensure thread-safe instantiation.
     */
    private static volatile LongGeneratorSingleton instance;
    
    /**
     * The current value of the generator, wrapped in an AtomicLong for thread safety.
     */
    @NonNull
    private final AtomicLong value;

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the generator with the specified initial value.
     *
     * @param value The initial value of the generator.
     */
    private LongGeneratorSingleton(@NonNull Long value) {
        this.value = new AtomicLong(value);
    }

    /**
     * Provides access to the singleton instance of the LongGeneratorSingleton class.
     * If the instance does not exist, it is created. This method is thread-safe.
     *
     * @return The singleton instance of the LongGeneratorSingleton.
     */
    public static LongGeneratorSingleton getInstance() {
        if (instance == null) {
            synchronized (LongGeneratorSingleton.class) {
                if (instance == null) {
                    instance = new LongGeneratorSingleton(0L);
                }
            }
        }
        return instance;
    }

    /**
     * Generates the next unique Long value in the sequence.
     * This method is thread-safe.
     *
     * @return The next unique Long value.
     */
    @Override
    public @NonNull Long nextValue() {
        return value.incrementAndGet();
    }
}
