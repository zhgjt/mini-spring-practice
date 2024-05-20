package org.example.context;

public class ContextRefreshedEvent extends ApplicationEvent {
    private static final long seriaVersionUID = 1L;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ContextRefreshedEvent(Object source) {
        super(source);
    }

    public String toString() {
        return "It's in ContextRefreshedEvent now!";
    }
}
