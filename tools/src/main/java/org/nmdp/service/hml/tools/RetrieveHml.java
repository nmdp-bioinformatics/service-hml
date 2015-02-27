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
    private final String root;
    private final String extension;
    private final File inputFile;
    private final HmlService hmlService;
    static final String DEFAULT_ENDPOINT_URL = "http://localhost:8080/";
    static final String USAGE = "retrieve-hml -u " + DEFAULT_ENDPOINT_URL + " -i ids.txt.gz";

    private RetrieveHml(final String endpointUrl, final File inputFile, final String root, final String extension) {
        checkNotNull(endpointUrl);

        Injector injector = Guice.createInjector(new HmlServiceModule(), new AbstractModule() {
                @Override
                protected void configure() {
                    bind(String.class).annotatedWith(EndpointUrl.class).toInstance(endpointUrl);
                }
            });

        this.inputFile = inputFile;
        this.root = root;
        this.extension = extension;
        hmlService = injector.getInstance(HmlService.class);
    }

    /**
     * Create a new retrieve HML callable with the specified endpoint URL and hmlid element root and extension attributes.
     *
     * @param endpointUrl endpoint URL, must not be null
     * @param root hmlid element root attribute, must not be null
     * @param extension hmlid element extension attribute
     */
    public RetrieveHml(final String endpointUrl, final String root, final String extension) {
        this(endpointUrl, null, root, extension);
        checkNotNull(root);
   }

    /**
     * Create a new retrieve HML callable with the specified endpoint URL and input file.
     *
     * @param endpointUrl endpoint URL, must not be null
     * @param inputFile input file, if any
     */
    public RetrieveHml(final String endpointUrl, final File inputFile) {
        this(endpointUrl, inputFile, null, null);
    }


    @Override
    public Integer call() {
        BufferedReader reader = null;
        try {
            if (root != null) {
                String id = root;
                if (extension != null && extension.length() > 0) {
                    id = root + "/" + extension;
                }
                call(id);
            }
            else {
                reader = reader(inputFile);
                while (reader.ready()) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    call(line.trim());
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

    private void call(final String id) throws IOException {
        Hml hml = hmlService.getHml(id);
        try (PrintWriter writer = writer(new File(id.replace("/", "-") + ".hml.gz"))) {
            HmlWriter.write(hml, writer);
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
        StringArgument root = new StringArgument("r", "root", "hmlid element root attribute", false);
        StringArgument extension = new StringArgument("e", "extension", "hmlid element extension attribute", false);

        ArgumentList arguments = new ArgumentList(about, help, endpointUrl, inputFile, root, extension);
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

            if (root.wasFound()) {
                retrieveHml = new RetrieveHml(endpointUrl.getValue(DEFAULT_ENDPOINT_URL), root.getValue(), extension.getValue());
            }
            else {
                retrieveHml = new RetrieveHml(endpointUrl.getValue(DEFAULT_ENDPOINT_URL), inputFile.getValue());
            }
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
