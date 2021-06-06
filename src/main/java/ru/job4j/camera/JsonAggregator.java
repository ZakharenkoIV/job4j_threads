package ru.job4j.camera;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class JsonAggregator {

    public static void main(String[] args) {
        String camerasJson = new JsonAggregator().getJsonVideoCameras(
                "http://www.mocky.io/v2/5c51b9dd3400003252129fb5");
        System.out.println(camerasJson);
    }

    public String getJsonVideoCameras(String url) {
        JSONArray camerasJson = new JSONArray(getResponse(url));
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        CompletionService<JSONObject> service = new ExecutorCompletionService<>(fixedThreadPool);
        CopyOnWriteArrayList<Future<JSONObject>> tasksList = getTasksList(
                camerasJson, service);
        JSONArray tasksResults = getTasksResults(tasksList);
        fixedThreadPool.shutdown();
        return tasksResults.toString();
    }

    private JSONArray getTasksResults(CopyOnWriteArrayList<Future<JSONObject>> futureList) {
        JSONArray tasksResults = new JSONArray();
        for (Future<JSONObject> jsonObjectFuture : futureList) {
            try {
                tasksResults.put(jsonObjectFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return tasksResults;
    }

    private CopyOnWriteArrayList<Future<JSONObject>> getTasksList(
            JSONArray camerasJson, CompletionService<JSONObject> service) {
        CopyOnWriteArrayList<Future<JSONObject>> tasksList = new CopyOnWriteArrayList<>();
        for (Object o : camerasJson) {
            Future<JSONObject> submit = service.submit(() -> {
                JSONObject camera = new JSONObject(o.toString());
                JSONObject sourceDataUrl = new JSONObject(
                        getResponse(camera.get("sourceDataUrl").toString()));
                JSONObject tokenDataUrl = new JSONObject(
                        getResponse(camera.get("tokenDataUrl").toString()));
                return new JSONObject()
                        .put("id", camera.get("id"))
                        .put("urlType", sourceDataUrl.get("urlType"))
                        .put("videoUrl", sourceDataUrl.get("videoUrl"))
                        .put("value", tokenDataUrl.get("value"))
                        .put("ttl", tokenDataUrl.get("ttl"));
            });
            tasksList.add(submit);
        }
        return tasksList;
    }

    private String getResponse(String url) {
        String response = "";
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            response = out.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
