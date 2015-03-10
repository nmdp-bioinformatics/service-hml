/*

    hml-tools  HML service command line tools.
    Copyright (c) 2015 National Marrow Donor Program (NMDP)

    This library is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation; either version 3 of the License, or (at
    your option) any later version.

    This library is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
    License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this library;  if not, write to the Free Software Foundation,
    Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.

    > http://www.gnu.org/licenses/lgpl.html

*/
package org.nmdp.service.hml.tools;

import static com.google.common.base.Preconditions.checkNotNull;

import static org.dishevelled.compress.Readers.reader;
import static org.dishevelled.compress.Writers.writer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.concurrent.Callable;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import org.dishevelled.commandline.ArgumentList;
import org.dishevelled.commandline.CommandLine;
import org.dishevelled.commandline.CommandLineParseException;
import org.dishevelled.commandline.CommandLineParser;
import org.dishevelled.commandline.Switch;
import org.dishevelled.commandline.Usage;

import org.dishevelled.commandline.argument.FileArgument;
import org.dishevelled.commandline.argument.StringArgument;

import org.nmdp.ngs.hml.HmlWriter;

import org.nmdp.ngs.hml.jaxb.Hml;

import org.nmdp.service.hml.client.EndpointUrl;
import org.nmdp.service.hml.client.HmlService;
import org.nmdp.service.hml.client.HmlServiceModule;

/**
 * Retrieve HML.
 */
public final class RetrieveHml implements Callable<Integer> {
    private final File inputFile;
    private final HmlService hmlService;
    static final String DEFAULT_ENDPOINT_URL = "http://localhost:8080/";
    static final String USAGE = "retrieve-hml -u " + DEFAULT_ENDPOINT_URL + " -i ids.txt.gz\n\n   HML documents are written to files ./${root}-${extension}.xml.gz";


    /**
     * Create a new retrieve HML callable with the specified endpoint URL and input file.
     *
     * @param endpointUrl endpoint URL, must not be null
     * @param inputFile input file, if any
     */
    public RetrieveHml(final String endpointUrl, final File inputFile) {
        checkNotNull(endpointUrl);

        Injector injector = Guice.createInjector(new HmlServiceModule(), new AbstractModule() {
                @Override
                protected void configure() {
                    bind(String.class).annotatedWith(EndpointUrl.class).toInstance(endpointUrl);
                }
            });

        this.inputFile = inputFile;
        hmlService = injector.getInstance(HmlService.class);
    }


    @Override
    public Integer call() {
        BufferedReader reader = null;
        try {
            reader = reader(inputFile);
            while (reader.ready()) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                String tokens[] = line.trim().split("/");
                String root = tokens[0];
                String extension = tokens.length > 1 ? tokens[1] : null;

                Hml hml = hmlService.getHml(root, extension);

                StringBuilder sb = new StringBuilder(root);
                if (extension != null && extension.length() > 0) {
                    sb.append("-");
                    sb.append(extension);
                }
                sb.append(".hml.gz");

                try (PrintWriter writer = writer(new File(sb.toString()))) {
                    HmlWriter.write(hml, writer);
                }
            }
            return 0;
        }
        catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
        finally {
            try {
                reader.close();
            }
            catch (Exception e) {
                // ignore
            }
        }
    }


    /**
     * Main.
     *
     * @param args command line arguments
     */
    public static void main(final String args[]) {
        Switch about = new Switch("a", "about", "display about message");
        Switch help = new Switch("h", "help", "display help message");
        StringArgument endpointUrl = new StringArgument("u", "endpoint-url", "endpoint URL, default " + DEFAULT_ENDPOINT_URL, false);
        FileArgument inputFile = new FileArgument("i", "input-file", "input file of HML ids (root or root/extension), one per line, default stdin", false);

        ArgumentList arguments = new ArgumentList(about, help, endpointUrl, inputFile);
        CommandLine commandLine = new CommandLine(args);

        RetrieveHml retrieveHml = null;
        try
        {
            CommandLineParser.parse(commandLine, arguments);
            if (about.wasFound()) {
                About.about(System.out);
                System.exit(0);
            }
            if (help.wasFound()) {
                Usage.usage(USAGE, null, commandLine, arguments, System.out);
                System.exit(0);
            }
            retrieveHml = new RetrieveHml(endpointUrl.getValue(DEFAULT_ENDPOINT_URL), inputFile.getValue());
        }
        catch (CommandLineParseException e) {
            Usage.usage(USAGE, e, commandLine, arguments, System.err);
            System.exit(-1);
        }
        catch (IllegalArgumentException e) {
            Usage.usage(USAGE, e, commandLine, arguments, System.err);
            System.exit(-1);
        }
        try {
            System.exit(retrieveHml.call());
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
