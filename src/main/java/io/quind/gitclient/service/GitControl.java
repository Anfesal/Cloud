package io.quind.gitclient.service;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;


public class GitControl {

    private String localPath;
    private String remotePath;
    private Repository localRepo;
    private Git git;
    private CredentialsProvider cp;
    private String name = "felipe.salcedoro@gmail.com";
    private String password = "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";

    public GitControl(String localPath, String remotePath) throws IOException {
        this.localPath = localPath;
        this.remotePath = remotePath;
        this.localRepo = new FileRepository(localPath + "/.git");
        cp = new UsernamePasswordCredentialsProvider(this.name, this.password);
        git = new Git(localRepo);
    }

    public void cloneRepo() throws  GitAPIException {
        Git.cloneRepository()
                .setURI(remotePath)
                .setCredentialsProvider(cp)
                .setDirectory(new File(localPath))
                .call();
    }

    public void addToRepo() throws  GitAPIException {
        AddCommand add = git.add();
        add.addFilepattern(".").call();
    }

    public void commitToRepo(String message) throws JGitInternalException,  GitAPIException {
             git.commit().setMessage(message).call();
    }

    public void pushToRepo() throws  JGitInternalException, GitAPIException {
        PushCommand pc = git.push();
        pc.setCredentialsProvider(cp)
                .setForce(true)
                .setPushAll();
        try {
            Iterator<PushResult> it = pc.call().iterator();
            if (it.hasNext()) {
                System.out.println(it.next().toString());
            }
        } catch (InvalidRemoteException e) {
            e.printStackTrace();
        }
    }

    public void pullFromRepo() throws GitAPIException {

        git.pull().call();
    }

}