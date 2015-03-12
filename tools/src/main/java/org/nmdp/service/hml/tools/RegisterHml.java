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

import org.nmdp.ngs.hml.HmlReader;

import org.nmdp.ngs.hml.jaxb.Hml;

import org.nmdp.service.hml.client.EndpointUrl;
import org.nmdp.service.hml.client.HmlService;
import org.nmdp.service.hml.client.HmlServiceModule;

import retrofit.client.Header;
import retrofit.client.Response;

/**
 * Register HML.
 */
public final class RegisterHml implements Callable<Integer> {
    private final File inputFile;
    private final File outputFile;
    private final HmlService hmlService;
    static final String DEFAULT_ENDPOINT_URL = "http://localhost:8080/";
    static final String USAGE = "register-hml -u " + DEFAULT_ENDPOINT_URL + " -i foo.xml.gz";

    /**
     * Create a new register HML callable with the specified endpoint URL, input file, and output file.
     *
     * @param endpointUrl endpoint URL, must not be null
     * @param inputFile input file, if any
     * @param outputFile output file, if any
     */
    public RegisterHml(final String endpointUrl, final File inputFile, final File outputFile) {
        checkNotNull(endpointUrl);

        Injector injector = Guice.createInjector(new HmlServiceModule(), new AbstractModule() {
                @Override
                protected void configure() {
                    bind(String.class).annotatedWith(EndpointUrl.class).toInstance(endpointUrl);
                }
            });

        this.inputFile = inputFile;
        this.outputFile = outputFile;
        hmlService = injector.getInstance(HmlService.class);
    }


    @Override
    public Integer call() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = reader(inputFile);
            writer = writer(outputFile);

            Hml hml = HmlReader.read(reader);
            Response response = hmlService.registerHml(hml);
            int status = response.getStatus();
            if (status == 201) {
                for (Header header : response.getHeaders()) {
                    if ("Location".equals(header.getName())) {
                        writer.println(header.getValue());
                    }
                }
                return 0;
            }
            return status;
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
            try {
                writer.close();
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
        FileArgument inputFile = new FileArgument("i", "input-file", "input file in HML format, default stdin", false);
        FileArgument outputFile = new FileArgument("o", "ouput-file", "output file of location URLs, default stdout", false);

        ArgumentList arguments = new ArgumentList(about, help, endpointUrl, inputFile, outputFile);
        CommandLine commandLine = new CommandLine(args);

        RegisterHml registerHml = null;
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

            registerHml = new RegisterHml(endpointUrl.getValue(DEFAULT_ENDPOINT_URL), inputFile.getValue(), outputFile.getValue());
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
            System.exit(registerHml.call());
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
