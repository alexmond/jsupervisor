# BulkProcessOperationsApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**restartAll**](BulkProcessOperationsApi.md#restartAll) | **POST** /api/v1/processes/bulk/restart | Restart all processes |
| [**startAll**](BulkProcessOperationsApi.md#startAll) | **POST** /api/v1/processes/bulk/start | Start all processes |
| [**stopAll**](BulkProcessOperationsApi.md#stopAll) | **POST** /api/v1/processes/bulk/stop | Stop all processes |



## restartAll

> restartAll()

Restart all processes

Restart all configured processes

### Example

```java
// Import classes:
import org.alexmond.jsupervisor.client.invoker.ApiClient;
import org.alexmond.jsupervisor.client.invoker.ApiException;
import org.alexmond.jsupervisor.client.invoker.Configuration;
import org.alexmond.jsupervisor.client.invoker.models.*;
import org.alexmond.jsupervisor.client.api.BulkProcessOperationsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost");

        BulkProcessOperationsApi apiInstance = new BulkProcessOperationsApi(defaultClient);
        try {
            apiInstance.restartAll();
        } catch (ApiException e) {
            System.err.println("Exception when calling BulkProcessOperationsApi#restartAll");
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

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | All processes restarted successfully |  -  |
| **500** | Error occurred while restarting processes |  -  |


## startAll

> startAll()

Start all processes

Start all configured processes that are not currently running

### Example

```java
// Import classes:
import org.alexmond.jsupervisor.client.invoker.ApiClient;
import org.alexmond.jsupervisor.client.invoker.ApiException;
import org.alexmond.jsupervisor.client.invoker.Configuration;
import org.alexmond.jsupervisor.client.invoker.models.*;
import org.alexmond.jsupervisor.client.api.BulkProcessOperationsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost");

        BulkProcessOperationsApi apiInstance = new BulkProcessOperationsApi(defaultClient);
        try {
            apiInstance.startAll();
        } catch (ApiException e) {
            System.err.println("Exception when calling BulkProcessOperationsApi#startAll");
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

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | All processes started successfully |  -  |
| **500** | Error occurred while starting processes |  -  |
| **409** | Some processes are already running |  -  |


## stopAll

> stopAll()

Stop all processes

Stop all currently running processes

### Example

```java
// Import classes:
import org.alexmond.jsupervisor.client.invoker.ApiClient;
import org.alexmond.jsupervisor.client.invoker.ApiException;
import org.alexmond.jsupervisor.client.invoker.Configuration;
import org.alexmond.jsupervisor.client.invoker.models.*;
import org.alexmond.jsupervisor.client.api.BulkProcessOperationsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost");

        BulkProcessOperationsApi apiInstance = new BulkProcessOperationsApi(defaultClient);
        try {
            apiInstance.stopAll();
        } catch (ApiException e) {
            System.err.println("Exception when calling BulkProcessOperationsApi#stopAll");
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

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | All processes stopped successfully |  -  |
| **500** | Error occurred while stopping processes |  -  |
| **404** | No running processes found |  -  |

