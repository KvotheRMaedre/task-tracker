package tech.kvothe.tasktracker.service;

import tech.kvothe.tasktracker.entity.Status;
import tech.kvothe.tasktracker.entity.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static tech.kvothe.tasktracker.utils.Utils.printMsg;

public class TaskService {

    private final Path path;
    private Long sequenceId = 1L;
    private List<Task> listTask = new ArrayList<>();

    public TaskService(Path path) {
        this.path = path;
        initiateSaveFile();
        loadTasks();
    }

    public void initiateSaveFile() {
        try {
            if(!Files.exists(path)){
                Files.createFile(path);
            }
        } catch (IOException e) {
            System.out.println("Error: Error creating the save file," + e.getMessage());
        }
    }

    private void loadTasks() {
        try {
            if (Files.exists(path)) {
                String jsonContent = Files.readString(path);
                if (!jsonContent.isBlank()){
                    String[] taskList = jsonContent.replace("[", "")
                            .replace("]", "")
                            .split("},");
                    for (String taskJson : taskList){
                        if (!taskJson.endsWith("}")){
                            taskJson = taskJson + "}";
                            listTask.add(createTaskFromJson(taskJson));
                        } else {
                            listTask.add(createTaskFromJson(taskJson));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void add(String description) {
        var task = new Task(
                sequenceId,
                description,
                Status.TODO,
                Instant.now(),
                Instant.now()
        );

        listTask.add(task);
        printMsg("Task added successfully (ID: "+ task.getId() +")");
    }

    public void update(String id, String description) {
        var task = listTask.stream()
                .filter(t -> t.getId() == Long.parseLong(id))
                .findFirst();

        if (task.isEmpty()) {
            printMsg("Theres no task with this id: " + id);
            return;
        }

        task.get().setDescription(description);
        task.get().setUpdatedAt(Instant.now());
        printMsg("Task updated successfully (ID: "+ id +")");
    }

    public void changeStatus(String id, Status status) {
        var task = listTask.stream()
                .filter(t -> t.getId() == Long.parseLong(id))
                .findFirst();

        if (task.isEmpty()) {
            printMsg("Theres no task with this id: " + id);
            return;
        }

        task.get().setStatus(status);
        task.get().setUpdatedAt(Instant.now());
        printMsg("Task updated successfully (ID: "+ id +")");
    }

    public void delete(String id) {
        var task = listTask.stream()
                .filter(t -> t.getId() == Long.parseLong(id))
                .findFirst();

        if (task.isEmpty()) {
            printMsg("Theres no task with this id: " + id);
            return;
        }

        listTask.remove(task.get());
        printMsg("Task deleted successfully (ID: "+ id +")");
    }

    public void getAll() {
        if (listTask.isEmpty()) {
            printMsg("There is no tasks at the moment!");
            return;
        }

        System.out.println("List of all tasks:");
        listTask.forEach(task -> {
            printMsg(createJsonFromTask(task));
        });
    }

    public void save() {
        if (listTask.isEmpty())
            return;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[\n");
        for (int i = 0; i < listTask.size(); i++){
            stringBuilder.append(createJsonFromTask(listTask.get(i)));
            if (i < listTask.size() - 1){
                stringBuilder.append(",\n");
            }
        }
        stringBuilder.append("\n]");

        String jsonContent = stringBuilder.toString();
        try {
            FileWriter file = new FileWriter(path.toString());
            file.write(jsonContent);
            file.close();
            //Files.writeString(path, jsonContent);
        } catch (IOException e){
            System.out.println("Error ao salver dados: " + e.getMessage());
        }
    }

    public Task createTaskFromJson(String json) {
        json = json.replace("{", "").replace("}", "").replace("\"", "");
        String[] taskFields = json.split(",");

        String id = taskFields[0].split(":")[1].strip();
        String description = taskFields[1].split(":")[1].strip();
        String statusString = taskFields[2].split(":")[1].strip();
        String createdAtStr = taskFields[3].split("[a-z]:")[1].strip();
        String updatedAtStr = taskFields[4].split("[a-z]:")[1].strip();

        Status status = Status.valueOf(statusString.toUpperCase().replace("-", "_"));

        Task task = new Task(
                Long.parseLong(id),
                description,
                status,
                Instant.parse(createdAtStr),
                Instant.parse(updatedAtStr)
        );

        if (task.getId() >= sequenceId) {
            sequenceId = task.getId() + 1;
        }

        return task;
    }

    public String createJsonFromTask(Task task) {
        return "{\n" +
                "\t\"id\": \"" + task.getId() + "\",\n" +
                "\t\"description\": \"" + task.getDescription() + "\",\n" +
                "\t\"status\": \"" + task.getStatus().getDescription() + "\",\n" +
                "\t\"createdAt\": \"" + task.getCreatedAt() + "\",\n" +
                "\t\"updatedAt\": \"" + task.getUpdatedAt() + "\"\n" +
                "}";
    }
}
