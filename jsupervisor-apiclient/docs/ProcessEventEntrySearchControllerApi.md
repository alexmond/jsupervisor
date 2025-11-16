# ProcessEventEntrySearchControllerApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**executeSearchProcessevententryGet**](ProcessEventEntrySearchControllerApi.md#executeSearchProcessevententryGet) | **GET** /api/v1/events/search/byProcessName |  |
| [**executeSearchProcessevententryGet1**](ProcessEventEntrySearchControllerApi.md#executeSearchProcessevententryGet1) | **GET** /api/v1/events/search/byStatus |  |



## executeSearchProcessevententryGet

> CollectionModelEntityModelProcessEventEntry executeSearchProcessevententryGet(processName)



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
        defaultClient.setBasePath("http://localhost");

        ProcessEventEntrySearchControllerApi apiInstance = new ProcessEventEntrySearchControllerApi(defaultClient);
        String processName = "processName_example"; // String | 
        try {
            CollectionModelEntityModelProcessEventEntry result = apiInstance.executeSearchProcessevententryGet(processName);
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


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **processName** | **String**|  | [optional] |

### Return type

[**CollectionModelEntityModelProcessEventEntry**](CollectionModelEntityModelProcessEventEntry.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **404** | Not Found |  -  |


## executeSearchProcessevententryGet1

> CollectionModelEntityModelProcessEventEntry executeSearchProcessevententryGet1(status)



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
        defaultClient.setBasePath("http://localhost");

        ProcessEventEntrySearchControllerApi apiInstance = new ProcessEventEntrySearchControllerApi(defaultClient);
        String status = "not_started"; // String | 
        try {
            CollectionModelEntityModelProcessEventEntry result = apiInstance.executeSearchProcessevententryGet1(status);
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


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **status** | **String**|  | [optional] [enum: not_started, running, finished, unknown, failed, failed_to_start, stopped, stopping, aborted, starting, healthy, unhealthy] |

### Return type

[**CollectionModelEntityModelProcessEventEntry**](CollectionModelEntityModelProcessEventEntry.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **404** | Not Found |  -  |

