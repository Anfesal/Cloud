package io.quind.gitclient.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.util.io.DisabledOutputStream;


/**
 * Snippet which shows how to show diffs between two commits.
 *
 * @author dominik.stadler at gmx.at
 */
public class ShowChangedFilesBetweenCommits {

    public static void main(String[] args) throws IOException, GitAPIException {

        int linesAdded = 0;
        int linesDeleted = 0;
        int filesChanged = 0;
        try (Repository repo = CookbookHelper.openJGitCookbookRepository()) {
            final String[] list = new File(".").list();
            RevWalk rw = new RevWalk(repo);


            RevCommit commit = rw.parseCommit(repo.resolve("3667cf6ba57f4a89a7fdbfd555a9000ba9bcda48")); // Any ref will work here (HEAD, a sha1, tag, branch)
            RevCommit parent = rw.parseCommit(commit.getParent(0).getId());
            DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);

            df.setRepository(repo);
            df.setDiffComparator(RawTextComparator.DEFAULT);
            df.setDetectRenames(true);
            List<DiffEntry> diffs;
            diffs = df.scan(parent.getTree(), commit.getTree());
            filesChanged = diffs.size();

            if (list == null) {
                throw new IllegalStateException("Did not find any files at " + new File("filename.txt").getAbsolutePath());
            }
            final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd HH:mm");
            Git git = new Git(repo);
            Iterable<RevCommit> logs = git.log().call();
            int count = 0;
            System.out.println("---------------------------------------------------------------");
            for (RevCommit rev : logs) {
                System.out.println("Commit: " + rev.getAuthorIdent() + ", name: " + rev.getFullMessage() + ", id: " + rev.getId().getName());
                count++;
            }
            for (DiffEntry diff : diffs) {

                for (Edit edit : df.toFileHeader(diff).toEditList()) {
                    linesDeleted += edit.getEndA() - edit.getBeginA();
                    linesAdded += edit.getEndB() - edit.getBeginB();
                    System.out.println("Lines Added: " + linesAdded + " and" +" Lines Deleted: " +linesDeleted);
                }
            }
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
