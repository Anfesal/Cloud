package io.quind.gitclient.service;


import java.io.IOException;
import org.eclipse.jgit.api.errors.GitAPIException;


public class GitClone {
    public static void main(String[] args) throws IOException,  GitAPIException {

        GitControl gc = new GitControl("/home/andres/Documents/clonehere/refac", "https://github.com/eclipse/jgit.git", "0");
        gc.cloneRepo();

    }
}
