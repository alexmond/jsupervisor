# ProcessEventEntryRequestBody

Represents an event entry for process state changes and lifecycle events

## Properties

| Name              | Type                                | Description                                             | Notes      |
|-------------------|-------------------------------------|---------------------------------------------------------|------------|
| **id**            | **Long**                            | Unique identifier for the process event                 | [optional] |
| **pid**           | **Long**                            | Process ID of the running process                       | [optional] |
| **eventTime**     | **OffsetDateTime**                  | Timestamp when the event occurred                       | [optional] |
| **processName**   | **String**                          | Name of the managed process                             | [optional] |
| **startTime**     | **OffsetDateTime**                  | Timestamp when the process was started                  | [optional] |
| **endTime**       | **OffsetDateTime**                  | Timestamp when the process ended, null if still running | [optional] |
| **exitCode**      | **Integer**                         | Exit code of the process, null if still running         | [optional] |
| **newStatus**     | [**NewStatusEnum**](#NewStatusEnum) | New status of the process after the event               | [optional] |
| **oldStatus**     | [**OldStatusEnum**](#OldStatusEnum) | Previous status of the process before the event         | [optional] |
| **processUptime** | **String**                          | Duration for which the process has been running         | [optional] |

## Enum: NewStatusEnum

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

## Enum: OldStatusEnum

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



