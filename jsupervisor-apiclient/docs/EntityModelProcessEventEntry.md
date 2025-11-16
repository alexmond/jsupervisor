

# EntityModelProcessEventEntry


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **Long** |  |  [optional] |
|**pid** | **Long** |  |  [optional] |
|**eventTime** | **OffsetDateTime** |  |  [optional] |
|**processName** | **String** |  |  [optional] |
|**startTime** | **OffsetDateTime** |  |  [optional] |
|**endTime** | **OffsetDateTime** |  |  [optional] |
|**exitCode** | **Integer** |  |  [optional] |
|**newStatus** | [**NewStatusEnum**](#NewStatusEnum) |  |  [optional] |
|**oldStatus** | [**OldStatusEnum**](#OldStatusEnum) |  |  [optional] |
|**processUptime** | **String** |  |  [optional] |
|**links** | [**List&lt;Link&gt;**](Link.md) |  |  [optional] |



## Enum: NewStatusEnum

| Name | Value |
|---- | -----|
| NOT_STARTED | &quot;not_started&quot; |
| RUNNING | &quot;running&quot; |
| FINISHED | &quot;finished&quot; |
| UNKNOWN | &quot;unknown&quot; |
| FAILED | &quot;failed&quot; |
| FAILED_TO_START | &quot;failed_to_start&quot; |
| STOPPED | &quot;stopped&quot; |
| STOPPING | &quot;stopping&quot; |
| ABORTED | &quot;aborted&quot; |
| STARTING | &quot;starting&quot; |
| HEALTHY | &quot;healthy&quot; |
| UNHEALTHY | &quot;unhealthy&quot; |



## Enum: OldStatusEnum

| Name | Value |
|---- | -----|
| NOT_STARTED | &quot;not_started&quot; |
| RUNNING | &quot;running&quot; |
| FINISHED | &quot;finished&quot; |
| UNKNOWN | &quot;unknown&quot; |
| FAILED | &quot;failed&quot; |
| FAILED_TO_START | &quot;failed_to_start&quot; |
| STOPPED | &quot;stopped&quot; |
| STOPPING | &quot;stopping&quot; |
| ABORTED | &quot;aborted&quot; |
| STARTING | &quot;starting&quot; |
| HEALTHY | &quot;healthy&quot; |
| UNHEALTHY | &quot;unhealthy&quot; |



