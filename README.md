#Watermark Service

***WARNING***
I didn't cover the service jmeter tests, I have no JMeter installed, I am sorry. If it is important for the Test task, I will complete it

## Runing the service

### Use the following options to start the service locally:
* VM Options:
No vm options
* Main class: com.springer.ExeciseApplication

## Service address

http://localhost:8080

## HTTP endpoints

### API:``/v1/watermark``
- Request Method: ``POST``
- Content-Type: ``application/json``

Send document for watermark processing

### Request
URI: ``http://localhost:8080/v1/watermark``

### Body

Body for book

```json
{
    "title": "Simple title",
    "author": "German Sidorenko",
    "topic": "business"
}
```

Body for journal

```json
{
    "title": "Simple title",
    "author": "German Sidorenko",
}
```

### Response

Id for status checking:
```code
81fefc27-28a5-4426-b1ea-73e14064a25c
```

## API:``/v1/status/{id}``
- Request Method: ``GET``
- Content-Type: ``application/json``

Check status of processing document

### Request
URI: ``http://localhost:8080/v1/status/81fefc27-28a5-4426-b1ea-73e14064a25c``

### Response

If document is in process:
```json
{
  "message": "document is being processed by the service. please wait",
  "status": "in progress"
}
```

If id is not correct (not found):
```json
{
  "message": "document id not found. please check your id",
  "status": "not found"
}
```

If BOOK is processed:
```json
{
  "title": "Simple title",
  "author": "German Sidorenko",
  "watermark": {
    "content": "book",
    "title": "Simple title",
    "author": "German Sidorenko",
    "topic": "business"
  },
  "topic": "business"
}
```

If JOURNAL is processed:
```json
{
  "title": "Simple title",
  "author": "German Sidorenko",
  "watermark": {
    "content": "journal",
    "title": "Simple title",
    "author": "German Sidorenko"
  }
}
```