package io.quind.gitclient.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

// Simple example that shows how to get the Blame-information for a file
public class BlameFile {


    public static void main (String[] args) throws IOException, GitAPIException {

        try (Repository repo = CookbookHelper.openJGitCookbookRepository()) {
            final String[] list = new File(".").list();
            if (list == null) {
                throw new IllegalStateException("Did not find any files at " + new File("filename.txt").getAbsolutePath());
            }
            final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd HH:mm");
            for (String file : list) {
                if (new File(file).isDirectory()) {
                    continue;
                }

                System.out.println("Blaming " + file);

                final BlameResult result = new Git(repo).blame().setFilePath(file)
                        .setTextComparator(RawTextComparator.WS_IGNORE_ALL).call();
                final RawText rawText = result.getResultContents();

                    // use the following instead to list commits on a specific branch
                    //ObjectId branchId = repo.resolve("HEAD");
                   // Iterable<RevCommit> commits = git.log().add(branchId).call();


                    for (int i = 0; i < rawText.size(); i++) {
                        final PersonIdent sourceAuthor = result.getSourceAuthor(i);
                        final RevCommit sourceCommit = result.getSourceCommit(i);
                        //System.out.println("Had " + rawText.size() + " commits ");
                        System.out.println(sourceAuthor.getName() +
                                (sourceCommit != null ? " - " + DATE_FORMAT.format(((long)sourceCommit.getCommitTime())*1000) + " - " + sourceCommit.getFullMessage() +
                                        " - " + rawText.getLineDelimiter(): "" ) +
                                ": " + rawText.getString(i));
                    }
                }


        }
    }

}