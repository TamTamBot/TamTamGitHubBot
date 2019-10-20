package com.github.testbot.github;

import chat.tamtam.botapi.client.impl.JacksonSerializer;
import chat.tamtam.botapi.exceptions.SerializationException;
import com.github.testbot.models.github.GitHubRepositoryModel;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.github.testbot.constans.GitHubConstants.*;

@Slf4j
@Component
public class CustomHttpClient {

    private final JacksonSerializer serializer;

    private final OkHttpClient client = new OkHttpClient();

    public CustomHttpClient(JacksonSerializer serializer) {
        this.serializer = serializer;
    }

    public GitHubRepositoryModel pingGithubRepo(final String fullName) throws IOException, SerializationException {

        final Request request = new Request.Builder().url(GIT_HUB_ROOT_API_URL + GIT_HUB_REPOS_URL + fullName).get().build();

        Response response = client.newCall(request).execute();
        log.info(response.toString());
        if (response.code() == HttpStatus.OK.value()) {
            if (response.body() != null) {
                return serializer.deserialize(response.body().string(), GitHubRepositoryModel.class);
            }
        }
        throw new IllegalStateException("Incorrect response code in " + response.toString());

    }
}

