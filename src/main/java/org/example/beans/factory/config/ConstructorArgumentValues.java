package org.example.beans.factory.config;

import java.util.ArrayList;
import java.util.List;

public class ConstructorArgumentValues {
    private final List<ConstructorArgumentValue> args = new ArrayList<>();

    public void addArgumentValue(ConstructorArgumentValue constructorArgumentValue) {
        args.add(constructorArgumentValue);
    }

    public ConstructorArgumentValue getIndexedArgumentValue(int i) {
        return args.get(i);
    }

    public int getArgumentCount() {
        return args.size();
    }

    public boolean isEmpty() {
        return args.isEmpty();
    }
}
