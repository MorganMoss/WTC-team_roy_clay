package za.co.wethinkcode.server.handler.command;

public class BackCommand extends Movement{
    @Override
    public void setRobot(String robot) {
        super.setRobot(robot);
        direction = direction.left().left();
    }
}
