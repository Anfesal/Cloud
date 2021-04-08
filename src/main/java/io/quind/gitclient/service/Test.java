package io.quind.gitclient.service;

import java.io.IOException;
import org.eclipse.jgit.api.errors.GitAPIException;


public class Test {

    public static void main(String[] args) throws IOException, GitAPIException {
        String localPath = "/home/andres/Documents/clonehere/Test2";
        String remotePath = "https://github.com/Anfesal/Lab2_Bootstrap.git";
        GitControl gc = new GitControl(localPath, remotePath);
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