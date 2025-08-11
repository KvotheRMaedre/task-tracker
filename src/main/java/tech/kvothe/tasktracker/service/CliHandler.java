package tech.kvothe.tasktracker.service;

import tech.kvothe.tasktracker.entity.Status;

import java.nio.file.Path;

import static tech.kvothe.tasktracker.utils.Utils.printMsg;

public class CliHandler {

    private Path path;
    private String[] args;

    final TaskService taskService;

    public CliHandler(Path path, String[] args, TaskService taskService) {
        this.path = path;
        this.args = args;
        this.taskService = taskService;
    }

    public void handleInput(){
        if (args.length == 0) {
            printMsg("Command: java -cp bin tech/kvothe/tasktracker/TaskTrackerApplication <command> <args>");
            return;
        }

        switch (args[0]) {
            case "add":
                if (args.length < 2) {
                    printMsg("Command: java -cp bin tech/kvothe/tasktracker/TaskTrackerApplication add <description>");
                    break;
                }

                taskService.add(args[1]);
                break;
            case "update":
                if (args.length < 3) {
                    printMsg("Command: java -cp bin tech/kvothe/tasktracker/TaskTrackerApplication update <id> <description>");
                    break;
                }

                taskService.update(args[1], args[2]);
                break;
            case "delete":
                if (args.length < 2) {
                    printMsg("Command: java -cp bin tech/kvothe/tasktracker/TaskTrackerApplication delete <id>");
                    break;
                }

                taskService.delete(args[1]);
                break;
            case "mark-in-progress":
                if (args.length < 2) {
                    printMsg("Command: java -cp bin tech/kvothe/tasktracker/TaskTrackerApplication mark-in-progress <id>");
                    break;
                }

                taskService.changeStatus(args[1], Status.IN_PROGRESS);
                break;
            case "mark-done":
                if (args.length < 2) {
                    printMsg("Command: java -cp bin tech/kvothe/tasktracker/TaskTrackerApplication mark-done <id>");
                    break;
                }

                taskService.changeStatus(args[1], Status.DONE);
                break;
            case "list":

                if (args.length >= 2) {
                    switch (args[1]) {
                        case "done":
                            taskService.getByStatus(Status.DONE);
                            break;
                        case "in-progress":
                            taskService.getByStatus(Status.IN_PROGRESS);
                            break;
                        case "todo":
                            taskService.getByStatus(Status.TODO);
                            break;
                        default:
                            taskService.getAll();
                            break;
                    }

                } else {
                    taskService.getAll();
                }
                break;
            default:
                printMsg("Command: java -cp bin tech/kvothe/tasktracker/TaskTrackerApplication <command> <args>");

        }
        taskService.save();
    }
}
