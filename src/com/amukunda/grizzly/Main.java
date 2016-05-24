package com.amukunda.grizzly;

import com.amukunda.grizzly.internal.model.Snapshot;
import com.amukunda.grizzly.internal.oql.OQLEngine;
import com.amukunda.grizzly.internal.oql.OQLException;
import com.amukunda.grizzly.internal.oql.ObjectVisitor;
import com.amukunda.grizzly.internal.parser.Reader;

import java.io.File;
import java.io.IOException;

public class Main {

    public static final String query1 = "select s.count from java.lang.String s";
    public static final String query2 = "var h = 'hello'; toHtml(h)";
    public static int cnt = 0;

    public static void main(String[] args) {
        System.out.println("Starting test!");

        String filename = "/home/amukundan/dump.hprof";
        try {
            File heapdump = new File(filename);
            Snapshot snapshot = Reader.readFile(heapdump.getAbsolutePath(), true, 0);
            snapshot.resolve(true);

            final OQLEngine engine = new OQLEngine(snapshot);
            engine.executeQuery(query1, new ObjectVisitor() {
                public boolean visit(Object object) {
                    cnt++;
                    try {
                        System.out.println(engine.toHtml(object));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("<pre>");
                        e.printStackTrace(System.out);
                        System.out.println("</pre>");
                    }
                    return false;
                }
            });
            System.out.println("end. cnt=" + cnt);
        } catch (OQLException exp) {
            System.out.println(exp.getMessage());
            exp.printStackTrace(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
