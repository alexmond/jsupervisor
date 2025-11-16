# ProfileControllerApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**descriptor**](ProfileControllerApi.md#descriptor) | **GET** /api/v1/profile/events |  |
| [**listAllFormsOfMetadata**](ProfileControllerApi.md#listAllFormsOfMetadata) | **GET** /api/v1/profile |  |



## descriptor

> String descriptor()



### Example

```java
// Import classes:
import org.alexmond.jsupervisor.client.invoker.ApiClient;
import org.alexmond.jsupervisor.client.invoker.ApiException;
import org.alexmond.jsupervisor.client.invoker.Configuration;
import org.alexmond.jsupervisor.client.invoker.models.*;
import org.alexmond.jsupervisor.client.api.ProfileControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost");

        ProfileControllerApi apiInstance = new ProfileControllerApi(defaultClient);
        try {
            String result = apiInstance.descriptor();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProfileControllerApi#descriptor");
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

**String**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*, application/alps+json, application/schema+json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |


## listAllFormsOfMetadata

> RepresentationModelObject listAllFormsOfMetadata()



### Example

```java
// Import classes:
import org.alexmond.jsupervisor.client.invoker.ApiClient;
import org.alexmond.jsupervisor.client.invoker.ApiException;
import org.alexmond.jsupervisor.client.invoker.Configuration;
import org.alexmond.jsupervisor.client.invoker.models.*;
import org.alexmond.jsupervisor.client.api.ProfileControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost");

        ProfileControllerApi apiInstance = new ProfileControllerApi(defaultClient);
        try {
            RepresentationModelObject result = apiInstance.listAllFormsOfMetadata();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProfileControllerApi#listAllFormsOfMetadata");
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

[**RepresentationModelObject**](RepresentationModelObject.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

