package io.quind.gitclient.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Lines {
    public static void main(String[] args) throws IOException, GitAPIException {
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
            int count = 0;

            String mtrx[];
            mtrx = new String[20];
            for (RevCommit rev : logs) {
                System.out.println("Commit: " + rev.getAuthorIdent());
                mtrx[count] = rev.getId().getName();
                count++;

            }
            for (int m= 0; m < count; m++) {
                System.out.println(mtrx[m]);
            }

            String prb[];
            prb = new String[1];
            prb[0] = "019042651e121eb2d3ee26b68947184111aabe20";
            System.out.println("eeeeeeeeeeeeeeee " + prb[0]);


            RevCommit commit = rw.parseCommit(repo.resolve("019042651e121eb2d3ee26b68947184111aabe20")); // Any ref will work here (HEAD, a sha1, tag, branch)
            RevCommit parent = rw.parseCommit(commit.getParent(0).getId());
            DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);

            df.setRepository(repo);
            df.setDiffComparator(RawTextComparator.DEFAULT);
            df.setDetectRenames(true);
            List<DiffEntry> diffs;
            diffs = df.scan(parent.getTree(), commit.getTree());
            filesChanged = diffs.size();

            for (DiffEntry diff : diffs) {

                for (Edit edit : df.toFileHeader(diff).toEditList()) {
                    linesDeleted += edit.getEndA() - edit.getBeginA();
                    linesAdded += edit.getEndB() - edit.getBeginB();
                }

            }

            System.out.println("Lines Added: " + linesAdded + " and" +" Lines Deleted: " +linesDeleted);
        }
    }


}
