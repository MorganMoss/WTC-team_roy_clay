package za.co.wethinkcode.server.handler.world.map;

import za.co.wethinkcode.server.handler.world.entity.immovable.Immovable;

import java.util.List;

/**
 * Interface to represent a map. A World will be loaded with a Maze, and will delegate the work to check if a path is blocked by certain immovables etc to this maze instance.
 */
public interface Map {
    /**
     * @return the list of immovables, or an empty list if no immovables exist.
     */
    List<Immovable> getImmovables();
}
