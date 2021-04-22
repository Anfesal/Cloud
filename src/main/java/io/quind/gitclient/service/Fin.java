package io.quind.gitclient.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Fin {
    public static void main(String[] args) throws IOException, GitAPIException {

        try (Repository repo = CookbookHelper.openJGitCookbookRepository()) {
            final String[] list = new File(".").list();
            if (list == null) {
                throw new IllegalStateException("Did not find any files at " + new File("filename.txt").getAbsolutePath());
            }
            RevWalk rw = new RevWalk(repo);
            Git git = new Git(repo);
            Iterable<RevCommit> logs = git.log().call();
            int count = 0;

            String mtrx[];
            mtrx = new String[20];
            String author[];
            author = new String[20];
            for (RevCommit rev : logs) {
                //System.out.println("Commit: " + rev.getAuthorIdent());
                author[count] = rev.getAuthorIdent().getName();
                mtrx[count] = rev.getId().getName();
                //linesDiff(mtrx[2]);
               // System.out.println("Commit: " + count+ " "+ mtrx[count]);
                count++;

            }
            for(int j = 0; j < 15; j++){
                linesDiff(mtrx[j], author[j]);
            }

        }
    }

    public static void linesDiff(String message, String author) throws JGitInternalException,  GitAPIException {
        int linesAdded = 0;
        int linesDeleted = 0;
        int filesChanged = 0;
        try (Repository repo = CookbookHelper.openJGitCookbookRepository()) {
            final String[] list = new File(".").list();
            if (list == null) {
                throw new IllegalStateException("Did not find any files at " + new File("filename.txt").getAbsolutePath());
            }


            RevWalk rw = new RevWalk(repo);
            Git git = new Git(repo);
            Iterable<RevCommit> logs = git.log().call();

                RevCommit commit = rw.parseCommit(repo.resolve(message)); // Any ref will work here (HEAD, a sha1, tag, branch)
                RevCommit parent = rw.parseCommit(commit.getParent(0).getId());
                DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);

                df.setRepository(repo);
                df.setDiffComparator(RawTextComparator.DEFAULT);
                df.setDetectRenames(true);
                for (RevCommit rev : logs) {
                    System.out.println("CommitAuthor: " + rev.getAuthorIdent());
                }
                List<DiffEntry> diffs;
                diffs = df.scan(parent.getTree(), commit.getTree());
                filesChanged = diffs.size();

                for (DiffEntry diff : diffs) {

                    for (Edit edit : df.toFileHeader(diff).toEditList()) {
                        linesDeleted += edit.getEndA() - edit.getBeginA();
                        linesAdded += edit.getEndB() - edit.getBeginB();
                    }

                }

            System.out.println("commit "+ message + " Lines Added: " + linesAdded + " and" +" Lines Deleted: " +linesDeleted + " " + author);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
