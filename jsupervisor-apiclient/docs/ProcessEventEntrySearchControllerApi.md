# ProcessEventEntrySearchControllerApi

All URIs are relative to *http://localhost:8086*

| Method                                                                                                               | HTTP request                                             | Description |
|----------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------|-------------|
| [**executeSearchProcessevententryGet**](ProcessEventEntrySearchControllerApi.md#executeSearchProcessevententryGet)   | **GET** /api/v1/processEventEntries/search/byProcessName |             |
| [**executeSearchProcessevententryGet1**](ProcessEventEntrySearchControllerApi.md#executeSearchProcessevententryGet1) | **GET** /api/v1/processEventEntries/search/byStatus      |             |

## executeSearchProcessevententryGet

> PagedModelEntityModelProcessEventEntry executeSearchProcessevententryGet(processName, page, size, sort)

### Example

```java
// Import classes:

import org.alexmond.jsupervisor.client.invoker.ApiClient;
import org.alexmond.jsupervisor.client.invoker.ApiException;
import org.alexmond.jsupervisor.client.invoker.Configuration;
import org.alexmond.jsupervisor.client.invoker.models.*;
import org.alexmond.jsupervisor.client.api.ProcessEventEntrySearchControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8086");

        ProcessEventEntrySearchControllerApi apiInstance = new ProcessEventEntrySearchControllerApi(defaultClient);
        String processName = "processName_example"; // String | 
        Integer page = 0; // Integer | Zero-based page index (0..N)
        Integer size = 20; // Integer | The size of the page to be returned
        List<String> sort = Arrays.asList(); // List<String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
        try {
            PagedModelEntityModelProcessEventEntry result = apiInstance.executeSearchProcessevententryGet(processName, page, size, sort);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProcessEventEntrySearchControllerApi#executeSearchProcessevententryGet");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

| Name            | Type                                | Description                                   | Notes                                                                         |
|-----------------|-------------------------------------|-----------------------------------------------|-------------------------------------------------------------------------------|
| **processName** | **String**                          |                                               | [optional]                                                                    |
| **page**        | **Integer**                         | Zero-based page index (0..N)                  | [optional] [default to 0]                                                     |
| **size**        | **Integer**                         | The size of the page to be returned           | [optional] [default to 20]                                                    |
| **sort**        | [**List&lt;String&gt;**](String.md) | Sorting criteria in the format: property,(asc | desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional] |

### Return type

[**PagedModelEntityModelProcessEventEntry**](PagedModelEntityModelProcessEventEntry.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

### HTTP response details

| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200**     | OK          | -                |
| **404**     | Not Found   | -                |

## executeSearchProcessevententryGet1

> PagedModelEntityModelProcessEventEntry executeSearchProcessevententryGet1(status, page, size, sort)

### Example

```java
// Import classes:

import org.alexmond.jsupervisor.client.invoker.ApiClient;
import org.alexmond.jsupervisor.client.invoker.ApiException;
import org.alexmond.jsupervisor.client.invoker.Configuration;
import org.alexmond.jsupervisor.client.invoker.models.*;
import org.alexmond.jsupervisor.client.api.ProcessEventEntrySearchControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8086");

        ProcessEventEntrySearchControllerApi apiInstance = new ProcessEventEntrySearchControllerApi(defaultClient);
        String status = "not_started"; // String | 
        Integer page = 0; // Integer | Zero-based page index (0..N)
        Integer size = 20; // Integer | The size of the page to be returned
        List<String> sort = Arrays.asList(); // List<String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
        try {
            PagedModelEntityModelProcessEventEntry result = apiInstance.executeSearchProcessevententryGet1(status, page, size, sort);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProcessEventEntrySearchControllerApi#executeSearchProcessevententryGet1");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

| Name       | Type                                | Description                                   | Notes                                                                                                                                         |
|------------|-------------------------------------|-----------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| **status** | **String**                          |                                               | [optional] [enum: not_started, running, finished, unknown, failed, failed_to_start, stopped, stopping, aborted, starting, healthy, unhealthy] |
| **page**   | **Integer**                         | Zero-based page index (0..N)                  | [optional] [default to 0]                                                                                                                     |
| **size**   | **Integer**                         | The size of the page to be returned           | [optional] [default to 20]                                                                                                                    |
| **sort**   | [**List&lt;String&gt;**](String.md) | Sorting criteria in the format: property,(asc | desc). Default sort order is ascending. Multiple sort criteria are supported.                                                                 | [optional] |

### Return type

[**PagedModelEntityModelProcessEventEntry**](PagedModelEntityModelProcessEventEntry.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

### HTTP response details

| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200**     | OK          | -                |
| **404**     | Not Found   | -                |

