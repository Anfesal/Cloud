package io.quind.gitclient.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.api.ListBranchCommand;


public class GitControl {

    private String localPath;
    private String remotePath;
    private Repository repository;
    private Git git;
    private CredentialsProvider cp;
    private String name = "felipe.salcedoro@gmail.com";
    private String password = "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";

    public GitControl(String localPath, String remotePath, String type) throws IOException {
        this.localPath = localPath;
        this.remotePath = remotePath;
        this.repository = new FileRepository(localPath + "/.git");
        if (type.equals("1")){
            cp = new UsernamePasswordCredentialsProvider(this.name, this.password);
        }
        git = new Git(repository);
    }

    public void cloneRepo() throws  IOException, GitAPIException {
        Git.cloneRepository()
                .setURI(remotePath)
                .setCredentialsProvider(cp)
                .setDirectory(new File(localPath))
                .call();
        Iterable<RevCommit> logs = git.log()

                .call();
        int count = 0;
        for (RevCommit rev : logs) {
            System.out.println("Commit: " + rev.getAuthorIdent() + ", name: " + rev.getFullMessage() + ", id: " + rev.getFooterLines());
            count++;
        }
        System.out.println("Had " + count + " commits overall on current branch");
        logs = git.log()
                // for all log.all()
                .addPath("README.md")
                .call();
        count = 0;
        for (RevCommit rev : logs) {
            System.out.println("Commit: " + rev.getAuthorIdent() + ", name: " + rev.getFullMessage() + ", id: " + rev.getId().getName() + ", encoding: " + rev.getEncodingName() + ", encoding: " + rev.getCommitterIdent());
            count++;
        }
        System.out.println("Had " + count + " commits on README.md");


    }

    public void addToRepo() throws  GitAPIException {

            final String[] list = new File(".").list();
            if (list == null) {
                throw new IllegalStateException("Did not find any files at " + new File(".").getAbsolutePath());
            }

            final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd HH:mm");
            for (String file : list) {
                if (new File(file).isDirectory()) {
                    continue;
                }

                System.out.println("Blaming " + file);
                final BlameResult result = new Git(repository).blame().setFilePath(file)
                        .setTextComparator(RawTextComparator.WS_IGNORE_ALL).call();
                final RawText rawText = result.getResultContents();
                for (int i = 0; i < rawText.size(); i++) {
                    final PersonIdent sourceAuthor = result.getSourceAuthor(i);
                    final RevCommit sourceCommit = result.getSourceCommit(i);
                    System.out.println(sourceAuthor.getName() +
                            (sourceCommit != null ? " - " + DATE_FORMAT.format(((long)sourceCommit.getCommitTime())*1000) +
                                    " - " + sourceCommit.getName() : "") +
                            ": " + rawText.getString(i));
                }
            }

        //AddCommand add = git.add();
        //add.addFilepattern(".").call();
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