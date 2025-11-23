# ProcessManagementApi

All URIs are relative to *http://localhost:8086*

| Method                                                           | HTTP request                              | Description        |
|------------------------------------------------------------------|-------------------------------------------|--------------------|
| [**getAllProcesses**](ProcessManagementApi.md#getAllProcesses)   | **GET** /api/v1/processes                 | List all processes |
| [**getProcessStatus**](ProcessManagementApi.md#getProcessStatus) | **GET** /api/v1/processes/status/{name}   | Get process status |
| [**restartProcess**](ProcessManagementApi.md#restartProcess)     | **POST** /api/v1/processes/restart/{name} | Restart process    |
| [**startProcess**](ProcessManagementApi.md#startProcess)         | **POST** /api/v1/processes/start/{name}   | Start process      |
| [**stopProcess**](ProcessManagementApi.md#stopProcess)           | **POST** /api/v1/processes/stop/{name}    | Stop process       |

## getAllProcesses

> List&lt;ProcessStatusRest&gt; getAllProcesses()

List all processes

Retrieve status information for all configured processes

### Example

```java
// Import classes:

import org.alexmond.jsupervisor.client.invoker.ApiClient;
import org.alexmond.jsupervisor.client.invoker.ApiException;
import org.alexmond.jsupervisor.client.invoker.Configuration;
import org.alexmond.jsupervisor.client.invoker.models.*;
import org.alexmond.jsupervisor.client.api.ProcessManagementApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8086");

        ProcessManagementApi apiInstance = new ProcessManagementApi(defaultClient);
        try {
            List<ProcessStatusRest> result = apiInstance.getAllProcesses();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProcessManagementApi#getAllProcesses");
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

[**List&lt;ProcessStatusRest&gt;**](ProcessStatusRest.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

### HTTP response details

| Status code | Description                              | Response headers |
|-------------|------------------------------------------|------------------|
| **200**     | Successfully retrieved list of processes | -                |

## getProcessStatus

> ProcessStatusRest getProcessStatus(name)

Get process status

Get the current status of a specific process

### Example

```java
// Import classes:

import org.alexmond.jsupervisor.client.invoker.ApiClient;
import org.alexmond.jsupervisor.client.invoker.ApiException;
import org.alexmond.jsupervisor.client.invoker.Configuration;
import org.alexmond.jsupervisor.client.invoker.models.*;
import org.alexmond.jsupervisor.client.api.ProcessManagementApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8086");

        ProcessManagementApi apiInstance = new ProcessManagementApi(defaultClient);
        String name = "name_example"; // String | Process name
        try {
            ProcessStatusRest result = apiInstance.getProcessStatus(name);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProcessManagementApi#getProcessStatus");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

| Name     | Type       | Description  | Notes |
|----------|------------|--------------|-------|
| **name** | **String** | Process name |       |

### Return type

[**ProcessStatusRest**](ProcessStatusRest.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

### HTTP response details

| Status code | Description                           | Response headers |
|-------------|---------------------------------------|------------------|
| **200**     | Successfully retrieved process status | -                |
| **404**     | Process not found                     | -                |
| **500**     | Failed to retrieve process status     | -                |

## restartProcess

> restartProcess(name)

Restart process

Restart a specific process

### Example

```java
// Import classes:

import org.alexmond.jsupervisor.client.invoker.ApiClient;
import org.alexmond.jsupervisor.client.invoker.ApiException;
import org.alexmond.jsupervisor.client.invoker.Configuration;
import org.alexmond.jsupervisor.client.invoker.models.*;
import org.alexmond.jsupervisor.client.api.ProcessManagementApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8086");

        ProcessManagementApi apiInstance = new ProcessManagementApi(defaultClient);
        String name = "name_example"; // String | Process name
        try {
            apiInstance.restartProcess(name);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProcessManagementApi#restartProcess");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

| Name     | Type       | Description  | Notes |
|----------|------------|--------------|-------|
| **name** | **String** | Process name |       |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined

### HTTP response details

| Status code | Description                            | Response headers |
|-------------|----------------------------------------|------------------|
| **200**     | Process restart initiated successfully | -                |
| **404**     | Process not found                      | -                |
| **500**     | Failed to restart process              | -                |

## startProcess

> ResponseMessage startProcess(name)

Start process

Start a specific process

### Example

```java
// Import classes:

import org.alexmond.jsupervisor.client.invoker.ApiClient;
import org.alexmond.jsupervisor.client.invoker.ApiException;
import org.alexmond.jsupervisor.client.invoker.Configuration;
import org.alexmond.jsupervisor.client.invoker.models.*;
import org.alexmond.jsupervisor.client.api.ProcessManagementApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8086");

        ProcessManagementApi apiInstance = new ProcessManagementApi(defaultClient);
        String name = "name_example"; // String | Process name
        try {
            ResponseMessage result = apiInstance.startProcess(name);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProcessManagementApi#startProcess");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

| Name     | Type       | Description  | Notes |
|----------|------------|--------------|-------|
| **name** | **String** | Process name |       |

### Return type

[**ResponseMessage**](ResponseMessage.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

### HTTP response details

| Status code | Description                          | Response headers |
|-------------|--------------------------------------|------------------|
| **200**     | Process start initiated successfully | -                |
| **400**     | Invalid process configuration        | -                |
| **500**     | Failed to start process              | -                |

## stopProcess

> stopProcess(name)

Stop process

Stop a specific process

### Example

```java
// Import classes:

import org.alexmond.jsupervisor.client.invoker.ApiClient;
import org.alexmond.jsupervisor.client.invoker.ApiException;
import org.alexmond.jsupervisor.client.invoker.Configuration;
import org.alexmond.jsupervisor.client.invoker.models.*;
import org.alexmond.jsupervisor.client.api.ProcessManagementApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8086");

        ProcessManagementApi apiInstance = new ProcessManagementApi(defaultClient);
        String name = "name_example"; // String | Process name
        try {
            apiInstance.stopProcess(name);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProcessManagementApi#stopProcess");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

| Name     | Type       | Description  | Notes |
|----------|------------|--------------|-------|
| **name** | **String** | Process name |       |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined

### HTTP response details

| Status code | Description                         | Response headers |
|-------------|-------------------------------------|------------------|
| **200**     | Process stop initiated successfully | -                |
| **404**     | Process not found                   | -                |
| **500**     | Failed to stop process              | -                |

