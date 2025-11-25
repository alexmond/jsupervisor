# SupervisorInformationApi

All URIs are relative to *http://localhost:8086*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getSupervisorInfo**](SupervisorInformationApi.md#getSupervisorInfo) | **GET** /api/v1/supervisor/info | Get supervisor info |



## getSupervisorInfo

> SupervisorRest getSupervisorInfo()

Get supervisor info

Retrieve basic supervisor information

### Example

```java
// Import classes:
import org.alexmond.jsupervisor.client.invoker.ApiClient;
import org.alexmond.jsupervisor.client.invoker.ApiException;
import org.alexmond.jsupervisor.client.invoker.Configuration;
import org.alexmond.jsupervisor.client.invoker.models.*;
import org.alexmond.jsupervisor.client.api.SupervisorInformationApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8086");

        SupervisorInformationApi apiInstance = new SupervisorInformationApi(defaultClient);
        try {
            SupervisorRest result = apiInstance.getSupervisorInfo();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling SupervisorInformationApi#getSupervisorInfo");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

[**SupervisorRest**](SupervisorRest.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **400** | Business error |  -  |
| **500** | Internal server error |  -  |
| **200** | OK |  -  |

