# ProcessEventEntryEntityControllerApi

All URIs are relative to *http://localhost:8086*

| Method                                                                                                                                 | HTTP request                                | Description |
|----------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------|-------------|
| [**deleteItemResourceProcessevententryDelete**](ProcessEventEntryEntityControllerApi.md#deleteItemResourceProcessevententryDelete)     | **DELETE** /api/v1/processEventEntries/{id} |             |
| [**getCollectionResourceProcessevententryGet**](ProcessEventEntryEntityControllerApi.md#getCollectionResourceProcessevententryGet)     | **GET** /api/v1/processEventEntries         |             |
| [**getItemResourceProcessevententryGet**](ProcessEventEntryEntityControllerApi.md#getItemResourceProcessevententryGet)                 | **GET** /api/v1/processEventEntries/{id}    |             |
| [**patchItemResourceProcessevententryPatch**](ProcessEventEntryEntityControllerApi.md#patchItemResourceProcessevententryPatch)         | **PATCH** /api/v1/processEventEntries/{id}  |             |
| [**postCollectionResourceProcessevententryPost**](ProcessEventEntryEntityControllerApi.md#postCollectionResourceProcessevententryPost) | **POST** /api/v1/processEventEntries        |             |
| [**putItemResourceProcessevententryPut**](ProcessEventEntryEntityControllerApi.md#putItemResourceProcessevententryPut)                 | **PUT** /api/v1/processEventEntries/{id}    |             |

## deleteItemResourceProcessevententryDelete

> deleteItemResourceProcessevententryDelete(id)



delete-processevententry

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
        defaultClient.setBasePath("http://localhost:8086");

        ProcessEventEntryEntityControllerApi apiInstance = new ProcessEventEntryEntityControllerApi(defaultClient);
        String id = "id_example"; // String | 
        try {
            apiInstance.deleteItemResourceProcessevententryDelete(id);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProcessEventEntryEntityControllerApi#deleteItemResourceProcessevententryDelete");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

| Name   | Type       | Description | Notes |
|--------|------------|-------------|-------|
| **id** | **String** |             |       |

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
| **204**     | No Content  | -                |
| **404**     | Not Found   | -                |

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
        defaultClient.setBasePath("http://localhost:8086");

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

| Name     | Type                                | Description                                   | Notes                                                                         |
|----------|-------------------------------------|-----------------------------------------------|-------------------------------------------------------------------------------|
| **page** | **Integer**                         | Zero-based page index (0..N)                  | [optional] [default to 0]                                                     |
| **size** | **Integer**                         | The size of the page to be returned           | [optional] [default to 20]                                                    |
| **sort** | [**List&lt;String&gt;**](String.md) | Sorting criteria in the format: property,(asc | desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional] |

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
| **200**     | OK          | -                |

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
        defaultClient.setBasePath("http://localhost:8086");

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

| Name   | Type       | Description | Notes |
|--------|------------|-------------|-------|
| **id** | **String** |             |       |

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
| **200**     | OK          | -                |
| **404**     | Not Found   | -                |

## patchItemResourceProcessevententryPatch

> EntityModelProcessEventEntry patchItemResourceProcessevententryPatch(id, processEventEntryRequestBody)



patch-processevententry

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
        defaultClient.setBasePath("http://localhost:8086");

        ProcessEventEntryEntityControllerApi apiInstance = new ProcessEventEntryEntityControllerApi(defaultClient);
        String id = "id_example"; // String | 
        ProcessEventEntryRequestBody processEventEntryRequestBody = new ProcessEventEntryRequestBody(); // ProcessEventEntryRequestBody | 
        try {
            EntityModelProcessEventEntry result = apiInstance.patchItemResourceProcessevententryPatch(id, processEventEntryRequestBody);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProcessEventEntryEntityControllerApi#patchItemResourceProcessevententryPatch");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

| Name                             | Type                                                                | Description | Notes |
|----------------------------------|---------------------------------------------------------------------|-------------|-------|
| **id**                           | **String**                                                          |             |       |
| **processEventEntryRequestBody** | [**ProcessEventEntryRequestBody**](ProcessEventEntryRequestBody.md) |             |       |

### Return type

[**EntityModelProcessEventEntry**](EntityModelProcessEventEntry.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*

### HTTP response details

| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200**     | OK          | -                |
| **204**     | No Content  | -                |

## postCollectionResourceProcessevententryPost

> EntityModelProcessEventEntry postCollectionResourceProcessevententryPost(processEventEntryRequestBody)



create-processevententry

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
        defaultClient.setBasePath("http://localhost:8086");

        ProcessEventEntryEntityControllerApi apiInstance = new ProcessEventEntryEntityControllerApi(defaultClient);
        ProcessEventEntryRequestBody processEventEntryRequestBody = new ProcessEventEntryRequestBody(); // ProcessEventEntryRequestBody | 
        try {
            EntityModelProcessEventEntry result = apiInstance.postCollectionResourceProcessevententryPost(processEventEntryRequestBody);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProcessEventEntryEntityControllerApi#postCollectionResourceProcessevententryPost");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

| Name                             | Type                                                                | Description | Notes |
|----------------------------------|---------------------------------------------------------------------|-------------|-------|
| **processEventEntryRequestBody** | [**ProcessEventEntryRequestBody**](ProcessEventEntryRequestBody.md) |             |       |

### Return type

[**EntityModelProcessEventEntry**](EntityModelProcessEventEntry.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*

### HTTP response details

| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201**     | Created     | -                |

## putItemResourceProcessevententryPut

> EntityModelProcessEventEntry putItemResourceProcessevententryPut(id, processEventEntryRequestBody)



update-processevententry

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
        defaultClient.setBasePath("http://localhost:8086");

        ProcessEventEntryEntityControllerApi apiInstance = new ProcessEventEntryEntityControllerApi(defaultClient);
        String id = "id_example"; // String | 
        ProcessEventEntryRequestBody processEventEntryRequestBody = new ProcessEventEntryRequestBody(); // ProcessEventEntryRequestBody | 
        try {
            EntityModelProcessEventEntry result = apiInstance.putItemResourceProcessevententryPut(id, processEventEntryRequestBody);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProcessEventEntryEntityControllerApi#putItemResourceProcessevententryPut");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

| Name                             | Type                                                                | Description | Notes |
|----------------------------------|---------------------------------------------------------------------|-------------|-------|
| **id**                           | **String**                                                          |             |       |
| **processEventEntryRequestBody** | [**ProcessEventEntryRequestBody**](ProcessEventEntryRequestBody.md) |             |       |

### Return type

[**EntityModelProcessEventEntry**](EntityModelProcessEventEntry.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*

### HTTP response details

| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200**     | OK          | -                |
| **201**     | Created     | -                |
| **204**     | No Content  | -                |

