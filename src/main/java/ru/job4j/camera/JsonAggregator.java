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
        String s = new JsonAggregator().getJsonVideoCameras(
                "http://www.mocky.io/v2/5c51b9dd3400003252129fb5");
        System.out.println(s);
    }

    public String getJsonVideoCameras(String url) {
        JSONArray jsonArray = new JSONArray(download(url));
        ExecutorService executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        CompletionService<JSONObject> completionService = new ExecutorCompletionService<>(executor);
        loadingTasks(jsonArray, completionService);
        JSONArray resultArray = getResultTasks(jsonArray.length(), completionService);
        executor.shutdown();
        return resultArray.toString();
    }

    private JSONArray getResultTasks(int arrayLength, CompletionService<JSONObject> service) {
        JSONArray resultArray = new JSONArray();
        for (int i = 0; i < arrayLength; i++) {
            try {
                resultArray.put(service.take().get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return resultArray;
    }

    private void loadingTasks(JSONArray jsonArray, CompletionService<JSONObject> service) {
        for (Object o : jsonArray) {
            service.submit(() -> {
                JSONObject camera = new JSONObject(o.toString());
                JSONObject sourceDataUrl = new JSONObject(
                        download(camera.get("sourceDataUrl").toString()));
                JSONObject tokenDataUrl = new JSONObject(
                        download(camera.get("tokenDataUrl").toString()));
                return new JSONObject()
                        .put("id", camera.get("id"))
                        .put("urlType", sourceDataUrl.get("urlType"))
                        .put("videoUrl", sourceDataUrl.get("videoUrl"))
                        .put("value", tokenDataUrl.get("value"))
                        .put("ttl", tokenDataUrl.get("ttl"));
            });
        }
    }

    private String download(String url) {
        String result = "";
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            result = out.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
