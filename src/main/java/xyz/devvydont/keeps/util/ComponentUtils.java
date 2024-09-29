package xyz.devvydont.keeps.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ComponentUtils {

    /**
     * Removes the italics from a collection of components.
     *
     * @param components The components to clean.
     * @return The cleaned components.
     */
    public static List<Component> cleanItalics(Collection<Component> components) {
        var cleanComponents = new ArrayList<Component>(components.size());
        for (var component : components) {
            cleanComponents.add(component.decoration(TextDecoration.ITALIC, false));
        }
        return cleanComponents;
    }

}
