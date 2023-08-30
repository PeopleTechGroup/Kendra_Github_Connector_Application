## Kendra Connector API Documentation

The Kendra Connector is a RESTful API for interacting with Amazon Kendra, a service that enables powerful search and intelligent question-answering capabilities over your data. This API provides endpoints to manage Kendra indexes, data sources, and perform searches.

### Endpoints

#### Create Index

Create a new Kendra index.

- **Endpoint:** POST `/api/v1/github-connector/create/index`
- **Headers:**
    - `indexName`: Name of the index to be created.
    - `description`: Description of the index.

##### Request

```http
POST /api/v1/github-connector/create/index
Content-Type: application/json

{
  "indexName": "sample-index",
  "description": "Sample Kendra index for GitHub data"
}
```

##### Response

- Status: 200 OK
- Body: JSON representation of the created index.

---

#### Check Index Status

Check the status of a Kendra index.

- **Endpoint:** GET `/api/v1/github-connector/index/status`
- **Headers:**
    - `indexId`: ID of the Kendra index.

##### Request

```http
GET /api/v1/github-connector/index/status
Content-Type: application/json
indexId: <index-id>
```

##### Response

- Status: 200 OK
- Body: JSON representation of the index status.

---

#### Check Index Exists

Check if a Kendra index exists.

- **Endpoint:** GET `/api/v1/github-connector/index/exists`
- **Headers:**
    - `indexId`: ID of the Kendra index.

##### Request

```http
GET /api/v1/github-connector/index/exists
Content-Type: application/json
indexId: <index-id>
```

##### Response

- Status: 200 OK
- Body: Boolean indicating index existence.

---

#### Get All Available Indexes

Get a list of all available Kendra indexes.

- **Endpoint:** GET `/api/v1/github-connector/indexes`

##### Request

```http
GET /api/v1/github-connector/indexes
```

##### Response

- Status: 200 OK
- Body: List of index information pairs.

---

#### Create Data Source

Create a Kendra data source for GitHub data.

- **Endpoint:** POST `/api/v1/github-connector/createDatasource`
- **Headers:**
    - `indexId`: ID of the Kendra index.

##### Request

```http
POST /api/v1/github-connector/createDatasource
Content-Type: application/json
indexId: <index-id>
```

##### Response

- Status: 200 OK
- Body: ID of the created data source.

---

#### Check Data Source Exists

Check if a Kendra data source exists.

- **Endpoint:** GET `/api/v1/github-connector/datasource/exists`
- **Headers:**
    - `indexId`: ID of the Kendra index.
    - `dataSourceId`: ID of the data source.

##### Request

```http
GET /api/v1/github-connector/datasource/exists
Content-Type: application/json
indexId: <index-id>
dataSourceId: <data-source-id>
```

##### Response

- Status: 200 OK
- Body: Boolean indicating data source existence.

---

#### Get All Data Sources

Get a list of all data sources for a Kendra index.

- **Endpoint:** GET `/api/v1/github-connector/datasource/all`
- **Headers:**
    - `indexId`: ID of the Kendra index.

##### Request

```http
GET /api/v1/github-connector/datasource/all
Content-Type: application/json
indexId: <index-id>
```

##### Response

- Status: 200 OK
- Body: List of data source information pairs.

---

#### Search Kendra Index

Perform a search query on a Kendra index.

- **Endpoint:** GET `/api/v1/github-connector/search`
- **Headers:**
    - `indexId`: ID of the Kendra index.
    - `query`: Search query.

##### Request

```http
GET /api/v1/github-connector/search
Content-Type: application/json
indexId: <index-id>
query: <search-query>
```

##### Response

- Status: 200 OK
- Body: List of query result items.

---

#### Get Data Source Configuration

Get configuration details for the Kendra data source.

- **Endpoint:** GET `/api/v1/github-connector/dataSourceConfiguration`

##### Request

```http
GET /api/v1/github-connector/dataSourceConfiguration
```

##### Response

- Status: 200 OK
- Body: Configuration details.

### Usage

To use the Kendra Connector API, send HTTP requests to the appropriate endpoints as described above. Replace `<index-id>` and other placeholders with actual values. Make sure to include required headers as specified in the documentation.

This API provides a convenient way to interact with Amazon Kendra and manage indexes, data sources, and search queries programmatically.