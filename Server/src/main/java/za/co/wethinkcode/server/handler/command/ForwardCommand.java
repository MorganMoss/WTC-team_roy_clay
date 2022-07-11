package za.co.wethinkcode.server.handler.command;

import za.co.wethinkcode.server.Configuration;
import za.co.wethinkcode.server.handler.world.World;
import za.co.wethinkcode.server.handler.world.entity.Entity;
import za.co.wethinkcode.server.handler.world.entity.movable.Movable;

import java.awt.*;
import java.util.List;

public class ForwardCommand extends Movement {

    @Override
    public void setArguments(List<String> arguments) {
        super.setArguments(arguments);
    }

}

