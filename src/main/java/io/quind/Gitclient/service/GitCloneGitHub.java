package io.quind.Gitclient.service;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class GitCloneGitHub {
    public static void main(String[] args) throws  GitAPIException {
        Git.cloneRepository()
        .setURI( "https://github.com/Anfesal/Lab2_Bootstrap.git" )
        .setCredentialsProvider( new UsernamePasswordCredentialsProvider( "felipe.salcedoro@gmail.com", "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" ) )
                .setDirectory(new File("/home/andres/Documents/clonehere/jueves"))
                .call();
    }
}
