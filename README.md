# Sea-otter
JPEG and PNG images optimized application

- upload image file
- support edit command
  - coordinate crop, position crop, point with scale crop
  - resize, scale resize
  - angle rotate, flip rotate
- support image 
  - JPG, JPEG, TIFF, BMP, PNG, HDR
- support storage 
  - aws s3
  - local storage  
     
# New Features!
 - Upload the original image.
 - It supports json in the form of base64 safe url.
 
# Installation
### Quick start
```sh
git clone https://github.com/fennec-fox/sea-otter.git
./quick-start.sh
```
Swagger api page is http://localhost:6100/swagger-ui.html

### Configuration
```yaml
app:
  uploader: local
  local-storage:
    path: /var/tmp/image
    url: http://localhost
    shard-type: date
  aws-s3:
    bucket: sea-otter
    path: /edit
    cloud-front:
      url: http://d111111abcdef8.cloudfront.net
    shard-type: date
```
- app.uploader: `local`, `s3`
  - `local`: using local storage 
  - `s3`: using aws s3
- app.local-storage: local storage config
  - path: upload file directory
  - url: web host 
     - When a file is uploaded, it generates a url. Enter the host information of the url to be used. 
     - ex) when url is `http://localhost`, uploaded url is `http://localhost/2019-10-28/edited/5db6b0fc9216993dcfacde84.jpg`
  - shard-type: `date`, `sha1`
    - `date`: uri use date path
      - ex) `http://localhost/2019-10-28/edited/5db6b0fc9216993dcfacde84.jpg`
    - `sha1`: uri use encrypt string path  
      - ex) `http://localhost/5db6b0fc9216993dcfa2s3sf/edited/5db6b0fc9216993dcfacde84.jpg`
- app.aws-s3: amazon s3 config (If you use aws, you should configure aws certification on the server.)
  - bucket: s3 bucket name
  - path: bucket directory path
  - cloud-front: amazon cloud front config
    - url: amazon cloud front host
  - shard-type: `date`, `sha1`
    - `date`: uri use date path
      - ex) `http://d111111abcdef8.cloudfront.net/2019-10-28/edited/5db6b0fc9216993dcfacde84.jpg`
    - `sha1`: uri use encrypt string path  
      - ex) `http://d111111abcdef8.cloudfront.net/5db6b0fc9216993dcfa2s3sf/edited/5db6b0fc9216993dcfacde84.jpg`        
  
# Example
- common response spec
  - width: image width
  - height: image height
  - path: image url 
  - format: image format
  - original: whether the uploaded image is original
### simple upload
#### multipart file upload
Request 
```sh
curl -X POST "http://localhost:6100/upload/simple/multipart" -H "accept: application/json" -H "Content-Type: multipart/form-data" -F "hasOriginal=false" -F "multiPartFile=@map-marker-green.png;type=image/png"
```

Response
```json
{
  "content": [
    {
      "width": 48,
      "height": 48,
      "path": "http://localhost/2019-10-29/edited/5db7a6eae8dc502049df1734.jpg",
      "format": "jpg",
      "original": false
    }
  ]
}
```

#### base64 upload
Request 
```sh
curl -X POST "http://localhost:6100/upload/simple/base64/form" -H "accept: application/json" -H "Content-Type: application/x-www-form-urlencoded" -d "base64=data%3Aimage%2Fjpeg%3Bbase64%2C%2F9j%2F4AAQSkZJRgABAgAAAQABAAD%2F2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL%2F2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL%2FwAARCAABAAEDASIAAhEBAxEB%2F8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL%2F8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4%2BTl5ufo6erx8vP09fb3%2BPn6%2F8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL%2F8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3%2BPn6%2F9oADAMBAAIRAxEAPwDu6KKKxGf%2F2Q%3D%3D&hasOriginal=true"
```

Response
```json
{
  "content": [
    {
      "width": 1,
      "height": 1,
      "path": "http://localhost/2019-10-29/untouched/5db7e194e8dc502049df1739.jpeg",
      "format": "jpeg",
      "original": true
    },
    {
      "width": 1,
      "height": 1,
      "path": "http://localhost/2019-10-29/edited/5db7e194e8dc502049df1739.jpg",
      "format": "jpg",
      "original": false
    }
  ]
}
```
#### base64 safe url upload by json
Due to json's parsing issue, we need to convert the image to base64 and then convert the base64 to a base64 safe url.
Request
```sh
curl -X POST "http://localhost:6100/upload/simple/base64/json" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"base64\": \"ZGF0YTppbWFnZS9qcGVnO2Jhc2U2NCwvOWovNEFBUVNrWkpSZ0FCQWdBQUFRQUJBQUQvMndCREFBZ0dCZ2NHQlFnSEJ3Y0pDUWdLREJRTkRBc0xEQmtTRXc4VUhSb2ZIaDBhSEJ3Z0pDNG5JQ0lzSXh3Y0tEY3BMREF4TkRRMEh5YzVQVGd5UEM0ek5ETC8yd0JEQVFrSkNRd0xEQmdORFJneUlSd2hNakl5TWpJeU1qSXlNakl5TWpJeU1qSXlNakl5TWpJeU1qSXlNakl5TWpJeU1qSXlNakl5TWpJeU1qSXlNakl5TWpML3dBQVJDQUFCQUFFREFTSUFBaEVCQXhFQi84UUFId0FBQVFVQkFRRUJBUUVBQUFBQUFBQUFBQUVDQXdRRkJnY0lDUW9MLzhRQXRSQUFBZ0VEQXdJRUF3VUZCQVFBQUFGOUFRSURBQVFSQlJJaE1VRUdFMUZoQnlKeEZES0JrYUVJSTBLeHdSVlMwZkFrTTJKeWdna0tGaGNZR1JvbEppY29LU28wTlRZM09EazZRMFJGUmtkSVNVcFRWRlZXVjFoWldtTmtaV1puYUdscWMzUjFkbmQ0ZVhxRGhJV0doNGlKaXBLVGxKV1dsNWlabXFLanBLV21wNmlwcXJLenRMVzJ0N2k1dXNMRHhNWEd4OGpKeXRMVDFOWFcxOWpaMnVIaTQrVGw1dWZvNmVyeDh2UDA5ZmIzK1BuNi84UUFId0VBQXdFQkFRRUJBUUVCQVFBQUFBQUFBQUVDQXdRRkJnY0lDUW9MLzhRQXRSRUFBZ0VDQkFRREJBY0ZCQVFBQVFKM0FBRUNBeEVFQlNFeEJoSkJVUWRoY1JNaU1vRUlGRUtSb2JIQkNTTXpVdkFWWW5MUkNoWWtOT0VsOFJjWUdSb21KeWdwS2pVMk56ZzVPa05FUlVaSFNFbEtVMVJWVmxkWVdWcGpaR1ZtWjJocGFuTjBkWFozZUhsNmdvT0VoWWFIaUltS2twT1VsWmFYbUptYW9xT2twYWFucUttcXNyTzB0YmEzdUxtNndzUEV4Y2JIeU1uSzB0UFUxZGJYMk5uYTR1UGs1ZWJuNk9ucTh2UDA5ZmIzK1BuNi85b0FEQU1CQUFJUkF4RUFQd0R1NktLS3hHZi8yUT09\", \"hasOriginal\": false}"
```

Response
```json
{
  "content": [
    {
      "width": 1,
      "height": 1,
      "path": "http://localhost/2019-10-29/edited/5db8142cd4ed6e32ac35fb05.jpg",
      "format": "jpg",
      "original": false
    }
  ]
}
```
