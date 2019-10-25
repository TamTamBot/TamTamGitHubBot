# GithubNotifyBot

**GithubNotifyBot** is a simple bot to [TamTam](https://tamtam.chat/) to track activity in your remote GitHub 
repositories.

When one of the actions is performed in a remote repository, the bot will send you 
a *notification* in personal messages that an event of a certain type has occurred.

You can track:
* your personal repositories by setting appropriate webhooks on them
* any remote remote repositories that have a webhook installed for GithubNotifyBot

Supported GitHub actions for which notifications come:
* Comment to commit
* Create branch
* Push
* Pull request
* Pull request review
* Pull request review commit

Getting Started
---

You can find this bot on [TamTam]((https://tamtam.chat/)) by the name `@testGitBot` or by clicking on the [link](https://tt.me/testGitBot).

Getting Help
---

If you have any questions, see our **"[Wiki](https://github.com/TamTamBot/TamTamGitHubBot/wiki)"** page. This page provides 
instructions on setting up and installing the GitHub OAuth token to bot. If you did not find the answers to your 
questions, you can leave an issue and we will try to solve your problem.

Permissions
---

Our bot works using the GitHub Webhook API, so to connect to your repositories and install webhook, 
we need a token with scopes: `admin:repo_hook`, `admin:org_hook`. 

If you do not want to give the bot access to the 
webhooks control, you must pass the correct token, but without any scopes.

For information on how to create a GitHub OAuth token with the necessary scopes you can read the 
**"[Wiki](https://github.com/TamTamBot/TamTamGitHubBot/wiki)"**.

License
---

GithubNotifyBot is Open Source software released under the 
[Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html).
