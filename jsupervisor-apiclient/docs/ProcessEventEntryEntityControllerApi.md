# ProcessEventEntryEntityControllerApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getCollectionResourceProcessevententryGet**](ProcessEventEntryEntityControllerApi.md#getCollectionResourceProcessevententryGet) | **GET** /api/v1/events |  |
| [**getItemResourceProcessevententryGet**](ProcessEventEntryEntityControllerApi.md#getItemResourceProcessevententryGet) | **GET** /api/v1/events/{id} |  |



## getCollectionResourceProcessevententryGet

> PagedModelEntityModelProcessEventEntry getCollectionResourceProcessevententryGet(page, size, sort)



get-processevententry

### Example

```java
// Import classes:
import org.alexmond.jsupervisor.client.invoker.ApiClient;
import org.alexmond.jsupervisor.client.invoker.ApiException;
import org.alexmond.jsupervisor.client.invoker.Configuration;
import org.alexmond.jsupervisor.client.invoker.models.*;
import org.alexmond.jsupervisor.client.api.ProcessEventEntryEntityControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost");

        ProcessEventEntryEntityControllerApi apiInstance = new ProcessEventEntryEntityControllerApi(defaultClient);
        Integer page = 0; // Integer | Zero-based page index (0..N)
        Integer size = 20; // Integer | The size of the page to be returned
        List<String> sort = Arrays.asList(); // List<String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
        try {
            PagedModelEntityModelProcessEventEntry result = apiInstance.getCollectionResourceProcessevententryGet(page, size, sort);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProcessEventEntryEntityControllerApi#getCollectionResourceProcessevententryGet");
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
| **page** | **Integer**| Zero-based page index (0..N) | [optional] [default to 0] |
| **size** | **Integer**| The size of the page to be returned | [optional] [default to 20] |
| **sort** | [**List&lt;String&gt;**](String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional] |

### Return type

[**PagedModelEntityModelProcessEventEntry**](PagedModelEntityModelProcessEventEntry.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*, application/x-spring-data-compact+json, text/uri-list


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |


## getItemResourceProcessevententryGet

> EntityModelProcessEventEntry getItemResourceProcessevententryGet(id)



get-processevententry

### Example

```java
// Import classes:
import org.alexmond.jsupervisor.client.invoker.ApiClient;
import org.alexmond.jsupervisor.client.invoker.ApiException;
import org.alexmond.jsupervisor.client.invoker.Configuration;
import org.alexmond.jsupervisor.client.invoker.models.*;
import org.alexmond.jsupervisor.client.api.ProcessEventEntryEntityControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost");

        ProcessEventEntryEntityControllerApi apiInstance = new ProcessEventEntryEntityControllerApi(defaultClient);
        String id = "id_example"; // String | 
        try {
            EntityModelProcessEventEntry result = apiInstance.getItemResourceProcessevententryGet(id);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProcessEventEntryEntityControllerApi#getItemResourceProcessevententryGet");
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
| **id** | **String**|  | |

### Return type

[**EntityModelProcessEventEntry**](EntityModelProcessEventEntry.md)

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

