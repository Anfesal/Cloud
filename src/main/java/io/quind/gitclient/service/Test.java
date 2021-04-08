package io.quind.gitclient.service;

import java.io.IOException;
import org.eclipse.jgit.api.errors.GitAPIException;


public class Test {

    public static void main(String[] args) throws IOException, GitAPIException {

        GitControl gc = new GitControl("/home/andres/Documents/clonehere/Test2", "https://github.com/Anfesal/Lab2_Bootstrap.git", "1");
        //Clone repository
        gc.cloneRepo();
        //Add files to repository
        gc.addToRepo();
        //Commit with a custom message
        gc.commitToRepo("Modified testfile.txt");
        //Push commits
        gc.pushToRepo();
        //Pull
        gc.pullFromRepo();
    }

}