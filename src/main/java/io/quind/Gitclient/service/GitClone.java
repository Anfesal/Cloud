package io.quind.Gitclient.service;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;


public class GitClone {
    public static void main(String[] args) throws  GitAPIException {
        Git.cloneRepository()
                .setURI("https://github.com/eclipse/jgit.git")
                .setDirectory(new File("/home/andres/Documents/clonehere"))
                .call();
    }
}
