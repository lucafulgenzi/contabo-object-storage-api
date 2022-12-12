# Contabo Object Storage API

This is a simple project to use the Amazon S3 API with Contabo Object Storage.

--- 

Env file example:
```
OBJECT_STORAGE_ACCESS_KEY=your_access_key
OBJECT_STORAGE_SECRET_KEY=your_secret_key
OBJECT_STORAGE_URL=your_url
OBJECT_STORAGE_REGION=your_region
```

Features:
 - [X] Upload file
 - [ ] Upload folder
 - [X] Download single file
 - [ ] Download folder
 - [X] Delete single file
 - [ ] Delete multiple files
 - [ ] Delete folder
 - [X] List files
 - [ ] Create bucket
 - [ ] Delete bucket
 - [X] List buckets
 - [ ] Copy/Move/Rename file

--- 

Endpoints example:

Upload file:
```
POST contabo/upload

BODY (form-data):
    bucket: your_bucket es. (my-bucket)
    location: location_on_object_storage es. (Documents/new_file.txt)
    file: file_to_upload
```
---
Download file:
```
GET contabo/download?bucket={my-bucket}&location={location_on_object_storage}
```
---
Delete file:
```
DELETE contabo/delete?bucket={my-bucket}&location={location_on_object_storage}
```
---
List files:
```
GET contabo/list/{my-bucket}?simple={true/false}
```
---
List buckets:
```
GET contabo/buckets
```