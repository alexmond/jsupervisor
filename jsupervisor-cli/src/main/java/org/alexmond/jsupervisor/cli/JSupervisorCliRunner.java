package org.alexmond.jsupervisor.cli;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.client.api.ProcessManagementApi;
import org.alexmond.jsupervisor.client.invoker.ApiClient;
import org.alexmond.jsupervisor.client.invoker.ApiException;
import org.alexmond.jsupervisor.client.invoker.Configuration;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Spring Boot Application Runner that handles YAML/JSON schema validation.
 * This runner processes command line arguments, validates input files against JSON schemas,
 * and outputs validation results in various formats (JSON, YAML, JUnit, or colored text).
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JSupervisorCliRunner implements ApplicationRunner {

    private final Environment environment;

    /**
     * Executes the validation process when the application starts.
     * Handles command line arguments, validates configuration, processes input files,
     * and outputs results in the specified format.
     *
     * @param args Application arguments containing validation options and file paths
     */
    @Override
    public void run(ApplicationArguments args) {
        if (environment.matchesProfiles("test")) {
            return;
        }
        String serverUrl = "http://localhost:8086";
        String processName = "testapp1";
        String command = args.getNonOptionArgs().get(0);

        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8086");

        ProcessManagementApi processManagementApi = new ProcessManagementApi(defaultClient);

        try{
            switch (command) {
                case "start" ->  processManagementApi.startProcess("testapp1");
                case "stop" -> processManagementApi.stopProcess("testapp1");
                case "restart" -> processManagementApi.restartProcess("testapp1");
                case "help" -> printHelp();
                default -> printHelp();
            }
        } catch (ApiException e) {
            System.err.println("Exception when calling BulkProcessOperationsApi#restartAll");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }


    /**
     * Displays usage instructions and available command line options.
     * Exits the application with status code 0 after printing the help message.
     */
    private void printHelp() {
        String helpText = """
                Usage: java -jar yaml-schema-validator.jar [options] <file1> <file2> ...
                
                Options:
                  --help                               Show this help message
                  --schema=<path>                      Path to the JSON schema file (required unless schema-override is false)
                  --schema-override=<true|false>       If set, uses --schema instead of $schema from YAML/JSON
                  --report-type=<type>                 Output format: text (default), json, yaml, junit
                  --report-file-name=<name>            Write report to the given file (prints to stdout if not set)
                  --http-timeout=<dur>                 HTTP timeout for fetching remote schemas (e.g., 10s, 2m). Default: 10s
                  --ignore-ssl-errors=<true|false>     Ignore SSL certificate validation errors when fetching schemas
                  --color=<true|false>                 Use ANSI colors in text output (default: enabled)
                """;
        System.out.println(helpText);
    }
}