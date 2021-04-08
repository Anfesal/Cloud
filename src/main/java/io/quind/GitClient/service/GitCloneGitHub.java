package io.quind.GitClient.service;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class GitCloneGitHub {
    public static void main(String[] args) throws InvalidRemoteException, TransportException, GitAPIException {
        Git.cloneRepository()
        .setURI( "https://github.com/Anfesal/Clase12.git" )
        .setCredentialsProvider( new UsernamePasswordCredentialsProvider( "felipe.salcedoro@gmail.com", "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" ) )
                .setDirectory(new File("/home/andres/Documents/clonehere/GitHub"))
                .call();
    }
}
