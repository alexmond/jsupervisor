# ProcessStatusRest

Represents process status information

## Properties

| Name               | Type                          | Description                                                                                                                                     | Notes      |
|--------------------|-------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|------------|
| **name**           | **String**                    | Name of the process                                                                                                                             | [optional] |
| **status**         | [**StatusEnum**](#StatusEnum) | Current status of the process (e.g., running, stopped, failed)                                                                                  | [optional] |
| **pid**            | **Long**                      | Process ID assigned by the operating system                                                                                                     | [optional] |
| **startTime**      | **OffsetDateTime**            | Timestamp when the process was started                                                                                                          | [optional] |
| **endTime**        | **OffsetDateTime**            | Timestamp when the process ended                                                                                                                | [optional] |
| **exitCode**       | **Integer**                   | Process exit code                                                                                                                               | [optional] |
| **processRuntime** | **String**                    | Process runtime duration                                                                                                                        | [optional] |
| **processUptime**  | **String**                    | Formatted process uptime                                                                                                                        | [optional] |
| **stdoutLogfile**  | **String**                    | File path where the standard output (stdout) of the process will be logged. If not specified, stdout will be inherited from the parent process. | [optional] |
| **stderrLogfile**  | **String**                    | File path where the standard error (stderr) of the process will be logged. If not specified, stderr will be inherited from the parent process.  | [optional] |
| **failedErrorLog** | **String**                    | Error log content when process failed to start                                                                                                  | [optional] |
| **alive**          | **Boolean**                   |                                                                                                                                                 | [optional] |

## Enum: StatusEnum

| Name            | Value                       |
|-----------------|-----------------------------|
| NOT_STARTED     | &quot;not_started&quot;     |
| RUNNING         | &quot;running&quot;         |
| FINISHED        | &quot;finished&quot;        |
| UNKNOWN         | &quot;unknown&quot;         |
| FAILED          | &quot;failed&quot;          |
| FAILED_TO_START | &quot;failed_to_start&quot; |
| STOPPED         | &quot;stopped&quot;         |
| STOPPING        | &quot;stopping&quot;        |
| ABORTED         | &quot;aborted&quot;         |
| STARTING        | &quot;starting&quot;        |
| HEALTHY         | &quot;healthy&quot;         |
| UNHEALTHY       | &quot;unhealthy&quot;       |



