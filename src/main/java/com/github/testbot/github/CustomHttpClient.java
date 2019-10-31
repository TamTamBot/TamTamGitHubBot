package com.github.testbot.github;

import chat.tamtam.botapi.client.impl.JacksonSerializer;
import chat.tamtam.botapi.exceptions.SerializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.testbot.models.database.UserModel;
import com.github.testbot.models.github.GitHubCreateWebhook;
import com.github.testbot.models.github.GitHubRepositoryModel;
import com.github.testbot.models.github.GitHubWebhookConfig;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

import static com.github.testbot.constans.GitHubConstants.*;

@Slf4j
@Component
public class CustomHttpClient {

    @Value("${server.bot.url}")
    private String serverUrl;

    private final JacksonSerializer serializer;

    private final OkHttpClient client = new OkHttpClient();

    public CustomHttpClient(JacksonSerializer serializer) {
        this.serializer = serializer;
    }

    /**
     * Ping GitHub repository to verify the that the repository exists and the correct webhook exists.
     *
     * @param fullRepoName full repository name to subscribe
     * @return repository model. To more information see {@link GitHubRepositoryModel}
     * @throws IOException when errors occur when sending a request to Github
     * @throws SerializationException if response body can not be deserialize to {@link GitHubRepositoryModel}
     */

    public GitHubRepositoryModel pingGithubRepo(final String fullRepoName) throws IOException, SerializationException {

        final Request request = new Request.Builder().url(GIT_HUB_ROOT_API_URL + GIT_HUB_REPOS_URL + fullRepoName).get().build();

        Response response = client.newCall(request).execute();
        log.info(response.toString());
        if (response.code() == HttpStatus.OK.value()) {
            if (response.body() != null) {
                return serializer.deserialize(response.body().string(), GitHubRepositoryModel.class);
            }
        }
        throw new IllegalStateException("Incorrect response code in " + response.toString());
    }

    /**
     * @param userModel TamTamUser for check access token
     * @return is the token correct
     * @throws IOException if we can not send request to GitHub
     */
    public boolean checkAccessTokenForWebhooksOperations(final UserModel userModel) throws IOException {
        String credential = Credentials.basic(userModel.getGithubUserName(), userModel.getAccessToken());
        final Request request = new Request.Builder().url(GIT_HUB_ROOT_API_URL + "user")
                .get().header("Authorization", credential).build();
        Response response = client.newCall(request).execute();
        log.info(response.toString());
        if (response.code() == HttpStatus.OK.value()) {
            return true;
        } else {
            log.warn("Status code: {}", response.code());
            return false;
        }
    }

    public Optional<Long> addWebhookToRepo(final UserModel userModel, final String fullRepoName) throws IOException, SerializationException {
        String apiUrl = GIT_HUB_ROOT_API_URL + GIT_HUB_REPOS_URL + fullRepoName + "/hooks";
        GitHubWebhookConfig webhookConfig = new GitHubWebhookConfig(serverUrl + "/github", "json", "0");
        GitHubCreateWebhook createWebhook = new GitHubCreateWebhook("web", true,
                Collections.singletonList("*"), webhookConfig);
        String json = "";
        try {
            json = new String(Objects.requireNonNull(serializer.serialize(createWebhook)));
        } catch (SerializationException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), json);
        String credential = Credentials.basic(userModel.getGithubUserName(), userModel.getAccessToken());
        final Request request = new Request.Builder().url(apiUrl)
                .post(body).header("Authorization", credential).build();
        Response response = client.newCall(request).execute();
        Map responseData = serializer.deserialize(Objects.requireNonNull(response.body()).string(), Map.class);
        log.info(response.toString());
        if (response.code() == HttpStatus.CREATED.value()) {
            log.info("Webhook created successfully.");
            Long webhookId = Long.parseLong(String.valueOf(responseData.get("id")));
            return Optional.of(webhookId);
        } else if (response.code() == HttpStatus.UNPROCESSABLE_ENTITY.value()) {
            log.info("Webhook already created.");
            return Optional.empty();
        } else {
            log.warn("Status code: {}", response.code());
            return Optional.empty();
        }
    }

    public boolean deleteWebhookFromRepository(GitHubRepositoryModel repoModel) throws IOException {
        String deleteUrl = GIT_HUB_ROOT_API_URL + GIT_HUB_REPOS_URL + repoModel.getFullName() + "/hooks/" + repoModel.getWebhookId();
        String credential = Credentials.basic(repoModel.getOwner().getLogin(), repoModel.getAccessToken());
        final Request request = new Request.Builder().url(deleteUrl)
                .delete().header("Authorization", credential).build();
        Response response = client.newCall(request).execute();
        return response.code() == HttpStatus.NO_CONTENT.value();
    }
}

