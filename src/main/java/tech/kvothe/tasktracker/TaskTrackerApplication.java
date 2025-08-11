package tech.kvothe.tasktracker;

import tech.kvothe.tasktracker.service.CliHandler;
import tech.kvothe.tasktracker.service.TaskService;

import java.io.IOException;
import java.nio.file.Path;

import static tech.kvothe.tasktracker.utils.Utils.printMsg;

public class TaskTrackerApplication {
    public static void main(String[] args) throws IOException {
        final Path FILE_PATH = Path.of("tasks.json");
        final TaskService taskService = new TaskService(FILE_PATH);

        CliHandler cliHandler = new CliHandler(FILE_PATH, args, taskService);
        cliHandler.handleInput();
    }
}