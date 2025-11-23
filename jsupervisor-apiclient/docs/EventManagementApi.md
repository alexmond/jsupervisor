# EventManagementApi

All URIs are relative to *http://localhost:8086*

| Method                                                               | HTTP request                                    | Description                |
|----------------------------------------------------------------------|-------------------------------------------------|----------------------------|
| [**getEventById**](EventManagementApi.md#getEventById)               | **GET** /api/v1/events/{id}                     | Get event by ID            |
| [**getEvents**](EventManagementApi.md#getEvents)                     | **GET** /api/v1/events                          | Get paginated events       |
| [**getEventsByProcess**](EventManagementApi.md#getEventsByProcess)   | **GET** /api/v1/events/by-process/{processName} | Get events by process name |
| [**getEventsByStatus**](EventManagementApi.md#getEventsByStatus)     | **GET** /api/v1/events/by-status/{status}       | Get events by status       |
| [**getTotalEventsCount**](EventManagementApi.md#getTotalEventsCount) | **GET** /api/v1/events/count                    | Get total event count      |

## getEventById

> ProcessEventEntry getEventById(id)

Get event by ID

Retrieves detailed information about a specific process event

### Example

```java
// Import classes:
import org.alexmond.jsupervisor.client.invoker.ApiClient;
import org.alexmond.jsupervisor.client.invoker.ApiException;
import org.alexmond.jsupervisor.client.invoker.Configuration;
import org.alexmond.jsupervisor.client.invoker.models.*;
import org.alexmond.jsupervisor.client.api.EventManagementApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8086");

        EventManagementApi apiInstance = new EventManagementApi(defaultClient);
        Long id = 123L; // Long | Event ID
        try {
            ProcessEventEntry result = apiInstance.getEventById(id);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling EventManagementApi#getEventById");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

| Name   | Type     | Description | Notes |
|--------|----------|-------------|-------|
| **id** | **Long** | Event ID    |       |

### Return type

[**ProcessEventEntry**](ProcessEventEntry.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

### HTTP response details

| Status code | Description     | Response headers |
|-------------|-----------------|------------------|
| **200**     | Event found     | -                |
| **404**     | Event not found | -                |

## getEvents

> Page getEvents(page, size, sortBy, sortDirection)

Get paginated events

Retrieves a paginated list of all process events with customizable sorting

### Example

```java
// Import classes:
import org.alexmond.jsupervisor.client.invoker.ApiClient;
import org.alexmond.jsupervisor.client.invoker.ApiException;
import org.alexmond.jsupervisor.client.invoker.Configuration;
import org.alexmond.jsupervisor.client.invoker.models.*;
import org.alexmond.jsupervisor.client.api.EventManagementApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8086");

        EventManagementApi apiInstance = new EventManagementApi(defaultClient);
        Integer page = 0; // Integer | Page number (0-based)
        Integer size = 20; // Integer | Number of items per page
        String sortBy = "eventTime"; // String | Field to sort by
        String sortDirection = "asc"; // String | Sort direction
        try {
            Page result = apiInstance.getEvents(page, size, sortBy, sortDirection);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling EventManagementApi#getEvents");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

| Name              | Type        | Description              | Notes                                                                |
|-------------------|-------------|--------------------------|----------------------------------------------------------------------|
| **page**          | **Integer** | Page number (0-based)    | [optional]                                                           |
| **size**          | **Integer** | Number of items per page | [optional]                                                           |
| **sortBy**        | **String**  | Field to sort by         | [optional] [enum: eventTime, processName, newStatus, oldStatus, pid] |
| **sortDirection** | **String**  | Sort direction           | [optional] [enum: asc, desc]                                         |

### Return type

[**Page**](Page.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json, */*

### HTTP response details

| Status code | Description                              | Response headers |
|-------------|------------------------------------------|------------------|
| **200**     | Successfully retrieved events            | -                |
| **400**     | Invalid pagination or sorting parameters | -                |

## getEventsByProcess

> PageProcessEventEntry getEventsByProcess(processName, page, size, sortBy, sortDirection)

Get events by process name

Retrieves paginated events for a specific process

### Example

```java
// Import classes:
import org.alexmond.jsupervisor.client.invoker.ApiClient;
import org.alexmond.jsupervisor.client.invoker.ApiException;
import org.alexmond.jsupervisor.client.invoker.Configuration;
import org.alexmond.jsupervisor.client.invoker.models.*;
import org.alexmond.jsupervisor.client.api.EventManagementApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8086");

        EventManagementApi apiInstance = new EventManagementApi(defaultClient);
        String processName = "myapp"; // String | Process name
        Integer page = 0; // Integer | Page number (0-based)
        Integer size = 20; // Integer | Number of items per page
        String sortBy = "eventTime"; // String | Field to sort by
        String sortDirection = "desc"; // String | Sort direction
        try {
            PageProcessEventEntry result = apiInstance.getEventsByProcess(processName, page, size, sortBy, sortDirection);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling EventManagementApi#getEventsByProcess");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

| Name              | Type        | Description              | Notes                             |
|-------------------|-------------|--------------------------|-----------------------------------|
| **processName**   | **String**  | Process name             |                                   |
| **page**          | **Integer** | Page number (0-based)    | [optional] [default to 0]         |
| **size**          | **Integer** | Number of items per page | [optional] [default to 20]        |
| **sortBy**        | **String**  | Field to sort by         | [optional] [default to eventTime] |
| **sortDirection** | **String**  | Sort direction           | [optional] [default to desc]      |

### Return type

[**PageProcessEventEntry**](PageProcessEventEntry.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

### HTTP response details

| Status code | Description                   | Response headers |
|-------------|-------------------------------|------------------|
| **200**     | Successfully retrieved events | -                |
| **404**     | Process not found             | -                |

## getEventsByStatus

> PageProcessEventEntry getEventsByStatus(status, page, size, sortBy, sortDirection)

Get events by status

Retrieves paginated events matching a specific process status

### Example

```java
// Import classes:
import org.alexmond.jsupervisor.client.invoker.ApiClient;
import org.alexmond.jsupervisor.client.invoker.ApiException;
import org.alexmond.jsupervisor.client.invoker.Configuration;
import org.alexmond.jsupervisor.client.invoker.models.*;
import org.alexmond.jsupervisor.client.api.EventManagementApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8086");

        EventManagementApi apiInstance = new EventManagementApi(defaultClient);
        String status = "not_started"; // String | Process status to filter by
        Integer page = 0; // Integer | Page number (0-based)
        Integer size = 20; // Integer | Number of items per page
        String sortBy = "eventTime"; // String | Field to sort by
        String sortDirection = "desc"; // String | Sort direction
        try {
            PageProcessEventEntry result = apiInstance.getEventsByStatus(status, page, size, sortBy, sortDirection);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling EventManagementApi#getEventsByStatus");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

| Name              | Type        | Description                 | Notes                                                                                                                              |
|-------------------|-------------|-----------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| **status**        | **String**  | Process status to filter by | [enum: not_started, running, finished, unknown, failed, failed_to_start, stopped, stopping, aborted, starting, healthy, unhealthy] |
| **page**          | **Integer** | Page number (0-based)       | [optional] [default to 0]                                                                                                          |
| **size**          | **Integer** | Number of items per page    | [optional] [default to 20]                                                                                                         |
| **sortBy**        | **String**  | Field to sort by            | [optional] [default to eventTime]                                                                                                  |
| **sortDirection** | **String**  | Sort direction              | [optional] [default to desc]                                                                                                       |

### Return type

[**PageProcessEventEntry**](PageProcessEventEntry.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

### HTTP response details

| Status code | Description                   | Response headers |
|-------------|-------------------------------|------------------|
| **200**     | Successfully retrieved events | -                |
| **400**     | Invalid status value          | -                |

## getTotalEventsCount

> Map&lt;String, Long&gt; getTotalEventsCount()

Get total event count

Returns the total number of events in the system

### Example

```java
// Import classes:
import org.alexmond.jsupervisor.client.invoker.ApiClient;
import org.alexmond.jsupervisor.client.invoker.ApiException;
import org.alexmond.jsupervisor.client.invoker.Configuration;
import org.alexmond.jsupervisor.client.invoker.models.*;
import org.alexmond.jsupervisor.client.api.EventManagementApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8086");

        EventManagementApi apiInstance = new EventManagementApi(defaultClient);
        try {
            Map<String, Long> result = apiInstance.getTotalEventsCount();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling EventManagementApi#getTotalEventsCount");
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

**Map&lt;String, Long&gt;**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

### HTTP response details

| Status code | Description                  | Response headers |
|-------------|------------------------------|------------------|
| **200**     | Successfully retrieved count | -                |

