package com.github.senyast4745.testbot.github;

import com.github.senyast4745.testbot.constans.GitHubConstants;
import okhttp3.OkHttpClient;
import okhttp3.OkUrlFactory;
import okhttp3.Request;
import okhttp3.Response;
import org.kohsuke.github.GHAuthorization;
import org.kohsuke.github.GHIssueSearchBuilder;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.extras.OkHttp3Connector;
import org.kohsuke.github.extras.OkHttpConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;

public class CustomHttpClient {

    private volatile OkHttpClient client = new OkHttpClient();

    private Logger log = LoggerFactory.getLogger(CustomHttpClient.class);

    public boolean pingGithubRepo(String fullName) throws IOException {


        return true;

       /* Request request = new Request.Builder().url(GitHubConstants.GIT_HUB_ROOT_URL + "/repos" + fullName + "/hooks")
                .addHeader("User-Agent", "TamTam-GitHub Bot").build();
        try (Response response = client.newCall(request).execute()) {
            switch (response.code() / 100){
                case 2:
                    if(response.body() != null) {
                        log.info(response.body().string());
                        return true;
                    }
                    log.error("Code " + response.code());
                    break;
                case 4:
                case 5:
                    if(response.body() != null) {
                        log.error(response.body().string());
                    }
                    log.error("Code " + response.code());;
                    break;
                default:
                    if(response.body() != null) {
                        log.error(response.body().string());
                    }
                    log.error("Code " + response.code());;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //FIXME
        return true;*/
//        return false;
    }
}
