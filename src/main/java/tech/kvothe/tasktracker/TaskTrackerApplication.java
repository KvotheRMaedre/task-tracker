package tech.kvothe.tasktracker;

//javac -sourcepath src -d bin .\src\main\java\tech\kvothe\tasktracker\TaskTrackerApplication.java
//java -cp bin tech/kvothe/tasktracker/TaskTrackerApplication

import tech.kvothe.tasktracker.entity.Status;
import tech.kvothe.tasktracker.service.TaskService;

import java.io.IOException;
import java.nio.file.Path;

import static tech.kvothe.tasktracker.utils.Utils.printMsg;

public class TaskTrackerApplication {
    public static void main(String[] args) throws IOException {
        final Path FILE_PATH = Path.of("tasks.json");
        final TaskService taskService = new TaskService(FILE_PATH);

        String[] cliCommands = {"mark-done", "1"};

        if (cliCommands.length == 0){
            printMsg("Command: java -cp bin tech/kvothe/tasktracker/TaskTrackerApplication <command> <args>");
            return;
        }

        switch (cliCommands[0]) {
            case "add":
                if (cliCommands.length < 2){
                    printMsg("Command: java -cp bin tech/kvothe/tasktracker/TaskTrackerApplication add <description>");
                    break;
                }

                taskService.add(cliCommands[1]);
                break;
            case "update":
                if (cliCommands.length < 3){
                    printMsg("Command: java -cp bin tech/kvothe/tasktracker/TaskTrackerApplication update <id> <description>");
                    break;
                }

                taskService.update(cliCommands[1],cliCommands[2]);
                break;
            case "delete":
                if (cliCommands.length < 2){
                    printMsg("Command: java -cp bin tech/kvothe/tasktracker/TaskTrackerApplication delete <id>");
                    break;
                }

                taskService.delete(cliCommands[1]);
                break;
            case "mark-in-progress":
                if (cliCommands.length < 2){
                    printMsg("Command: java -cp bin tech/kvothe/tasktracker/TaskTrackerApplication mark-in-progress <id>");
                    break;
                }

                taskService.changeStatus(cliCommands[1], Status.IN_PROGRESS);
                break;
            case "mark-done":
                if (cliCommands.length < 2){
                    printMsg("Command: java -cp bin tech/kvothe/tasktracker/TaskTrackerApplication mark-done <id>");
                    break;
                }

                taskService.changeStatus(cliCommands[1], Status.DONE);
                break;
            default:
                printMsg("Command: java -cp bin tech/kvothe/tasktracker/TaskTrackerApplication <command> <args>");

        }

        taskService.save();
    }


}