package za.co.wethinkcode.robotworlds;

import java.util.ArrayList;

public class ReplayCommand extends Command{

    @Override
    public boolean execute(Robot target) {

        int steps = 0;
        int historyLength = target.getHistory().size();
        String args = getArgument();



        if (args.length()==0){
        //normal replay
        for (int i = 0; i < historyLength; i++) {
            String tempInstruction= target.getHistory().get(i);
            target.handleCommand(Command.create(tempInstruction));
            steps ++;
            target.getPrint += "\n"+target;
        }
        }

        else if (args.length()==1){
            //replay last number
            int num=Integer.parseInt(getArgument());
            int histLength = target.getHistory().size()-num;
            for (int i = histLength; i < historyLength; i++) {
                String tempInstruction= target.getHistory().get(i);

                target.handleCommand(Command.create(tempInstruction));
                steps ++;
                target.getPrint += "\n"+target;
            }
            }

        else if (args.length() == 3) {
            //replay number - number
            String[] nums = getArgument().split("-");
            int num1 = Integer.parseInt(nums[0]);
            int num2 = Integer.parseInt(nums[1]);
            int histLength = target.getHistory().size() - num1;
            for (int i = histLength; i < historyLength - num2; i++) {
                String tempInstruction = target.getHistory().get(i);
                target.handleCommand(Command.create(tempInstruction));
                steps++;
                target.getPrint += "\n" + target;
            }
        }

        else if (args.contains("reversed") ) {
            args = args.replace("reversed", "").trim();

            ArrayList<String> reversedList = new ArrayList<>();
            int count=0;

            for (int i = historyLength - 1; i >= 0; i--) {
                reversedList.add(count,target.getHistory().get(i));
                count++;}

            if (args.length() == 0) {
                //replay reversed
                for (int i = historyLength - 1; i >= 0; i--) {
                    String tempInstruction = target.getHistory().get(i);
                    target.handleCommand(Command.create(tempInstruction));
                    steps++;
                    target.getPrint += "\n" + target;
                }
            }

            else if (args.length() <= 2) {
                //replay reversed last number
                int num=Integer.parseInt(args);
                int histLength = reversedList.size()-num;

                for (int i = histLength-1; i < histLength+1; i++) {
                    String tempInstruction= reversedList.get(i);

                    target.handleCommand(Command.create(tempInstruction));
                    steps ++;
                    if (i != historyLength){
                        target.getPrint += "\n"+target;
                    }
                }
            }

            else if (args.length() == 3) {
                //replay reversed number - number
                String[] nums = args.split("-");

                int num1 = Integer.parseInt(nums[0]);
                int num2 = Integer.parseInt(nums[1]);
                int numTotal=num1-num2;
                int histLength = reversedList.size();

                    for (int i = histLength-numTotal; i < historyLength; i++) {
                    String tempInstruction = reversedList.get(i);
                    target.handleCommand(Command.create(tempInstruction));
                    steps++;
                        target.getPrint += "\n" + target;
                    }
            }
        }

        target.setStatus("replayed "+steps+" commands.");
        return true;
    }

    public ReplayCommand(String arg) {
        super("replay", arg);
    }
}


