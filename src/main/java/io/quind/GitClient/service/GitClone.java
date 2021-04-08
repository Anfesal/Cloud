package io.quind.GitClient.service;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

public class GitClone {
    public static void main(String[] args) throws InvalidRemoteException, TransportException, GitAPIException {
        Git.cloneRepository()
                .setURI("https://github.com/eclipse/jgit.git")
                .setDirectory(new File("/home/andres/Documents/clonehere"))
                .call();
    }
}
