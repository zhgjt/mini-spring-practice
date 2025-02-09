package org.example.context;

import java.util.EventObject;

public class ApplicationEvent extends EventObject {
    private static final long seriaVersionUID = 1L;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ApplicationEvent(Object source) {
        super(source);
    }
}
