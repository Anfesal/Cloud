package io.quind.gitclient.service;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class GitCloneGitHub {
    public static void main(String[] args) throws  IOException, GitAPIException {

        GitControl gc = new GitControl("/home/andres/Documents/clonehere/jueves", "https://github.com/Anfesal/Lab2_Bootstrap.git", "1");
        gc.cloneRepo();

    }
}
