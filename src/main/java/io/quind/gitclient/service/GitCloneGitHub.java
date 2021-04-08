package io.quind.gitclient.service;


import org.eclipse.jgit.api.errors.GitAPIException;
import java.io.IOException;

public class GitCloneGitHub {
    public static void main(String[] args) throws  IOException, GitAPIException {

        GitControl gc = new GitControl("/home/andres/Documents/clonehere/jueves", "https://github.com/Anfesal/Lab2_Bootstrap.git", "1");
        gc.cloneRepo();
    }
}
